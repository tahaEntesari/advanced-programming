package Card;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class AbilityInitializationFunctions {
    public static void deal(Cards.card card, int attackAmount, boolean attackAmountIsWholeSum, int numberOfTargets,
                     boolean randomTargets){
        /*
        attackAmountIsWholeSum: if set to false, the selected targets will EACH get attackAmount whereas if set true,
        the total attack that all the targets take must add up to attackAmount.
        numberOfTargets = -2: attack all enemy minion. -3: attack all enemy minion and hero, -4: all minions,
        -5: all minions except this one.

        deal.put("amount", attackAmount); // -1: destroy each minion
         */

        card.addToAbilities("deal",
                dealObject(attackAmount, attackAmountIsWholeSum, numberOfTargets, randomTargets));
    }

    public static JSONObject dealObject(int attackAmount, boolean attackAmountIsWholeSum, int numberOfTargets,
                                        boolean randomTargets){
        JSONObject deal = new JSONObject();
        deal.put("amount", attackAmount); // -1: destroy each minion
        deal.put("attackAmountIsWholeSum", attackAmountIsWholeSum);
        deal.put("numberOfTargets", numberOfTargets);
        deal.put("randomTargets", randomTargets);
        return deal;
    }

    public static void draw(Cards.card card, int numberOfCardsToDraw, boolean specificCardDraw, String specificCardDrawType,
                     boolean discardSpecificCards, String discardCardClass){

        card.addToAbilities("draw", drawObject(numberOfCardsToDraw, specificCardDraw, specificCardDrawType,
                discardSpecificCards, discardCardClass));
    }

    public static JSONObject drawObject(int numberOfCardsToDraw, boolean specificCardDraw, String specificCardDrawType,
                                 boolean discardSpecificCards, String discardCardClass){
        JSONObject draw = new JSONObject();
        draw.put("numberOfCardsToDraw", numberOfCardsToDraw);
        draw.put("specificCardDraw", specificCardDraw);
        draw.put("specificCardDrawType", specificCardDrawType);
        draw.put("discardSpecificCards", discardSpecificCards);
        draw.put("discardCardClass", discardCardClass);
        return draw;
    }

    public static void discover(Cards.card card,
//                                int numberOfCardsToDiscover,
                                boolean specificCardClass,
                         String specificCardClassName,
//                                boolean addToHandOrDeck,
                         boolean willChangeCard, JSONObject newAbilities){
        /*
        addToHandOrDeck: if True, will add to hand else, deck.
         */
        JSONObject discover = new JSONObject();
        discover.put("specificCardClass", specificCardClass);
        discover.put("specificCardClassName", specificCardClassName);
        discover.put("willChangeCard", willChangeCard);
        discover.put("newAbilities", newAbilities);

        card.addToAbilities("discover", discover);
    }

    public static void restore(Cards.card card, boolean canOnlyRestoreHero, int restoreAmount){
        JSONObject restore = new JSONObject();
        restore.put("canOnlyRestoreHero", canOnlyRestoreHero);
        restore.put("amount", restoreAmount);

        card.addToAbilities("restore", restore);
    }

    public static void give(Cards.card card, JSONObject newAbilities,
                            int numberOfTargets, int hpBoost, int attackBoost){
        card.addToAbilities("give", giveObject(newAbilities, numberOfTargets, hpBoost, attackBoost));
    }

    public static JSONObject giveObject(JSONObject newAbilities, int numberOfTargets, int hpBoost, int attackBoost){
        JSONObject give = new JSONObject();
        give.put("newAbilities", newAbilities);
        give.put("targets", numberOfTargets); // 1: choose. -1: random. -2: this card
        give.put("hpBoost", hpBoost); // -2: double a minions hp
        give.put("attackBoost", attackBoost);
        return give;
    }

//    public void reinstate(Cards.card card, int numberOfCardsToReturn, boolean toHand, boolean specificCardClass,
//                       String specificCardClassName){
//        JSONObject reinstate = new JSONObject();
//        reinstate.put("numberOfCardsToReturn", numberOfCardsToReturn);
//
//        card.abilities.put("reinstate", reinstate);
//    }

    public static void summon(Cards.card card, int numberOfCardsToSummon,
                              String fromHandDeckNowhere,
//                       boolean specificCardClass, String specificCardClassName,
                       boolean specificCard, String specificCardName){
        /*
        fromHandDeckNoWhere: "hand", "deck", "nowhere"
         */
        card.addToAbilities("summon", summonObject(numberOfCardsToSummon, fromHandDeckNowhere,
                specificCard, specificCardName));
    }

    public static JSONObject summonObject(int numberOfCardsToSummon,
                                          String fromHandDeckNowhere,
//                                          boolean specificCardClass, String specificCardClassName,
                                          boolean specificCard, String specificCardName){
        JSONObject summon = new JSONObject();
        summon.put("numberOfCardsToSummon", numberOfCardsToSummon);
        summon.put("fromHandDeckNoWhere", fromHandDeckNowhere);
//        summon.put("specificCardClass", specificCardClass);
//        summon.put("specificCardClassName", specificCardClassName);
        summon.put("specificCard", specificCard);
        summon.put("specificCardName", specificCardName);
        return summon;
    }
}
