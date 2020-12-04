package GUI.Panels.PlayPanel;

import Arena.ArenaActionListener;

import javax.swing.*;
import java.awt.*;

public class EndTurnPanel {
    private JPanel mainPanel;
    private JLabel firstPlayerNumberOfCardsInDeck, secondPlayerNumberOfCardsInDeck;
    private JButton endTurnButton;

    public EndTurnPanel(ArenaActionListener arenaActionListener){
        mainPanel = new JPanel();
        endTurnButton = new JButton("End Turn");
        endTurnButton.addActionListener(arenaActionListener);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setVisible(true);
        mainPanel.add(endTurnButton, BorderLayout.CENTER);

        firstPlayerNumberOfCardsInDeck = new JLabel("Cards in deck: 20");
        firstPlayerNumberOfCardsInDeck.setForeground(Color.CYAN);
        secondPlayerNumberOfCardsInDeck = new JLabel("Cards in deck: 20");
        secondPlayerNumberOfCardsInDeck.setForeground(Color.CYAN);

        mainPanel.add(firstPlayerNumberOfCardsInDeck, BorderLayout.SOUTH);
        mainPanel.add(secondPlayerNumberOfCardsInDeck, BorderLayout.NORTH);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updateNumberOfCardsInDeckLabel(boolean firstPlayer, int number){
        if (firstPlayer) firstPlayerNumberOfCardsInDeck.setText("Cards in deck: " + number);
        else secondPlayerNumberOfCardsInDeck.setText("Cards in deck: " + number);
    }

    public void updateNumberOfCardsInDeckLabel(int first, int second){
        updateNumberOfCardsInDeckLabel(true, first);
        updateNumberOfCardsInDeckLabel(false, second);
    }

    public void setBounds(int x, int y, int width, int height){
        mainPanel.setBounds(new Rectangle(x, y, width, height));
    }
}
