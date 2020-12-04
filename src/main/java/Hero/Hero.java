package Hero;

import Card.Cards;
import Card.InitiateCards;
import Configurations.Main_config_file;
import Deck.Deck;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import Deck.DeckFunctions;

import java.io.Serializable;
import java.util.ArrayList;

import static LoggingModule.LoggingClass.logUser;
import static Utility.SerializationFunctions.cardDeserialize;

public class Hero implements Serializable {
    private String name;
    private int heroLevel;
    private String description;
    private int maxHealth;
    private ArrayList<Deck> decks = new ArrayList<>();
    private int chosenDeckIndex;
    private ArrayList<String> allCards = new ArrayList<>(); // This will hold all neutral cards plus this heroes
    // special cards.
    private ArrayList<String> availableCards = new ArrayList<>(); // Cards open for the current hero.
    private JSONArray allCardsOfHeroWithDetails;
    private JSONObject cardHeroClass;
    private Powers.heroPowers heroPower;

    private int numberOfGamesWithThisHero = 0;
    private int numberOfWinsWithThisHero = 0;
    private double rationOfWinsWithThisHero = 0;

    public Hero(String name, String description, boolean setAvailableCardsDefault, String heroPowerName,
                int heroPowerMana, String heroPowerDescription) {
        this.name = name;
        this.description = description;
        heroLevel = 1;
        setHealth();
        setAllCards();
        setInitialAvailableCards();
        if (setAvailableCardsDefault) {
            createNewDeck(HeroFunctions.getDefaultDeckCards(name), "Default deck");
            chosenDeckIndex = 0;
        }
        heroPower = new Powers.heroPowers(heroPowerName, heroPowerMana, heroPowerDescription);
        cardHeroClass = new JSONObject();
        completeCardHeroClass();
    }

    private void completeCardHeroClass() {
        for (String card : allCards) {
            Cards.card tempCard = cardDeserialize(Main_config_file.returnCardSaveDataLocation(card));
            cardHeroClass.put(card, tempCard.getHeroClass());
        }
    }

    public int createNewDeck(ArrayList<String> cardNames, ArrayList<Integer> cardRecurrences, String deckName) {
        /*
        This function creates a deck with recurrences mentioned in the parameters.
         */
        int sum = 0;
        for (Integer cardRecurrence : cardRecurrences) {
            sum += cardRecurrence;
        }
        if (sum != 20) {
            logUser("Error. Incorrect deck size.");
            return 400;
        }
        if (cardNames.size() != cardRecurrences.size()) {
            logUser("Error. The name list and the recurrence list don't match.");
            return 401;
        }
        JSONObject cards = new JSONObject();
        for (int i = 0; i < cardNames.size(); i++) {
            cards.put(cardNames.get(i), cardRecurrences.get(i));
        }
        Deck newDeck = new Deck(deckName, name, cards, allCardsOfHeroWithDetails);
        logUser("Deck creation successful");
        return 200;
    }

    public int createNewDeck(ArrayList<String> cardNames, String deckName) {
        /*
        This function takes as input an array of 10 names and creates a deck in which there are 2 occurrences of each
        card.
         */
        if (cardNames.size() > 10) {
            logUser("Error. The proposed deck has too many cards.");
            return 400;
        }
        JSONObject cards = new JSONObject();
        for (String cardName : cardNames) {
            cards.put(cardName, 2);
        }
        Deck newDeck = new Deck(deckName, name, cards, allCardsOfHeroWithDetails);
        decks.add(newDeck);
        logUser("Deck creation successful");
        return 200;
    }

    private void setHealth() {
        if ("Warlock".equals(name)) {
            maxHealth = 35;
        } else {
            maxHealth = 30;
        }
    }

    private void setAllCards() {
        allCards = HeroFunctions.getAllCardsForAHero(name);
        allCardsOfHeroWithDetails = HeroFunctions.getAllCardsForAHeroWithDetails(name);
    }

    private void setInitialAvailableCards() {
        availableCards = HeroFunctions.getAllAvailableCards(name);
    }


    public String getName() {
        return name;
    }

    public int getHeroLevel() {
        return heroLevel;
    }

    public int getNumberOfCardsOwnedByHero() {
        return availableCards.size();
    }

    public int getDeckSize() {
        return decks.get(chosenDeckIndex).getDeckSize();
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getAllCards() {
        return allCards;
    }

    public ArrayList<String> getAvailableCards() {
        return availableCards;
    }

    public JSONObject getCurrentDeckCards() {
        return decks.get(chosenDeckIndex).getDeckCards();
    }

    public ArrayList<String> getBuyableCards() {
        return DeckFunctions.getDisjointSetOfTwo(allCards, availableCards);
    }

    public ArrayList<String> getNeutralBuyableCards() {
        ArrayList<String> results = getBuyableCards();
        ArrayList<String> temp = new ArrayList<>(results);
        for (String s : temp) {
            if (!cardHeroClass.get(s).equals(InitiateCards.getHeroClass()[0])) {
                results.remove(s);
            }
        }
        return results;
    }

    public ArrayList<String> getHeroClassBuyableCards() {
        ArrayList<String> results = getBuyableCards();
        ArrayList<String> temp = getNeutralBuyableCards();
        results.removeAll(temp);
        return results;
    }

    public ArrayList<String> getSellableCards(){
        return new ArrayList<>(availableCards);
    }

    public ArrayList<String> getNeutralSellableCards() {
        ArrayList<String> results = getSellableCards();
        ArrayList<String> temp = new ArrayList<>(results);
        for (String s : temp) {

            if(!cardHeroClass.get(s).equals(InitiateCards.getHeroClass()[0])){
                results.remove(s);
            }
        }
        return results;
    }
    public ArrayList<String> getHeroClassSellableCards(){
        ArrayList<String> results = getSellableCards();
        ArrayList<String> temp = getNeutralSellableCards();
        results.removeAll(temp);
        return results;
    }


    public ArrayList<String> getAddableCards() {
        ArrayList<String> addableCards = new ArrayList<>(availableCards);
        ArrayList<String> cloneAddable = new ArrayList<>(addableCards);
        int maximumInstanceOfEachCard = decks.get(chosenDeckIndex).getMaximumInstanceOfEachCard();
        JSONObject temp = decks.get(chosenDeckIndex).getDeckCards();
        try {
            for (String st : cloneAddable) {
                if ((int) temp.get(st) == maximumInstanceOfEachCard) {
                    addableCards.remove(st);
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return addableCards;
    }

    public int changeDeck(String deckName) {
        if (decks.get(chosenDeckIndex).getDeckName().equals(deckName)) {
            logUser("This is the currently selected deck!");
            System.out.println(chosenDeckIndex);
            return 201;
        }
        int index = -1;
        for (int i = 0; i < decks.size(); i++) {
            if (decks.get(i).getDeckName().equals(deckName)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            logUser("Error. Incorrect deck name.");
            return 404;
        }
        chosenDeckIndex = index;
        logUser("Deck change successful.");
        return 200;
    }

    public int getCurrentNumberOfCardsInDeck() {
        return decks.get(chosenDeckIndex).getCurrentNumberOfCardsInDeck();
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void addToAvailableCards(String cardName) {
        availableCards.add(cardName);
    }

    public void removeFromAvailableCards(String cardName) {
        if (availableCards.contains(cardName))
            availableCards.remove(cardName);
    }

    public void deleteDeck(Deck deck){
        chosenDeckIndex = 0;
        decks.remove(deck);
    }

    public void addDeck(Deck deck){
        decks.add(deck);
    }

    public JSONArray getAllCardsOfHeroWithDetails() {
        return allCardsOfHeroWithDetails;
    }

    public void incrementNumberOfGamesPlayedByHero(boolean win){
        numberOfGamesWithThisHero++;
        if (win) numberOfWinsWithThisHero++;
        decks.get(chosenDeckIndex).incrementNumberOfGamesPlayedWithThisDeck(win);
        calculateRatioOfWins();
    }
    private void calculateRatioOfWins(){
        if (numberOfGamesWithThisHero == 0) rationOfWinsWithThisHero = 0;
        else
            rationOfWinsWithThisHero = 100 * (double) numberOfWinsWithThisHero / (double) numberOfGamesWithThisHero;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public Powers.heroPowers getHeroPower() {
        return heroPower;
    }

    public Deck getCurrentDeck(){
        return decks.get(chosenDeckIndex);
    }
}
