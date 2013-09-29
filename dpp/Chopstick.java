import java.awt.*;

class Chopstick {

    boolean taken=false;
    PhilCanvas display;
    int identity;

    Chopstick(PhilCanvas display, int identity) {
        this.display = display;
        this.identity = identity;
    }

    synchronized void put() {
        taken=false;
        display.setChop(identity,taken);
        notify();
    }

    synchronized void get() throws java.lang.InterruptedException {
        while (taken)
            wait();
        taken=true;
        display.setChop(identity,taken);
    }
}
