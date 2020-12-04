package GUI.Panels.PlayPanel;

import Arena.ArenaActionListener;
import Arena.GameHandler;
import Configurations.GUIConfig;
import GUI.UtilityFunctions.GetImageLocation;
import GUI.UtilityFunctions.ToolTip;
import Arena.HeroForArena;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class HeroPanel implements MouseListener {
    private JPanel mainPanel;
    private Image image = null;
    private int width, height;
    private HeroForArena hero;
    private boolean firstPlayer;
    private ArenaActionListener arenaActionListener;
    private GameHandler gameHandler;

    public HeroPanel(boolean firstPlayer, ArenaActionListener arenaActionListener, GameHandler gameHandler) {
        this.firstPlayer = firstPlayer;
        this.gameHandler = gameHandler;
        this.arenaActionListener = arenaActionListener;
        mainPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.addMouseListener(this);
        width = GUIConfig.arenaHeroImageWidth;
        height = GUIConfig.arenaHeroImageHeight;
    }

    public void setImage(String heroName) {
        if(heroName == null){
            image = null;
            return;
        }
        String imageFileLocation = GetImageLocation.getHeroImageForArena(heroName);
        try {
            image = ImageIO.read(new File(imageFileLocation)).getScaledInstance(
                    width, height, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setHero(HeroForArena hero){
        this.hero = hero;
        try {
            setImage(hero.getName());
            updateTooltip();
        } catch (Exception e){
            setImage(null);
            updateTooltip();
        }

    }

    public void updateTooltip(){
        ToolTip.setTextToolTip(mainPanel, hero);
    }

    public void setLocation(){
        if (firstPlayer)
            mainPanel.setBounds(GUIConfig.arenaHeroImageX, GUIConfig.arenaHeroImageY, width, height);
        else
            mainPanel.setBounds(GUIConfig.arenaHeroImageX, GUIConfig.arenaHeroImageYOpponent, width, height);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updateGameHandler(GameHandler gameHandler){
        this.gameHandler = gameHandler;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (firstPlayer == gameHandler.getCurrentTurn()){
            if (gameHandler.requestedForTarget()) {
                arenaActionListener.toggleCrosshairShow(hero);
            }
            else  {
                arenaActionListener.toggleArrowShow(hero);
            }
        } else {
            if (gameHandler.requestedTargetDoubleWay()) {
                arenaActionListener.toggleArrowShow(hero);
            }
            else if (gameHandler.requestedForTarget()) {
                arenaActionListener.toggleCrosshairShow(hero);
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
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }
}
