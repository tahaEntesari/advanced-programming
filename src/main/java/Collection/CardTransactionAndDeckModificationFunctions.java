package Collection;

import Deck.Deck;
import Deck.DeckFunctions;
import GUI.Panels.CollectionPanels.CollectionPanel;
import GameState.GameState;
import Shop.ShopTransactionFunctions;

import javax.swing.*;

import static LoggingModule.LoggingClass.logUser;

public class CardTransactionAndDeckModificationFunctions {
    private CollectionPanel collectionPanelMainClass;
    GameState gameState = GameState.getInstance();

    public CardTransactionAndDeckModificationFunctions(CollectionPanel collectionPanelMainClass) {
        this.collectionPanelMainClass = collectionPanelMainClass;
    }
    public void removeFromDeck(String cardName) {
        DeckFunctions.removeCardFromDeck(collectionPanelMainClass.getCurrentChosenDeck(), cardName);
        JOptionPane.showMessageDialog(collectionPanelMainClass.getDeckViewMainPanel(), "Successfully removed 1 instance.");
        collectionPanelMainClass.showDeckCards();
    }

    public void addToDeck(String cardName) {
        Deck currentChosenDeck = collectionPanelMainClass.getCurrentChosenDeck();
        JLayeredPane collectionPanel = collectionPanelMainClass.getCollectionPanel();
        if (JOptionPane.showConfirmDialog(collectionPanelMainClass.getCollectionPanel(), "Add to deck?",
                "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
            if (currentChosenDeck == null) {
                String message = "No deck is chosen to add this card to!";
                logUser(message);
                JOptionPane.showMessageDialog(collectionPanel, message);
                return;
            }
            int addResult = DeckFunctions.addCardToDeck(currentChosenDeck, cardName);
            collectionPanelMainClass.showDeckCards();
            switch (addResult) {
                case 409:
                    JOptionPane.showMessageDialog(collectionPanel, "The deck is at full capacity." +
                            " First remove a card.");
                    break;
                case 403:
                    JOptionPane.showMessageDialog(collectionPanel, "Error. There are already 2 instances " +
                            "of this card in your deck");
                    break;
                case 404:
                    JOptionPane.showMessageDialog(collectionPanel, "Error. The card hero class and the deck " +
                            "hero are not a match.");
                    break;

                case 200:
                    JOptionPane.showMessageDialog(collectionPanel, "Successful!");
                    if (collectionPanelMainClass.isEnteredCreateNewDeckMode()
                            && currentChosenDeck.getCurrentNumberOfCardsInDeck() == currentChosenDeck.getDeckSize()) {
                        collectionPanelMainClass.setEnteredCreateNewDeckMode(false);
                        DeckFunctions.getHeroFromDeck(currentChosenDeck, gameState.getUser()).addDeck(currentChosenDeck);
                        gameState.getUser().serializeUser();
                    }
                    break;
            }
        }
    }

    public void buyCard(String cardName) {
        JLayeredPane collectionPanel = collectionPanelMainClass.getCollectionPanel();
        if (JOptionPane.showConfirmDialog(collectionPanel, "You currently don't own this card. " +
                        "Do you want to buy it?", "WARNING",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
            int transactionResult = ShopTransactionFunctions.buyCard(cardName);
            String transactionResultMessage = "";
            switch (transactionResult) {
                case 402:
                    transactionResultMessage = "Error. Insufficient balance";
                    break;
                case 200:
                    transactionResultMessage = "Purchase successful. Card added to your collection.";
                    break;
            }
            JOptionPane.showMessageDialog(collectionPanel, transactionResultMessage);
            collectionPanelMainClass.refreshPage();
            collectionPanelMainClass.requestFocus();
        }

    }
}
