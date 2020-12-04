package GUI.Panels.PlayPanel.SelectionPanels;

import Arena.ArenaActionListener;
import Card.Cards;
import Configurations.GUIConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class DiscoverPanel {
    private JPanel mainPanel;
    private CardPanel[] cardPanels;
    private boolean firstPlayer;


    public DiscoverPanel(ArenaActionListener arenaActionListener) {
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(100, 200, 0, 200));
        mainPanel.setOpaque(true);
        mainPanel.setVisible(false);

        mainPanel.setLayout(null);
        cardPanels = new CardPanel[3];
        int count = 0;
        for (int i = 0; i < 3; i++) {
            cardPanels[i] = new CardPanel(arenaActionListener){
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    super.mouseClicked(mouseEvent);
                    arenaActionListener.finishDiscover();
                }
            };
            mainPanel.add(cardPanels[i].getMainPanel());
            cardPanels[i].setLocationAndBound((count) * GUIConfig.initialCardArenaXLocation, 0);
            count++;
            cardPanels[i].setVisibility(false);
        }
    }
    public void setPlayerAndCards(boolean firstPlayer, ArrayList<Cards.card> cards){
        mainPanel.setVisible(true);
        mainPanel.setOpaque(true);
        this.firstPlayer = firstPlayer;
        for (int i = 0; i < 3; i++) {
            cardPanels[i].setCard(cards.get(i));
            if (cards.get(i) != null) {
                cardPanels[i].setImage(cards.get(i).getName());
                cardPanels[i].setVisibility(true);
            }
        }
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Cards.card getSelected(){
        for (int i = 0; i < 3; i++) {
            if (cardPanels[i].isSelectedForChange()) return cardPanels[i].getCard();
        }
        return null;
    }

    public void reset(){
        mainPanel.setVisible(false);
        for (CardPanel cardPanel : cardPanels) {
            cardPanel.reset();
        }
    }
}
