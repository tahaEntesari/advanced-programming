package Collection;

import Card.InitiateCards;
import Deck.Deck;
import GUI.Panels.CollectionPanels.CollectionPanel;
import GameState.GameState;
import Hero.Hero;
import PanelFunctions.ShopAndCollectionVisualizationFunctions;

import javax.swing.*;
import java.awt.*;

public class DrawerFunctions {
    private CollectionPanel collectionPanelMainClass;
    private GameState gameState = GameState.getInstance();

    public DrawerFunctions(CollectionPanel collectionPanelMainClass) {
        this.collectionPanelMainClass = collectionPanelMainClass;
    }
    public void resetVis(){
        collectionPanelMainClass.setDisplayingHeroClass(0);
        collectionPanelMainClass.setCurrentManaFilterButton(11);

//        collectionPanelMainClass.getCardLayoutForDeckView().show(collectionPanelMainClass.getDeckViewMainPanel(),
//                "overview");
//        collectionPanelMainClass.setCurrentChosenDeck(null);
        collectionPanelMainClass.refreshPage();
    }

    public void refreshPage(){
        collectionPanelMainClass.getDeckOverviewPanel().completeDecksPanel();
        collectionPanelMainClass.requestFocus();
        collectionPanelMainClass.setDisplayingPageNumber(0);

        collectionPanelMainClass.getCollectionToolbarPanel().setSearchTextFieldText("");

        String displayingWhichCards = collectionPanelMainClass.getDisplayingWhichCards();
        if (displayingWhichCards.equals("all")) {
            collectionPanelMainClass.setCardsOnDisplay(
                    ShopAndCollectionVisualizationFunctions.getAllCardsOfAHeroClass(
                            gameState.getUser(),
                            InitiateCards.getHeroClass()[collectionPanelMainClass.getDisplayingHeroClass()],
                            collectionPanelMainClass.getCurrentManaFilterButton()));

        } else if (displayingWhichCards.equals("available")) {
            collectionPanelMainClass.setCardsOnDisplay(
                    ShopAndCollectionVisualizationFunctions.getAvailableCardsOfAHeroClass(
                            gameState.getUser(),
                            InitiateCards.getHeroClass()[collectionPanelMainClass.getDisplayingHeroClass()],
                            collectionPanelMainClass.getCurrentManaFilterButton()));
        } else if (displayingWhichCards.equals("buyable")) {
            collectionPanelMainClass.setCardsOnDisplay(
                    ShopAndCollectionVisualizationFunctions.getPurchasableCards(
                            gameState.getUser(),
                            InitiateCards.getHeroClass()[collectionPanelMainClass.getDisplayingHeroClass()],
                            collectionPanelMainClass.getCurrentManaFilterButton()));
        }
        collectionPanelMainClass.cardsToDraw(collectionPanelMainClass.getCardsOnDisplay());

        collectionPanelMainClass.colorTheButtons();
        collectionPanelMainClass.revalidate();
        collectionPanelMainClass.repaint();
        collectionPanelMainClass.requestFocus();
    }

    public void showDeckCards(String heroName, String deckName) {
        outer:
        for (Hero hero : gameState.getUser().getHeroes()) {
            if (hero.getName().equals(heroName)) {
                for (Deck deck : hero.getDecks()) {
                    if (deck.getDeckName().equals(deckName)) {
                        collectionPanelMainClass.setCurrentChosenDeck(deck);
                        break outer;
                    }
                }
            }
        }
        showDeckCards();
    }

    public void showDeckCards() {
        collectionPanelMainClass.getDeckDisplayPanel().showDeckCards(collectionPanelMainClass);
        collectionPanelMainClass.getCardLayoutForDeckView().show(collectionPanelMainClass.getDeckViewMainPanel(), "display");
    }

    public void colorTheButtons() {
        JButton[] heroClassesButtons = collectionPanelMainClass.getCollectionToolbarPanel().getHeroClassesButtons();
        heroClassesButtons[collectionPanelMainClass.getDisplayingHeroClass()].setBackground(Color.RED);
        for (int i = 0; i < InitiateCards.getHeroClass().length; i++) {
            if (i == collectionPanelMainClass.getDisplayingHeroClass()) continue;
            heroClassesButtons[i].setBackground(Color.WHITE);
        }
        JButton displayAll = collectionPanelMainClass.getCollectionToolbarPanel().getDisplayAll();
        JButton displayAvailable = collectionPanelMainClass.getCollectionToolbarPanel().getDisplayAvailable();
        JButton displayBuyable = collectionPanelMainClass.getCollectionToolbarPanel().getDisplayBuyable();

        if (collectionPanelMainClass.getDisplayingWhichCards().equals("all")) {
            displayAll.setBackground(Color.GREEN);
            displayAvailable.setBackground(Color.WHITE);
            displayBuyable.setBackground(Color.WHITE);
        } else if (collectionPanelMainClass.getDisplayingWhichCards().equals("available")) {
            displayAll.setBackground(Color.WHITE);
            displayAvailable.setBackground(Color.GREEN);
            displayBuyable.setBackground(Color.WHITE);
        } else if (collectionPanelMainClass.getDisplayingWhichCards().equals("buyable")) {
            displayAll.setBackground(Color.WHITE);
            displayAvailable.setBackground(Color.WHITE);
            displayBuyable.setBackground(Color.GREEN);
        }


        for (int i = 0; i < 12; i++) {
            if (i == collectionPanelMainClass.getCurrentManaFilterButton()) {
                collectionPanelMainClass.getManaFilterPanel().getManaFilterButtons()[i].setBackground(Color.BLUE);
            } else {
                collectionPanelMainClass.getManaFilterPanel().getManaFilterButtons()[i].setBackground(Color.WHITE);
            }
        }
    }
}
