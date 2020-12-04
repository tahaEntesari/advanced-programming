package Hero;

import Card.InitiateCards;
import Configurations.Main_config_file;
import Deck.Deck;
import Utility.FileFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static Deck.DeckFunctions.cardInDeck;

public class HeroFunctions {
    public static ArrayList<String> cardInWhichDecks(ArrayList<Deck> decks, String cardName, String heroName){
        ArrayList<String> result = new ArrayList<>();
        for (Deck deck : decks) {
            if(cardInDeck(deck, cardName) > 0) {
                result.add(heroName + ": " + deck.getDeckName() + "\n");
            }
        }
        return result;
    }

    public static ArrayList<String> getAllCardsForAHero(String heroName) {
        ArrayList<String> allCards = new ArrayList<>();
        JSONArray array = FileFunctions.loadJsonArray(Main_config_file.getAllCardsJSONFile());
        JSONObject temp_obj;
        for (int i = 0; i < array.size(); i++) {
            temp_obj = (JSONObject) array.get(i);
            String heroClass = (String) temp_obj.get("heroClass");
            if (heroClass.equals("Neutral") || heroClass.equals(heroName)) {
                allCards.add((String) temp_obj.get("name"));
            }
        }
        return allCards;
    }

    public static JSONArray getAllCardsForAHeroWithDetails(String heroName){
        JSONArray array = FileFunctions.loadJsonArray(Main_config_file.getAllCardsJSONFile());
        JSONArray resultArray = new JSONArray();
        JSONObject temp_obj;
        for (int i = 0; i < array.size(); i++) {
            temp_obj = (JSONObject) array.get(i);
            String heroClass = (String) temp_obj.get("heroClass");
            if (heroClass.equals("Neutral") || heroClass.equals(heroName)) {
                resultArray.add(temp_obj.clone());
            }
        }
        return resultArray;
    }

    public static ArrayList<String> getAllAvailableCards(String heroName){
        Set<String> availableCards = new HashSet<>();
        availableCards.add("Polymorph");
        availableCards.add("Goldshire Footman");
        availableCards.add("Stonetusk Boar");
        availableCards.add("Shieldbearer");
        availableCards.add("Novice Engineer");
        availableCards.add("Lord of the Arena");
        availableCards.add("Regenerate");
        availableCards.add("Holy Fire");
        availableCards.add("Arcane Missiles");
        availableCards.add("Friendly Smith");
        availableCards.add("Ashbringer");
        availableCards.add("Depth Charge");
        availableCards.add("Bloodfen Raptor");
        availableCards.add("Sunreaver Warmage");
        availableCards.add("Flamestrike");
        availableCards.add("Living Monument");
        availableCards.add("Dreadscale");
        availableCards.add("Mosh'Ogg Enforcer");
        availableCards.add("Gnomish Army Knife");
        availableCards.add("Sprint");
        availableCards.add("Pharaoh's Blessing");
        availableCards.add("Sathrovarr");
        availableCards.add("Curio Collector");
        availableCards.add("Book of Specters");
        availableCards.add("Strength in Numbers");
        availableCards.add("Shadowfiend");
        availableCards.add("High Priest Amet");
        availableCards.add("Tomb warden");
        availableCards.add("Security Rover");
        availableCards.add("Divine Spirit");
        availableCards.add("Silver Hand Recruit");
        availableCards.add("Viserion");
        availableCards.add("Locust");
        availableCards.add("Terminator");
        availableCards.add("Sheep");



        ArrayList<String> result = new ArrayList<>(availableCards);
        ArrayList<String> temp = new ArrayList<>(result);
        String [] heroClasses = InitiateCards.getHeroClass();
        for (String s : temp) {
            if(!(Card.CardUtilityFunctions.getCardObjectFromName(s).getHeroClass().equals(heroClasses[0])
                    || Card.CardUtilityFunctions.getCardObjectFromName(s).getHeroClass().equals(heroName))){
                result.remove(s);
            }
        }
        return result;
    }

    public static ArrayList<String> getDefaultDeckCards(String heroName) {
        ArrayList<String> availableCards = new ArrayList<>();
        switch (heroName) {
            case "Mage":
                availableCards.add("Polymorph");
                availableCards.add("Goldshire Footman");
                availableCards.add("Stonetusk Boar");
                availableCards.add("Shieldbearer");
                availableCards.add("Novice Engineer");
                availableCards.add("Divine Spirit");
                availableCards.add("Lord of the Arena");
                availableCards.add("Regenerate");
                availableCards.add("Holy Fire");
                availableCards.add("Arcane Missiles");

                break;
            case "Rogue":
                availableCards.add("Friendly Smith");
                availableCards.add("Goldshire Footman");
                availableCards.add("Ashbringer");
                availableCards.add("Stonetusk Boar");
                availableCards.add("Depth Charge");
                availableCards.add("Bloodfen Raptor");
                availableCards.add("Sunreaver Warmage");
                availableCards.add("Sprint");
                availableCards.add("Flamestrike");
                availableCards.add("Living Monument");

                break;
            case "Warlock":
                availableCards.add("Dreadscale");
                availableCards.add("Regenerate");
                availableCards.add("Mosh'Ogg Enforcer");
                availableCards.add("Divine Spirit");
                availableCards.add("Stonetusk Boar");
                availableCards.add("Depth Charge");
                availableCards.add("Novice Engineer");
                availableCards.add("Sunreaver Warmage");
                availableCards.add("Goldshire Footman");
                availableCards.add("Shieldbearer");
                break;
            case "Paladin":
                availableCards.add("Gnomish Army Knife");
                availableCards.add("Sprint");
                availableCards.add("Pharaoh's Blessing");
                availableCards.add("Sathrovarr");
                availableCards.add("Curio Collector");
                availableCards.add("Ashbringer");
                availableCards.add("Sunreaver Warmage");
                availableCards.add("Novice Engineer");
                availableCards.add("Book of Specters");
                availableCards.add("Strength in Numbers");
                break;
            case "Priest":
                availableCards.add("Shadowfiend");
                availableCards.add("High Priest Amet");
                availableCards.add("Tomb warden");
                availableCards.add("Security Rover");
                availableCards.add("Goldshire Footman");
                availableCards.add("Strength in Numbers");
                availableCards.add("Regenerate");
                availableCards.add("Sprint");
                availableCards.add("Divine Spirit");
                availableCards.add("Novice Engineer");
                break;
        }
        return availableCards;
    }
}
