package Arena;

import Card.AbilityInitializationFunctions;
import Card.Cards;
import Deck.Deck;
import User.User;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static LoggingModule.LoggingClass.logUser;

public class ContestantHandler {
    private ArrayList<Cards.card> deckCards;
    private ArrayList<Cards.card> handCards;
    private ArrayList<Cards.minion> cardsOnTheGround;
    private ArrayList<Cards.card> newCards;


    private User user;
    private HeroForArena heroForArena;
    private String passiveName;
    private int numberOfCardsToDrawEachRound = 1;
    private boolean offCardsPassive = false;
    private boolean manaJumpPassive = false;
    private boolean freePowerPassive = false;
    private int heroIndex;
    private boolean firstPlayer;
    private boolean hasTaunt = false;
    private Deck mainDeck;
    private ArrayList<JSONObject> turnStartActions = new ArrayList<>();
    private ArrayList<JSONObject> turnEndActions = new ArrayList<>();
    private ArrayList<JSONObject> uponDrawActions = new ArrayList<>();
    private ArrayList<JSONObject> uponSummonActions = new ArrayList<>();
    private ArrayList<JSONObject> quests = new ArrayList<>();
    private GameHandler gameHandler;

    private Random random;


    public ContestantHandler(int heroIndex, String passiveName, User user, boolean firstPlayer, GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        this.heroIndex = heroIndex;
        this.firstPlayer = firstPlayer;
        random = new Random(System.nanoTime());

        this.user = user;
        heroForArena = new HeroForArena(user.getHeroes().get(heroIndex), firstPlayer, passiveName);

        if (heroForArena.getName().equals("Paladin")) {
            JSONObject paladinTurnEnd = new JSONObject();
            paladinTurnEnd.put("caller", heroForArena);
            paladinTurnEnd.put("give", AbilityInitializationFunctions.giveObject(
                    null, -1, 1, 1));
            turnEndActions.add(paladinTurnEnd);
        }

        this.passiveName = passiveName;
        handlePassive();

        deckCards = new ArrayList<>();
        handCards = new ArrayList<>();
        mainDeck = user.getHeroes().get(heroIndex).getCurrentDeck();
        JSONObject tempHeroDeck = user.getHeroes().get(heroIndex).getCurrentDeckCards();
        int numberOfRecurrences;
        for (Object o : tempHeroDeck.keySet()) {
            String s = (String) o;
            numberOfRecurrences = (Integer) tempHeroDeck.get(s);
            for (int i = 0; i < numberOfRecurrences; i++) {
                addCardToDeck(s);
            }
        }
        cardsOnTheGround = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Cards.card card = returnACard();
            handCards.add(card);
        }

        newCards = new ArrayList<>();
    }

    private void addCardToDeck(String cardName) {
        Cards.card tempCard = Utility.SerializationFunctions.cardDeserialize(cardName);
        addCardToDeck(tempCard);
    }

    public void addCardToDeck(Cards.card card) {
        if (offCardsPassive)
            card.setInitialMana(card.getMana() - 1);
        deckCards.add(card);
    }


    private void handlePassive() {
        if (passiveName == null) return;
        switch (passiveName) {
            case "Twice Draw":
                numberOfCardsToDrawEachRound = 2;
                break;
            case "Off Cards":
                offCardsPassive = true;
                break;
            case "Warriors":
                break;
            case "Free Power":
                freePowerPassive = true;
                int mana = Math.max(heroForArena.getHeroPower().getMana() - 1, 0);
                heroForArena.getHeroPower().setMana(mana);
                heroForArena.getHeroPower().setMaxNumberOfUsagesEachTurn(2);
                break;
            case "Mana Jump":
                manaJumpPassive = true;
                break;
        }
    }

    public Cards.card returnACard() {
        Cards.card cardObject = null;
        int index;
        if (deckCards.size() <= 0) {
            return null;
        }
        index = random.nextInt(deckCards.size());
        cardObject = deckCards.remove(index);

        handleUponDraw(cardObject);
        return cardObject;
    }

    private Cards.card returnACard(String type) {
        Cards.card cardObject = null;
        int index;
        List<Cards.card> desiredCards = deckCards
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
        if (deckCards.size() <= 0 || desiredCards.size() == 0) {
            return null;
        }
        index = random.nextInt(desiredCards.size());
        cardObject = deckCards.remove(index);
        handleUponDraw(cardObject);
        return cardObject;
    }

    public void returnCardFromHandToDeck(String cardName) {
        for (Cards.card handCard : handCards) {
            if (handCard.getName().equals(cardName)) {
                deckCards.add(handCard);
                handCards.remove(handCard);
                break;
            }
        }
    }

    public void addACardToHand(Cards.card card) {
        card.setOwner(firstPlayer);
        handleHeroSpecialPowersForCards(card);
        handCards.add(card);
    }

    public ArrayList<Cards.card> addACardToHand(String type) {
        ArrayList<Cards.card> cardsToAdd = new ArrayList<>();
        Cards.card cardObject;
        for (int i = 0; i < numberOfCardsToDrawEachRound; i++) {
            if (type.equals("none"))
                cardObject = returnACard();
            else
                cardObject = returnACard(type);
            if (cardObject != null)
                cardsToAdd.add(cardObject);
        }
        for (Cards.card card : cardsToAdd) {
            card.setOwner(firstPlayer);
            handleHeroSpecialPowersForCards(card);
            handCards.add(card);
        }
        return cardsToAdd;
    }

    public ArrayList<Cards.card> addACardToHand(int numberOfCardsToDraw) {
        ArrayList<Cards.card> cardsToAdd;
        int currentNumberOfCardsToDraw = numberOfCardsToDrawEachRound;
        numberOfCardsToDrawEachRound = numberOfCardsToDraw;
        cardsToAdd = addACardToHand("none");
        numberOfCardsToDrawEachRound = currentNumberOfCardsToDraw;
        return cardsToAdd;
    }

    public ArrayList<Cards.card> addACardToHand(int numberOfCardsToDraw, String type) {
        ArrayList<Cards.card> cardsToAdd;
        int currentNumberOfCardsToDraw = numberOfCardsToDrawEachRound;
        numberOfCardsToDrawEachRound = numberOfCardsToDraw;
        cardsToAdd = addACardToHand(type);
        numberOfCardsToDrawEachRound = currentNumberOfCardsToDraw;
        return cardsToAdd;
    }

    public void setNewCards(ArrayList<Cards.card> newCards) {
        this.newCards = newCards;
    }

    public ArrayList<Cards.card> getNewCardsAndReset() {
        ArrayList<Cards.card> temp = new ArrayList<>(newCards);
        newCards.clear();
        return temp;
    }

    public ArrayList<Cards.card> getHandCards() {
        return handCards;
    }

    public boolean isManaJumpPassive() {
        return manaJumpPassive;
    }

    public HeroForArena getHeroForArena() {
        return heroForArena;
    }

    public int getNumberOfCardsRemainingInDeck() {
        return deckCards.size();
    }

    public int getNumberOfCardsOnTheGround() {
        return cardsOnTheGround.size();
    }


    public void addToCardsOnTheGround(Cards.minion newCard) {
        if (cardsOnTheGround.size() == 7) return;
        newCard.setOwner(firstPlayer);
        cardsOnTheGround.add(newCard);
    }

    public void playACard(Cards.card card) {
        if (firstPlayer)
            mainDeck.incrementCardUsage(card.getName());
        if (card.getType().equals("Minion")) addToCardsOnTheGround((Cards.minion) card);
        updateQuestStatus(card);
    }

    public void removeFromCardsOnTheGround(Cards.minion minion) {
        if (!cardsOnTheGround.contains(minion)) {
            logUser("Error. This minion is not contained in this contestant");
            System.exit(0);
        }
        cardsOnTheGround.remove(minion);
        if (minion.getHealth() <= 0 && passiveName.equals("Warriors")) {
            heroForArena.equipArmour(2);
        }
    }

    public synchronized ArrayList<Cards.minion> getCardsOnTheGround() {
        return cardsOnTheGround;
    }

    public ArrayList<Cards.card> getDeckCards() {
        return deckCards;
    }

    public boolean isOffCardsPassive() {
        return offCardsPassive;
    }

    public void executeRound() {
        setNewCards(addACardToHand("none"));
        for (Cards.minion minion : cardsOnTheGround) {
            minion.setCanAttackThisTurn(true);
            minion.setCanAttackHero(true);
        }
        heroForArena.refreshWeaponAttack();
        heroForArena.setCanBeAttacked(true);
    }

    private void handleHeroSpecialPowersForCards(Cards.card card) {
        if (heroForArena.getName().equals("Mage") && card.getType().equals("Spell") && !card.getHeroClass().equals("Mage")) {
            card.setInitialMana(card.getMana() - 2);
        } else if (heroForArena.getName().equals("Rogue") && !card.getHeroClass().equals("Rogue") && !card.getHeroClass().equals("Neutral")) {
            card.setInitialMana(card.getMana() - 2);
        } else if (heroForArena.getName().equals("Priest") && card instanceof Cards.spell) {
            JSONObject cardAbilities = card.getAbilities();
            if (cardAbilities.get("give") != null) {
                JSONObject giveProperties = (JSONObject) cardAbilities.get("give");
                giveProperties.put("hpBoost", 2 * (Integer) giveProperties.get("hpBoost"));
                card.addToAbilities("give", giveProperties);
            }
            if (cardAbilities.get("restore") != null) {
                JSONObject restoreProperties = (JSONObject) cardAbilities.get("restore");
                restoreProperties.put("amount", 2 * (Integer) restoreProperties.get("amount"));
                card.addToAbilities("restore", restoreProperties);
            }
        }
    }

    public boolean hasTaunt() {
        return hasTaunt;
    }

    public void setHasTaunt(boolean hasTaunt) {
        this.hasTaunt = hasTaunt;
    }

    public void equipWeapon(Cards.weapon weapon) {
        heroForArena.equipWeapon(weapon);
    }

    public void turnStartActions() {
        ArrayList<JSONObject> toRemove = new ArrayList<>();
        for (JSONObject turnStartAction : turnStartActions) {
            Object o = turnStartAction.get("caller");
            if (o instanceof HeroForArena) {
                if (((HeroForArena) o).getHealth() <= 0) {
                    toRemove.add(turnStartAction);
                    continue;
                }
            } else if (o instanceof Cards.minion) {
                if (((Cards.minion) o).getHealth() <= 0) {
                    toRemove.add(turnStartAction);
                    continue;
                }
            } else if (o instanceof Cards.weapon) {
                if (((Cards.weapon) o).getDurability() <= 0) {
                    toRemove.add(turnStartAction);
                    continue;
                }
            }
            for (Object o1 : turnStartAction.keySet()) {
                String s = (String) o1;
                if (s.equals("caller")) continue;
                gameHandler.invokeAbilityHandlersViaReflection(s, null, (JSONObject) turnStartAction);
            }
        }
        turnStartActions.removeAll(toRemove);
    }

    public void turnEndActions() {
        ArrayList<JSONObject> toRemove = new ArrayList<>();
        for (JSONObject turnEndAction : turnEndActions) {
            Object o = turnEndAction.get("caller");
            if (o instanceof HeroForArena) {
                if (((HeroForArena) o).getHealth() <= 0) {
                    toRemove.add(turnEndAction);
                    continue;
                }
            } else if (o instanceof Cards.minion) {
                if (((Cards.minion) o).getHealth() <= 0) {
                    toRemove.add(turnEndAction);
                    continue;
                }
            } else if (o instanceof Cards.weapon) {
                if (((Cards.weapon) o).getDurability() <= 0) {
                    toRemove.add(turnEndAction);
                    continue;
                }
            }
            for (Object o1 : turnEndAction.keySet()) {
                String s = (String) o1;
                if (s.equals("caller")) continue;
                System.out.println(s);
                System.out.println(turnEndAction);
                gameHandler.invokeAbilityHandlersViaReflection(s, null, (JSONObject) turnEndAction);
            }
        }
        turnEndActions.removeAll(toRemove);
    }

    public void addToTurnStart(Cards.card card, String keyName, JSONObject abilityDescription) {
        JSONObject temp = new JSONObject();
        temp.put("caller", card);
        temp.put(keyName, abilityDescription);
        turnStartActions.add(temp);
    }

    public void addToTurnEnd(Cards.card card, String keyName, JSONObject abilityDescription) {
        JSONObject temp = new JSONObject();
        temp.put("caller", card);
        temp.put(keyName, abilityDescription);
        turnEndActions.add(temp);
    }

    public void addQuest(JSONObject newQuest) {
        quests.add(newQuest);
    }

    public void handleQuestReward(JSONObject reward) {
        for (Object o : reward.keySet()) {
            String s = (String) o;
            Cards.card randomDummyCard = new Cards.quest("dummy", 0, "Neutral", "Common",
                    "");
            randomDummyCard.setOwner(firstPlayer);
            gameHandler.invokeAbilityHandlersViaReflection(s, randomDummyCard, (JSONObject) reward);
        }
    }

    public void addToUponDrawActions(JSONObject uponDrawAction) {
        uponDrawActions.add(uponDrawAction);
    }

    public void addToUponSummonActions(JSONObject uponSummonAction) {
        uponSummonActions.add(uponSummonAction);
    }

    private void handleUponDraw(Cards.card card) {
        ArrayList<JSONObject> toRemove = new ArrayList<>();
        for (JSONObject uponDrawAction : uponDrawActions) {
            Cards.minion caller = (Cards.minion) uponDrawAction.get("caller");
            if (caller.getHealth() <= 0) {
                toRemove.add(uponDrawAction);
                continue;
            }
            switch (caller.getName()) {
                case "Shadowfiend":
                    card.setMana(card.getMana() - 1);
                    break;
                default:
                    System.out.println(uponDrawAction);
                    JSONObject temp = (JSONObject) uponDrawAction.get("uponDraw");
                    Cards.card cardToActOn;
                    System.out.println(temp);
                    if (temp.get("actOn").equals("previous")) cardToActOn = caller;
                    else cardToActOn = card;
                    for (Object o : temp.keySet()) {
                        String s = (String) o;
                        if (s.equals("actOn")) continue;
                        gameHandler.invokeAbilityHandlersViaReflection(s, cardToActOn, (JSONObject) temp);
                    }
            }
        }
        uponDrawActions.removeAll(toRemove);
    }

    public void handleUponSummon(Cards.minion minion) {
        ArrayList<JSONObject> toRemove = new ArrayList<>();
        for (JSONObject uponSummonAction : uponSummonActions) {
            Cards.minion caller = (Cards.minion) uponSummonAction.get("caller");
            if (caller.getHealth() <= 0) {
                toRemove.add(uponSummonAction);
                continue;
            }
            switch (caller.getName()) {
                case "High Priest Amet":
                    minion.setHealth(caller.getHealth());
                    break;
            }
        }
        uponDrawActions.removeAll(toRemove);
    }

    private void updateQuestStatus(Cards.card playedCard) {
        ArrayList<JSONObject> completedQuests = new ArrayList<>();
        for (JSONObject quest : quests) {
            JSONObject q = (JSONObject) quest.get("quest");
            if (q.get("specificClass").equals(playedCard.getType())) {
                q.put("completed", (Integer) q.get("completed") + playedCard.getMana());
            }
            if ((Integer) q.get("completed") >= (Integer) q.get("manaAmount")) {
                handleQuestReward((JSONObject) quest.get("reward"));
                completedQuests.add(quest);
            }
        }
        quests.removeAll(completedQuests);
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }
}
