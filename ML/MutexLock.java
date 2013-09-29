//@author: j.n.magee 11/11/96

import java.awt.*;
import java.applet.*;

/********************************************************/

class Loop implements Runnable {

    Semaphore lock_;

    public Loop (Semaphore lock) {
        lock_=lock;
    }

    public void run() {
        int i=0;
        while(true)  {
            int s = DisplayThread.getSplit();
            for(;i < (60-s)/2;++i) DisplayThread.rotate();
            lock_.down();
            DisplayThread.mark();
            for(;i < 30+s/2;++i) DisplayThread.rotate();
            DisplayThread.unmark();
            lock_.up();
            for(;i < 60;++i)DisplayThread.rotate();
            i = 0;
        }
    }
}

/********************************************************/

public class MutexLock extends Applet {

    ThreadPanel thread1_;
    ThreadPanel thread2_;
    ThreadPanel thread3_;

    public void init() {
        setLayout(new BorderLayout());
        TextCanvas t = new TextCanvas("Mutex");
        Semaphore lock = new DisplaySemaphore(t,1);
        add("Center",t);
        Panel p = new Panel();
        p.add(thread1_=new ThreadPanel("Thread 1",Color.blue,new Loop(lock)));
        p.add(thread2_=new ThreadPanel("Thread 2",Color.blue,new Loop(lock)));
        p.add(thread3_=new ThreadPanel("Thread 3",Color.blue,new Loop(lock)));
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

