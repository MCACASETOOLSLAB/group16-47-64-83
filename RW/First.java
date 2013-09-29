//@author: j.n.magee 11/11/96

import java.awt.*;
import java.applet.*;

/********************************************************/

class Loop implements Runnable {

    public void run() {
        int i=0;
        while(true)  {
             if (i==15) DisplayThread.mark();
            if (i==30) DisplayThread.unmark();
            DisplayThread.rotate();
            i = (i+1) % 60;
        }
    }
}

/********************************************************/

public class First extends Applet {

    ThreadPanel read1_;
    ThreadPanel read2_;
    ThreadPanel write1_;
    ThreadPanel write2_;

    public void init() {
        setLayout(new GridLayout(2,2));
        add(read1_=new ThreadPanel("Reader 1",Color.blue,new Loop()));
        add(read2_=new ThreadPanel("Reader 2",Color.blue,new Loop()));
        add(write1_=new ThreadPanel("Writer 1",Color.yellow,new Loop()));
        add(write2_=new ThreadPanel("Writer 2",Color.yellow,new Loop()));
     }

    public void start() {
        super.start();
    }

    public void stop() {
        read1_.stop();
        read2_.stop();
        write1_.stop();
        write1_.stop();
     }

}

