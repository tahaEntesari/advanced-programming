package Shop;

import Card.InitiateCards;
import GUI.Panels.ShopPanels.ShopPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static LoggingModule.LoggingClass.logUser;

public class ShopGeneralListener implements ActionListener {
    private ShopPanel shopPanel;

    public ShopGeneralListener(ShopPanel shopPanel) {
        this.shopPanel = shopPanel;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String message;
        switch (actionEvent.getActionCommand()) {
            case "Go":
                shopPanel.handleGo();
                break;
            case "Next":
                shopPanel.handleNext();
                break;
            case "Previous":
                shopPanel.handlePrevious();
                break;
            case "Reset":
                message = "Shop: Reset filters";
                shopPanel.setDisplayingBuyableOrSellable("buy");
                shopPanel.resetVis();
                logUser(message);
                break;
            case "Display Buyable":
                message = "Shop: Display Buyable cards";
                shopPanel.setDisplayingBuyableOrSellable("buy");
                shopPanel.refreshPage();
                logUser(message);
                break;
            case "Display Sellable":
                message = "Shop: Display Sellable cards";
                shopPanel.setDisplayingBuyableOrSellable("sell");
                shopPanel.refreshPage();
                logUser(message);
                break;
            default:
                String[] heroClasses = InitiateCards.getHeroClass();
                for (int i = 0; i < heroClasses.length; i++) {
                    if (actionEvent.getActionCommand().equals(
                            heroClasses[i].substring(0, shopPanel.getNumberOfCharactersOfHeroNameToDisplay()))) {
                        shopPanel.setDisplayingHeroClass(i);
                        break;
                    }
                }
                shopPanel.refreshPage();
                break;
        }
    }
}
