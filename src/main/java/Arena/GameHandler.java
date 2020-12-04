package Arena;

import Card.AbilityInitializationFunctions;
import Card.CardUtilityFunctions;
import Card.Cards;
import Card.Interfaces.Restore;
import Card.Interfaces.Vulnerable;
import Configurations.GameConfig;
import GameState.GameState;
import Hero.Powers;
import User.User;
import org.json.simple.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

import static Arena.AbilityHandlers.handleGive;
import static LoggingModule.LoggingClass.logUser;

public class GameHandler implements Runnable {
    private User user;
    private boolean currentTurn = true;
    private boolean gameFinished = false;

    private ContestantHandler firstContestant, secondContestant;
    private boolean firstTimeNoDraw = true;

    private ArenaEventHandler arenaEventHandler;
    private boolean requestedTargetDoubleWay = false;
    private boolean requestedForTarget = false; // This is for spells and battlecries and ...
    private boolean requestFriendlyTarget = false;
    private boolean canChooseHero = true;
    private Vulnerable previousCaller = null;
    private Vulnerable crosshairTarget = null;
    private ArrayList<Cards.minion> cardsToDeleteFromGround = new ArrayList<>();

    public GameHandler(String firstHero, String firstPassive, String secondHero, String secondPassive,
                       ArenaEventHandler arenaEventHandler) {
        this.arenaEventHandler = arenaEventHandler;
        GameState gameState = GameState.getInstance();
        user = gameState.getUser();

        int firstHeroIndex = 0, secondHeroIndex = 0;
        for (int i = 0; i < user.getHeroes().size(); i++) {
            if (user.getHeroes().get(i).getName().equals(firstHero)) firstHeroIndex = i;
            if (user.getHeroes().get(i).getName().equals(secondHero)) secondHeroIndex = i;
        }
        firstContestant = new ContestantHandler(firstHeroIndex, firstPassive, user, true, this);
        secondContestant = new ContestantHandler(secondHeroIndex, secondPassive, user, false, this);

        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        boolean breakCondition = true;
        long startTime;

//        check the inactive abilities of minions on the ground in ROUND loop;

        while (breakCondition) {
            if (firstContestant.getHeroForArena().getHealth() <= 0 || secondContestant.getHeroForArena().getHealth() <= 0) {
                arenaEventHandler.finishGame();
                breakCondition = false;
            }
            arenaEventHandler.updateTooltips();
            arenaEventHandler.updateNumberOfCardsInDeck();
            checkCardRemoval();
            checkTauntBothPlayers();
            startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() < startTime + 500) ;
        }
    }

    public ContestantHandler getCurrentPlayer() {
        if (currentTurn) return firstContestant;
        return secondContestant;
    }

    public ContestantHandler getPlayer(boolean firstPlayer) {
        if (firstPlayer) return firstContestant;
        return secondContestant;
    }

    public void finishGame(boolean backExitButtonPressed) {
        if (backExitButtonPressed)
            user.incrementNumberOfGamesPlayedByUser(false);
        else {
            gameFinished = true;
            if (secondContestant.getHeroForArena().getHealth() <= 0)
                user.incrementNumberOfGamesPlayedByUser(true);
            else
                user.incrementNumberOfGamesPlayedByUser(false);
        }

    }

    public void battle(Vulnerable attackInitiator, Vulnerable attackReceiver) {
        String initiatorName, receiverName;
        int firstAttackAmount, secondAttackAmount;
        if (attackInitiator instanceof HeroForArena) {
            firstAttackAmount = ((HeroForArena) attackInitiator).getAttackAmount();
            initiatorName = ((HeroForArena) attackInitiator).getName();
        } else {
            firstAttackAmount = ((Cards.minion) attackInitiator).getAttackPower();
            initiatorName = ((Cards.minion) attackInitiator).getName();
        }
        if (attackReceiver instanceof HeroForArena) {
            secondAttackAmount = 0;
            receiverName = ((HeroForArena) attackReceiver).getName();
        } else {
            secondAttackAmount = ((Cards.minion) attackReceiver).getAttackPower();
            receiverName = ((Cards.minion) attackReceiver).getName();
        }
        if (attackInitiator instanceof Cards.minion && ((Cards.minion) attackInitiator).hasLifesteal()) {
            restore(getPlayer(((Cards.minion) attackInitiator).getOwner()).getHeroForArena(), secondAttackAmount);
        }
        if (attackReceiver instanceof Cards.minion && ((Cards.minion) attackReceiver).hasLifesteal()) {
            restore(getPlayer(((Cards.minion) attackReceiver).getOwner()).getHeroForArena(), secondAttackAmount);
        }

        if (attackInitiator instanceof Cards.minion && attackReceiver instanceof Cards.minion) {
            if (((Cards.minion) attackInitiator).isPoisonous())
                firstAttackAmount = ((Cards.minion) attackReceiver).getHealth();
            if (((Cards.minion) attackReceiver).isPoisonous())
                secondAttackAmount = ((Cards.minion) attackInitiator).getHealth();
        }

        handleTakeDamage(attackInitiator, secondAttackAmount);
        handleTakeDamage(attackReceiver, firstAttackAmount);
        arenaEventHandler.addToHistory("Battle: " + initiatorName + " attacked " + receiverName);
    }

    private void handleTakeDamage(Vulnerable pawn, int attackAmount) {
        if (pawn instanceof Cards.minion && ((Cards.minion) pawn).hasUponDamage()) {
            JSONObject temp = (JSONObject) ((Cards.minion) pawn).getActionBasedAbilities().get("uponDamage");
            for (Object actionName : temp.keySet()) {
                String s = (String) actionName;
                invokeAbilityHandlersViaReflection(s, (Cards.card) pawn, (JSONObject) temp);
            }
        }
        pawn.takeDamage(attackAmount);
    }

    public void returnCardFromHandToDeck(boolean firstPlayer, String cardName) {
        getPlayer(firstPlayer).returnCardFromHandToDeck(cardName);
    }

    public void addACardToPlayersDeck(boolean firstPlayer, Cards.card card) {
        card.setOwner(firstPlayer);
        getPlayer(firstPlayer).addCardToDeck(card);
    }

    public ArrayList<Cards.card> addACardToPlayersHand(boolean firstPlayer, int numberOfCardsToDraw) {
        return getPlayer(firstPlayer).addACardToHand(numberOfCardsToDraw);
    }

    public int getNumberOfCardsRemainingInDeck(boolean firstPlayer) {
        return getPlayer(firstPlayer).getNumberOfCardsRemainingInDeck();
    }

    public synchronized void checkCardRemoval() {
        cardsToDeleteFromGround.clear();
        cardsToDeleteFromGround.addAll(
                firstContestant.getCardsOnTheGround()
                        .stream()
                        .filter(x -> x.getHealth() <= 0).collect(Collectors.toList()));
        cardsToDeleteFromGround.addAll(
                secondContestant.getCardsOnTheGround()
                        .stream()
                        .filter(x -> x.getHealth() <= 0).collect(Collectors.toList()));
        cardsToDeleteFromGround.forEach(this::removeACard);
    }

    public synchronized void removeACard(Cards.minion minion) {
        System.out.println("Removing card " + minion);
        boolean cardOwner = minion.getOwner();
        getPlayer(cardOwner).removeFromCardsOnTheGround(minion);

        arenaEventHandler.removeACard(cardOwner, minion);
    }

    private boolean checkTauntValidity(ContestantHandler player) {
        boolean flag = false;
        for (Cards.minion minion : player.getCardsOnTheGround()) {
            if (minion.hasTaunt()) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private void checkTauntBothPlayers() {
        firstContestant.setHasTaunt(checkTauntValidity(firstContestant));
        secondContestant.setHasTaunt(checkTauntValidity(secondContestant));
    }

    public Cards.card playACard(Cards.card card, int currentMana) {
        int cardsOnTheGroundSize = getCurrentPlayer().getNumberOfCardsOnTheGround();
        if (cardsOnTheGroundSize == GameConfig.maxNumberOfCardsOnTheGround) {
            logUser("Card play unsuccessful. Arena full.");
            return null;
        }

        if (card.getMana() > currentMana) {
            logUser("Card play unsuccessful. Low on Mana.");
            return null;
        }
        logUser("Card play successful.");
        getCurrentPlayer().playACard(card);
        if (card instanceof Cards.minion)
            getPlayer(card.getOwner()).handleUponSummon((Cards.minion) card);
        handleCardAbilities(card);
        return card;
    }

    public void summon(Cards.minion minion, String fromWhere) {
        if (minion == null) return;
        ContestantHandler player = getPlayer(minion.getOwner());
        if (player.getNumberOfCardsOnTheGround() >= 7) return;
        player.addToCardsOnTheGround(minion);
        if (fromWhere.equals("deck")) {
            player.getDeckCards().remove(minion);
        }
        arenaEventHandler.summonCard(minion);
        getPlayer(minion.getOwner()).handleUponSummon(minion);
        handleMinionCardAbilities(minion, true);
    }

    private void handleCardAbilities(Cards.card card) {
        switch (card.getType()) {
            case "Minion":
                handleMinionCardAbilities((Cards.minion) card, false);
                break;
            case "Spell":
                handleSpellCardAbilities((Cards.spell) card);
                break;
            case "Weapon":
                handleWeaponCardAbilities((Cards.weapon) card);
                break;
            case "Quest":
                getPlayer(card.getOwner()).addQuest(((Cards.quest) card).getQuestDescription());
                break;
        }
    }

    private void handleWeaponCardAbilities(Cards.weapon weapon) {
        getCurrentPlayer().equipWeapon(weapon);
        switch (weapon.getName()) {
            case "Shadowblade":
                getCurrentPlayer().getHeroForArena().setCanBeAttacked(false);
                break;
        }
        handleActionBasedAbilities(weapon);
    }

    private void handleActionBasedAbilities(Cards.card card) {
        JSONObject actionBasedAbilities = null;
        if (card instanceof Cards.weapon)
            actionBasedAbilities = ((Cards.weapon) card).getActionBasedAbilities();
        else if (card instanceof Cards.minion)
            actionBasedAbilities = ((Cards.minion) card).getActionBasedAbilities();
        for (Object o : actionBasedAbilities.keySet()) {
            String s = (String) o;
            if (actionBasedAbilities.get(s) != null) {
                switch (s) {
                    case "turnStart":
                        JSONObject temp = (JSONObject) actionBasedAbilities.get(s);
                        String type = "";
                        for (Object o1 : temp.keySet()) {
                            type = (String) o1;
                        }
                        getCurrentPlayer().addToTurnStart(card, type, (JSONObject) temp.get(type));
                        break;
                    case "turnEnd":
                        JSONObject temp2 = (JSONObject) actionBasedAbilities.get(s);
                        String type2 = "";
                        for (Object o1 : temp2.keySet()) {
                            type2 = (String) o1;
                        }
                        getCurrentPlayer().addToTurnEnd(card, type2, (JSONObject) temp2.get(type2));
                        break;
                    case "uponDamage":
                        break;
                    case "uponDeath":
                        break;
                    case "uponDraw":
                        uponDrawActions(card);
                        break;
                    case "uponSummon":
                        uponSummonActions(card);
                        break;
                }
            }
        }
    }

    private void handleMinionCardAbilities(Cards.minion minion, boolean fromSummon) {
        Thread thread;
        JSONObject abilities = minion.getAbilities();
        switch (minion.getName()) {
            case "Sathrovarr":
                if (fromSummon) break;
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Thread waitThread = getOneWayTarget(true, false);
                        try {
                            waitThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Cards.minion pawn = (Cards.minion) getCrosshairTargetAndReset();
                        if (pawn != null) {
                            if (pawn == minion) return;
                            Cards.minion copy = Utility.SerializationFunctions.minionDeserialize(pawn.getName());
                            copy.setOwner(pawn.getOwner());
                            summon(copy, "nowhere");

                            copy = Utility.SerializationFunctions.minionDeserialize(pawn.getName());
                            Cards.minion copy2 = Utility.SerializationFunctions.minionDeserialize(pawn.getName());
                            boolean pawnOwner = pawn.getOwner();
                            ContestantHandler player = getPlayer(pawnOwner);
                            player.addCardToDeck(copy);
                            if (player.isOffCardsPassive()) copy2.setInitialMana(copy2.getMana() - 1);
                            player.addACardToHand(copy2);
                            ArrayList<Cards.card> temp = new ArrayList<>();
                            temp.add(copy2);
                            arenaEventHandler.addCardsToHand(pawnOwner, temp);
                        }
                    }
                });
                thread.start();
                break;

            case "Sunreaver Warmage":
                if (fromSummon) break;
                boolean flag = false;
                for (Cards.card handCard : getPlayer(minion.getOwner()).getHandCards()) {
                    if (handCard instanceof Cards.spell && handCard.getMana() > 4) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    AbilityHandlers.deal(
                            minion, this,
                            AbilityInitializationFunctions.dealObject(4, false,
                                    1, false));
                }
                break;
            default:
                for (Object o : abilities.keySet()) {
                    String s = (String) o;
                    if (abilities.get(s) == null) continue;
                    if (abilities.get(s) instanceof Boolean) {
                        switch (s) {
                            case "taunt":
                                if (minion.getOwner()) firstContestant.setHasTaunt(true);
                                else secondContestant.setHasTaunt(true);
                                break;
                            case "charge":
                                minion.setCanAttackThisTurn(true);
                                break;
                            case "rush":
                                minion.setCanAttackThisTurn(true);
                                minion.setCanAttackHero(false);
                                break;
                            case "windfury":
                                minion.activateWindfury();
                                break;
                            case "poisonous":
                                minion.setPoisonous();
                                break;
                        }
                    }
                }
                handleAbilitiesInJSONObject(minion);
        }
        handleActionBasedAbilities(minion);
    }

    private void handleSpellCardAbilities(Cards.spell card) {
        Thread thread;
        switch (card.getName()) {
            case "Polymorph":
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Thread waitThread = getOneWayTarget(false, false);
                        try {
                            waitThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Cards.minion pawn = (Cards.minion) getCrosshairTargetAndReset();
                        if (pawn != null) {
                            Cards.minion sheep = Utility.SerializationFunctions.minionDeserialize("Sheep");
                            transformMinion(pawn, sheep);
                        }
                    }
                });
                thread.start();
                break;
            default:
                handleAbilitiesInJSONObject(card);
        }
    }

    private void handleAbilitiesInJSONObject(Cards.card card) {
        JSONObject abilities = card.getAbilities();
        for (Object o : abilities.keySet()) {
            String s = (String) o;
            if (abilities.get(s) instanceof JSONObject) {
                invokeAbilityHandlersViaReflection(s, card, abilities);
            }
        }
    }

    public void invokeAbilityHandlersViaReflection(String function, Cards.card card, JSONObject abilities) {
        try {
            System.out.println(function);
            Class.forName("Arena.AbilityHandlers").getMethod(function, Cards.card.class, GameHandler.class,
                    JSONObject.class).invoke(null, card, this, (JSONObject) abilities.get(function));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void transformMinion(Cards.minion initialMinion, Cards.minion transformInto) {
        if (initialMinion == null) return;
        boolean cardOwner = initialMinion.getOwner();
        transformInto.setOwner(cardOwner);
        initialMinion.setHealth(0);
        if (cardOwner) {
            firstContestant.getCardsOnTheGround().remove(initialMinion);
            firstContestant.getCardsOnTheGround().add(transformInto);
        } else {
            secondContestant.getCardsOnTheGround().remove(initialMinion);
            secondContestant.getCardsOnTheGround().add(transformInto);
        }
        arenaEventHandler.transformMinion(cardOwner, initialMinion, transformInto);
    }

    public void dealDamage(Vulnerable pawn, int attackAmount) {
        if (pawn == null) {
            logUser("Null provided to deal damage. This could be due to user timer finish and the users inability" +
                    "to choose a valid target in the specified time.");
            return;
        }
        handleTakeDamage(pawn, attackAmount);
    }

    public void dealDamage(ArrayList<Vulnerable> pawns, int attackAmount) {
        for (Vulnerable pawn : pawns) {
            dealDamage(pawn, attackAmount);
        }
    }

    public void restore(Restore pawn, int amount) {
        if (pawn == null) {
            logUser("Null provided to restore. This could be due to user timer finish and the users inability" +
                    "to choose a valid target in the specified time.");
            return;
        }
        pawn.restoreHP(amount);
    }

    public void restoreHero(int amount) {
        getCurrentPlayer().getHeroForArena().restoreHP(amount);
    }

    public void drawCards(int numberOfCardsToDraw, String cardTypeToDraw, String discardType) {
        ArrayList<Cards.card> newCards;
        if (cardTypeToDraw.equals("all")) {
            newCards = getCurrentPlayer().addACardToHand(numberOfCardsToDraw);
            if (!discardType.equals("none")) {
                newCards.removeIf(newCard -> newCard.getType().equals(discardType));
            }
        } else {
            newCards = getCurrentPlayer().addACardToHand(numberOfCardsToDraw, cardTypeToDraw);
        }
        arenaEventHandler.addCardsToHand(currentTurn, newCards);
    }

    public Powers.heroPowers useHeroPower(int currentMana) {
        Powers.heroPowers heroPower;
        heroPower = getCurrentPlayer().getHeroForArena().getHeroPower();
        if (heroPower.getNumberOfUsagesLeftThisTurn() == 0) {
            heroPower.setUseStatus("NoneRemaining");
            return heroPower;
        } else if (heroPower.getMana() > currentMana) {
            logUser("Hero power use unsuccessful. Low on Mana");
            heroPower.setUseStatus("LowMana");
            return heroPower;
        } else {
            heroPower.usedHeroPower();
            logUser("Used hero Power");
            heroPower.setUseStatus("Success");
            handleHeroPowerAbilities(heroPower);
            return heroPower;
        }
    }

    private void handleHeroPowerAbilities(Powers.heroPowers heroPower) {
        Thread thread;
        switch (heroPower.getName()) {
            case "Fireblast":
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Thread waitThread = getOneWayTarget(false, true);

                        try {
                            waitThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Vulnerable pawn = getCrosshairTargetAndReset();

                        dealDamage(pawn, 1);
                    }
                });
                thread.start();
                break;
            case "Burgle":
                ArrayList<Cards.card> newCards = new ArrayList<>();
                ContestantHandler player = getCurrentPlayer();
                newCards.add(getPlayer(!player.isFirstPlayer()).returnACard());
                if (player.getHeroForArena().weaponIsEquipped()) {
                    newCards.add(player.returnACard());
                }
                arenaEventHandler.addCardsToHand(currentTurn, newCards);
                break;
            case "Sacrifice thai life":
                handleTakeDamage(getPlayer(currentTurn).getHeroForArena(), 2);
                arenaEventHandler.handleSacrificeHeroPowerChoose();
                break;
            case "The Silver Hand":
                for (int i = 0; i < 2; i++) {
                    Cards.minion minion =
                            Utility.SerializationFunctions.minionDeserialize("Silver Hand Recruit");
                    minion.setOwner(currentTurn);
                    summon(minion, "nowhere");
                }
                break;
            case "Heal":
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Thread waitThread = getOneWayTarget(true, true);
                        try {
                            waitThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Vulnerable pawn = getCrosshairTargetAndReset();

                        restore((Restore) pawn, 4);
                    }
                });
                thread.start();
                break;
        }
    }

    public void handleSacrificeCase1() {
        System.out.println("In case 1");
        Random random = new Random(System.nanoTime());
        ArrayList<Cards.minion> minions;
        minions = getCurrentPlayer().getCardsOnTheGround();
        if (minions.size() != 0) {
            Cards.minion minion = minions.get(random.nextInt(minions.size()));
            handleGive(minion, 1, 1);
        }
    }

    public void handleSacrificeCase2() {
        System.out.println("In case 2");
        ArrayList<Cards.card> newCards = new ArrayList<>();
        newCards.add(getCurrentPlayer().returnACard());
        arenaEventHandler.addCardsToHand(currentTurn, newCards);
    }

    private void handleTurnStartActions(boolean firstPlayer) {
        getPlayer(firstPlayer).turnStartActions();
    }

    private void handleTurnEndActions(boolean firstPlayer) {
        getPlayer(firstPlayer).turnEndActions();
    }

    private void uponDrawActions(Cards.card minion) {
        JSONObject drawAction = new JSONObject();
        drawAction.put("caller", minion);
        if (minion instanceof Cards.minion)
            drawAction.put("uponDraw", ((Cards.minion) minion).getActionBasedAbilities().get("uponDraw"));
        else if (minion instanceof Cards.weapon)
            drawAction.put("uponDraw", ((Cards.weapon) minion).getActionBasedAbilities().get("uponDraw"));
        getPlayer(minion.getOwner()).addToUponDrawActions(drawAction);
    }

    private void uponSummonActions(Cards.card minion) {
        JSONObject summonAction = new JSONObject();
        summonAction.put("caller", minion);
        if (minion instanceof Cards.minion)
            summonAction.put("uponSummon", ((Cards.minion) minion).getActionBasedAbilities().get("uponSummon"));
        else if (minion instanceof Cards.weapon)
            summonAction.put("uponSummon", ((Cards.weapon) minion).getActionBasedAbilities().get("uponSummon"));
        getPlayer(minion.getOwner()).addToUponSummonActions(summonAction);
    }

    public Thread getOneWayTarget(boolean friendly, boolean canChooseHero) {
        requestedForTarget = true;
        requestFriendlyTarget = friendly;
        crosshairTarget = null;
        this.canChooseHero = canChooseHero;
        return arenaEventHandler.toggleCrosshairShow(null);
    }

    public HeroForArena getHeroForArena(boolean firstPlayer) {
        return getPlayer(firstPlayer).getHeroForArena();
    }

    public ArrayList<Cards.card> getHand(boolean firstPlayer) {
        return getPlayer(firstPlayer).getHandCards();
    }

    public boolean isManaJumpPassive(boolean firstPlayer) {
        return getPlayer(firstPlayer).isManaJumpPassive();
    }

    public void executeRound() {
        if (!firstTimeNoDraw) {
            // This is the case in which the current turn was the first player's.
            getPlayer(!currentTurn).executeRound();
        }
        handleTurnEndActions(currentTurn);
        currentTurn = !currentTurn;
        handleTurnStartActions(currentTurn);
        firstTimeNoDraw = false;
    }

    public boolean getCurrentTurn() {
        return currentTurn;
    }

    public ArrayList<Cards.card> getPlayerNewCards(boolean firstPlayer) {
        return getPlayer(firstPlayer).getNewCardsAndReset();
    }

    public ArrayList<Vulnerable> getEnemyPawns() {
        ArrayList<Vulnerable> pawns = new ArrayList<>(getEnemyMinions());
        pawns.add(getPlayer(!currentTurn).getHeroForArena());
        return pawns;
    }

    public ArrayList<Vulnerable> getEnemyMinions() {
        ArrayList<Vulnerable> minions = new ArrayList<>();
        minions.addAll(getPlayer(!currentTurn).getCardsOnTheGround());

        return minions;
    }

    public ArrayList<Vulnerable> getFriendlyMinions() {
        ArrayList<Vulnerable> minions = new ArrayList<>();
        minions.addAll(getCurrentPlayer().getCardsOnTheGround());

        return minions;
    }

    public boolean requestedForTarget() {
        return requestedForTarget;
    }

    public void setRequestedForTarget(boolean requestedForTarget) {
        this.requestedForTarget = requestedForTarget;
    }

    public boolean requestFriendlyTarget() {
        return requestFriendlyTarget;
    }

    public void setRequestFriendlyTarget(boolean requestFriendlyTarget) {
        this.requestFriendlyTarget = requestFriendlyTarget;
    }

    public boolean requestedTargetDoubleWay() {
        return requestedTargetDoubleWay;
    }

    public void setRequestedTargetDoubleWay(boolean requestedTargetDoubleWay) {
        this.requestedTargetDoubleWay = requestedTargetDoubleWay;
    }

    public Vulnerable getPreviousCaller() {
        return previousCaller;
    }

    public void setPreviousCaller(Vulnerable previousCaller) {
        this.previousCaller = previousCaller;
    }

    public boolean isCrosshairTargetSet() {
        return crosshairTarget != null;
    }

    public Vulnerable getCrosshairTargetAndReset() {
        Vulnerable target = crosshairTarget;
        crosshairTarget = null;
        canChooseHero = true;
        return target;
    }

    public void setCrosshairTarget(Vulnerable crosshairTarget) {
        this.crosshairTarget = crosshairTarget;
    }

    public boolean canChooseHero() {
        return canChooseHero;
    }

    public void setCanChooseHero(boolean canChooseHero) {
        this.canChooseHero = canChooseHero;
    }

    public void discover(boolean specificCardClass, String specificCardClassName, boolean willChangeCard,
                         JSONObject newAbilities) {
        ArrayList<Cards.card> possibleCards = new ArrayList<>();
        ArrayList<Cards.card> cardsToShow = new ArrayList<>();
        if (specificCardClass) {
            for (String availableCard : user.getAvailableCards()) {
                Cards.card card = CardUtilityFunctions.getCardObjectFromName(availableCard);
                if (card.getType().equals(specificCardClassName)) {
                    possibleCards.add(card);
                }
            }
        } else {
            for (String availableCard : user.getAvailableCards()) {
                possibleCards.add(CardUtilityFunctions.getCardObjectFromName(availableCard));
            }
        }
        Random random = new Random(System.nanoTime());
        int bound = Math.min(3, possibleCards.size());
        for (int i = 0; i < bound; i++) {
            cardsToShow.add(possibleCards.remove(random.nextInt(possibleCards.size())));
        }
        while (cardsToShow.size() < 3) cardsToShow.add(null);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Cards.card selectedCard = arenaEventHandler.handleDiscover(cardsToShow);
                if (willChangeCard) {
                    if (selectedCard instanceof Cards.weapon) {
                        handleGive((Cards.weapon) selectedCard,
                                (Integer) newAbilities.get("durabilityBoost"), (Integer) newAbilities.get("attackBoost"));
                    } else System.out.println("Error. non weapon passed to method");
                }

                arenaEventHandler.addToHistory("Discovered " + selectedCard.getName());
                addACardToPlayersDeck(currentTurn, selectedCard);
            }
        });
        thread.start();
    }

    public ArrayList<Cards.card> getCurrentDeck() {
        return getCurrentPlayer().getDeckCards();
    }

    public User getUser() {
        return user;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public boolean hasTaunt(boolean firstPlayer) {
        return getPlayer(firstPlayer).hasTaunt();
    }
}
