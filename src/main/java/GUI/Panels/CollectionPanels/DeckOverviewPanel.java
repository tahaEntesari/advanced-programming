package GUI.Panels.CollectionPanels;

import Configurations.GUIConfig;
import Deck.Deck;
import GUI.Panels.DeckButtonlikePanel;
import GameState.GameState;
import Hero.Hero;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DeckOverviewPanel {
    private JPanel mainPanel;
    private CollectionPanel collectionPanelMainClass;

    public DeckOverviewPanel(CollectionPanel collectionPanel) {
        mainPanel = new JPanel();
        this.collectionPanelMainClass = collectionPanel;
        mainPanel.setAutoscrolls(true);
        BoxLayout boxlayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(boxlayout);
        mainPanel.setPreferredSize(new Dimension(GUIConfig.cardWidthCollectionPanel,
                5 * GUIConfig.deckSinglePanelHeightCollection));

    }

    public void completeDecksPanel() {
        mainPanel.removeAll();
        ArrayList<String> deckNames = new ArrayList<>();
        ArrayList<String> decksCorrespondingHeroes = new ArrayList<>();
        for (Hero hero : GameState.getInstance().getUser().getHeroes()) {
            for (Deck deck : hero.getDecks()) {
                deckNames.add(deck.getDeckName());
                decksCorrespondingHeroes.add(hero.getName());
            }
        }
        ArrayList<DeckButtonlikePanel> decks = new ArrayList<>();
        for (String deckName : deckNames) {
            decks.add(new DeckButtonlikePanel(collectionPanelMainClass.getMapperForDeckButtonLike(),
                    GUIConfig.deckSinglePanelWidthCollection, GUIConfig.deckSinglePanelHeightCollection));
        }
        int count = 0;
        String deckHero;
        for (DeckButtonlikePanel panel : decks) {
            mainPanel.add(panel.getMainPanel());
            deckHero = decksCorrespondingHeroes.get(count);
            panel.setDeckName(deckNames.get(count), deckHero);
            count++;
        }
    }
    public void setMaximumSize(Dimension d){
        mainPanel.setMaximumSize(d);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
