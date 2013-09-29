//@author: j.n.magee 11/11/96

import java.awt.*;
import java.applet.*;

interface Counter{
    public void inc(); // increment Counter value
    public void dec(); // decrement Counter value
    public int value(); // return value of counter
}

class DisplayCounter implements Counter{

    TextCanvas display_;
    int count_;

    DisplayCounter(TextCanvas t, int val) {
        count_= val;
        display_=t;
        display_.setcolor(Color.cyan);
        display_.setvalue(count_);
    }

    public void inc() {
        ++count_;
        display_.setvalue(count_);
        try {Thread.sleep(200);} catch (InterruptedException e){}
    }

    public void dec() {
        --count_;
       display_.setvalue(count_);
    }

    public int value() {
        return count_;
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
        resize(100,100);
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
