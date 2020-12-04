package Collection;

import Card.CardUtilityFunctions;
import Card.InitiateCards;
import Deck.Deck;
import Deck.DeckFunctions;
import GUI.Panels.CollectionPanels.CollectionPanel;
import GameState.GameState;
import Hero.Hero;
import Utility.ClosestMatch;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import static Deck.DeckFunctions.getHeroFromDeck;
import static LoggingModule.LoggingClass.logUser;

public class CollectionEventHandlingFunctions {
    private CollectionPanel collectionPanelMainClass;
    private GameState gameState = GameState.getInstance();
    private String[] heroClasses = InitiateCards.getHeroClass();

    public CollectionEventHandlingFunctions(CollectionPanel collectionPanelMainClass) {
        this.collectionPanelMainClass = collectionPanelMainClass;
    }

    public void handleGo() {
        String searchFor = collectionPanelMainClass.getCollectionToolbarPanel().getSearchTextField().getText();
        if (searchFor.equals("")) return;
        logUser("Searched for card " + searchFor + " in shop.");
        String closestMatch = ClosestMatch.getClosestMatch(searchFor, gameState.getUser().getAllCards());

        for (int i = 0; i < heroClasses.length; i++) {
            if (CardUtilityFunctions.getCardObjectFromName(closestMatch).
                    getHeroClass().equals(heroClasses[i])) {
                collectionPanelMainClass.setDisplayingHeroClass(i);
                break;
            }
        }
        if (DeckFunctions.cardInSet(gameState.getUser().getAvailableCards(), closestMatch)) {
            collectionPanelMainClass.setDisplayingWhichCards("available");
        } else {
            collectionPanelMainClass.setDisplayingWhichCards("buyable");

        }
        collectionPanelMainClass.cardsToDraw(closestMatch);
        collectionPanelMainClass.requestFocus();

    }

    public void handleNext() {
        String message = "Collection: Next page command issued.";
        if (8 * (collectionPanelMainClass.getDisplayingPageNumber() + 1) <
                collectionPanelMainClass.getCardsOnDisplay().size()) {
            collectionPanelMainClass.setDisplayingPageNumber(collectionPanelMainClass.getDisplayingPageNumber() + 1);
            collectionPanelMainClass.cardsToDraw(collectionPanelMainClass.getCardsOnDisplay());
        } else {
            message += " Last page reached!";
        }
        logUser(message);
        collectionPanelMainClass.requestFocus();
    }

    public void handlePrevious() {
        String message = "Collection: Previous page command issued.";
        if (collectionPanelMainClass.getDisplayingPageNumber() > 0) {
            collectionPanelMainClass.setDisplayingPageNumber(collectionPanelMainClass.getDisplayingPageNumber() - 1);
            collectionPanelMainClass.cardsToDraw(collectionPanelMainClass.getCardsOnDisplay());
        } else {
            message += " Already on the first page!";
        }
        logUser(message);
        collectionPanelMainClass.requestFocus();
    }

    public void handleUpDown(boolean up) {
        String message = "Shop: change hero class via keyboard.";
        boolean successful = false;
        if (up) {
            if (collectionPanelMainClass.getDisplayingHeroClass() < heroClasses.length - 1) {
                collectionPanelMainClass.setDisplayingHeroClass(collectionPanelMainClass.getDisplayingHeroClass() + 1);
                collectionPanelMainClass.refreshPage();
                successful = true;
            }
        } else {
            if (collectionPanelMainClass.getDisplayingHeroClass() > 0) {
                collectionPanelMainClass.setDisplayingHeroClass(collectionPanelMainClass.getDisplayingHeroClass() - 1);
                collectionPanelMainClass.refreshPage();
                successful = true;
            }
        }
        if (successful) message += " Successful.";
        else message += " On last page of side.";
        logUser(message);
    }

    public void deckBackButtonClicked() {
        logUser("Collection: deck back button clicked.");
        Deck currentChosenDeck = collectionPanelMainClass.getCurrentChosenDeck();
        if (currentChosenDeck != null &&
                currentChosenDeck.getCurrentNumberOfCardsInDeck() != currentChosenDeck.getDeckSize()) {
            logUser("Error. Deck not at full capacity. Unable to go back");
            JOptionPane.showMessageDialog(collectionPanelMainClass.getCollectionPanel(), "Deck not full. Cannot go back");
            return;
        }
        collectionPanelMainClass.setCurrentChosenDeck(null);
        collectionPanelMainClass.getMainPanel().requestFocus();
        collectionPanelMainClass.getCardLayoutForDeckView().show(collectionPanelMainClass.getDeckViewMainPanel(), "overview");
        collectionPanelMainClass.refreshPage();
        collectionPanelMainClass.requestFocus();
    }

    public void deckDeleteButtonClicked() {
        logUser("Collection: deck delete button clicked");

        if (JOptionPane.showConfirmDialog(collectionPanelMainClass.getCollectionPanel(), "Delete deck?", "WARNING",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
            int deletionResult = DeckFunctions.deleteDeck(collectionPanelMainClass.getCurrentChosenDeck(), gameState.getUser());
            if (deletionResult == 400) {
                Hero deckHero = getHeroFromDeck(collectionPanelMainClass.getCurrentChosenDeck(), gameState.getUser());
                if(deckHero.getDecks().get(0).getDeckName().equals(collectionPanelMainClass.getCurrentChosenDeck().getDeckName())) {
                    logUser("Collection: deck delete unsuccessful since the hero only has one deck.");
                    JOptionPane.showMessageDialog(collectionPanelMainClass.getCollectionPanel(), "Deck delete unsuccessful since" +
                            " the hero only has one deck.");
                    return;
                } else {
                    logUser("Deleting deck amidst deck creation.");
                    JOptionPane.showMessageDialog(collectionPanelMainClass.getCollectionPanel(), "Deck deleted successfully");
                    collectionPanelMainClass.setCurrentChosenDeck(null);
                    deckBackButtonClicked();
                }
            } else if (deletionResult == 200) {
                logUser("Deleting deck " + collectionPanelMainClass.getCurrentChosenDeck().getDeckName());
                JOptionPane.showMessageDialog(collectionPanelMainClass.getCollectionPanel(), "Deck deleted successfully");
                collectionPanelMainClass.setCurrentChosenDeck(null);
                gameState.getUser().serializeUser();
                deckBackButtonClicked();
            }
        }
    }

    public void deckRenameButtonClicked() {
        logUser("Collection: deck rename button clicked");
        Deck currentChosenDeck = collectionPanelMainClass.getCurrentChosenDeck();
        String newName = JOptionPane.showInputDialog(collectionPanelMainClass, "Enter the deck's new name");
        if (newName == null || newName.equals("")) {
            logUser("Collection: name unchanged.");
        } else {
            Hero hero = getHeroFromDeck(currentChosenDeck, gameState.getUser());
            for (Deck deck : hero.getDecks()) {
                if (deck.getDeckName().equals(newName)) {
                    logUser("There already exists a deck with this name in the hero's collection.");
                    JOptionPane.showMessageDialog(collectionPanelMainClass.getCollectionPanel(), "There already exists a deck with this name " +
                            "in the hero's collection.");
                    return;
                }
            }
            currentChosenDeck.setDeckName(newName);
            if (currentChosenDeck.getDeckSize() == currentChosenDeck.getCurrentNumberOfCardsInDeck())
                gameState.getUser().serializeUser();
            collectionPanelMainClass.showDeckCards();
            collectionPanelMainClass.refreshPage();
            collectionPanelMainClass.requestFocus();
        }
    }

    public void deckChangeHeroButtonClicked() {
        logUser("Collection: deck change hero button clicked.");
        Deck currentChosenDeck = collectionPanelMainClass.getCurrentChosenDeck();
        if (!DeckFunctions.heroChangeFeasible(currentChosenDeck)) {
            logUser("Unsuccessful since there exist hero class cards in deck.");
            JOptionPane.showMessageDialog(collectionPanelMainClass.getCollectionPanel(), "Cannot change hero since there are hero " +
                    "class cards in the deck");
            return;
        }
        ArrayList<String> possibleHeroes = new ArrayList<>();
        for (String heroClass : heroClasses) {
            if (heroClass.equals(currentChosenDeck.getHero()) || heroClass.equals(heroClasses[0]))
                continue;
            possibleHeroes.add(heroClass);
        }

        String heroToBecome = JOptionPane.showInputDialog(
                collectionPanelMainClass.getCollectionPanel(), possibleHeroes, "Choose new hero", JOptionPane.PLAIN_MESSAGE);

        if (heroToBecome == null || heroToBecome.equals("")) return;
        heroToBecome = ClosestMatch.getClosestMatch(heroToBecome, possibleHeroes);
        int heroChangeResult = DeckFunctions.changeDeckHero(currentChosenDeck, gameState.getUser(), heroToBecome);
        if (heroChangeResult == 400) {
            logUser("Collection: deck hero change unsuccessful since the hero only has one deck.");
            JOptionPane.showMessageDialog(collectionPanelMainClass.getCollectionPanel(), "Unsuccessful. The deck's hero only has " +
                    "a single deck.");
        } else if (heroChangeResult == 200) {
            logUser("Collection: hero change successful.");
            JOptionPane.showMessageDialog(collectionPanelMainClass.getCollectionPanel(), "Successful");
            collectionPanelMainClass.showDeckCards();
            collectionPanelMainClass.refreshPage();
        }
        collectionPanelMainClass.requestFocus();
    }

    public void createNewDeckFunction() {
        if (!(collectionPanelMainClass.getCurrentChosenDeck() == null
                || collectionPanelMainClass.getCurrentChosenDeck().getDeckSize()
                == collectionPanelMainClass.getCurrentChosenDeck().getCurrentNumberOfCardsInDeck())){
            JOptionPane.showMessageDialog(collectionPanelMainClass.getCollectionPanel(),
                    "The current deck that you are modifying is not at its full capacity.\n" +
                            "First complete this deck.");
            return;
        }
        logUser("Collection: create new deck button clicked.");
        String deckName = JOptionPane.showInputDialog(
                collectionPanelMainClass.getCollectionPanel(), "Enter a name for the new deck", "New deck", JOptionPane.PLAIN_MESSAGE);
        if (deckName == null || deckName.equals("")) {
            logUser("Invalid deck name");
            return;
        }
        ArrayList<String> possibleHeroes = new ArrayList<>();
        for (String heroClass : heroClasses) {
            if (heroClass.equals(heroClasses[0]))
                continue;
            possibleHeroes.add(heroClass);
        }
        String heroToBecome = JOptionPane.showInputDialog(
                collectionPanelMainClass.getCollectionPanel(), possibleHeroes, "Choose new hero", JOptionPane.PLAIN_MESSAGE);
        if (heroToBecome == null || heroToBecome.equals("")) {
            logUser("Invalid hero name");
            return;

        }
        for (Hero hero : gameState.getUser().getHeroes()) {
            if (hero.getName().equals(heroToBecome)) {
                for (Deck deck : hero.getDecks()) {
                    if (deck.getDeckName().equals(deckName)) {
                        logUser("Error. The selected hero already has a deck with this name.");
                        JOptionPane.showMessageDialog(collectionPanelMainClass.getCollectionPanel(), "Error. The selected hero already has " +
                                "a deck with this name.");
                        return;
                    }
                }
            }
        }
        collectionPanelMainClass.setEnteredCreateNewDeckMode(true);
        heroToBecome = ClosestMatch.getClosestMatch(heroToBecome, possibleHeroes);
        collectionPanelMainClass.setCurrentChosenDeck(new Deck(deckName, heroToBecome));
        DeckFunctions.addCardToDeck(collectionPanelMainClass.getCurrentChosenDeck(), "Regenerate");
        collectionPanelMainClass.showDeckCards();
        collectionPanelMainClass.refreshPage();
        collectionPanelMainClass.getMainPanel().requestFocus();
        logUser("Successfully created new deck.");
        collectionPanelMainClass.requestFocus();
    }

    public void setCurrentDeckAsHerosMainDeckClicked() {
        Deck currentChosenDeck = collectionPanelMainClass.getCurrentChosenDeck();
        Hero hero = getHeroFromDeck(currentChosenDeck, gameState.getUser());
        hero.changeDeck(currentChosenDeck.getDeckName());
        JOptionPane.showMessageDialog(collectionPanelMainClass.getCollectionPanel(), "Successful!");
        collectionPanelMainClass.requestFocus();
    }

    public boolean backToMainPanelCommandIssued() {
        Deck currentChosenDeck = collectionPanelMainClass.getCurrentChosenDeck();
        if (currentChosenDeck == null
                || currentChosenDeck.getDeckSize() == currentChosenDeck.getCurrentNumberOfCardsInDeck()) return true;
        JOptionPane.showMessageDialog(collectionPanelMainClass.getCollectionPanel(), "You cannot go back since your current " +
                "editing deck is not at full capacity. Exiting will result in loss of unsaved data.");
        collectionPanelMainClass.requestFocus();
        return false;
    }

    public void resetButtonClicked(){
        String message = "Collection: Reset filters";
        collectionPanelMainClass.setDisplayingWhichCards("all");
        collectionPanelMainClass.resetVis();
        logUser(message);
    }
    public void dispAllclicked(){
        String message = "Collection: Display All cards";
        collectionPanelMainClass.setDisplayingWhichCards("all");
        collectionPanelMainClass.refreshPage();
        logUser(message);
    }
    public void dispAvailableClicked(){
        String message = "Collection: Display Available cards";
        collectionPanelMainClass.setDisplayingWhichCards("available");
        collectionPanelMainClass.refreshPage();
        logUser(message);
    }
    public void dispBuyableClicked(){
        String message = "Collection: Display Buyable cards";
        collectionPanelMainClass.setDisplayingWhichCards("buyable");
        collectionPanelMainClass.refreshPage();
        logUser(message);
    }
    public void handleDefaultEventHandlerCase(ActionEvent actionEvent){
        for (int i = 0; i < heroClasses.length; i++) {
            if (actionEvent.getActionCommand().equals(
                    heroClasses[i].substring(0, collectionPanelMainClass.getNumberOfCharactersOfHeroNameToDisplay()))) {
                collectionPanelMainClass.setDisplayingHeroClass(i);
                break;
            }
        }

        if (actionEvent.getActionCommand().equals("None")) {
            collectionPanelMainClass.setCurrentManaFilterButton(11);
        } else {
            for (int i = 0; i < 11; i++) {
                StringBuilder text = new StringBuilder();
                text.append(i);
                if (actionEvent.getActionCommand().equals(text.toString())) {
                    collectionPanelMainClass.setCurrentManaFilterButton(i);
                    break;
                }
            }
        }
        collectionPanelMainClass.refreshPage();
    }
}
