//@author: j.n.magee 11/11/96

import java.awt.*;
import java.applet.*;



/********************************************************/
//
// The Semaphore Class
// up() is the V operation
// down() is the P operation
//
class Semaphore {

    private int value_;

    Semaphore (int initial) {
        value_ = initial;
    }

    synchronized public void up() {
        ++value_;
        notify();
    }

    synchronized public void down() {
        while (value_== 0) {
            try {wait();} catch (InterruptedException e){}
        }
        --value_;
    }
}

/********************************************************/

class DisplaySemaphore extends Semaphore {

    TextCanvas display_;
    int count_; //shadow value for display

    DisplaySemaphore(TextCanvas t, int val) {
        super(val);
        count_=val;
        display_=t;
        display_.setcolor(Color.cyan);
        display_.setvalue(count_);
    }

    synchronized public void up() {
        super.up();
        ++count_;
        display_.setvalue(count_);
        try {Thread.sleep(200);} catch (InterruptedException e){}
    }

    synchronized public void down() {
       super.down();
       --count_;
       display_.setvalue(count_);
    }
}

/********************************************************/

class TextCanvas extends Canvas {
    int value_ = 0;
    String title_;

    Font f1 = new Font("Helvetica",Font.BOLD,36);
    Font f2 = new Font("Times",Font.ITALIC+Font.BOLD,24);

    TextCanvas(String title) {
        super();
        title_=title;
	}

    public void setcolor(Color c){
        setBackground(c);
        repaint();
    }

    public void setvalue(int newval){
        value_ = newval;
        repaint();
    }

    public void paint(Graphics g){

            g.setColor(Color.black);

         // Display the title
            g.setFont(f2);
            FontMetrics fm = g.getFontMetrics();
            int w = fm.stringWidth(title_);
            int h = fm.getHeight();
            int x = (size().width - w)/2;
            int y = h;
            g.drawString(title_, x, y);
            g.drawLine(x,y+3,x+w,y+3);


         // Display the value
            g.setFont(f1);
            fm = g.getFontMetrics();
            String s1 = String.valueOf(value_);
            w = fm.stringWidth(s1);
            h = fm.getHeight();
            x = (size().width - w)/2;
            y = (size().height+ h)/2;
            g.drawString(s1, x, y);
         }
}

/********************************************************/
