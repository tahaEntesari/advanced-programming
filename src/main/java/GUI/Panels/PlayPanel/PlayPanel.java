package GUI.Panels.PlayPanel;

import Arena.ArenaActionListener;
import Arena.ArenaEventHandler;
import Arena.GameHandler;
import Arena.HeroForArena;
import Audio.SimpleAudioPlayer;
import Card.Cards;
import Card.Interfaces.Vulnerable;
import Configurations.GUIConfig;
import Configurations.GameConfig;
import GUI.ActionHandler;
import GUI.Arrow.ArrowDrawer;
import GUI.Crosshair.CrosshairDrawer;
import GUI.Panels.MyPanel;
import GUI.Panels.PlayPanel.SelectionPanels.ChooseBetweenTwoChoices;
import GUI.Panels.PlayPanel.SelectionPanels.DiscoverPanel;
import GUI.Panels.PlayPanel.SelectionPanels.InitialCardChangePanel;
import GUI.Panels.PlayPanel.MovingPanel.MovingCardHandler;
import GUI.UtilityFunctions.GetImageLocation;
import Utility.Timers.TimerInfinite;
import Utility.Timers.TimerThreadForCardDisappearance;
import Utility.Timers.TimerThreadForTurnEnd;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class PlayPanel {
    private ActionHandler mainFrameActionHandler;
    private JLayeredPane arenaPanel;
    private MyPanel myPanel;
    private HistoryPanel historyPanel;
    private ArenaHalfPanel arenaHalfPanel;
    private ArenaHalfPanel arenaHalfPanelOpponent;
    private ArenaEventHandler arenaEventHandler;
    private ArenaActionListener arenaActionListener;
    private GameHandler gameHandler;
    private JPanel enlargedHandViewPanel;
    private Image enlargedHandViewImage;
    private JPanel spellPlayPanel;
    private Image spellPlayImage;
    private TimerThreadForTurnEnd timerThreadForTurnEnd;
    private SimpleAudioPlayer audioPlayer;
    private ArrowDrawer arrowDrawer;
    private CrosshairDrawer crosshairDrawer;
    private MovingCardHandler movingCardHandler;
    private InitialCardChangePanel initialCardChangePanel;
    private TimerInfinite timerInfinite = new TimerInfinite();
    private EndTurnPanel endTurnPanel;
    private ChooseBetweenTwoChoices sacrificeChoosePanel;
    private boolean[] changedFirstCards = {false, false};
    private DiscoverPanel discoverPanel;

    public PlayPanel(ActionHandler actionHandler, String imageFileLocation) {
        mainFrameActionHandler = actionHandler;
        myPanel = new MyPanel(actionHandler, 2, imageFileLocation);
        arenaPanel = myPanel.getCenterPanel();


        arenaEventHandler = new ArenaEventHandler(this);
        arenaActionListener = new ArenaActionListener(arenaEventHandler);
        arenaPanel.addKeyListener(arenaActionListener);

        historyPanel = new HistoryPanel();

        arenaHalfPanel = new ArenaHalfPanel(arenaActionListener, true, gameHandler);
        arenaHalfPanelOpponent = new ArenaHalfPanel(arenaActionListener, false, gameHandler);

        JPanel cardPlacementPanel = new JPanel();
        cardPlacementPanel.setOpaque(false);
        cardPlacementPanel.setLayout(new GridLayout(2, 1));
        cardPlacementPanel.add(arenaHalfPanelOpponent.getMainPanel());
        cardPlacementPanel.add(arenaHalfPanel.getMainPanel());


        JPanel defaultLayerPanel = new JPanel();
        defaultLayerPanel.setLayout(new BorderLayout());
        defaultLayerPanel.add(cardPlacementPanel, BorderLayout.CENTER);
        defaultLayerPanel.add(historyPanel.getDummyPanelBeforeEnlarging(), BorderLayout.WEST);
        defaultLayerPanel.setVisible(true);
        defaultLayerPanel.setOpaque(false);

        arenaPanel.add(defaultLayerPanel, JLayeredPane.DEFAULT_LAYER);
        defaultLayerPanel.setBounds(0, 0, GUIConfig.width, GUIConfig.height - 30);

        completeEnlargedHandView();
        completeEnlargedHistoryView();

        completeSpellPlayView();

        completeTimer();
        audioPlayer = SimpleAudioPlayer.getInstance();
        completeArrowDrawer();
        completeCrosshairDrawer();
        completeMovingCard();
        completeInitialCardChangePanel();
        completeEndTurnPanel();
        completeDiscoverPanel();
        completeBurgleChoosePanel();

    }

    private void completeBurgleChoosePanel(){
        sacrificeChoosePanel = new ChooseBetweenTwoChoices();
        arenaPanel.add(sacrificeChoosePanel.getMainPanel(), JLayeredPane.POPUP_LAYER);
        sacrificeChoosePanel.getMainPanel().setBounds(new Rectangle(0, 0, GUIConfig.width, GUIConfig.height));
    }

    private void completeDiscoverPanel() {
        discoverPanel = new DiscoverPanel(arenaActionListener);
        arenaPanel.add(discoverPanel.getMainPanel(), JLayeredPane.POPUP_LAYER);
        discoverPanel.getMainPanel().setBounds(new Rectangle(0, 0, GUIConfig.width, GUIConfig.height));
    }

    private void completeEndTurnPanel() {
        endTurnPanel = new EndTurnPanel(arenaActionListener);
        arenaPanel.add(endTurnPanel.getMainPanel(), JLayeredPane.DEFAULT_LAYER);
        endTurnPanel.setBounds(GUIConfig.endTurnPanelXLocation, GUIConfig.endTurnPanelYLocation,
                GUIConfig.endTurnPanelWidth, GUIConfig.endTurnPanelHeight);
    }

    private void completeInitialCardChangePanel() {
        initialCardChangePanel = new InitialCardChangePanel(arenaActionListener);
        arenaPanel.add(initialCardChangePanel.getMainPanel(), JLayeredPane.POPUP_LAYER);
        initialCardChangePanel.getMainPanel().setBounds(new Rectangle(0, 0, GUIConfig.width, GUIConfig.height));
    }

    private void completeMovingCard() {
        movingCardHandler = new MovingCardHandler();
        arenaPanel.add(movingCardHandler.getPanel(), JLayeredPane.POPUP_LAYER);
        movingCardHandler.getPanel().setBounds(new Rectangle(0, 0, GUIConfig.width, GUIConfig.height));
    }

    private void completeCrosshairDrawer() {
        crosshairDrawer = new CrosshairDrawer();
        arenaPanel.add(crosshairDrawer.getPanel(), JLayeredPane.POPUP_LAYER);
        crosshairDrawer.getPanel().setBounds(new Rectangle(0, 0, GUIConfig.width, GUIConfig.height));
    }

    private void completeArrowDrawer() {
        arrowDrawer = new ArrowDrawer();
        arenaPanel.add(arrowDrawer.getPanel(), JLayeredPane.POPUP_LAYER);
        arrowDrawer.getPanel().setBounds(new Rectangle(0, 0, GUIConfig.width, GUIConfig.height));

    }

    private void completeEnlargedHistoryView() {
        arenaPanel.add(historyPanel.getMainPanel(), JLayeredPane.POPUP_LAYER);
        historyPanel.setBounds();
    }

    private void completeEnlargedHandView() {
        enlargedHandViewPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(enlargedHandViewImage, 0, 0, null);
            }
        };
        enlargedHandViewPanel.setOpaque(false);
        enlargedHandViewPanel.setVisible(false);
        arenaPanel.add(enlargedHandViewPanel, JLayeredPane.POPUP_LAYER);
        enlargedHandViewPanel.setBounds(GUIConfig.enlargedHandViewXlocation, GUIConfig.enlargedHandViewYLocation,
                GUIConfig.enlargedHandViewWidth, GUIConfig.enlargedHandViewHeight);

    }

    private void completeSpellPlayView() {
        spellPlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(spellPlayImage, 0, 0, null);
            }
        };
        spellPlayPanel.setOpaque(false);
        spellPlayPanel.setVisible(false);
        arenaPanel.add(spellPlayPanel, JLayeredPane.POPUP_LAYER);
        spellPlayPanel.setBounds(GUIConfig.spellViewXlocation, GUIConfig.spellViewYLocation,
                GUIConfig.spellViewWidth, GUIConfig.spellViewHeight);
    }

    private void completeTimer() {
        timerThreadForTurnEnd = new TimerThreadForTurnEnd(arenaEventHandler);
        arenaPanel.add(timerThreadForTurnEnd.getTimerPanel(), JLayeredPane.DEFAULT_LAYER);
        timerThreadForTurnEnd.getTimerPanel().setBounds(20, GUIConfig.spellViewYLocation, 20, 150);
    }

    public void resetVis(String firstContestantHero, String firstContestantPassive,
                         String secondContestantHero, String secondContestantPassive) {
        reset(firstContestantHero, firstContestantPassive, secondContestantHero, secondContestantPassive);
        handleInitialCardChange();
        arenaHalfPanel.setHero(gameHandler.getHeroForArena(true));
        arenaHalfPanelOpponent.setHero(gameHandler.getHeroForArena(false));
        endTurnPanel.updateNumberOfCardsInDeckLabel(gameHandler.getNumberOfCardsRemainingInDeck(true),
                gameHandler.getNumberOfCardsRemainingInDeck(false));

        arenaHalfPanel.manaJumpInfoPassiveInitialize(gameHandler.isManaJumpPassive(true));
        arenaHalfPanelOpponent.manaJumpInfoPassiveInitialize(gameHandler.isManaJumpPassive(false));
        timerThreadForTurnEnd.resetThreadSettings();
        Thread timerThread = new Thread(timerThreadForTurnEnd);
        timerThread.start();
        arenaPanel.requestFocus();

        if (gameHandler.getCurrentTurn()) {
            arenaHalfPanelOpponent.toggleCardBackShow();
        } else {
            arenaHalfPanel.toggleCardBackShow();
        }

    }

    private void reset(String firstHero, String firstPassive, String secondHero, String secondPassive) {
        arenaHalfPanel.reset();
        arenaHalfPanelOpponent.reset();
        historyPanel.reset();
        gameHandler = new GameHandler(firstHero, firstPassive, secondHero, secondPassive, arenaEventHandler);
        updateGameHandlers();
        arenaHalfPanel.setHero(null);
        arenaHalfPanelOpponent.setHero(null);
        endTurnPanel.updateNumberOfCardsInDeckLabel(0, 0);
        changedFirstCards[0] = false;
        changedFirstCards[1] = false;
        arenaPanel.repaint();
        arenaPanel.revalidate();
    }

    private void updateGameHandlers() {
        arenaHalfPanel.updateGameHandler(gameHandler);
        arenaHalfPanelOpponent.updateGameHandler(gameHandler);

    }

    public boolean backCommandIssued() {
        if (gameHandler.isGameFinished())
            return true;
        boolean allowedToGoBack;
        if (JOptionPane.showConfirmDialog(arenaPanel, "Are you sure? Going back will result in your opponent" +
                " winning this game", "Go Back?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            allowedToGoBack = true;
            System.out.println("Yeeeeellllllllllllllllllllllllllllo. I removed the reset from " +
                    "backcommandissued and if any strange thing is happening, it is probably due to this change.");
            //            reset();
            finishGame(true);
        } else {
            allowedToGoBack = false;
        }
        return allowedToGoBack;
    }

    public void addCardToHand(boolean firstPlayer, ArrayList<Cards.card> cardObjects) {
        for (Cards.card cardObject : cardObjects) {
            addCardToHand(firstPlayer, cardObject);
        }
    }

    public void addCardToHand(boolean firstPlayer, Cards.card cardObject) {
        if (firstPlayer)
            arenaHalfPanel.addCardToHand(cardObject);
        else
            arenaHalfPanelOpponent.addCardToHand(cardObject);
    }

    public void executeRound() {
        if (gameHandler.getCurrentTurn())
            arenaHalfPanel.executeRound();
        else
            arenaHalfPanelOpponent.executeRound();
        gameHandler.executeRound();
        arenaHalfPanel.executeHandDisplayRound();
        arenaHalfPanelOpponent.executeHandDisplayRound();
        addCardToHand(true, gameHandler.getPlayerNewCards(true));
        addCardToHand(false, gameHandler.getPlayerNewCards(false));
        endTurnPanel.updateNumberOfCardsInDeckLabel(gameHandler.getNumberOfCardsRemainingInDeck(true),
                gameHandler.getNumberOfCardsRemainingInDeck(false));
        audioPlayer.stopSound(SimpleAudioPlayer.Sounds.timerTickToc);
        timerThreadForTurnEnd.resetTimer();
        hideEnlargedPanel();
        escape();
        arenaPanel.requestFocus();
    }

    public void escape() {
        if (arrowDrawer.isActive()) {
            arrowDrawer.setFinished(true);
            gameHandler.setRequestedTargetDoubleWay(false);
        }
        if (crosshairDrawer.isActive()) {
            crosshairDrawer.setFinished(true);
            gameHandler.setRequestedForTarget(false);
            gameHandler.getCrosshairTargetAndReset();
            gameHandler.setCanChooseHero(true);
        }
    }

    public void finishGame(boolean backExitButtonPressed) {
        timerThreadForTurnEnd.endTimer();
        escape();
        gameHandler.finishGame(backExitButtonPressed);
        if (!backExitButtonPressed)
            mainFrameActionHandler.setActionEvent(new ActionEvent(this, 0, "GameFinished"));
        gameHandler.getUser().serializeUser();

    }

    public void warnTimeOut() {
        audioPlayer.playSound(SimpleAudioPlayer.Sounds.timerTickToc);
    }

    public void updateTooltips() {
        arenaHalfPanel.updateTooltips();
        arenaHalfPanelOpponent.updateTooltips();
    }

    public void updateNumberOfCardsInDeck() {
        endTurnPanel.updateNumberOfCardsInDeckLabel(gameHandler.getNumberOfCardsRemainingInDeck(true),
                gameHandler.getNumberOfCardsRemainingInDeck(false));
    }

    public void showEnlargedPanel(String cardName) {
        String imageFileLocation = GetImageLocation.getCardImageLocation(cardName);
        try {
            enlargedHandViewImage = ImageIO.read(new File(imageFileLocation)).getScaledInstance(
                    GUIConfig.enlargedHandViewWidth, GUIConfig.enlargedHandViewHeight, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        enlargedHandViewPanel.setVisible(true);
    }

    public void showSpellPanel(String cardName) {
        String imageFileLocation = GetImageLocation.getCardImageLocation(cardName);
        try {
            spellPlayImage = ImageIO.read(new File(imageFileLocation)).getScaledInstance(
                    GUIConfig.spellViewWidth, GUIConfig.spellViewHeight, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        spellPlayPanel.setVisible(true);
        TimerThreadForCardDisappearance waitThread =
                new TimerThreadForCardDisappearance(GameConfig.timeToWaitCardDisappear, this);
        Thread thread = new Thread(waitThread);
        thread.start();
    }

    public void hideSpellPanel() {
        spellPlayPanel.setVisible(false);
    }

    public void showPossiblePlacesForCard(Cards.card card) {
        if (gameHandler.getCurrentTurn())
            arenaHalfPanel.showPossibleCardLocations(card);
        else
            arenaHalfPanelOpponent.showPossibleCardLocations(card);
    }

    public void startMovingCardPanel(String cardName) {
        movingCardHandler.setCard(cardName);
        Thread thread = new Thread(movingCardHandler);
        thread.start();
    }

    public void stopMovingCardPanel() {
        movingCardHandler.setFinished();
    }

    public void hidePossiblePlacesForCard() {
        arenaHalfPanel.hidePossibleCardLocations();
        arenaHalfPanelOpponent.hidePossibleCardLocations();
    }

    private boolean canActivateArrow(Vulnerable caller) {
        if (caller instanceof HeroForArena) {
            if (((HeroForArena) caller).getOwner() == gameHandler.getCurrentTurn()
                    && !gameHandler.requestedForTarget()
                    && !gameHandler.requestedTargetDoubleWay()
                    && ((HeroForArena) caller).weaponIsEquipped()) return true;
            else if (((HeroForArena) caller).getOwner() != gameHandler.getCurrentTurn()
                    && gameHandler.requestedTargetDoubleWay()) return true;
        } else if (caller instanceof Cards.minion) {
            if (gameHandler.getCurrentTurn() == ((Cards.minion) caller).getOwner()
                    && !gameHandler.requestedTargetDoubleWay()
                    && ((Cards.minion) caller).canAttackThisTurn()) return true;
            else if (gameHandler.getCurrentTurn() != ((Cards.minion) caller).getOwner()
                    && gameHandler.requestedTargetDoubleWay()
                    && ((Cards.minion) caller).canBeAttacked()) return true;
        }
        return false;
    }

    public Thread toggleArrowShow(Vulnerable caller) {
        Thread thread = null;
        if (!arrowDrawer.isActive()) {
            // can activate only if the minion or hero is able to attack
            if (!canActivateArrow(caller)) return null;
            gameHandler.setRequestedTargetDoubleWay(true);
            gameHandler.setPreviousCaller(caller);
            thread = new Thread(arrowDrawer);
            thread.start();
            try {
                while (!arrowDrawer.isActive())
                    Thread.sleep(50); // I think this sleep will make sure that the arrow has started
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Vulnerable previousCaller = gameHandler.getPreviousCaller();
            boolean secondCardOwner = true;
            if (caller instanceof HeroForArena) {
                if (gameHandler.getCurrentTurn() == ((HeroForArena) caller).getOwner()) return null;
                if (previousCaller instanceof Cards.minion && !((Cards.minion) previousCaller).canAttackHero())
                    return null;
                secondCardOwner = ((HeroForArena) caller).getOwner();
            } else if (caller instanceof Cards.minion) {
                if (gameHandler.getCurrentTurn() == ((Cards.minion) caller).getOwner()) return null;
                secondCardOwner = ((Cards.minion) caller).getOwner();
            }
            if (gameHandler.hasTaunt(secondCardOwner)) {
                if (caller instanceof HeroForArena) return null;
                else if (caller instanceof Cards.minion) {
                    if (!((Cards.minion) caller).hasTaunt()) return null;
                }
            }

            gameHandler.battle(previousCaller, caller);
            deactivatePawnAttack();
            gameHandler.setRequestedTargetDoubleWay(false);
            arrowDrawer.setFinished(true);
        }
        return thread;
    }

    private void deactivatePawnAttack() {
        Vulnerable pawn = gameHandler.getPreviousCaller();
        if (pawn instanceof HeroForArena) {
            ((HeroForArena) pawn).useWeapon();
        } else if (pawn instanceof Cards.minion) {
            ((Cards.minion) pawn).setCanAttackThisTurn(false);
        }
        gameHandler.setPreviousCaller(null);
    }

    private boolean canActivateCrosshair(Vulnerable pawn) {
        return pawn == null;
    }

    public Thread toggleCrosshairShow(Vulnerable pawn) {
        Thread thread = null;
        if (!crosshairDrawer.isActive()) {
            if (!canActivateCrosshair(pawn)) return null;
            gameHandler.setCrosshairTarget(null);
            thread = new Thread(crosshairDrawer);
            thread.start();
            try {
                while (!crosshairDrawer.isActive())
                    Thread.sleep(50); // I think this sleep will make sure that the arrow has started
                return thread;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            if (!checkPawnValidityForCrosshair(pawn)) return null;
            gameHandler.setCrosshairTarget(pawn);
            gameHandler.setRequestedForTarget(false);
            crosshairDrawer.setFinished(true);
        }
        return thread;
    }

    private boolean checkPawnValidityForCrosshair(Vulnerable pawn) {
        boolean condition = false;
        if (pawn instanceof HeroForArena && !gameHandler.canChooseHero()) return false;

        if (pawn instanceof HeroForArena)
            condition = ((HeroForArena) pawn).getOwner() == gameHandler.getCurrentTurn();
        else if (pawn instanceof Cards.minion)
            condition = ((Cards.minion) pawn).getOwner() == gameHandler.getCurrentTurn();
        if (!gameHandler.requestFriendlyTarget()) {
            condition = !condition;
        }
        return condition;
    }

    public void finishDiscover() {
        timerInfinite.setFinished();
    }

    public void handleSacrificeHeroPowerChoose(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                sacrificeChoosePanel.setVisibility(true);
                while(!sacrificeChoosePanel.isFinished()) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                boolean burgleChooseResult = sacrificeChoosePanel.getSelectedButtonAndReset();
                if (burgleChooseResult) {
                    gameHandler.handleSacrificeCase1();
                } else {
                    gameHandler.handleSacrificeCase2();
                }
            }
        });
        thread.start();

    }

    public Cards.card handleDiscover(ArrayList<Cards.card> cardsToSelectFrom) {
        Thread thread = new Thread(timerInfinite);
        discoverPanel.setPlayerAndCards(gameHandler.getCurrentTurn(), cardsToSelectFrom);
        thread.start();
        try {
            thread.join(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (thread.isAlive())
            timerInfinite.setFinished();
        Cards.card selectedCard = discoverPanel.getSelected();
        discoverPanel.reset();
        if (selectedCard == null) selectedCard = cardsToSelectFrom.get(0);
        return selectedCard;
    }

    public void transformMinion(boolean cardOwner, Cards.minion initialMinion, Cards.minion transformInto){
        if (cardOwner) {
            arenaHalfPanel.transformMinion(initialMinion, transformInto);
        } else {
            arenaHalfPanelOpponent.transformMinion(initialMinion, transformInto);
        }
        arenaPanel.repaint();
        arenaPanel.revalidate();
    }

    private void handleInitialCardChange() {
        initialCardChangePanel.setPlayerAndCards(true, gameHandler.getHand(true));
        Thread thread = new Thread(timerInfinite);
        thread.start();
        try {
            thread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initialCardChangePanel.reset();

        initialCardChangePanel.setPlayerAndCards(false, gameHandler.getHand(false));
        Thread thread2 = new Thread(timerInfinite);
        thread2.start();
        try {
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initialCardChangePanel.reset();
    }

    public void finishCardChange() {
        boolean firstPlayer = initialCardChangePanel.isFirstPlayer();
        int playerIndex = firstPlayer ? 0 : 1;
        if (!changedFirstCards[playerIndex]) {
            ArrayList<Integer> selectedToReturnToDeck = initialCardChangePanel.getThoseSelectedForChange();
            int numberOfCardsToAdd = selectedToReturnToDeck.size();
            if (numberOfCardsToAdd != 0) {
                String[] cardNames = new String[numberOfCardsToAdd];
                int count = 0;
                // We MUST first read the names of the cards into a array and then delete any, else there can be indexing errors.
                for (Integer integer : selectedToReturnToDeck) {
                    cardNames[count] = gameHandler.getHand(firstPlayer).get(integer).getName();
                    count++;
                }
                for (String cardName : cardNames) {
                    gameHandler.returnCardFromHandToDeck(firstPlayer, cardName);
                }
                gameHandler.addACardToPlayersHand(firstPlayer, numberOfCardsToAdd);
            }

            addCardToHand(firstPlayer, gameHandler.getHand(firstPlayer));
            timerInfinite.setFinished();
            changedFirstCards[playerIndex] = true;
        }
    }

    public void removeACard(boolean cardOwner, Cards.minion minion) {
        if (cardOwner) arenaHalfPanel.removeACard(minion);
        else arenaHalfPanelOpponent.removeACard(minion);
    }

    public void addActionToHistory(String action) {
        if (gameHandler.getCurrentTurn())
            historyPanel.addNewActionToHistory("first:" + action);
        else
            historyPanel.addNewActionToHistory("second:" + action);
    }

    public void hideEnlargedPanel() {
        enlargedHandViewPanel.setVisible(false);
    }

    public JPanel getMainPanel() {
        return myPanel.getMainPanel();
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public ArenaHalfPanel getArenaHalfPanel(boolean firstPlayer) {
        if (firstPlayer) return arenaHalfPanel;
        else return arenaHalfPanelOpponent;
    }

    public void changeImage(String imageFileLocation) {
        myPanel.changeImage(imageFileLocation);
    }
}
