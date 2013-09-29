//@author: j.n.magee 11/11/96

import java.awt.*;
import java.applet.*;

/********************************************************/

class Gate implements Runnable {

    private Channel chan_;
    private Character signal = new Character('#');
    private int rate_;

    public Gate (Channel c, int rate) {
        chan_=c;
        rate_=rate;
    }

    public void run() {
          while(true)  {
            for(int i=0;i < rate_;++i) DisplayThread.rotate();
            chan_.out(signal);
            DisplayThread.rotate();
          }
    }
}

/********************************************************/

class Place implements Runnable {
    private Channel arrive;
    private Channel leave;
    private Counter count;
    private int N;

    public Place(Channel l, Channel a, Counter i,int space) {
        leave=l;
        arrive=a;
        count=i;
        N=space;
    }


    public void run() {
        Select sel = new Select();
        sel.add(leave);
        sel.add(arrive);
        while(true) {
           for(int i=0;i<15;i++) DisplayThread.rotate();
           leave.guard(count.value()>0);
           arrive.guard(count.value()<N);
           switch (sel.choose()) {
            case 1:
                leave.in();
                count.dec();
                break;
            case 2:
                arrive.in();
                count.inc();
                break;
            }
        }
    }
}

 /********************************************************/



public class CarPark extends Applet {

    ThreadPanel thread1_;
    ThreadPanel thread2_;
    ThreadPanel thread3_;

    public void init() {
        setLayout(new BorderLayout());
        //counter
        TextCanvas t = new TextCanvas("CarPark");
        Counter c = new DisplayCounter(t,0);
        //channels
        ChannelCanvas l = new ChannelCanvas("Leave",false);
        ChannelCanvas a = new ChannelCanvas("Arrive",true);
        Channel leave = new DisplayChannel(l);
        Channel arrive = new DisplayChannel(a);

        Panel center = new Panel();
        center.add(a);
        center.add(t);
        center.add(l);
        add("Center",center);
        //threads
        Panel p = new Panel();
        p.add(thread3_=new ThreadPanel("Entry Gate",Color.blue,new Gate(arrive,29)));
        p.add(thread2_=new ThreadPanel("Place",Color.yellow,new Place(leave,arrive,c,4)));
        p.add(thread1_=new ThreadPanel("Exit Gate",Color.blue,new Gate(leave,59)));
        add("South",p);
    }

    public void start() {
        super.start();
    }

    public void stop() {
        thread1_.passivate();
        thread2_.passivate();
        thread3_.passivate();
    }

    public void destroy() {
        thread1_.stop();
        thread2_.stop();
        thread3_.stop();
    }
}

