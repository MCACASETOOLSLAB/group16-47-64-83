import java.awt.*;
import java.applet.*;

public class Diners extends Applet {

    PhilCanvas display;
    Thread[] phil= new Thread[PhilCanvas.NUMPHILS];
    Chopstick[] stick = new Chopstick[PhilCanvas.NUMPHILS];
    Scrollbar slider;
    Button restart;
    Button freeze;
    boolean fixed = false;

    public void init() {
        setLayout(new BorderLayout());
        String s = getParameter("Version");
        fixed = s.equals("FIXED");
        display = new PhilCanvas(this);
        add("Center",display);
        slider = new Scrollbar(Scrollbar.HORIZONTAL, 50, 5, 0, 100);
        restart = new Button("Restart");
        freeze = new Button("Freeze");
        Panel p1 = new Panel();
        p1.setLayout(new BorderLayout());
        p1.add("Center",slider);
        p1.add("East",restart);
        p1.add("West",freeze);
        add("South",p1);
    }

    Thread makePhilosopher(Diners d, int id, Chopstick left, Chopstick right) {
        if (fixed)
            return new FixedPhilosopher(d,id,left,right);
         else
            return new Philosopher(d,id,left,right);
        }

    public void start() {
       for (int i =0; i<display.NUMPHILS; ++i)
            stick[i] = new Chopstick(display,i);
       for (int i =0; i<display.NUMPHILS; ++i){
            phil[i] = makePhilosopher(this,i,
                        stick[(i-1+display.NUMPHILS)% display.NUMPHILS],
                        stick[i]);
            phil[i].start();
       }
    }

    public void stop() {
        for (int i =0; i<display.NUMPHILS; ++i)
            phil[i].stop();
    }

public boolean handleEvent(Event event) {
        if (event.id != event.ACTION_EVENT) {
            return super.handleEvent(event);
        } else if(event.target==restart) {
            if (display.deadlocked()) {
                stop();
                slider.setValue(50);
                start();
            }
            display.thaw();
            return true;
        } else if (event.target == freeze) {
            display.freeze();
            return true;
        } else
            return super.handleEvent(event);
    }
}
