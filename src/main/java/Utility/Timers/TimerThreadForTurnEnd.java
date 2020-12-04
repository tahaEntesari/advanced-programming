package Utility.Timers;

import Arena.ArenaEventHandler;
import Configurations.GameConfig;
import GUI.Panels.PlayPanel.PlayPanel;

import javax.swing.*;
import java.awt.*;

public class TimerThreadForTurnEnd implements Runnable {
    private long startTime;
    private boolean warned = false;
    private JPanel timerPanel;
    private JProgressBar progressBar;
    private boolean inPlayPanel = true;
    private ArenaEventHandler arenaEventHandler;

    public TimerThreadForTurnEnd(ArenaEventHandler arenaEventHandler) {
        startTime = System.currentTimeMillis();
        timerPanel = new JPanel();
        timerPanel.setVisible(true);
        timerPanel.setOpaque(false);
        progressBar = new JProgressBar(SwingConstants.VERTICAL);
        progressBar.setValue(100);
        progressBar.setForeground(Color.GREEN);
        timerPanel.add(progressBar);
        this.arenaEventHandler = arenaEventHandler;

    }

    @Override
    public void run() {
        int currentStatus;
        outer: while (true) {
            while (System.currentTimeMillis() < startTime + GameConfig.turnTimeOut * 1000) {
                if (!inPlayPanel) break outer;
                currentStatus = (GameConfig.turnTimeOut - (int) ((System.currentTimeMillis() - startTime) / 1000))
                        * 100 / GameConfig.turnTimeOut;
                progressBar.setValue(currentStatus);

                if (!warned && System.currentTimeMillis() > startTime + GameConfig.turnTimeOutWarning * 1000) {
                    warned = true;
                    progressBar.setForeground(Color.RED);
                    arenaEventHandler.warnTimeOut();
                }
            }
            arenaEventHandler.executeRound();
            resetTimer();
        }
    }

    public void resetTimer(){
        startTime = System.currentTimeMillis();
        progressBar.setValue(100);
        progressBar.setForeground(Color.GREEN);
        warned = false;
    }

    public void endTimer(){
        inPlayPanel = false;
    }

    public void resetThreadSettings(){
        resetTimer();
        inPlayPanel = true;
    }

    public JPanel getTimerPanel() {
        return timerPanel;
    }
}
