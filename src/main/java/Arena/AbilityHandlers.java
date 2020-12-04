package Arena;

import Card.CardUtilityFunctions;
import Card.Cards;
import Card.Interfaces.Restore;
import Card.Interfaces.Vulnerable;
import User.User;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AbilityHandlers {
    public static void deal(Cards.card card, GameHandler gameHandler, JSONObject dealProperties) {
        /*
        You MUST supply the in game card object for the case (-5)
         */
        System.out.println(dealProperties);
        try {
            int attackAmount = (Integer) dealProperties.get("amount");
            boolean attackAmountIsWholeSum = (Boolean) dealProperties.get("attackAmountIsWholeSum");
            int numberOfTargets = (Integer) dealProperties.get("numberOfTargets");
            boolean randomTargets = (Boolean) dealProperties.get("randomTargets");

            int damageToEachPawn = attackAmountIsWholeSum ? 1 : attackAmount;

            if (randomTargets) {
                ArrayList<Vulnerable> pawns = gameHandler.getEnemyPawns();
                Random random = new Random(System.nanoTime());
                try {
                    for (int i = 0; i < numberOfTargets; i++) {
                        gameHandler.dealDamage(pawns.get(random.nextInt(pawns.size())), damageToEachPawn);
                    }
                } catch (Exception e) {
                    // this is probably the case in which no target was available to choose.
                }
            } else {
                switch (numberOfTargets) {
                    case -2:
                        gameHandler.dealDamage(gameHandler.getEnemyMinions(), damageToEachPawn);
                        break;
                    case -3:
                        gameHandler.dealDamage(gameHandler.getEnemyPawns(), damageToEachPawn);
                        break;
                    case -4:
                        gameHandler.dealDamage(gameHandler.getFriendlyMinions(), damageToEachPawn);
                        gameHandler.dealDamage(gameHandler.getEnemyMinions(), damageToEachPawn);
                        break;
                    case -5:
                        gameHandler.dealDamage(gameHandler.getEnemyMinions(), damageToEachPawn);
                        for (Vulnerable pawn : gameHandler.getFriendlyMinions()) {
                            if (pawn == (Vulnerable) card) continue;
                            gameHandler.dealDamage(pawn, damageToEachPawn);
                        }
                        break;
                    default:
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Thread waitThread = gameHandler.getOneWayTarget(false, true);

                                try {
                                    waitThread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                Vulnerable pawn = gameHandler.getCrosshairTargetAndReset();

                                gameHandler.dealDamage(pawn, damageToEachPawn);
                            }
                        });
                        thread.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void draw(Cards.card card, GameHandler gameHandler, JSONObject drawProperties) {
        int numberOfCardsToDraw = (Integer) drawProperties.get("numberOfCardsToDraw");
        boolean specificCardDraw = (Boolean) drawProperties.get("specificCardDraw");
        String specificCardDrawType = (String) drawProperties.get("specificCardDrawType");
        boolean discardSpecificCards = (Boolean) drawProperties.get("discardSpecificCards");
        String discardCardClass = (String) drawProperties.get("discardCardClass");

        if (!specificCardDraw) specificCardDrawType = "all";
        if (!discardSpecificCards) discardCardClass = "none";
        gameHandler.drawCards(numberOfCardsToDraw, specificCardDrawType, discardCardClass);
    }

    public static void discover(Cards.card card, GameHandler gameHandler, JSONObject discoverProperties) {
        boolean specificCardClass = (Boolean) discoverProperties.get("specificCardClass");
        String specificCardClassName = (String) discoverProperties.get("specificCardClassName");
        boolean willChangeCard = (Boolean) discoverProperties.get("willChangeCard");
        JSONObject newAbilities = (JSONObject) discoverProperties.get("newAbilities");

        try {
            gameHandler.discover(specificCardClass, specificCardClassName, willChangeCard, newAbilities);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    public static void restore(Cards.card card, GameHandler gameHandler, JSONObject restoreProperties) {
        boolean canOnlyRestoreHero = (Boolean) restoreProperties.get("canOnlyRestoreHero");
        int restoreAmount = (Integer) restoreProperties.get("amount");
        if (canOnlyRestoreHero) {
            gameHandler.restoreHero(restoreAmount);
        } else {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Thread waitThread = gameHandler.getOneWayTarget(true, true);
                    try {
                        waitThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Vulnerable pawn = gameHandler.getCrosshairTargetAndReset();

                    gameHandler.restore((Restore) pawn, restoreAmount);
                }
            });
            thread.start();
        }
    }

    public static void give(Cards.card card, GameHandler gameHandler, JSONObject giveProperties) {
        int numberOfTargets = (Integer) giveProperties.get("targets");
        Cards.minion minion = null;
        if (numberOfTargets == 1) { // The user will choose a target
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Thread waitThread = gameHandler.getOneWayTarget(true, false);
                    try {
                        waitThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    gameHandler.setCanChooseHero(true);
                    handleGive((Cards.minion) gameHandler.getCrosshairTargetAndReset(), giveProperties);
                }
            });
            thread.start();
        } else if (numberOfTargets == -1) { // A random target
            ArrayList<Vulnerable> onTheGround = gameHandler.getFriendlyMinions();
            Random random = new Random(System.nanoTime());
            try {
                minion = (Cards.minion) onTheGround.get(random.nextInt(onTheGround.size()));
                handleGive(minion, giveProperties);
            } catch (Exception e) {
                // This is probably the case in which no target was available for random selection
            }
        } else if (numberOfTargets == -2) { // the current card
            minion = (Cards.minion) card;
            handleGive(minion, giveProperties);
        }


    }

    public static void handleGive(Cards.minion minion, int hpBoost, int attackBoost) {
        if (minion == null) return;
        if (hpBoost < 0) {
            hpBoost = -(hpBoost + 1) * minion.getHealth();
        }

        minion.setHealth(minion.getHealth() + hpBoost);
        minion.setAttackPower(minion.getAttackPower() + attackBoost);
    }

    public static void handleGive(Cards.minion minion, JSONObject giveProperties) {
        if (minion == null) return;
        int hpBoost = (Integer) giveProperties.get("hpBoost");
        int attackBoost = (Integer) giveProperties.get("attackBoost");

        handleGive(minion, hpBoost, attackBoost);

        JSONObject minionAbilities = minion.getAbilities();
        HashMap<String, Boolean> minionActivatedAbilities = minion.getActivatedAbilities();
        JSONObject newAbilities = (JSONObject) giveProperties.get("newAbilities");
        if (newAbilities != null) {
            for (Object o : newAbilities.keySet()) {
                String s = (String) o;
                minionAbilities.put(s, newAbilities.get(s));
                minionActivatedAbilities.put(s, false);
            }
        }
    }

    public static void handleGive(Cards.weapon weapon, int durabilityBoost, int attackBoost) {
        if (weapon == null) return;
        weapon.setDurability(weapon.getDurability() + durabilityBoost);
        weapon.setAttackPower(weapon.getAttackPower() + attackBoost);
    }

    public static void summon(Cards.card card, GameHandler gameHandler, JSONObject summonProperties) {
        int numberOfCardsToSummon = (Integer) summonProperties.get("numberOfCardsToSummon");
        String fromWhere = (String) summonProperties.get("fromHandDeckNoWhere");
        boolean specificCard = (Boolean) summonProperties.get("specificCard");
        String specificCardName = (String) summonProperties.get("specificCardName");

        if (specificCard) {
            Cards.minion minion = Utility.SerializationFunctions.minionDeserialize(specificCardName);
            for (int i = 0; i < numberOfCardsToSummon; i++) {
                minion.setOwner(card.getOwner());
                gameHandler.summon(minion, fromWhere);
            }
        } else {
            ArrayList<Cards.minion> possibleMinions = new ArrayList<>();
            if (fromWhere.equals("deck")) {
                for (Cards.card card1 : gameHandler.getCurrentDeck()) {
                    if (card1.getType().equals("Minion")) possibleMinions.add((Cards.minion) card1);
                }
            } else if (fromWhere.equals("hand")) {
                for (Cards.card card1 : gameHandler.getHand(gameHandler.getCurrentTurn())) {
                    if (card1.getType().equals("Minion")) possibleMinions.add((Cards.minion) card1);
                }
            }
            Random random = new Random(System.nanoTime());
            Cards.minion minion = possibleMinions.get(random.nextInt(possibleMinions.size()));
            prepareMinionForSummon(minion);
            if (card != null)
                minion.setOwner(card.getOwner());
            gameHandler.summon(minion, fromWhere);
            if (numberOfCardsToSummon > 1) {
                for (int i = 0; i < numberOfCardsToSummon - 1; i++) {
                    Cards.minion newMinion = minion.cloneThis();
                    prepareMinionForSummon(newMinion);
                    if (card != null)
                        minion.setOwner(card.getOwner());
                    gameHandler.summon(newMinion, fromWhere);
                }
            }
        }
    }

    private static void prepareMinionForSummon(Cards.minion minion) {
        minion.addToAbilities("battlecry", null);
        switch (minion.getName()) {
            case "Tomb Warden":
                minion.addToAbilities("summon", null);
                break;
            case "Twighlight flamecaller":
                minion.addToAbilities("deal", null);
                break;
            case "Plaguebringer":
                minion.addToAbilities("give", null);
        }
    }
}
