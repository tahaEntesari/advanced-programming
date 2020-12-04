package Deck;

import GameState.GameState;
import Hero.Hero;
import Card.*;
import User.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.smartcardio.Card;
import javax.swing.*;
import java.util.ArrayList;

import static LoggingModule.LoggingClass.logUser;

public class DeckFunctions {
    public static int changeDeckHero(Deck deck, User user, String heroNameToBecome){
        Hero deckHero = getHeroFromDeck(deck, user);
        if (deckHero.getDecks().size() == 1){
            return 400;
        } else {
            deleteDeck(deck, user);
            deck.setHero(heroNameToBecome);
            deckHero = getHeroFromDeck(deck, user);
            deckHero.addDeck(deck);
            if(deck.getCurrentNumberOfCardsInDeck() == deck.getDeckSize()) user.serializeUser();
            return 200;
        }
    }

    public static Hero getHeroFromDeck(Deck deck, User user){
        Hero deckHero = null;
        for (Hero hero : user.getHeroes()) {
            if(hero.getName().equals(deck.getHero())){
                deckHero = hero;
                break;
            }
        }
        return deckHero;
    }

    public static boolean heroChangeFeasible(Deck deck){
        String deckHero = deck.getHero();
        String cardName;
        for (Object o : deck.getDeckCards().keySet()) {
            cardName = (String) o;
            if(CardUtilityFunctions.getCardObjectFromName(cardName).getHeroClass().equals(deckHero)) return false;
        }
        return true;
    }
    public static int deleteDeck(Deck deck, User user){
        Hero deckHero = getHeroFromDeck(deck, user);
        if (deckHero.getDecks().size() == 1){
            return 400;
        } else {
            deckHero.deleteDeck(deck);
            return 200;
        }
    }

    public static int addCardToDeck(Deck deck, String cardName) {
        GameState gameState = GameState.getInstance();
        ArrayList<String> availableCardForThisHero = null;
        ArrayList<String> allCardsForThisHero = null;
        for (Hero hero : gameState.getUser().getHeroes()) {
            if (hero.getName().equals(deck.getHero())) {
                allCardsForThisHero = hero.getAllCards();
                availableCardForThisHero = hero.getAvailableCards();
            }
        }

        if (!cardInSet(allCardsForThisHero, cardName)) {
            logUser("Error. The entered card name is not in the list of cards for this hero.");
            return 404;
        }
        if (!cardInSet(availableCardForThisHero, cardName)) {
            logUser("Error. You do not own this card. You can buy it at the store.");
            return 401;
        }
        if (deck.getCurrentNumberOfCardsInDeck() == deck.getDeckSize()) {
            logUser("Error. The deck is at full capacity. First remove a card.");
            return 409;
        }
        int recurrence = cardInDeck(deck, cardName);
        if (recurrence == 2) {
            logUser("Error. There are already 2 instances of this card in your deck");
            return 403;
        }
        deck.addSingleCardToDeck(cardName, recurrence + 1);
        if (deck.getCurrentNumberOfCardsInDeck() == deck.getDeckSize()){
            GameState.getInstance().getUser().serializeUser();
        }
        return 200;
    }

    public static int removeCardFromDeck(Deck deck, String cardName) {
        GameState gameState = GameState.getInstance();
        ArrayList<String> allCardsForThisHero = null;
        for (Hero hero : gameState.getUser().getHeroes()) {
            if (hero.getName().equals(deck.getHero())) {
                allCardsForThisHero = hero.getAllCards();
            }
        }
        if (!cardInSet(allCardsForThisHero, cardName)) {
            logUser("Error. The entered card name is not in the list of cards for this hero.");
            return -1;
        }
        if (deck.getCurrentNumberOfCardsInDeck() == 0) {
            logUser("Error. The deck is already empty.");
            return -1;
        }
        int recurrence = cardInDeck(deck, cardName);
        if (recurrence == 0) {
            logUser("Error. There are no instances of this card in your deck.");
            return -1;
        }
        deck.removeSingleCardFromDeck(cardName, recurrence - 1);
        String message;
        message = "Successful. Removed 1 instance of the card from the deck. 1 remaining.";
        if (recurrence == 1) {
            message = "Successful. Removed card from deck.";
        }
        logUser(message);
        return recurrence - 1;

    }

    public static boolean cardInSet(ArrayList<String> set, String cardName) {
        return set.contains(cardName);
    }

    public static int cardInDeck(Deck deck, String cardName) {
        JSONObject deckCards = deck.getDeckCards();
        if (deckCards.get(cardName) == null) return 0;
        return (int) deckCards.get(cardName);
    }

    public static ArrayList<String> getDisjointSetOfTwo(ArrayList<String> largerSet, ArrayList<String> smallerSet) {
        ArrayList<String> disjoint = new ArrayList<>(largerSet);
        disjoint.removeAll(smallerSet);
        return disjoint;
    }


}
