/*
@author  j.n.magee 20/11/96
*/
import java.awt.*;
import java.util.*;
import java.applet.*;

/*********************Select*****************************/
// implements choice
class Select {
    Vector list = new Vector(2);

    public void add(Selectable s) {
        list.addElement(s);
        s.setSelect(this);
    }

    private void clearAll() {
        for (Enumeration e = list.elements(); e.hasMoreElements();){
           ((Selectable)e.nextElement()).clearOpen();
        }
    }

    private void openAll() {
        for (Enumeration e = list.elements(); e.hasMoreElements();){
            Selectable s = (Selectable)e.nextElement();
            if (s.testGuard()) s.setOpen();
        }
    }

    private int testAll() {
        int i = 0;
        int j = 1;
        for (Enumeration e = list.elements(); e.hasMoreElements() && i==0; ++j){
           Selectable s = (Selectable)e.nextElement();
           if (s.testReady() && s.testGuard()) i=j;
        }
        return i;
    }

    public synchronized int choose() {
        int readyIndex = 0;
        while (readyIndex==0) {
            readyIndex=testAll();
            if (readyIndex==0) {
                openAll();
                try{wait();}catch (InterruptedException e) {}
                clearAll();
            }
        }
        return readyIndex;
    }
}

class Selectable {
    private boolean open = false;
    private boolean ready = false;
    private Select inchoice = null;
    private boolean guard_ = true;

    void setSelect(Select s) {
        inchoice = s;
    }

    synchronized void block() {
          if (inchoice == null) {
            setOpen();
            while(!ready) {
                try{wait();} catch (InterruptedException e) {}
            }
            clearOpen();
        }
    }

    synchronized void signal() {
        if (inchoice == null) {
            ready=true;
            if (open) notify();
        } else {
            synchronized (inchoice) {
                ready = true;
                if (open) inchoice.notify();
            }
        }
    }

    boolean testReady() {
        return ready;
    }

    synchronized void clearReady() {
        ready = false;
    }

    void setOpen() {
        open=true;
    }

    void clearOpen() {
         open=false;
    }

    void guard(boolean g) {
        guard_=g;
    }

    boolean testGuard(){
        return guard_;
    }
}





/*********************CHANNEL*****************************/
// The definition of channel assumes that there is exactly one
// sender and one receiver.

class Channel extends Selectable{

    Object chan_ = null;

    public synchronized void out(Object v) {
        chan_ = v;
        signal();
        while (chan_ != null) {
            try {wait();} catch(InterruptedException e) {}
        }
    }

    public synchronized Object in() {
        block();
        clearReady();
        Object tmp = chan_; chan_ = null;
        notify();
        return(tmp);
    }
}

/**************************************************************/

class DisplayChannel extends Channel {
    ChannelCanvas disp_;
    Character ready = new Character('è');
    Character isOpen = null;

    DisplayChannel(ChannelCanvas disp) {
        disp_ = disp;
    }

    synchronized public void out(Object v) {
        disp_.display(v,isOpen);
        try{Thread.sleep(1000);} catch(InterruptedException e){}
        super.out(v);
     }

    void setOpen() {
        isOpen = ready;
        disp_.display(chan_,isOpen);
        super.setOpen();
    }

    void clearOpen() {
        isOpen = null;
        disp_.display(chan_,isOpen);
        super.clearOpen();
    }

    synchronized public Object in() {
        Object v = super.in();
        disp_.display(null,v);
        try{Thread.sleep(1000);} catch(InterruptedException e){}
        disp_.display(null,null);
        return (v);
    }

 }

/**************************************************************/

class ChannelCanvas extends Canvas {
    String title_;
    char[] chars_;
    boolean leftToRight_;

    Font f1 = new Font("Helvetica",Font.ITALIC+Font.BOLD,24);
    Font f2 = new Font("TimesRoman",Font.BOLD,36);

    ChannelCanvas(String title, boolean leftToRight) {
        super();
        leftToRight_ = leftToRight;
        title_=title;
        chars_=new char[2];
        chars_[0] = ' '; chars_[1] = ' ';
        resize(170,100);
        setBackground(Color.cyan);
  	}

    public void display(Object out, Object in){
        if (leftToRight_) {
            if (out!=null) chars_[0] = ((Character)out).charValue(); else chars_[0] = ' ';
            if (in!=null) chars_[1] = ((Character)in).charValue(); else chars_[1] = ' ';
        } else {
            if (out!=null) chars_[1] = ((Character)out).charValue(); else chars_[1] = ' ';
            if (in!=null) chars_[0] = ((Character)in).charValue(); else chars_[0] = ' ';
        }
        repaint();

    }

    public void paint(Graphics g) {
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
        // in & out Boxes
        y = size().height/2 - 15;
        offgraphics.setColor(Color.white);
        offgraphics.fillRect(10,y,50,50);
        offgraphics.fillRect(111,y,50,50);
        offgraphics.setColor(Color.black);
        offgraphics.setFont(f2);
        offgraphics.drawRect(10,y,50,50);
        offgraphics.drawRect(110,y,50,50);
        offgraphics.drawChars(chars_,0,1,25,y+35);
        offgraphics.drawChars(chars_,1,1,125,y+35);
        offgraphics.drawLine(60,y+24,110,y+24);
        offgraphics.drawLine(60,y+25,110,y+25);
        offgraphics.drawLine(60,y+26,110,y+26);
         g.drawImage(offscreen, 0, 0, null);
    }
}
