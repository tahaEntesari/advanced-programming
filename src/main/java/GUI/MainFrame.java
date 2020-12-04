package GUI;

import Configurations.GUIConfig;
import GUI.Panels.*;
import GUI.Panels.CollectionPanels.CollectionPanel;
import GUI.Panels.PlayPanel.PlayPanel;
import GUI.Panels.PlayPanel.PlayPanelPopUp;
import GUI.Panels.ShopPanels.ShopPanel;
import GUI.Panels.StatusPanels.StatusPanel;
import GameState.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.TimerTask;

public class MainFrame implements Runnable{
    private int xDim;
    private int yDim;
    private int hGap = 0;
    private int vGap = 0;
    private JFrame frame;
    private CardLayout cardLayout;
    private ActionHandler actionHandler;
    private PlayPanel playPanel;
    private ShopPanel shopPanel;
    private StatusPanel statusPanel;
    private CollectionPanel collectionPanel;
    private SettingsPanel settingsPanel;
    private GameLogInPanel gameLogInPanel;
    private GameMainPanel gameMainPanel;
    private String previousLocation = "LogIn";

    MainFrame(int xDim, int yDim) {
        frame = new JFrame();

        this.xDim = xDim;
        this.yDim = yDim;
        cardLayout = new CardLayout(hGap, vGap);
        frame.setLayout(cardLayout);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setSize(xDim, yDim);


        actionHandler = new ActionHandler(this);
        gameLogInPanel = new GameLogInPanel(xDim, yDim);
        gameMainPanel = new GameMainPanel(actionHandler, "./assets/gameEntryBox.jpg");
        playPanel = new PlayPanel(actionHandler, "./assets/arena1.jpg");
        shopPanel = new ShopPanel(actionHandler, "");
        statusPanel = new StatusPanel(actionHandler, "");
        collectionPanel = new CollectionPanel(actionHandler, "");
        settingsPanel = new SettingsPanel(actionHandler, "");
        frame.add(GUIConfig.statusPanel, statusPanel.getMainPanel());
        frame.add(GUIConfig.logInPanel, gameLogInPanel.getMainPanel());
        frame.add(GUIConfig.settingsPanel, settingsPanel.getMainPanel());
        frame.add(GUIConfig.collectionPanel, collectionPanel.getMainPanel());
        frame.add(GUIConfig.shopPanel, shopPanel.getMainPanel());
        frame.add(GUIConfig.gameMainPanel, gameMainPanel.getMainPanel());
        frame.add(GUIConfig.playPanel, playPanel.getMainPanel());
        cardLayout.show(frame.getContentPane(), GUIConfig.logInPanel);

        setTheTimerTask();
        Mapper.setMainFrame(this);
        Thread painter = new Thread(this);
        painter.start();
    }

    private void setTheTimerTask() {
        java.util.Timer timer = new java.util.Timer();
        TimerTask timerTask = this.actionHandler;
        timer.scheduleAtFixedRate(timerTask, 1000, 10);
    }

    public void exit() {
        boolean allowedToExit = true;
        if (previousLocation.equals(playPanel)){
            allowedToExit = playPanel.backCommandIssued();
        }
        if(allowedToExit) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

    public void backToMainPanel() {
        boolean allowedToGoBack = true;
        if (previousLocation.equals(GUIConfig.collectionPanel)){
            allowedToGoBack = collectionPanel.backToMainPanelCommandIssued();
        } else if(previousLocation.equals(GUIConfig.statusPanel)){
            statusPanel.setToUpdateTopDecks(true);
        } else if(previousLocation.equals(GUIConfig.playPanel)){
            allowedToGoBack = playPanel.backCommandIssued();
        }
        if (allowedToGoBack) {
            cardLayout.show(frame.getContentPane(), GUIConfig.gameMainPanel);
            refreshPage();
            previousLocation = GUIConfig.gameMainPanel;

        }
    }

    public void goToPanel(String panelName) {
        cardLayout.show(frame.getContentPane(), panelName);
        if (panelName.equals(GUIConfig.shopPanel)) {
            shopPanel.resetVis();
        }
        if (panelName.equals(GUIConfig.collectionPanel)) {
            collectionPanel.resetVis();
            JOptionPane.showMessageDialog(collectionPanel.getMainPanel(),
                    "Keep in mind that changes made to decks will only be saved\n" +
                            "if the deck is at its full capacity and will be ignored otherwise!");
        }
        if (panelName.equals(GUIConfig.statusPanel)){
            statusPanel.resetVis();
        }
        if (panelName.equals(GUIConfig.playPanel)){

            String firstContestantHero = PlayPanelPopUp.chooseHeroPopUp();
            if (firstContestantHero == null){
                JOptionPane.showMessageDialog(playPanel.getMainPanel(), "No hero chosen!");
                backToMainPanel();
                return;
            }
            String firstContestantPassive = PlayPanelPopUp.choosePassive();

            String secondContestantHero = PlayPanelPopUp.chooseHeroPopUp();
            if (secondContestantHero == null){
                JOptionPane.showMessageDialog(playPanel.getMainPanel(), "No hero chosen!");
                backToMainPanel();
                return;
            }
            String secondContestantPassive = PlayPanelPopUp.choosePassive();

            GameState.getInstance().getUser().changeHero(firstContestantHero);
            GameState.getInstance().getUser().setPassive(firstContestantPassive);

            playPanel.resetVis(firstContestantHero, firstContestantPassive, secondContestantHero,
                    secondContestantPassive);
        }

        refreshPage();
        previousLocation = panelName;
    }
    public void refreshPage(){
        frame.repaint();
        frame.revalidate();
    }

    public void changeBattlegroundBackground(int index){
        playPanel.changeImage("./assets/arena" + (index + 1) + ".jpg");
    }

    @Override
    public void run() {
        refreshPage();
    }
}
