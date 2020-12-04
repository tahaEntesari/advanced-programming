package GUI.Panels.PlayPanel;

import Arena.ArenaActionListener;
import Arena.GameHandler;
import Card.Cards;
import Configurations.GUIConfig;
import Configurations.GameConfig;
import GUI.Panels.CardPanel.CardPanel;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static LoggingModule.LoggingClass.logUser;

public class HandDisplayPanel {
    private JPanel mainPanel;
    private ArrayList<Cards.card> cards;
    private CardPanel[] cardPanels;
    private ArenaActionListener arenaActionListener;
    private boolean owner;
    private GameHandler gameHandler;
    private int cardHeight;

    public HandDisplayPanel(boolean firstPlayer, GameHandler gameHandler, ArenaActionListener arenaActionListener) {
        owner = firstPlayer;
        this.gameHandler = gameHandler;
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setOpaque(false);
        this.arenaActionListener = arenaActionListener;
        cards = new ArrayList<>();
        cardHeight = GUIConfig.handDisplayCardHeight;

        completeCardPanels();
    }

    private void completeCardPanels() {
        cardPanels = new CardPanel[GameConfig.maxNumberOfCardsInHand];
        int xLocation = GUIConfig.handDisplayCardDifferences;
        for (int i = GameConfig.maxNumberOfCardsInHand - 1; i >= 0; i--) {
            cardPanels[i] = new CardPanel(GUIConfig.handDisplayCardWidth, cardHeight) {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {

                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                    super.mousePressed(mouseEvent);
                    if (isTurnOk() && !gameHandler.requestedForTarget()) {
                        arenaActionListener.showPossiblePlacesForCard(getCard());
                        arenaActionListener.startMovingCardPanel(this.cardName);
                    }
                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {
                    super.mouseReleased(mouseEvent);
                    if (isTurnOk() && !gameHandler.requestedForTarget()) {
                        arenaActionListener.hidePossiblePlacesForCard();
                        arenaActionListener.stopMovingCardPanel();
                        if ((mouseEvent.getY() < -10 && owner) || (!owner && mouseEvent.getY() > cardHeight)) {
                            logUser("Mouse released: place a card: " + this.cardName);
                            arenaActionListener.placeACard(getCard());
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {
                    if (isTurnOk() && !gameHandler.requestedForTarget())
                        arenaActionListener.enlargeView(this.cardName);
                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {
                    if (isTurnOk() && !gameHandler.requestedForTarget())
                        arenaActionListener.hideEnlargedPanel();
                }
            };
            cardPanels[i].setOpacity();
            cardPanels[i].setVisibility(false);
            mainPanel.add(cardPanels[i].getMainPanel());
            cardPanels[i].setLocation(xLocation * i, 0);
        }
    }

    private boolean isTurnOk() {
        return owner == gameHandler.getCurrentTurn();
    }

    public void playACards(Cards.card card) {
        cards.remove(card);
        redraw();
    }

    public ArrayList<Cards.card> getCards() {
        return cards;
    }

    public void addCardToHand(Cards.card cardObject) {
        if (cards.size() == GameConfig.maxNumberOfCardsInHand) {
            return;
        }
        cards.add(cardObject);
        redraw();
    }

    public void redraw() {
        for (int i = 0; i < cards.size(); i++) {
            Cards.card cardObject = cards.get(i);
            cardPanels[i].setCard(cardObject);
            cardPanels[i].setVisibility(true);
            cardPanels[i].refresh();
        }
        for (int i = cards.size(); i < cardPanels.length; i++) {
            cardPanels[i].setVisibility(false);
        }
        refresh();
    }

    private void refresh() {
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void reset() {
        for (int i = 0; i < GameConfig.maxNumberOfCardsInHand; i++) {
            cardPanels[i].reset();
        }
        cards.clear();
    }

    public void updateGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public void executeRound() {
        toggleCardBackShow();
    }

    public void toggleCardBackShow() {
        boolean showBack = owner != gameHandler.getCurrentTurn();
        for (CardPanel cardPanel : cardPanels) {
            cardPanel.showCardBack(showBack);
        }
    }

    public void setTooltip() {
        for (CardPanel cardPanel : cardPanels) {
            cardPanel.setTooltip();
        }
    }
}
