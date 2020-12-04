package Shop;

import Card.InitiateCards;
import GUI.Panels.ShopPanels.ShopPanel;
import GameState.GameState;
import PanelFunctions.ShopAndCollectionVisualizationFunctions;

import javax.swing.*;
import java.awt.*;

public class DrawerFunctions {
    private ShopPanel shopPanel;
    private String [] heroClasses = InitiateCards.getHeroClass();

    public DrawerFunctions(ShopPanel shopPanel) {
        this.shopPanel = shopPanel;
    }
    public void resetVis() {
        shopPanel.setDisplayingHeroClass(0);
        refreshPage();

    }

    public void refreshPage() {
        shopPanel.setSorted(false);
        shopPanel.setDisplayingPageNumber(0);
        shopPanel.getShopToolbarPanel().setSearchTextFieldText("");
        shopPanel.requestFocus();
        if (shopPanel.getDisplayingBuyableOrSellable().equals("buy")) {
            shopPanel.setCardsOnDisplay(ShopAndCollectionVisualizationFunctions.
                    getPurchasableCards(GameState.getInstance().getUser(), heroClasses[shopPanel.getDisplayingHeroClass()]));

        } else if (shopPanel.getDisplayingBuyableOrSellable().equals("sell")) {
            shopPanel.setCardsOnDisplay(ShopAndCollectionVisualizationFunctions.
                    getSellableCards(GameState.getInstance().getUser(), heroClasses[shopPanel.getDisplayingHeroClass()]));
        }
        shopPanel.cardsToDraw();

        colorTheButtons();

        shopPanel.setDiamondAmount();
    }

    public void colorTheButtons() {
        JButton[] heroClassesButtons = shopPanel.getShopToolbarPanel().getHeroClassesButtons();

        heroClassesButtons[shopPanel.getDisplayingHeroClass()].setBackground(Color.RED);
        for (int i = 0; i < heroClasses.length; i++) {
            if (i == shopPanel.getDisplayingHeroClass()) continue;
            heroClassesButtons[i].setBackground(Color.WHITE);
        }
        if (shopPanel.getDisplayingBuyableOrSellable().equals("buy")) {
            shopPanel.getShopToolbarPanel().getDisplayBuyable().setBackground(Color.GREEN);
            shopPanel.getShopToolbarPanel().getDisplaySellable().setBackground(Color.WHITE);

        } else {
            shopPanel.getShopToolbarPanel().getDisplayBuyable().setBackground(Color.WHITE);
            shopPanel.getShopToolbarPanel().getDisplaySellable().setBackground(Color.GREEN);
        }
    }
}
