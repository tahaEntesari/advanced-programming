package Shop;

import Card.CardUtilityFunctions;
import Card.InitiateCards;
import Deck.DeckFunctions;
import GUI.Panels.ShopPanels.ShopPanel;
import User.User;
import Utility.ClosestMatch;

import static LoggingModule.LoggingClass.logUser;

public class ShopEventHandlerFunctions {
    private ShopPanel shopPanel;

    public ShopEventHandlerFunctions(ShopPanel shopPanel) {
        this.shopPanel = shopPanel;
    }

    public void handleGo(User user) {
        String cardToSearchFor = shopPanel.getShopToolbarPanel().getSearchTextField().getText();
        if (cardToSearchFor.equals("")) return;
        logUser("Searched for card " + cardToSearchFor + " in shop.");

        String closestMatch = ClosestMatch.getClosestMatch(cardToSearchFor, user.getAllCards());

        for (int i = 0; i < InitiateCards.getHeroClass().length; i++) {
            if (CardUtilityFunctions.getCardObjectFromName(closestMatch).
                    getHeroClass().equals(InitiateCards.getHeroClass()[i])) {
                shopPanel.setDisplayingHeroClass(i);
                break;
            }
        }
        if (DeckFunctions.cardInSet(user.getAvailableCards(), closestMatch)) {
            shopPanel.setDisplayingBuyableOrSellable("sell");

        } else {
            shopPanel.setDisplayingBuyableOrSellable("buy");
        }
        shopPanel.cardsToDraw(closestMatch);
    }

    public void handleNext() {
        String message = "Shop: Next page command issued.";
        if (8 * (shopPanel.getDisplayingPageNumber() + 1) < shopPanel.getCardsOnDisplay().size()) {
            shopPanel.setDisplayingPageNumber(shopPanel.getDisplayingPageNumber() + 1);
            shopPanel.cardsToDraw();
        } else {
            message += " Last page reached!";
        }
        logUser(message);
    }

    public void handlePrevious() {
        String message = "Shop: Previous page command issued.";
        if (shopPanel.getDisplayingPageNumber() > 0) {
            shopPanel.setDisplayingPageNumber(shopPanel.getDisplayingPageNumber() - 1);
            shopPanel.cardsToDraw();
        } else {
            message += " Already on the first page!";
        }
        logUser(message);
    }

    public void handleUpDown(boolean up) {
        String message = "Shop: change hero class via keyboard.";
        boolean successful = false;
        if (up) {
            if (shopPanel.getDisplayingHeroClass() < InitiateCards.getHeroClass().length - 1) {
                shopPanel.setDisplayingHeroClass(shopPanel.getDisplayingHeroClass() + 1);
                shopPanel.refreshPage();
                successful = true;
            }
        } else {
            if (shopPanel.getDisplayingHeroClass()> 0) {
                shopPanel.setDisplayingHeroClass(shopPanel.getDisplayingHeroClass() - 1);
                shopPanel.refreshPage();
                successful = true;
            }

        }
        if (successful) message += " Successful.";
        else message += " On last page of side.";
        logUser(message);
    }

}
