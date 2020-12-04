package GUI.Panels.PlayPanel.SelectionPanels;

import Arena.ArenaActionListener;
import Card.Cards;
import Configurations.GUIConfig;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InitialCardChangePanel{
    private JPanel mainPanel;
    private CardPanel[] cardPanels;
    private boolean firstPlayer;


    public InitialCardChangePanel(ArenaActionListener arenaActionListener) {
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(100, 200, 0, 200));
        mainPanel.setOpaque(true);
        mainPanel.setVisible(false);

        mainPanel.setLayout(null);
        cardPanels = new CardPanel[3];
        int count = 0;
        for (int i = 0; i < 3; i++) {
            cardPanels[i] = new CardPanel(arenaActionListener);
            mainPanel.add(cardPanels[i].getMainPanel());
            cardPanels[i].setLocationAndBound((count) * GUIConfig.initialCardArenaXLocation, 0);
            count++;
            cardPanels[i].setVisibility(false);
        }
        JButton changeOkButton = new JButton("Finish Card Change");
        mainPanel.add(changeOkButton);
        changeOkButton.setBounds(GUIConfig.initialCardArenaXLocation, GUIConfig.initialCardArenaYLocation,
                GUIConfig.initialCardArenaXLocation, 100);
        changeOkButton.addActionListener(arenaActionListener);
    }
    public void setPlayerAndCards(boolean firstPlayer, ArrayList<Cards.card> cards){
        mainPanel.setVisible(true);
        this.firstPlayer = firstPlayer;
        for (int i = 0; i < 3; i++) {
            cardPanels[i].setImage(cards.get(i).getName());
            cardPanels[i].setVisibility(true);
        }
        System.out.println("set cards.");
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public ArrayList<Integer> getThoseSelectedForChange(){
        ArrayList<Integer> selected = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (cardPanels[i].isSelectedForChange()) selected.add(i);
        }
        return selected;
    }

    public void reset(){
        mainPanel.setVisible(false);
        for (CardPanel cardPanel : cardPanels) {
            cardPanel.reset();
        }
    }
}
