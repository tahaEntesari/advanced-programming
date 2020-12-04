package PanelFunctions;

import java.util.ArrayList;

import User.User;

public class ShopAndCollectionVisualizationFunctions {
    public static ArrayList<String> getSellableCards(User user, String heroClass) {
        return user.getSellableCards(heroClass);
    }

    public static ArrayList<String> getPurchasableCards(User user, String heroClass) {
        return user.getBuyableHeroClassCards(heroClass);
    }

    public static ArrayList<String> getPurchasableCards(User user, String heroClass, int manaFilter) {
        ArrayList<String> result;
        if (manaFilter == 11) {
            result = user.getBuyableHeroClassCards(heroClass);
        } else {
            result = returnOnlySpecificMana(user.getBuyableHeroClassCards(heroClass), manaFilter);
        }
        return result;
    }

    public static ArrayList<String> getAllCardsOfAHeroClass(User user, String heroClass, int manaFilter) {
        ArrayList<String> result;
        if (manaFilter == 11) {
            result = user.getAllCards(heroClass);
        } else {
            result = returnOnlySpecificMana(user.getAllCards(heroClass), manaFilter);
        }
        return result;
    }

    public static ArrayList<String> getAvailableCardsOfAHeroClass(User user, String heroClass, int manaFilter) {
        ArrayList<String> result;
        if (manaFilter == 11) {
            result = user.getAvailableCards(heroClass);
        } else {
            result = returnOnlySpecificMana(user.getAvailableCards(heroClass), manaFilter);
        }
        return result;
    }

    private static ArrayList<String> returnOnlySpecificMana(ArrayList<String> list, int mana) {
        ArrayList<ArrayList<String>> tableOfSorted = returnTableOfSorted(list);
        return tableOfSorted.get(mana);
    }

    private static ArrayList<ArrayList<String>> returnTableOfSorted(ArrayList<String> unsorted) {
        ArrayList<ArrayList<String>> tableOfSorted = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            tableOfSorted.add(new ArrayList<String>());
        }
        String cardName;
        for (int i = 0; i < unsorted.size(); i++) {
            cardName = unsorted.get(i);
            tableOfSorted.get(Card.CardUtilityFunctions.getCardObjectFromName(cardName).getMana()).add(cardName);
        }
        return tableOfSorted;
    }

    public static ArrayList<String> sortCardsBasedOnMana(ArrayList<String> unsorted) {
        // I have made use of the fact that no card has mana higher than 10! (we can have cards with 0 mana!)
        ArrayList<String> sortedList = new ArrayList<>();
        ArrayList<ArrayList<String>> tableOfSorted = returnTableOfSorted(unsorted);
        for (ArrayList<String> subSortedList : tableOfSorted) {
            sortedList.addAll(subSortedList);
        }
        return sortedList;
    }

}
