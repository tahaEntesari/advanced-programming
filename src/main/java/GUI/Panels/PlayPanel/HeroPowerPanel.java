package GUI.Panels.PlayPanel;

import Arena.ArenaActionListener;
import Arena.GameHandler;
import Configurations.GUIConfig;
import GUI.UtilityFunctions.GetImageLocation;
import GUI.UtilityFunctions.ToolTip;
import Hero.Powers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class HeroPowerPanel implements MouseListener {
    private JPanel mainPanel;
    private Image image = null;
    private Powers.heroPowers heroPower;
    private int width, height;
    private ArenaActionListener arenaActionListener;
    private boolean firstPlayer;
    private GameHandler gameHandler;


    public HeroPowerPanel(boolean firstPlayer, GameHandler gameHandler, ArenaActionListener arenaActionListener) {
        this.gameHandler = gameHandler;
        this.firstPlayer = firstPlayer;
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setVisible(false);
        this.arenaActionListener = arenaActionListener;
        mainPanel.addMouseListener(this);
        width = GUIConfig.heroPowerWidth;
        height = GUIConfig.heroPowerHeight;
    }

    public void setBounds() {
        if (firstPlayer)
            mainPanel.setBounds(GUIConfig.heroPowerXLocation, GUIConfig.heroPowerYLocation, width, height);
        else
            mainPanel.setBounds(GUIConfig.heroPowerXLocation, GUIConfig.heroPowerYLocationOpponent, width, height);
    }

    public void setImage(String heroPowerName) {
        String imageFileLocation = GetImageLocation.getCardImageLocation(heroPowerName);
        try {
            image = ImageIO.read(new File(imageFileLocation)).getScaledInstance(
                    width, height, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPanel.setVisible(true);
    }

    public void setHeroPower(Powers.heroPowers heroPower) {
        this.heroPower = heroPower;
        if (heroPower == null) {
            image = null;
            mainPanel.setVisible(false);
            return;
        }
        setImage(heroPower.getName());
        setToolTip();
    }

    public void setToolTip() {
        ToolTip.setTextToolTip(mainPanel, heroPower);
    }

    public void refresh() {
        heroPower.setNumberOfUsagesLeftThisTurn(heroPower.getMaxNumberOfUsagesEachTurn());
        setToolTip();
        mainPanel.setVisible(true);
//        mainPanel.setBackground(Color.WHITE);

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private boolean isTurnOk(){
        return firstPlayer == gameHandler.getCurrentTurn();
    }

    public void updateGameHandler(GameHandler gameHandler){
        this.gameHandler = gameHandler;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (isTurnOk()){

        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (isTurnOk() && !gameHandler.requestedForTarget()) {
            if ((firstPlayer &&mouseEvent.getY() < -10) || (!firstPlayer && mouseEvent.getY() > height)) {
                int numberOfRemaining = arenaActionListener.useHeroPower();
                if (numberOfRemaining == 0) {
                    mainPanel.setVisible(false);
                }
            }
            setToolTip();
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        if (isTurnOk() && !gameHandler.requestedForTarget())
            arenaActionListener.enlargeView(heroPower.getName());
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        if (isTurnOk() && !gameHandler.requestedForTarget())
            arenaActionListener.hideEnlargedPanel();
    }
}
