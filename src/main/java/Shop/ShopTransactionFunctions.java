package Shop;

import Card.Cards;
import Card.InitiateCards;
import Configurations.Main_config_file;
import GUI.Panels.ShopPanels.ShopPanel;
import GameState.GameState;
import Hero.Hero;
import Hero.HeroFunctions;
import User.User;

import javax.swing.*;
import java.util.ArrayList;

import static Deck.DeckFunctions.cardInSet;
import static LoggingModule.LoggingClass.logUser;
import static Utility.SerializationFunctions.cardDeserialize;

public class ShopTransactionFunctions {
    public static void issueTransaction(String cardName, ShopPanel shopPanel) {
        int transactionResult;
        boolean toRefreshOrNot = false;
        String transactionResultMessage = "";
        if (shopPanel.getDisplayingBuyableOrSellable().equals("buy")) {
            transactionResult = ShopTransactionFunctions.buyCard(cardName);
            switch (transactionResult) {
                case 404:
                    transactionResultMessage = "Error. The entered card name is not in the list of cards of the game.";
                    break;
                case 400:
                    transactionResultMessage = "Error. You already own this card";
                    break;
                case 402:
                    transactionResultMessage = "Error. Insufficient balance";
                    break;
                case 200:
                    transactionResultMessage = "Purchase successful. Card added to your collection.";
                    toRefreshOrNot = true;
                    break;
            }
        } else {
            transactionResult = ShopTransactionFunctions.sellCard(cardName);
            switch (transactionResult) {
                case 404:
                    transactionResultMessage = "Error. The entered card name is not in the list of cards of the game.";
                    break;
                case 400:
                    transactionResultMessage = "Error. You currently don't own this card.";
                    break;
                case 403:
                    ArrayList<String> occurrences = ShopTransactionFunctions.findDecksContainingCard(
                            GameState.getInstance().getUser(), cardName);
                    StringBuilder message = new StringBuilder();
                    message.append("Error. The card you want to sell is in at least one deck. " +
                            "First remove it form these decks:\n");
                    for (String occurrence : occurrences) {
                        message.append(occurrence).append(" ");
                    }
                    transactionResultMessage = message.toString();
                    break;
                case 200:
                    transactionResultMessage = "Transaction successful. Card removed from your collection.";
                    toRefreshOrNot = true;
                    break;
            }
        }
        JOptionPane.showMessageDialog(shopPanel.getMainPanel(), transactionResultMessage);
        if (toRefreshOrNot) shopPanel.refreshPage();
    }

    public static int buyCard(String cardName) {
        User user = GameState.getInstance().getUser();
        if (!cardInSet(user.getAllCards(), cardName)) {
            logUser("Error. The entered card name is not in the list of cards of the game.");
            return 404;
        }
        if (cardInSet(user.getAvailableCards(), cardName)) {
            logUser("Error. You already own this card");
            return 400;
        }
        Cards.card tempCard = cardDeserialize(Main_config_file.returnCardSaveDataLocation(cardName));
        // CHECK WALLET BALANCE
        int price = tempCard.getPrice();
        int currentBalance = user.getWalletBalance();
        if (price > currentBalance) {
            logUser("Error. Insufficient balance");
            return 402;
        }
        // If wallet balance is also ok, reduce the card cost from the balance
        user.setWalletBalance(currentBalance - price);
        user.getAvailableCards().add(cardName);
        addToRelevantHeroLists(user, tempCard);
        logUser("Purchase successful. Card added to your collection.");
        user.serializeUser();
        return 200;
    }

    public static int sellCard(String cardName) {
        User user = GameState.getInstance().getUser();
        // The current implementation uses the fact that complement cards are created when this command is called
        // and using this, prevents buying cards already in you available cards
        if (!cardInSet(user.getAllCards(), cardName)) {
            logUser("Error. The entered card name is not in the list of cards of the game.");
            return 404;
        }
        if (!cardInSet(user.getAvailableCards(), cardName)) {
            logUser("Error. You currently don't own this card.");
            return 400;
        }
        ArrayList<String> occurrences = findDecksContainingCard(user, cardName);
        if (occurrences.size() > 0) {
            StringBuilder message = new StringBuilder();
            message.append("Error. The card you want to sell is in at least one deck. " +
                    "First remove it form these decks:\n");
            for (String occurrence : occurrences) {
                message.append(occurrence).append(" ");
            }
            logUser(message.toString());
            return 403;
        }
        Cards.card tempCard = cardDeserialize(Main_config_file.returnCardSaveDataLocation(cardName));
        user.setWalletBalance(user.getWalletBalance() + tempCard.getPrice());
        user.getAvailableCards().remove(cardName);
        removeFromRelevantHeroLists(user, tempCard);
        logUser("Transaction successful. Card removed from your collection.");
        user.serializeUser();
        return 200;
    }

    public static ArrayList<String> findDecksContainingCard(User user, String cardName) {
        ArrayList<String> occurences = new ArrayList<>();
        for (Hero hero : user.getHeroes()) {
            occurences.addAll(HeroFunctions.cardInWhichDecks(hero.getDecks(), cardName, hero.getName()));
        }
        return occurences;
    }

    private static void addToRelevantHeroLists(User user, Cards.card card) {
        if (card.getHeroClass().equals(InitiateCards.getHeroClass()[0])) {
            for (Hero hero : user.getHeroes()) {
                hero.addToAvailableCards(card.getName());
            }
        } else {
            for (int i = 0; i < user.getHeroes().size(); i++) {
                if (card.getHeroClass().equals(user.getHeroes().get(i).getName())) {
                    user.getHeroes().get(i).addToAvailableCards(card.getName());
                }
            }
        }
    }

    private static void removeFromRelevantHeroLists(User user, Cards.card card) {
        if (card.getHeroClass().equals(InitiateCards.getHeroClass()[0])) {
            for (Hero hero : user.getHeroes()) {
                hero.removeFromAvailableCards(card.getName());
            }
        } else {
            for (int i = 0; i < user.getHeroes().size(); i++) {
                if (card.getHeroClass().equals(user.getHeroes().get(i))) {
                    user.getHeroes().get(i).removeFromAvailableCards(card.getName());
                }
            }
        }
    }

}
