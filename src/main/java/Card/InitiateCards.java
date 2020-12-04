package Card;

import Configurations.Main_config_file;
import Utility.FileFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

import static Utility.SerializationFunctions.Serialize;

public class InitiateCards {

    static String[] rarity = {"Common", "Rare", "Epic", "Legendary"};
    static String[] heroClass = {"Neutral", "Mage", "Rogue", "Warlock", "Paladin", "Priest"};


    public static String[] getRarity() {
        return rarity;
    }

    public static String[] getHeroClass() {
        return heroClass;
    }

    public static void InstantiateAllCards() {
        JSONArray cardArray = new JSONArray();
        ArrayList<Cards.card> neutralCards = new ArrayList<>();
        ArrayList<Cards.card> mageCards = new ArrayList<>();
        ArrayList<Cards.card> rogueCards = new ArrayList<>();
        ArrayList<Cards.card> warlockCards = new ArrayList<>();
        ArrayList<Cards.card> paladinCards = new ArrayList<>();
        ArrayList<Cards.card> priestCards = new ArrayList<>();

        ArrayList<ArrayList<Cards.card>> allCards = new ArrayList<>();
        allCards.add(neutralCards);
        allCards.add(mageCards);
        allCards.add(rogueCards);
        allCards.add(warlockCards);
        allCards.add(paladinCards);
        allCards.add(priestCards);

        Cards.minion minion;
        Cards.spell spell;
        Cards.weapon weapon;
        Cards.quest quest;

        ///////////////////////////////////////////////// Minions /////////////////////////////////////////////////////
        minion = new Cards.minion("Goldshire Footman", 1, 2, 1, heroClass[0],
                rarity[0], "Taunt");
        minion.addToAbilities("taunt", true);
        neutralCards.add(minion);

        minion = new Cards.minion("Stonetusk Boar", 1, 1, 1, heroClass[0],
                rarity[0], "Charge");
        minion.addToAbilities("charge", true);
        neutralCards.add(minion);

        minion = new Cards.minion("Shieldbearer", 1, 4, 0, heroClass[0],
                rarity[0], "Taunt");
        minion.addToAbilities("taunt", true);
        neutralCards.add(minion);

        minion = new Cards.minion("Depth Charge", 1, 5, 0, heroClass[0],
                rarity[0], "At the start of your turn, deal 5 damage to all minions");
        JSONObject depthChargeTurnStartAbility = new JSONObject();
        depthChargeTurnStartAbility.put("deal",
                AbilityInitializationFunctions.dealObject(5, false,
                        -4, false));
        minion.addToSpecificActions("turnStart", depthChargeTurnStartAbility);
        neutralCards.add(minion);

        minion = new Cards.minion("Bloodfen Raptor", 2, 2, 3, heroClass[0],
                rarity[0], "");
        neutralCards.add(minion);

        minion = new Cards.minion("Novice Engineer", 2, 1, 1, heroClass[0],
                rarity[0], "Battlecry: Draw a card.");
        AbilityInitializationFunctions.draw(minion, 1, false, "",
                false, "");
        neutralCards.add(minion);

        minion = new Cards.minion("Sunreaver Warmage", 5, 4, 4, heroClass[0],
                rarity[0], "Battlecry: If you're holding a spell that costs 5 or more, deal 4 damage.");
        neutralCards.add(minion);

        minion = new Cards.minion("Lord of the Arena", 6, 5, 6, heroClass[0],
                rarity[0], "Taunt");
        minion.addToAbilities("taunt", true);
        neutralCards.add(minion);

        minion = new Cards.minion("Mosh'Ogg Enforcer", 8, 14, 2, heroClass[0],
                rarity[0], "Taunt\n Divine shield");
        minion.addToAbilities("taunt", true);
        minion.addToAbilities("divineShield", true);
        neutralCards.add(minion);

        minion = new Cards.minion("Living Monument", 10, 10, 10, heroClass[0],
                rarity[0], "Taunt");
        minion.addToAbilities("taunt", true);
        neutralCards.add(minion);

        minion = new Cards.minion("Sathrovarr", 9, 5, 5, heroClass[0],
                rarity[3], "Battlecry: Choose a friendly minion." +
                " Add a copy of it to your hand, deck, and battlefield.");
        neutralCards.add(minion);

        minion = new Cards.minion("Tomb warden", 8, 6, 3, heroClass[0],
                rarity[2], "Taunt\nBattlecry: Summon a copy of this minion.");
        minion.addToAbilities("taunt", true);
        AbilityInitializationFunctions.summon(minion, 1, "nowhere",
                 true, "Tomb warden");
        neutralCards.add(minion);

        minion = new Cards.minion("Security Rover", 6, 6, 2, heroClass[0],
                rarity[2], "Whenever this minion takes damage, summon a 2/3 Mech with Taunt.");
        JSONObject securityRoverAction = new JSONObject();
        securityRoverAction.put("summon", AbilityInitializationFunctions.summonObject(
                1, "nowhere",
                true, "Terminator"));
        minion.addToSpecificActions("uponDamage", securityRoverAction);
        neutralCards.add(minion);

        minion = new Cards.minion("Curio Collector", 5, 4, 4, heroClass[0],
                rarity[2], "Whenever you draw a card, gain +1/+1.");
        JSONObject curioCollectorAction = new JSONObject();
        curioCollectorAction.put("actOn", "previous");
        curioCollectorAction.put("give", AbilityInitializationFunctions.giveObject(null, -2,
                1, 1));
        minion.addToSpecificActions("uponDraw", curioCollectorAction);
        neutralCards.add(minion);

        minion = new Cards.minion("Sheep", 1, 1, 1, heroClass[0],
                rarity[2], "");
        neutralCards.add(minion);

        minion = new Cards.minion("Terminator", 2, 3, 2, heroClass[0],
                rarity[0], "Taunt");
        minion.addToAbilities("taunt", true);
        neutralCards.add(minion);

        minion = new Cards.minion("Viserion", 5, 6, 6, heroClass[0],
                rarity[0], "Reborn");
        minion.addToAbilities("reborn", true);
        neutralCards.add(minion);

        minion = new Cards.minion("Locust", 0, 1, 1, heroClass[0],
                rarity[0], "Rush");
        minion.addToAbilities("rush", true);
        neutralCards.add(minion);

        minion = new Cards.minion("Silver Hand Recruit", 1, 1, 1, heroClass[4],
                rarity[0], "");
        neutralCards.add(minion);

        ///////////////////////////////////////////////// Spells /////////////////////////////////////////////////////
        spell = new Cards.spell("Regenerate", 0, heroClass[0], rarity[0],
                "Restore 3 health");
        AbilityInitializationFunctions.restore(spell, false, 3);
        neutralCards.add(spell);

        spell = new Cards.spell("Divine Spirit", 2, heroClass[0], rarity[0],
                "Double a minions health");
        AbilityInitializationFunctions.give(spell, null, 1, -2, 0);
        neutralCards.add(spell);

        spell = new Cards.spell("Holy Fire", 6, heroClass[0], rarity[1],
                "Deal 5 damage. Restore 5 health to your here");
        AbilityInitializationFunctions.deal(spell, 5, false, 1, false);
        AbilityInitializationFunctions.restore(spell, true, 5);
        neutralCards.add(spell);

        spell = new Cards.spell("Arcane Missiles", 1, heroClass[0], rarity[0],
                "Deal 3 damage to random enemies.");
        AbilityInitializationFunctions.deal(spell, 3, true, 3, true);
        neutralCards.add(spell);

        spell = new Cards.spell("Fireball", 4, heroClass[0], rarity[0],
                "Deal 6 damage.");
        AbilityInitializationFunctions.deal(spell, 6, false, 1, false);
        neutralCards.add(spell);

        spell = new Cards.spell("Flamestrike", 7, heroClass[0], rarity[2],
                "Deal 4 damage to all enemy minions.");
        AbilityInitializationFunctions.deal(spell, 4, false, -2, false);
        neutralCards.add(spell);

        spell = new Cards.spell("Sprint", 7, heroClass[0], rarity[0], "Draw 4 cards");
        AbilityInitializationFunctions.draw(spell, 4, false, "",
                false, "");
        neutralCards.add(spell);

        spell = new Cards.spell("Swarm of locusts", 6, heroClass[0], rarity[1],
                "Summon seven 1/1 Locusts with Rush");
        AbilityInitializationFunctions.summon(spell, 7, "nowhere",
                 true, "Locust");
        neutralCards.add(spell);

        spell = new Cards.spell("Pharaoh's Blessing", 6, heroClass[0], rarity[1],
                "Give a minion +4/+4, Divine Shield, and Taunt.");
        JSONObject pharaohAbilities = new JSONObject();
        pharaohAbilities.put("divineShield", true);
        pharaohAbilities.put("taunt", true);
        AbilityInitializationFunctions.give(spell, pharaohAbilities, 1, 4, 4);
        neutralCards.add(spell);

        spell = new Cards.spell("Book of Specters", 2, heroClass[0], rarity[2],
                "Draw 3 cards. Discard any spells drawn");
        AbilityInitializationFunctions.draw(spell, 3, false, "",
                true, "Spell");
        neutralCards.add(spell);


        ///////////////////////////////////////////////// Quests /////////////////////////////////////////////////////
        quest = new Cards.quest("Strength in Numbers", 1, heroClass[0], rarity[0],
                "Sidequest: Spend 10 Mana on minions. Reward: Summon a minion from your deck.");
        JSONObject strengthInNumbersQuest = new JSONObject();
        strengthInNumbersQuest.put("completed", 0);
        strengthInNumbersQuest.put("manaAmount", 10);
        strengthInNumbersQuest.put("specificClass", "Minion");
        quest.addQuestDescription(strengthInNumbersQuest);
        JSONObject strengthInNumbersReward = new JSONObject();
        strengthInNumbersReward.put("summon", AbilityInitializationFunctions.summonObject(
                1, "deck",
                false, ""));
        quest.addQuestReward(strengthInNumbersReward);
        neutralCards.add(quest);

        quest = new Cards.quest("Learn Draconic", 1, heroClass[0], rarity[0],
                "Sidequest: Spend 8 Mana on spells. Reward: Summon a 6/6 Dragon.");
        JSONObject learnDraconicQuest = new JSONObject();
        strengthInNumbersQuest.put("completed", 0);
        learnDraconicQuest.put("manaAmount", 8);
        learnDraconicQuest.put("specificClass", "Spell");
        quest.addQuestDescription(learnDraconicQuest);
        JSONObject learnDraconicReward = new JSONObject();
        learnDraconicReward.put("summon", AbilityInitializationFunctions.summonObject(
                1, "nowhere",
                true, "Viserion"));
        quest.addQuestReward(learnDraconicReward);
        neutralCards.add(quest);

        ///////////////////////////////////////////////// Weapons /////////////////////////////////////////////////////
        weapon = new Cards.weapon("Blood Fury", 3, 8, 3, heroClass[0],
                rarity[3], "");
        neutralCards.add(weapon);

        weapon = new Cards.weapon("Poisoned Dagger", 1, 2, 2, heroClass[0],
                rarity[0], "");
        neutralCards.add(weapon);

        weapon = new Cards.weapon("Ashbringer", 5, 3, 5, heroClass[0],
                rarity[1], "");
        neutralCards.add(weapon);

        ///////////////////////////////////////////// Specific Hero Cards /////////////////////////////////////////////
        spell = new Cards.spell("Polymorph", 4, heroClass[1], rarity[0],
                "Transform a minion into a 1/1 sheep");
        mageCards.add(spell);

        minion = new Cards.minion("Twilight Flamecaller", 3, 2, 2, heroClass[1],
                rarity[0], "Battlecry: Deal 1 damage to all enemy minions");
        AbilityInitializationFunctions.deal(minion, 1, false, -2,
                false);
        mageCards.add(minion);


        spell = new Cards.spell("Friendly Smith", 1, heroClass[2], rarity[0],
                "Discover a weapon from any class and add it to your deck with +2/+2");
        JSONObject friendlySmithGiveAbilities = new JSONObject();
        friendlySmithGiveAbilities.put("durabilityBoost", 2);
        friendlySmithGiveAbilities.put("attackBoost", 2);
        AbilityInitializationFunctions.discover(spell, true, "Weapon",
                true, friendlySmithGiveAbilities);
        rogueCards.add(spell);

        minion = new Cards.minion("Plaguebringer", 4, 3, 3, heroClass[2],
                rarity[1], "Battlecry: Give a friendly minion poisonous.");
        JSONObject plaguebringerGiveAbilities = new JSONObject();
        plaguebringerGiveAbilities.put("hpBoost", 0);
        plaguebringerGiveAbilities.put("attackBoost", 0);
        JSONObject poisonGiveObject = new JSONObject();
        poisonGiveObject.put("poisonous", true);
        plaguebringerGiveAbilities.put("newAbilities", poisonGiveObject);
        minion.addToAbilities("give", plaguebringerGiveAbilities);
        rogueCards.add(minion);

        weapon = new Cards.weapon("Shadowblade", 3, 2, 3, heroClass[2],
                rarity[1], "Battlecry: Your hero is immune this turn.");
        rogueCards.add(weapon);


        minion = new Cards.minion("Dreadscale", 3, 2, 4, heroClass[3],
                rarity[0], "At the end of your turn, deal 1 damage to all other minions");
        JSONObject dreadscaleDealObject = new JSONObject();
        dreadscaleDealObject.put("deal", AbilityInitializationFunctions.dealObject(1,
                false, -5, false));
        minion.addToSpecificActions("turnEnd", dreadscaleDealObject);
        warlockCards.add(minion);


        spell = new Cards.spell("Twisting Nether", 8, heroClass[3],
                rarity[1], "Destroy all minions.");
        AbilityInitializationFunctions.deal(spell, -1, false, -4,
                false);
        warlockCards.add(spell);

        weapon = new Cards.weapon("Skull of the Man'ari", 5, 3, 0,
                heroClass[3], rarity[0], "At the start of your turn, summon a minion from your hand");
        JSONObject skullSummonObject = new JSONObject();
        skullSummonObject.put("summon", AbilityInitializationFunctions.summonObject(
                1, "hand", false, ""));
        weapon.addToActionBasedAbilities("turnStart", skullSummonObject);
        warlockCards.add(weapon);


        spell = new Cards.spell("Gnomish Army Knife", 5, heroClass[4], rarity[2],
                "Give a minion Charge, Windfury, Divine Shield, Lifesteal, Poisonous and Taunt");
        JSONObject gnomishGiveObject = new JSONObject();
        gnomishGiveObject.put("charge", true);
        gnomishGiveObject.put("windfury", true);
        gnomishGiveObject.put("divineShield", true);
        gnomishGiveObject.put("lifeSteal", true);
        gnomishGiveObject.put("poisonous", true);
        gnomishGiveObject.put("taunt", true);
        AbilityInitializationFunctions.give(spell, gnomishGiveObject, 1, 0, 0);
        paladinCards.add(spell);


        spell = new Cards.spell("Consecration", 4, heroClass[4], rarity[0],
                "Deal 2 damage to all enemies");
        AbilityInitializationFunctions.deal(spell, 2, false, -3, false);
        paladinCards.add(spell);


        minion = new Cards.minion("High Priest Amet", 4, 7, 2, heroClass[5],
                rarity[3], "Whenever you summon a minion, set its Health equal to this minion's.");
        minion.addToSpecificActions("uponSummon", new JSONObject());
        priestCards.add(minion);


        minion = new Cards.minion("Shadowfiend", 3, 3, 3, heroClass[5],
                rarity[2], "Whenever you draw a card, reduce its Cost by (1).");
        minion.addToSpecificActions("uponDraw", new JSONObject());
        priestCards.add(minion);



        for (ArrayList<Cards.card> cardPackage : allCards) {
            for (Cards.card card : cardPackage) {
                cardArray.add(card.getCardJsonObject());
                Serialize(card, Main_config_file.returnCardSaveDataLocation(card.getName()));
            }

        }
        FileFunctions.saveJsonArray(cardArray, Main_config_file.getAllCardsJSONFile());
    }
}

