package GUI.Panels.CollectionPanels;

import Configurations.GUIConfig;
import GUI.Panels.CardPanel.CardPanelForCollection;
import GUI.UtilityFunctions.GetImageLocation;
import PanelFunctions.ShopAndCollectionVisualizationFunctions;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainCardDisplayPanel {
    private JPanel mainPanel;
    private CardPanelForCollection[] cardPanels = new CardPanelForCollection[8];
    private CollectionPanel collectionPanel;

    public MainCardDisplayPanel(CollectionPanel collectionPanel){
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 4));
        this.collectionPanel = collectionPanel;
        completeCardPanelsAndAddToCardsPanel();
    }

    public void cardsToDraw(ArrayList<String> cardNames) {
        ArrayList<String> sortedCards = ShopAndCollectionVisualizationFunctions.sortCardsBasedOnMana(cardNames);
        int i = 0;
        String cardName;
        for (; i < 8; i++) {
            if (i + 8 * collectionPanel.getDisplayingPageNumber() >= sortedCards.size()) {
                cardPanels[i].getMainPanel().setVisible(false);
            } else {
                cardName = sortedCards.get(8 * collectionPanel.getDisplayingPageNumber() + i);
                updateCardPanelsSpecifications(cardPanels[i], cardName);

            }
        }
        collectionPanel.revalidate();
        collectionPanel.repaint();
    }

    public void cardsToDraw(String cardName) {
        updateCardPanelsSpecifications(cardPanels[0], cardName);
        for (int i = 1; i < 8; i++) {
            cardPanels[i].getMainPanel().setVisible(false);
        }
        collectionPanel.colorTheButtons();
        collectionPanel.revalidate();
        collectionPanel.repaint();
    }

    private void updateCardPanelsSpecifications(CardPanelForCollection cardPanel, String cardName) {
        cardPanel.getMainPanel().setVisible(true);
        cardPanel.setCardName(cardName);
        cardPanel.setCardAvailability();
        cardPanel.setImage(GetImageLocation.getCardImageLocation(cardName));
        GUI.UtilityFunctions.ToolTip.setTextToolTip(cardPanel.getMainPanel(), cardName);
    }

    private void completeCardPanelsAndAddToCardsPanel() {
        for (int i = 0; i < 8; i++) {
            cardPanels[i] = new CardPanelForCollection(GUIConfig.cardWidthCollectionPanel,
                    GUIConfig.cardHeightCollectionPanel, collectionPanel);
            cardPanels[i].setImage("./assets/testCard.png");
            mainPanel.add(cardPanels[i].getMainPanel());
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
