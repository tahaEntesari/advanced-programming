package GUI.Panels.CollectionPanels;

import javax.swing.*;

import Collection.Listeners.DeckDisplayButtonActionListener;
import org.json.simple.JSONObject;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DeckDisplayPanel {
    private JPanel deckDisplayPanel;
    private BoxLayout boxLayout;
    private DeckOptionPanel deckOptionPanel;

    public DeckDisplayPanel(CollectionPanel collectionPanel) {
        deckDisplayPanel = new JPanel();
        deckDisplayPanel.setAutoscrolls(true);
        boxLayout = new BoxLayout(deckDisplayPanel, BoxLayout.Y_AXIS);
        deckDisplayPanel.setLayout(boxLayout);

        deckOptionPanel = new DeckOptionPanel(collectionPanel);
    }
    public void showDeckCards(CollectionPanel collectionPanelMainClass){
        deckDisplayPanel.removeAll();
        JLabel description = new JLabel("Displaying " + collectionPanelMainClass.getCurrentChosenDeck().getHero() + ": "
                + collectionPanelMainClass.getCurrentChosenDeck().getDeckName());
        deckDisplayPanel.add(description);
        ActionListener actionListenerForDeckButtons = new DeckDisplayButtonActionListener(collectionPanelMainClass);
        JSONObject deckCards = collectionPanelMainClass.getCurrentChosenDeck().getDeckCards();
        ArrayList<JButton> cardButtons = new ArrayList<>();
        ArrayList<JPanel> cardButtonContainers = new ArrayList<>();
        int i = 0;
        for (Object o : deckCards.keySet()) {
            String cardName = (String) o;
            cardButtons.add(new JButton(cardName + ": " + deckCards.get(cardName)));
            cardButtons.get(i).addActionListener(actionListenerForDeckButtons);
            cardButtonContainers.add(new JPanel());
            cardButtonContainers.get(i).setLayout(new BorderLayout());
            cardButtonContainers.get(i).add(cardButtons.get(i), BorderLayout.CENTER);
            deckDisplayPanel.add(cardButtonContainers.get(i));
            i++;
        }

        deckDisplayPanel.add(deckOptionPanel.getMainPanel());

        deckDisplayPanel.repaint();
        deckDisplayPanel.revalidate();
    }

    public JPanel getDeckDisplayPanel() {
        return deckDisplayPanel;
    }
}
