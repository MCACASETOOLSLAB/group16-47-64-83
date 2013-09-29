
import java.awt.*;

class FixedPhilosopher extends Thread {

    int identity;
    boolean stopRequested = false;
    PhilCanvas view;
    Diners controller;
    Chopstick left;
    Chopstick right;

    FixedPhilosopher(Diners controller, int identity, Chopstick left, Chopstick right) {
        this.controller = controller;
        this.view = controller.display;
        this.identity = identity;
        this.left = left;
        this.right = right;
    }

    public void run() {
        while (!stopRequested) {
             try {
                //thinking
                view.setPhil(identity,view.THINKING);
                sleep(controller.slider.getValue()*(int)(100*Math.random()));
                //hungry
                view.setPhil(identity,view.HUNGRY);
                //get forks
                if (identity == 0) {
                    left.get();
                    view.setPhil(identity,view.GOTLEFT);
                } else {
                    right.get();
                    view.setPhil(identity,view.GOTRIGHT);
                }
                sleep(500);
                if (identity == 0)
                    right.get();
                else
                    left.get();
                //eating
                view.setPhil(identity,view.EATING);
                sleep(controller.slider.getValue()*(int)(50*Math.random()));
                right.put();
                left.put();
             } catch (java.lang.InterruptedException e) {}
        }
    }


    public void stopRequested() {
        stopRequested = true;
    }
}
