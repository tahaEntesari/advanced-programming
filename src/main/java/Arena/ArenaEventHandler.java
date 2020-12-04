package Arena;

import Card.Cards;
import Card.Interfaces.Vulnerable;
import GUI.Panels.PlayPanel.PlayPanel;
import Hero.Powers;

import java.util.ArrayList;

public class ArenaEventHandler {
    private PlayPanel playPanel;

    public ArenaEventHandler(PlayPanel playPanel) {
        this.playPanel = playPanel;
    }

    public void showEnlargedPanel(String cardName) {
        playPanel.showEnlargedPanel(cardName);
    }

    public void playASpell(String cardName) {
        playPanel.showSpellPanel(cardName);
    }

    public void hideEnlargedPanel() {
        playPanel.hideEnlargedPanel();
    }

    public void executeRound() {
        playPanel.executeRound();
    }

    public void finishCardChange() {
        playPanel.finishCardChange();
    }

    public void escape() {
        playPanel.escape();
    }

    public void warnTimeOut() {
        playPanel.warnTimeOut();
    }

    public void addToHistory(String message) {
        playPanel.addActionToHistory(message);
    }

    public void placeACard(Cards.card card) {
        GameHandler gameHandler = playPanel.getGameHandler();
        boolean currentTurn = gameHandler.getCurrentTurn();
        Cards.card playResult = gameHandler.playACard(card, playPanel.getArenaHalfPanel(currentTurn).getCurrentAvailableMana());
        if (playResult != null) {
            playPanel.getArenaHalfPanel(currentTurn).playACard(card);
            playPanel.addActionToHistory("Played \n" + card.getName());
        }
    }


    public void summonCard(Cards.minion minion) {
        playPanel.getArenaHalfPanel(minion.getOwner()).summonCard(minion);
    }

    public void showPossiblePlacesForCard(Cards.card card) {
        playPanel.showPossiblePlacesForCard(card);
    }

    public void startMovingCardPanel(String cardName) {
        playPanel.startMovingCardPanel(cardName);
    }

    public void stopMovingCardPanel() {
        playPanel.stopMovingCardPanel();
    }

    public void hidePossiblePlacesForCard() {
        playPanel.hidePossiblePlacesForCard();
    }

    public void toggleArrowShow(Vulnerable caller) {
        playPanel.toggleArrowShow(caller);
    }

    public Thread toggleCrosshairShow(Vulnerable caller) {
        return playPanel.toggleCrosshairShow(caller);
    }

    public void addCardsToHand(boolean firstPlayer, ArrayList<Cards.card> newCards) {
        playPanel.addCardToHand(firstPlayer, newCards);
    }

    public void handleSacrificeHeroPowerChoose() {
        playPanel.handleSacrificeHeroPowerChoose();
    }

    public void finishGame() {
        playPanel.finishGame(false);
    }

    public void updateTooltips() {
        playPanel.updateTooltips();
    }

    public void updateNumberOfCardsInDeck() {
        playPanel.updateNumberOfCardsInDeck();
    }

    public void finishDiscover() {
        playPanel.finishDiscover();
    }

    public Cards.card handleDiscover(ArrayList<Cards.card> cards) {
        return playPanel.handleDiscover(cards);
    }

    public void removeACard(boolean cardOwner, Cards.minion minion) {
        playPanel.removeACard(cardOwner, minion);
    }

    public void transformMinion(boolean cardOwner, Cards.minion initialMinion, Cards.minion transformInto) {
        playPanel.transformMinion(cardOwner, initialMinion, transformInto);
    }

    public int useHeroPower() {
        GameHandler gameHandler = playPanel.getGameHandler();
        boolean currentTurn = gameHandler.getCurrentTurn();
        Powers.heroPowers heroPower = gameHandler.useHeroPower(
                playPanel.getArenaHalfPanel(currentTurn).getCurrentAvailableMana());
        int numRemaining = 0;
        switch (heroPower.getUseStatus()) {
            case "Success":
                playPanel.getArenaHalfPanel(currentTurn).useHeroPower(heroPower);
                playPanel.addActionToHistory("Used \n Hero Power");
                numRemaining = heroPower.getNumberOfUsagesLeftThisTurn();
                break;
            case "LowMana":
                numRemaining = heroPower.getNumberOfUsagesLeftThisTurn();
                break;
            case "NoneRemaining":
                break;
        }
        return numRemaining;
    }
}
