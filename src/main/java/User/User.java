package User;

import Card.Cards;
import Card.InitiateCards;
import Configurations.Main_config_file;
import Deck.DeckFunctions;
import Hero.Hero;
import Utility.FileFunctions;
import org.json.simple.JSONObject;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static LoggingModule.LoggingClass.logUser;
import static Utility.SerializationFunctions.*;

public class User implements Serializable {
    private String username;
    private long userID;
    private String hashedPassword;
    private int walletBalance;
    private ArrayList<Hero> heroes = new ArrayList<>();
    private int currentHeroIndex;
    private ArrayList<String> allCards;
    private ArrayList<String> availableCards;
    private JSONObject cardHeroClass;

    private int numberOfGamesPlayed = 0;
    private int numberOfWins = 0;
    private double rationOfWins = 0;

    private String passiveName = null;

    User(String username, long userID, String hashedPassword) {
        this.username = username;
        this.userID = userID;
        this.hashedPassword = hashedPassword;
        walletBalance = 100;
        heroes.add(new Hero("Mage", "The sorceress of hearthstone. At her hand, all non-mage spells " +
                "cost 2 mana less.", true,
                "Fireblast", 2, "Deal 1 damage."));
        heroes.add(new Hero("Rogue", "The thief of these wastelands. All non-neutral and non-rogue " +
                "cards cost 2 less for her.", true,
                "Burgle", 3, "Pick a random card from the " +
                "opponents deck and add it to your hand. If the hero is equipped with a weapon," +
                " pick a random card from your own deck as well"));
        heroes.add(new Hero("Warlock", "The sacrificing demon around here. He will sacrifice cards " +
                "and health points in order to win. The gods have gifted him with 5 more health points",
                true, "Sacrifice thai life", 2,
                "Reduce 2 health points and then choose one of the following actions to do:" +
                        " 1.Give a random minion +1/+1 2.Add a card to your hand."));
        heroes.add(new Hero("Paladin", "A true leader. Giving +1/+1 to a random minion at the end of " +
                "each his turn", true, "The Silver Hand", 2,
                "Summon two 1/1 recruits"));
        heroes.add(new Hero("Priest", "Working with holy magic, all health restoration and giving " +
                "spells work with twice their normal effect", true, "Heal",
                2, "Restore 4 Health"));
        currentHeroIndex = 0;
        setAllCards();
        setAvailableCards();
        cardHeroClass = new JSONObject();
        completeCardHeroClass();
    }

    private void completeCardHeroClass() {
        for (String card : allCards) {
            Cards.card tempCard = cardDeserialize(Main_config_file.returnCardSaveDataLocation(card));
            cardHeroClass.put(card, tempCard.getHeroClass());
        }
    }

    private void setAllCards() {
        Set<String> allCardsSet = new HashSet<String>();
        for (Hero hero : heroes) {
            allCardsSet.addAll(hero.getAllCards());
        }
        this.allCards = new ArrayList<>(allCardsSet);
    }

    private void setAvailableCards() {
        Set<String> availableCardsSet = new HashSet<String>();
        for (Hero hero : heroes) {
            availableCardsSet.addAll(hero.getAvailableCards());
        }
        this.availableCards = new ArrayList<>(availableCardsSet);
    }


    public int changeHero(String heroName) {
        if (heroName == null) {
            return 403;
        }
        int index = -1;
        for (int i = 0; i < heroes.size(); i++) {
            if (heroes.get(i).getName().equals(heroName)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            logUser("Error. The provided hero name is incorrect.");
            return 404;
        }
        if (currentHeroIndex == index) {
            logUser("Error/Warning. The provided hero is already selected. Returning.");
            return 300;
        }
        currentHeroIndex = index;
        logUser("Successfully changed hero to " + heroes.get(index).getName());
        return 200;
    }


    public void serializeUser() {
        Serialize(this, Main_config_file.returnUserSaveDataLocation(username, userID));
    }

    public static User deserializeUser(String username, long userID) {
        User userObject = userDeserialize(Main_config_file.returnUserSaveDataLocation(username, userID));
        return userObject;
    }

    public int getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(int walletBalance) {
        assert walletBalance >= 0;
        this.walletBalance = walletBalance;
    }

    public String getCurrentHeroName() {
        return heroes.get(currentHeroIndex).getName();
    }

    public ArrayList<String> getAllCards() {
        return allCards;
    }

    public ArrayList<String> getAllCards(String heroClass) {
        ArrayList<String> result = new ArrayList<>();
        for (String card : allCards) {
            if (cardHeroClass.get(card).equals(heroClass))
                result.add(card);
        }
        return result;
    }

    public ArrayList<String> getAvailableCards() {
        return availableCards;
    }

    public ArrayList<String> getAvailableCards(String heroClass) {
        return getSellableCards(heroClass);
    }

    public ArrayList<String> getSellableCards(String heroName) {
        ArrayList<String> result = new ArrayList<>();
        for (String s : availableCards) {
            if (cardHeroClass.get(s).equals(heroName)) {
                result.add(s);
            }
        }
        return result;
    }

    public ArrayList<String> getBuyableHeroClassCards(String heroName) {
        ArrayList<String> result = new ArrayList<>();
        for (String s : DeckFunctions.getDisjointSetOfTwo(allCards, availableCards)) {
            if (cardHeroClass.get(s).equals(heroName)) {
                result.add(s);
            }
        }
        return result;
    }

    public int getHeroLevel() {
        return heroes.get(currentHeroIndex).getHeroLevel();
    }

    public void printCardInformation(String cardName) {
        logUser(FileFunctions.prettyJsonString(
                cardDeserialize(Main_config_file.returnCardSaveDataLocation(cardName)).getCardJsonObject()));
    }

    public void printCardInformation(ArrayList<String> cardNames) {
        for (String cardName : cardNames) printCardInformation(cardName);
    }

    public void printDeckCardsWithDetails() {
        for (Object st : heroes.get(currentHeroIndex).getCurrentDeckCards().keySet()) {
            printCardInformation(st.toString());
        }
    }

    public void printAllHeroesInformation() {
        for (Hero tempHero : heroes) {
            logUser("Hero name: " + tempHero.getName());
            logUser("Description:\n" + tempHero.getDescription());
        }
    }

    public void printAvailableCards() {
        printCardInformation(getAvailableCards());
    }

    @Override
    public User clone() throws CloneNotSupportedException {
        return deserializeUser(username, userID);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public ArrayList<String> getHeroNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Hero tempHero : heroes) {
            names.add(tempHero.getName());
        }
        return names;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public ArrayList<Hero> getHeroes() {
        return heroes;
    }

    public void calculateRatioOfWins() {
        rationOfWins = 100 * (double) numberOfWins / (double) numberOfGamesPlayed;
    }

    public void incrementNumberOfGamesPlayedByUser(boolean win) {
        numberOfGamesPlayed++;
        if (win) numberOfWins++;
        heroes.get(currentHeroIndex).incrementNumberOfGamesPlayedByHero(win);
        calculateRatioOfWins();
    }

    public int getCurrentHeroIndex() {
        return currentHeroIndex;
    }

    public void setPassive(String passiveName) {
        this.passiveName = passiveName;
    }

    public String getPassiveName() {
        return passiveName;
    }
}
