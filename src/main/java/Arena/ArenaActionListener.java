package Arena;

import Card.Cards;
import Card.Interfaces.Vulnerable;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static LoggingModule.LoggingClass.logUser;

public class ArenaActionListener implements java.awt.event.ActionListener, KeyListener {
    private ArenaEventHandler arenaEventHandler;


    public ArenaActionListener(ArenaEventHandler arenaEventHandler) {
        this.arenaEventHandler = arenaEventHandler;
    }

    public void enlargeView(String cardName) {
        arenaEventHandler.showEnlargedPanel(cardName);
    }

    public void playASpell(String cardName) {
        arenaEventHandler.playASpell(cardName);
    }

    public void hideEnlargedPanel() {
        arenaEventHandler.hideEnlargedPanel();
    }

    public void placeACard(Cards.card card) {
        arenaEventHandler.placeACard(card);
    }

    public void showPossiblePlacesForCard(Cards.card card) {
        arenaEventHandler.showPossiblePlacesForCard(card);
    }

    public void startMovingCardPanel(String cardName) {
        arenaEventHandler.startMovingCardPanel(cardName);
    }

    public void stopMovingCardPanel() {
        arenaEventHandler.stopMovingCardPanel();
    }

    public void hidePossiblePlacesForCard() {
        arenaEventHandler.hidePossiblePlacesForCard();
    }

    public int useHeroPower() {
        return arenaEventHandler.useHeroPower();
    }

    public void toggleArrowShow(Vulnerable caller) {
        arenaEventHandler.toggleArrowShow(caller);
    }

    public Thread toggleCrosshairShow(Vulnerable caller) {
        return arenaEventHandler.toggleCrosshairShow(caller);
    }

    public void finishDiscover() {
        arenaEventHandler.finishDiscover();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        logUser("Arena: " + actionEvent.getActionCommand());
        switch (actionEvent.getActionCommand()) {
            case "End Turn":
                arenaEventHandler.executeRound();
                break;
            case "Finish Card Change":
                arenaEventHandler.finishCardChange();
            case "sth":

                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        System.out.print("pressed ");
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.out.println("escape");
                arenaEventHandler.escape();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
