package GUI.Panels.ShopPanels;

import Card.CardUtilityFunctions;
import Configurations.GUIConfig;
import GUI.Panels.CardPanel.CardPanelForShop;
import GUI.UtilityFunctions.GetImageLocation;
import PanelFunctions.ShopAndCollectionVisualizationFunctions;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainCardDisplayPanel {
    private JPanel mainPanel;
    private CardPanelForShop[] cardPanels = new CardPanelForShop[8];
    private ShopPanel shopPanel;

    public MainCardDisplayPanel(ShopPanel shopPanel) {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 4));
        this.shopPanel = shopPanel;
        completeTheCardPanelsAndAddToShopPanel();
    }
    private void completeTheCardPanelsAndAddToShopPanel() {
        for (int i = 0; i < 8; i++) {
            cardPanels[i] = new CardPanelForShop(GUIConfig.cardWidthShopPanel,
                    GUIConfig.cardHeightShopPanel, shopPanel);
            cardPanels[i].setImage("./assets/testCard.png");
            mainPanel.add(cardPanels[i].getMainPanel());
        }
    }

    public void cardsToDraw(ArrayList<String> cardNames) {
        ArrayList<String> sortedCards = ShopAndCollectionVisualizationFunctions.sortCardsBasedOnMana(cardNames);
        int i = 0;
        String cardName;
        for (; i < 8; i++) {
            if (i + 8 * shopPanel.getDisplayingPageNumber() >= sortedCards.size()) {
                cardPanels[i].getMainPanel().setVisible(false);
            } else {
                cardName = sortedCards.get(8 * shopPanel.getDisplayingPageNumber() + i);
                updateCardPanelsSpecifications(cardPanels[i], cardName);
            }
        }
        shopPanel.revalidate();
        shopPanel.repaint();
    }

    public void cardsToDraw(String cardName) {
        updateCardPanelsSpecifications(cardPanels[0], cardName);
        for (int i = 1; i < 8; i++) {
            cardPanels[i].getMainPanel().setVisible(false);
        }
        shopPanel.colorTheButtons();
        shopPanel.revalidate();
        shopPanel.repaint();
    }

    private void updateCardPanelsSpecifications(CardPanelForShop cardPanel, String cardName) {
        cardPanel.getMainPanel().setVisible(true);
        cardPanel.setPrice(CardUtilityFunctions.getCardPrice(cardName));
        cardPanel.setTransactionType(shopPanel.getDisplayingBuyableOrSellable());
        cardPanel.setCardName(cardName);
        cardPanel.setImage(GetImageLocation.getCardImageLocation(cardName));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
