//@author: j.n.magee 11/11/96

import java.awt.*;
import java.applet.*;

/********************************************************/

public class ThreadPanel extends Panel {

    Button start_;
    Button stop_;
    DisplayThread A_;

    public ThreadPanel(String title, Color c, Runnable r) {
         super();
        // Set up Buttons
        this.setFont(new Font("Helvetica",Font.BOLD,14));
        Panel p = new Panel();
        p.add(start_=new Button("Start"));
        p.add(stop_=new Button("Stop"));
        setLayout(new BorderLayout());
        add("South",p);
        GraphicCanvas g1 = new GraphicCanvas(title,c);
        add("Center",g1);
        A_= new DisplayThread(g1,r,100);
        A_.start();
    }


    public void passivate() {
        A_.passivate();
     }

    public void stop() {
        A_.stop();
    }

    public boolean handleEvent(Event event) {
        if (event.id != event.ACTION_EVENT) {
            return super.handleEvent(event);
        } else if(event.target==start_) {
            A_.activate();
            return true;
       } else if(event.target==stop_) {
            A_.passivate();
            return true;
        } else
            return super.handleEvent(event);
    }
}

/********************************************************/

class DisplayThread extends Thread {

    GraphicCanvas display_;
    boolean suspended = true;
    int angle_=0;
    int segStart_=9999;
    int segEnd_=9999;
    int rate_;
    final static int step = 6;
    Runnable target_;

    DisplayThread(GraphicCanvas g, Runnable target, int rate) {
        display_ = g;
        display_.setColor(Color.red);
        target_=target;
        rate_=rate;
    }

    synchronized void mysuspend() {
        while (suspended)
            try {wait();} catch (InterruptedException e) {}
    }

    public void passivate() {
        if (!suspended) {
            suspended = true;
            display_.setColor(Color.red);
           }
    }

    public void activate() {
        if (suspended) {
            suspended = false;
            display_.setColor(Color.green);
            synchronized(this) {notify();}
        }
    }

    public static void rotate() {
        DisplayThread d = (DisplayThread)Thread.currentThread();
        d.mysuspend();
        d.angle_=(d.angle_+step)%360;
        d.display_.setAngle(d.angle_);
        try {Thread.sleep(d.rate_); } catch (InterruptedException e){}
    }

    public static void mark() {
        DisplayThread d = (DisplayThread)Thread.currentThread();
        d.segStart_=d.angle_;
        d.display_.setSegment(d.segStart_,d.segEnd_,Color.cyan);
    }

    public static void unmark() {
        DisplayThread d = (DisplayThread)Thread.currentThread();
        d.segEnd_=d.angle_;
        d.display_.setSegment(d.segStart_,d.segEnd_,Color.cyan);
    }


    public void run() {
          mysuspend();
          target_.run();
     }
 }


/********************************************************/

class GraphicCanvas extends Canvas {
    int angle_ = 0;
    String title_;
    Color arcColor_ = Color.blue;
    int segStart_ = 9999;
    int segEnd_ = 9999;
    Color segColor_ = Color.yellow;

    Font f1 = new Font("Times",Font.ITALIC+Font.BOLD,24);

    GraphicCanvas(String title, Color c) {
        super();
        title_=title;
        resize(130,130);
        arcColor_=c;
  	}

    public void setColor(Color c){
        setBackground(c);
        repaint();
    }

    public void setAngle(int a){
        angle_ = a;
        repaint();
    }

    public void setSegment(int start, int end, Color c) {
        segStart_ = start;
        segEnd_ = end;
        segColor_ = c;
    }

    public void paint(Graphics g){
        update(g);
    }

    Image offscreen;
    Dimension offscreensize;
    Graphics offgraphics;

    public synchronized void update(Graphics g){
        Dimension d = size();
	    if ((offscreen == null) || (d.width != offscreensize.width)
	                            || (d.height != offscreensize.height)) {
	        offscreen = createImage(d.width, d.height);
	        offscreensize = d;
	        offgraphics = offscreen.getGraphics();
	        offgraphics.setFont(getFont());
	    }

	    offgraphics.setColor(getBackground());
	    offgraphics.fillRect(0, 0, d.width, d.height);

             // Display the title
         offgraphics.setColor(Color.black);
         offgraphics.setFont(f1);
         FontMetrics fm = offgraphics.getFontMetrics();
         int w = fm.stringWidth(title_);
         int h = fm.getHeight();
         int x = (size().width - w)/2;
         int y = h;
         offgraphics.drawString(title_, x, y);
         offgraphics.drawLine(x,y+3,x+w,y+3);
         // Display the arc
         if (angle_<segStart_) {
            offgraphics.setColor(arcColor_);
            offgraphics.fillArc(25,35,90,90,0,angle_);
         } else if ( angle_>=segStart_ && angle_<segEnd_) {
            offgraphics.setColor(arcColor_);
            offgraphics.fillArc(25,35,90,90,0,segStart_);
            offgraphics.setColor(segColor_);
            offgraphics.fillArc(25,35,90,90,segStart_,angle_-segStart_);
         } else  {
            offgraphics.setColor(arcColor_);
            offgraphics.fillArc(25,35,90,90,0,segStart_);
            offgraphics.setColor(segColor_);
            offgraphics.fillArc(25,35,90,90,segStart_,segEnd_-segStart_);
            offgraphics.setColor(arcColor_);
            offgraphics.fillArc(25,35,90,90,segEnd_,angle_-segEnd_);
         }
         g.drawImage(offscreen, 0, 0, null);
    }
}

/****************************************************************************/