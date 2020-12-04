package GUI.Panels.PlayPanel;

import Arena.ArenaActionListener;
import Arena.GameHandler;
import Card.Cards;
import Card.Interfaces.Restore;
import Card.Interfaces.Vulnerable;
import Configurations.GUIConfig;
import Configurations.GameConfig;
import Arena.HeroForArena;
import Hero.Powers;

import javax.swing.*;

public class ArenaHalfPanel {
    private JPanel mainPanel;
    private int[] oddCardsLocationsX, evenCardsLocationsX;
    private int cardsLocationsY;
    private int playedCardWidth, playedCardHeight;
    private ManaPanel manaPanel;
    private MinionPanel[] oddPanels;
    private MinionPanel[] evenPanels;
    private HeroPanel heroPanel;
    private ArenaActionListener arenaActionListener;
    private HandDisplayPanel handDisplayPanel;
    private HeroPowerPanel heroPowerPanel;
    private int numberOfCardsOnTheGround = 0;
    private boolean firstPlayer;
    private boolean showHand;
    private GameHandler gameHandler;

    public ArenaHalfPanel(ArenaActionListener arenaActionListener, boolean firstPlayer, GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        this.firstPlayer = firstPlayer;
        showHand = firstPlayer;
        mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);
        this.arenaActionListener = arenaActionListener;
        completeCardLocations();
        completeCards();
        completeHeroImagePanel();
        completeManaPanel();
        completeHandDisplayPanel();
        completeHeroPowerPanel();

    }

    private void completeHeroPowerPanel() {
        heroPowerPanel = new HeroPowerPanel(firstPlayer, gameHandler, arenaActionListener);
        mainPanel.add(heroPowerPanel.getMainPanel());
        heroPowerPanel.setBounds();
    }

    private void completeHandDisplayPanel() {
        handDisplayPanel = new HandDisplayPanel(firstPlayer, gameHandler, arenaActionListener);
        mainPanel.add(handDisplayPanel.getMainPanel());
        int yLocation = 0;
        if (firstPlayer)
            yLocation = GUIConfig.playedCardHeight;
        handDisplayPanel.getMainPanel().setBounds(0, yLocation,
                GUIConfig.handDisplayCardWidth * (GameConfig.maxNumberOfCardsInHand - 1)
                        + GUIConfig.handDisplayCardWidth, GUIConfig.handDisplayCardHeight);
    }

    private void completeHeroImagePanel() {
        heroPanel = new HeroPanel(firstPlayer, arenaActionListener, gameHandler);
        mainPanel.add(heroPanel.getMainPanel());
        heroPanel.setLocation();
    }

    private void completeManaPanel() {
        manaPanel = new ManaPanel();
        mainPanel.add(manaPanel.getMainPanel());
        int manaWidth = GUIConfig.playPanelThisPlayerManaLocationWidth - 20;
        int manaHeight = GUIConfig.playPanelThisPlayerManaLocationHeight;
        int manaYLocation = GUIConfig.playPanelOpponentManaLocationY;
        if (firstPlayer) {
            manaYLocation = GUIConfig.height / 2 - GUIConfig.height / 10;
            manaWidth += 20;
        }
        manaPanel.getMainPanel().setBounds(manaWidth, manaYLocation, 600, manaHeight);

    }


    private void completeCards() {
        oddPanels = new MinionPanel[7];
        evenPanels = new MinionPanel[8];
        for (int i = 0; i < 8; i++) {
            evenPanels[i] = new MinionPanel(firstPlayer, arenaActionListener, gameHandler);
            mainPanel.add(evenPanels[i].getMainPanel());
            evenPanels[i].setLocationAndBound(evenCardsLocationsX[i], cardsLocationsY);
            evenPanels[i].setVisibility(false);
        }
        for (int i = 0; i < 7; i++) {
            oddPanels[i] = new MinionPanel(firstPlayer, arenaActionListener, gameHandler);
            mainPanel.add(oddPanels[i].getMainPanel());
            oddPanels[i].setLocationAndBound(oddCardsLocationsX[i], cardsLocationsY);
            oddPanels[i].setVisibility(false);
        }
    }

    private void completeCardLocations() {
        playedCardWidth = GUIConfig.playedCardWidth;
        playedCardHeight = GUIConfig.playedCardHeight;
        int difference = playedCardWidth / 2;
        if (firstPlayer)
            cardsLocationsY = 10;
        else
            cardsLocationsY = GUIConfig.arenaHeroImageYOpponent + GUIConfig.arenaHeroImageHeight - 20;
        oddCardsLocationsX = new int[7];
        evenCardsLocationsX = new int[8];
        oddCardsLocationsX[3] = GUIConfig.width / 2 - difference;
        for (int i = 0; i < 3; i++) {
            oddCardsLocationsX[i] = oddCardsLocationsX[3] - (3 - i) * 2 * difference;
            oddCardsLocationsX[4 + i] = oddCardsLocationsX[3] + (i + 1) * 2 * difference;
        }
        evenCardsLocationsX[2] = GUIConfig.width / 2 - 2 * difference;
        for (int i = 0; i < 2; i++) {
            evenCardsLocationsX[i] = evenCardsLocationsX[2] - (2 - i) * 2 * difference;
            evenCardsLocationsX[3 + i] = evenCardsLocationsX[2] + (i + 1) * 2 * difference;
        }
        evenCardsLocationsX[5] = evenCardsLocationsX[4] + 2 * difference;
        evenCardsLocationsX[7] = evenCardsLocationsX[5] + 2 * difference;
        evenCardsLocationsX[6] = evenCardsLocationsX[0] - 2 * difference;
        //////

    }

    public void addCardToHand(Cards.card cardObject) {
        handDisplayPanel.addCardToHand(cardObject);
    }

    public void playACard(Cards.card card) {
        handDisplayPanel.playACards(card);
        reduceMana(card.getMana());
        placeACardOnTheGround(card);
    }

    public void summonCard(Cards.minion minion) {
        if (handDisplayPanel.getCards().contains(minion))
            handDisplayPanel.playACards(minion);
        placeACardOnTheGround(minion);
    }

    public void useHeroPower(Powers.heroPowers heroPower) {
        reduceMana(heroPower.getMana());
    }

//    private void returnCardToHand(Cards.card card){
//        if (numberOfCardsOnTheGround % 2 == 0){
//
//        } else {
//
//        }
//    }

    public void removeACard(Cards.minion minion) {
        int panelIndex = 0;
        if (numberOfCardsOnTheGround % 2 == 0) {
            for (MinionPanel evenPanel : evenPanels) {
                if (evenPanel.getCard() == minion)
                    break;
                panelIndex++;
            }
        } else {
            for (MinionPanel oddPanel : oddPanels) {
                if (oddPanel.getCard() == minion)
                    break;
                panelIndex++;
            }
        }
        removeCardFromGround(panelIndex);
    }

    private void removeCardFromGround(int panelIndex) {
        if (numberOfCardsOnTheGround % 2 == 0) {
            for (MinionPanel evenPanel : evenPanels) {
                evenPanel.setVisibility(false);
                evenPanel.resetImage();
            }
            for (int i = 3 - numberOfCardsOnTheGround / 2; i < 3 + numberOfCardsOnTheGround / 2; i++) {
                if (i < panelIndex) {
                    oddPanels[i + 1].setCard(evenPanels[i].getCard());

                    oddPanels[i + 1].setVisibility(true);
                } else if (i == panelIndex) {
                    continue;
                } else {
                    oddPanels[i].setCard(evenPanels[i].getCard());
                    oddPanels[i].setVisibility(true);
                }
            }
        } else {
            for (MinionPanel oddPanel : oddPanels) {
                oddPanel.setVisibility(false);
                oddPanel.resetImage();
            }
            for (int i = 3 - numberOfCardsOnTheGround / 2; i < 3 + numberOfCardsOnTheGround / 2; i++) {
                if (i < panelIndex) {
                    evenPanels[i].setCard(oddPanels[i].getCard());
                } else {
                    evenPanels[i].setCard(oddPanels[i + 1].getCard());
                }
                evenPanels[i].setVisibility(true);
            }

        }
        numberOfCardsOnTheGround--;
    }

    private void placeACardOnTheGround(Cards.card card) {

        if (card.getType().equals("Minion")) {
            int indexPlacedIn = 7;
            if (numberOfCardsOnTheGround % 2 == 0) {
                for (int i = 0; i < 8; i++) {
                    evenPanels[i].setVisibility(false);
                    if (evenPanels[i].getEnteredNotExitedAndReset()) {
                        indexPlacedIn = i;
                    }
                }
                if (numberOfCardsOnTheGround == 0) {
                    oddPanels[3].setCard((Cards.minion) card);
                    oddPanels[3].setVisibility(true);
                } else {
                    if (indexPlacedIn == 6) indexPlacedIn = -1;
                    boolean placedTheNewCard = false;
                    int whereToStart = 3 - numberOfCardsOnTheGround / 2;
                    int count = whereToStart;
                    for (int i = whereToStart; i < whereToStart + numberOfCardsOnTheGround; i++) {
                        if (i - 1 == indexPlacedIn) {
                            oddPanels[count].setCard((Cards.minion) card);
                            oddPanels[count].setVisibility(true);
                            count++;
                            placedTheNewCard = true;
                        }
                        oddPanels[count].setCard(evenPanels[i].getCard());
                        oddPanels[count].setVisibility(true);
                        count++;
                    }
                    if (!placedTheNewCard) {
                        oddPanels[whereToStart + numberOfCardsOnTheGround].setCard((Cards.minion) card);
                        oddPanels[whereToStart + numberOfCardsOnTheGround].setVisibility(true);
                    }
                }
            } else {

                for (int i = 0; i < 7; i++) {
                    oddPanels[i].setVisibility(false);
                    if (oddPanels[i].getEnteredNotExitedAndReset()) {
                        indexPlacedIn = i;
                    }
                }
                boolean placedTheNewCard = false;
                int whereToStart = 2 - numberOfCardsOnTheGround / 2;
                int count = whereToStart;
                for (int i = whereToStart; i < whereToStart + numberOfCardsOnTheGround; i++) {
                    if (i == indexPlacedIn) {
                        evenPanels[count].setCard((Cards.minion) card);
                        evenPanels[count].setVisibility(true);
                        count++;
                        placedTheNewCard = true;
                    }
                    evenPanels[count].setCard(oddPanels[i + 1].getCard());
                    evenPanels[count].setVisibility(true);
                    count++;
                }
                if (!placedTheNewCard) {
                    evenPanels[whereToStart + numberOfCardsOnTheGround].setCard((Cards.minion) card);
                    evenPanels[whereToStart + numberOfCardsOnTheGround].setVisibility(true);
                }
            }
            numberOfCardsOnTheGround++;
        } else {
            arenaActionListener.playASpell(card.getName());
        }
    }

    public void showPossibleCardLocations(Cards.card card) {
        if (card.getType().equals("Minion")) {
            if (numberOfCardsOnTheGround == 0) {
                oddPanels[3].highlightBorder();
            } else if (numberOfCardsOnTheGround == 6) {
                for (MinionPanel evenPanel : evenPanels) {
                    evenPanel.highlightBorder();
                }
            } else {
                int whereToStart = 2 - numberOfCardsOnTheGround / 2;
                ;
                if (numberOfCardsOnTheGround % 2 == 0) {
                    for (int i = whereToStart; i < 6 - whereToStart; i++) {
                        evenPanels[i].highlightBorder();
                    }
                } else {
                    for (int i = whereToStart; i < 7 - whereToStart; i++) {
                        oddPanels[i].highlightBorder();
                    }
                }
            }
        }
    }

    public void hidePossibleCardLocations() {
        for (MinionPanel evenPanel : evenPanels) {
            evenPanel.removeBorder();
        }
        for (MinionPanel oddPanel : oddPanels) {
            oddPanel.removeBorder();
        }
    }

    public int getCurrentAvailableMana() {
        return manaPanel.getCurrentMana();
    }

    public void setHero(HeroForArena hero) {
        heroPanel.setHero(hero);
        if (hero == null)
            setHeroPower(null);
        else
            setHeroPower(hero.getHeroPower());
    }

    private void setHeroPower(Powers.heroPowers heroPower) {
        heroPowerPanel.setHeroPower(heroPower);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void reduceMana(int number) {
        manaPanel.setCurrentMana(manaPanel.getCurrentMana() - number);
    }

    public void executeRound() {
        manaPanel.executeRound();
        heroPowerPanel.refresh();
    }

    public void executeHandDisplayRound() {
        handDisplayPanel.executeRound();
    }

    public void manaJumpInfoPassiveInitialize(boolean manaJumpPassive) {
        if (manaJumpPassive)
            manaPanel.executeRound();
    }

    public void reset() {
        manaPanel.reset();
        handDisplayPanel.reset();
        numberOfCardsOnTheGround = 0;
        for (int i = 0; i < 6; i++) {
            oddPanels[i].setVisibility(false);
            evenPanels[i].setVisibility(false);
            oddPanels[i].resetImage();
            evenPanels[i].resetImage();
        }
        oddPanels[6].setVisibility(false);
        oddPanels[6].resetImage();

    }

    public void updateGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        heroPowerPanel.updateGameHandler(gameHandler);
        handDisplayPanel.updateGameHandler(gameHandler);
        heroPanel.updateGameHandler(gameHandler);
        for (MinionPanel evenPanel : evenPanels) {
            evenPanel.updateGameHandler(gameHandler);
        }
        for (MinionPanel oddPanel : oddPanels) {
            oddPanel.updateGameHandler(gameHandler);
        }
    }

    public void updateTooltips() {
        for (MinionPanel oddPanel : oddPanels) {
            oddPanel.setToolTip();
        }
        for (MinionPanel evenPanel : evenPanels) {
            evenPanel.setToolTip();
        }
        heroPanel.updateTooltip();
        handDisplayPanel.setTooltip();
        heroPowerPanel.setToolTip();
    }

    public void toggleCardBackShow() {
        handDisplayPanel.toggleCardBackShow();
    }

    public void transformMinion(Cards.minion initialMinion, Cards.minion transformInto){
        if (numberOfCardsOnTheGround % 2 == 0){
            for (MinionPanel evenPanel : evenPanels) {
                if (evenPanel.getCard() == initialMinion) {
                    evenPanel.setCard(transformInto);
                }
            }
        } else {
            for (MinionPanel oddPanel : oddPanels) {
                if (oddPanel.getCard() == initialMinion) {
                    oddPanel.setCard(transformInto);
                }
            }
        }

    }
}
