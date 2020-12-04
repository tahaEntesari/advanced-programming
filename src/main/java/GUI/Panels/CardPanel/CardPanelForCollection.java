package GUI.Panels.CardPanel;

import Deck.DeckFunctions;
import GUI.Panels.CollectionPanels.CollectionPanel;

import java.awt.event.MouseEvent;

import GameState.GameState;

public class CardPanelForCollection extends CardPanel {
    private CollectionPanel collectionPanel;
    private boolean cardAvailability;
    private GameState gameState;
    public CardPanelForCollection(int width, int height, CollectionPanel collectionPanel){
        super(width, height);
        this.collectionPanel = collectionPanel;
        gameState = GameState.getInstance();
    }

    public void setCardAvailability(boolean cardAvailability) {
        this.cardAvailability = cardAvailability;
        super.setAvailability(cardAvailability);
    }
    public void setCardAvailability(){
        cardAvailability = DeckFunctions.cardInSet(gameState.getUser().getAvailableCards(), cardName);
        super.setAvailability(cardAvailability);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(cardAvailability){
            collectionPanel.addToDeck(cardName);
        } else {
            collectionPanel.buyCard(cardName);
        }
    }
}
