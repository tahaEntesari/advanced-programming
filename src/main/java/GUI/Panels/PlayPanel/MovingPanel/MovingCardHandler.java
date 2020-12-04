package GUI.Panels.PlayPanel.MovingPanel;

import javax.swing.*;
import java.awt.*;

public class MovingCardHandler implements Runnable {
    private MovingCard movingCard;
    private boolean finished;

    public MovingCardHandler(){
        movingCard = new MovingCard();
        finished = false;
    }

    public void setFinished() {
        this.finished = true;
    }

    public void setCard(String cardName){
        movingCard.setImage(cardName);
    }

    public JPanel getPanel(){
        return movingCard.getPanel();
    }

    @Override
    public void run() {
        Point p;
        while (!finished) {
            p = MouseInfo.getPointerInfo().getLocation();
            movingCard.setLocation(p);
        }
        finished = false;
        movingCard.setVisibility(false);
    }
}
