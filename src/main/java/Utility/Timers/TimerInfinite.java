package Utility.Timers;

public class TimerInfinite implements Runnable{
    private boolean finished;

    public TimerInfinite() {
        finished = false;
    }

    public void setFinished() {
        finished = true;
    }

    @Override
    public void run() {
        while (!finished) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        finished = false;
    }
}
