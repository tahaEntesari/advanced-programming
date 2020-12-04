package GUI.Panels.PlayPanel;

import Arena.ArenaActionListener;
import Arena.GameHandler;
import Card.Cards;
import Configurations.GUIConfig;
import GUI.UtilityFunctions.GetImageLocation;
import GUI.UtilityFunctions.ToolTip;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class MinionPanel implements MouseListener {
    private JPanel mainPanel;
    private Cards.minion card;
    private Image image = null;
    private int width, height;
    private boolean isVisible;
    private Border highLightBorder;
    private boolean enteredNotExited = false;
    private ArenaActionListener arenaActionListener;
    private boolean minionOwner;
    private GameHandler gameHandler;

    public MinionPanel(boolean firstPlayer, ArenaActionListener arenaActionListener, GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        minionOwner = firstPlayer;
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setVisible(false);
        mainPanel.addMouseListener(this);
        mainPanel.setFocusable(true);

        highLightBorder = BorderFactory.createLineBorder(Color.YELLOW, 5);

        width = GUIConfig.playedCardWidth;
        height = GUIConfig.playedCardHeight;
        this.arenaActionListener = arenaActionListener;
    }

    public void setVisibility(boolean visible) {
        mainPanel.setVisible(visible);
        isVisible = visible;
        if (!visible) {
            mainPanel.setBorder(null);
        }
    }

    public void setVisibilityTemporary(boolean visible){
        mainPanel.setVisible(visible);
    }

    public void highlightBorder(){
        mainPanel.setBorder(highLightBorder);
        setVisibilityTemporary(true);
        mainPanel.repaint();
        mainPanel.revalidate();

    }

    public void removeBorder(){
        mainPanel.setBorder(null);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    public void setImage(String cardName) {
        String imageFileLocation = GetImageLocation.getCardImageLocation(cardName);
        try {
            image = ImageIO.read(new File(imageFileLocation)).getScaledInstance(
                    width, height, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetImage(){
        image = null;
    }

    public void setCard(Cards.minion card) {
        enteredNotExited = false;
        this.card = card;
        setImage(card.getName());
        setToolTip();
    }

    public boolean getEnteredNotExitedAndReset(){
        boolean result = enteredNotExited;
//        enteredNotExited = false;
        return result;
    }

    public void setToolTip() {
        ToolTip.setTextToolTip(mainPanel, card);
    }

    public String getCardName() {
        return card.getName();
    }

    public Cards.minion getCard() {
        return card;
    }

    public void setLocationAndBound(int x, int y) {
        mainPanel.setBounds(x, y, width, height);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updateGameHandler(GameHandler gameHandler){
        this.gameHandler = gameHandler;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (gameHandler.getCurrentTurn() == minionOwner) {
            if (!gameHandler.requestedTargetDoubleWay() && !gameHandler.requestedForTarget()){
                arenaActionListener.toggleArrowShow(card);
            } else {
                if (gameHandler.requestedForTarget()) {
                    arenaActionListener.toggleCrosshairShow(card);
                }
            }
        } else {
            if (gameHandler.requestedTargetDoubleWay() && card.canBeAttacked()){
                arenaActionListener.toggleArrowShow(card);
            } else if (gameHandler.requestedForTarget()) {
                arenaActionListener.toggleCrosshairShow(card);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        enteredNotExited = true;
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        enteredNotExited = false;
    }
}
