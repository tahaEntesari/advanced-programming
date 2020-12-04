package Utility.Timers;

import GUI.Panels.PlayPanel.PlayPanel;

public class TimerThreadForCardDisappearance implements Runnable{
    private int amountToWaitInMilliSeconds;
    private long startingTime;
    private PlayPanel playPanel;

    public TimerThreadForCardDisappearance(int amountToWaitInMilliSeconds, PlayPanel playPanel) {
        startingTime = System.currentTimeMillis();
        this.amountToWaitInMilliSeconds = amountToWaitInMilliSeconds;
        this.playPanel = playPanel;
    }

    @Override
    public void run() {
        while(System.currentTimeMillis() < startingTime + amountToWaitInMilliSeconds);
        playPanel.hideSpellPanel();

    }
}