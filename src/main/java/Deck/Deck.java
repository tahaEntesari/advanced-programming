package Deck;

import Card.InitiateCards;
import GameState.GameState;
import Hero.Hero;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.Random;

public class Deck implements Serializable, Comparable<Deck> {
    private String deckName;
    private String hero;
    private int numberOfGamesWithThisDeck = 0;
    private int numberOfWinsWithThisDeck = 0;
    private double averageManaCost;
    private double ratioOfWinsWithThisDeck;
    private JSONObject cardUsageRecord;  // in the form: "name:numberOfCardUsages
    private JSONObject deckCards;  // in the form: "name":numberOfOccurrences
    private JSONArray allCardsOfThisHeroWithDetails;
    private int maximumInstanceOfEachCard = 2;
    private int currentNumberOfCardsInDeck = 20;
    private int deckSize = 20;

    public Deck(String deckName, String hero, JSONObject deckCards, JSONArray allCardsOfThisHeroWithDetails) {
        this.deckName = deckName;
        this.hero = hero;
        this.deckCards = deckCards;
        this.allCardsOfThisHeroWithDetails = allCardsOfThisHeroWithDetails;
        ratioOfWinsWithThisDeck = 0;
        setInitialCardUsageRecord();
        calculateDeckAverageManaCost();
    }

    public Deck(String deckName, String hero) {
        this.deckName = deckName;
        this.hero = hero;
        setAllCardsOfThisHeroWithDetails();
        deckCards = new JSONObject();
        cardUsageRecord = new JSONObject();
        ratioOfWinsWithThisDeck = 0;
        currentNumberOfCardsInDeck = 0;
    }

    private void setAllCardsOfThisHeroWithDetails() {
        GameState gameState = GameState.getInstance();
        for (Hero hero1 : gameState.getUser().getHeroes()) {
            if (hero1.getName().equals(hero)) {
                this.allCardsOfThisHeroWithDetails = hero1.getAllCardsOfHeroWithDetails();
            }
        }
    }


    private void setInitialCardUsageRecord() {
        cardUsageRecord = new JSONObject();
        for (Object cardName : deckCards.keySet()) {
            String cardNameStr = (String) cardName;
            cardUsageRecord.put(cardNameStr, 0);
        }
    }

    public void addSingleCardToDeck(String cardName, int recurrence) {
        deckCards.put(cardName, recurrence);
        currentNumberOfCardsInDeck++;
        if (recurrence == 1) { // Add this card To the cardUsage object as well.
            cardUsageRecord.put(cardName, 0);
        }
        calculateDeckAverageManaCost();
    }

    public void removeSingleCardFromDeck(String cardName, int occurrenceToRemain) {
        // occurrenceToRemain = 0: Remove completely. 1: Remove a single instance
        if (occurrenceToRemain == 0) {
            deckCards.remove(cardName);
            cardUsageRecord.remove(cardName);
        } else if (occurrenceToRemain == 1) {
            deckCards.put(cardName, 1);
        }
        currentNumberOfCardsInDeck--;
        calculateDeckAverageManaCost();
    }

    public void calculateRatioOfWinsWithThisDeck() {
        if (numberOfWinsWithThisDeck == 0) {
            ratioOfWinsWithThisDeck = 0;
        } else
            ratioOfWinsWithThisDeck = 100 * (double) numberOfGamesWithThisDeck / ((double) numberOfWinsWithThisDeck);
    }

    public double getRatioOfWinsWithThisDeck() {
        return ratioOfWinsWithThisDeck;
    }

    private void calculateDeckAverageManaCost() {
        double result = 0;
        for (Object o : deckCards.keySet()) {
            String cardName = (String) o;
            result += (double) ((int) deckCards.get(cardName) * (long) getCardObjectFromName(cardName).get("mana"));
        }
        averageManaCost = result / deckSize;
    }

    public double getDeckAverageManaCost() {
        return averageManaCost;
    }

    private JSONObject getCardObjectFromName(String cardName) {
        JSONObject temp = null;
        for (int i = 0; i < allCardsOfThisHeroWithDetails.size(); i++) {
            temp = (JSONObject) allCardsOfThisHeroWithDetails.get(i);
            if (temp.get("name").equals(cardName)) break;
        }
        return temp;
    }

    private int compareTwoCards(String firstCardName, String secondCardName) {
        String[] rarity = InitiateCards.getRarity();
        JSONObject firstCard = getCardObjectFromName(firstCardName);
        JSONObject secondCard = getCardObjectFromName(secondCardName);
        int firstRarity = 0;
        int secondRarity = 0;
        for (int i = 0; i < rarity.length; i++) {
            if (rarity[i].equals(firstCard.get("rarity"))) {
                firstRarity = i;
            }
            if (rarity[i].equals(secondCard.get("rarity"))) {
                secondRarity = i;
            }
        }
        if (firstRarity > secondRarity) return 0;
        else if (secondRarity > firstRarity) return 1;
        else {
            long firstMana = (long) firstCard.get("mana");
            long secondMana = (long) secondCard.get("mana");
            if (firstMana > secondMana) return 0;
            else if (secondMana > firstMana) return 1;
            else {
                boolean firstIsMinion = firstCard.get("type").equals("Minion");
                boolean secondIsMinion = secondCard.get("type").equals("Minion");
                if (firstIsMinion && !secondIsMinion) return 0;
                else if (!firstIsMinion && secondIsMinion) return 1;
            }
        }
        Random random = new Random(System.nanoTime());
        return random.nextInt(2);
    }

    public String getMostUsedCard() {
        int maxUsage = -1;
        int temp;
        String maxUsedCardsName = "";

        for (Object o : cardUsageRecord.keySet()) {
            String cardName = (String) o;
            temp = (int) cardUsageRecord.get(cardName);
            if (temp > maxUsage) {
                maxUsage = temp;
                maxUsedCardsName = cardName;
            } else if (temp == maxUsage) {
                if (compareTwoCards(maxUsedCardsName, cardName) == 1) {
                    maxUsedCardsName = cardName;
                }
            }
        }
        return maxUsedCardsName;
    }

    public void incrementNumberOfGamesPlayedWithThisDeck(boolean win) {
        numberOfGamesWithThisDeck++;
        if (win) numberOfWinsWithThisDeck++;
        calculateRatioOfWinsWithThisDeck();
    }

    public void incrementCardUsage(String cardName) {
        cardUsageRecord.put(cardName, (int) cardUsageRecord.get(cardName) + 1);
    }

    public int getMaximumInstanceOfEachCard() {
        return maximumInstanceOfEachCard;
    }

    public int getCurrentNumberOfCardsInDeck() {
        return currentNumberOfCardsInDeck;
    }

    public int getDeckSize() {
        return deckSize;
    }

    public JSONObject getDeckCards() {
        return deckCards;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getHero() {
        return hero;
    }

    public JSONArray getAllCardsOfThisHeroWithDetails() {
        return allCardsOfThisHeroWithDetails;
    }

    public void setHero(String hero) {
        this.hero = hero;
    }

    public int getNumberOfWinsWithThisDeck() {
        return numberOfWinsWithThisDeck;
    }

    public int getNumberOfGamesWithThisDeck() {
        return numberOfGamesWithThisDeck;
    }

    @Override
    public int compareTo(Deck deck) {
        if (Math.round(ratioOfWinsWithThisDeck) > Math.round(deck.getRatioOfWinsWithThisDeck())) return 1;
        else if (Math.round(ratioOfWinsWithThisDeck) < Math.round(deck.getRatioOfWinsWithThisDeck())) return -1;
        else {
            if (numberOfWinsWithThisDeck > deck.getNumberOfWinsWithThisDeck()) return 1;
            else if (numberOfWinsWithThisDeck < deck.getNumberOfWinsWithThisDeck()) return -1;
            else {
                if (numberOfGamesWithThisDeck > deck.getNumberOfGamesWithThisDeck()) return 1;
                else if (numberOfGamesWithThisDeck < deck.getNumberOfGamesWithThisDeck()) return -1;
                else {
                    if (getDeckAverageManaCost() > deck.getDeckAverageManaCost()) return 1;
                    else if (getDeckAverageManaCost() < deck.getDeckAverageManaCost()) return -1;
                    Random random = new Random(System.nanoTime());
                    return random.nextInt(3) - 1;

                }
            }
        }
    }

}
