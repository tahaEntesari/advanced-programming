package GUI.Panels.CollectionPanels;

import Collection.Listeners.CollectionDeckActionListener;
import GUI.Panels.CollectionPanels.CollectionPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DeckOptionPanel {
    private JPanel mainPanel;

    public DeckOptionPanel(CollectionPanel collectionPanel) {
        mainPanel = new JPanel();
        completeDeckOptionsPanel(collectionPanel);
    }

    private void completeDeckOptionsPanel(CollectionPanel collectionPanel) {
        JButton renameDeck = new JButton("Rename Deck");
        JButton changeDeckHero = new JButton("Change Hero");
        JButton deckBackButton = new JButton("Back");
        JButton deleteDeck = new JButton("Delete Deck");
        JButton currentNumberOfCardsInDeck = new JButton("Number Of Cards");
        JButton setDeckAsHerosMainDeck = new JButton("Set as main deck");

        mainPanel.setLayout(new GridLayout(3, 2));

        mainPanel.add(renameDeck);
        mainPanel.add(changeDeckHero);
        mainPanel.add(deckBackButton);
        mainPanel.add(deleteDeck);
        mainPanel.add(setDeckAsHerosMainDeck);
        mainPanel.add(currentNumberOfCardsInDeck);

        ActionListener deckOptionsActionListener = new CollectionDeckActionListener(collectionPanel);

        renameDeck.addActionListener(deckOptionsActionListener);
        changeDeckHero.addActionListener(deckOptionsActionListener);
        deckBackButton.addActionListener(deckOptionsActionListener);
        deleteDeck.addActionListener(deckOptionsActionListener);
        setDeckAsHerosMainDeck.addActionListener(deckOptionsActionListener);

        currentNumberOfCardsInDeck.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                currentNumberOfCardsInDeck.setText("" + collectionPanel.getCurrentChosenDeck().getCurrentNumberOfCardsInDeck());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                currentNumberOfCardsInDeck.setText("Number Of Cards");
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
