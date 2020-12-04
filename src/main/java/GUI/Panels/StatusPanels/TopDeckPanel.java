package GUI.Panels.StatusPanels;

import Configurations.GUIConfig;
import GUI.Panels.DeckButtonlikePanel;
import Deck.Deck;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class TopDeckPanel {
    private JPanel mainPanel;
    private StatusPanel statusPanel;
    private ArrayList<DeckButtonlikePanel> deckPanels;
    private ArrayList<Deck> topDecks;
    private boolean addedToPanel = false;

    public TopDeckPanel(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
        mainPanel = new JPanel();
        BoxLayout boxlayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(boxlayout);
        Border innerBorder = BorderFactory.createTitledBorder("Top Decks");
        Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
        mainPanel.setMinimumSize(new Dimension(GUIConfig.topDeckButtonWidth, GUIConfig.height));

        deckPanels = new ArrayList<>();
    }

    public void updateDecks(){
        topDecks = Status.GetTopDecks.getTopDecks();
        updatePanels();
        addPanelsToMain();
        addedToPanel = true;

    }

    private void updatePanels(){
        deckPanels.clear();
        for (int i = 0; i < topDecks.size(); i++) {
            deckPanels.add(new DeckButtonlikePanel(statusPanel.getMapperForDeckButtonLike(),
                    GUIConfig.deckWidthTopDeckStatusPanel, GUIConfig.deckHeightTopDeckStatusPanel));
            deckPanels.get(i).setDeckName(topDecks.get(i).getDeckName(), topDecks.get(i).getHero());
        }

    }
    private void addPanelsToMain(){
        mainPanel.removeAll();
        for (DeckButtonlikePanel panel : deckPanels) {
            mainPanel.add(panel.getMainPanel());
        }
        mainPanel.repaint();
        mainPanel.revalidate();
    }



    public JPanel getMainPanel() {
        return mainPanel;
    }

}
