package GUI.Panels.PlayPanel.SelectionPanels;

import Arena.ArenaActionListener;
import Card.Cards;
import Configurations.GUIConfig;
import GUI.UtilityFunctions.GetImageLocation;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class CardPanel implements MouseListener {
    private JPanel mainPanel;
    private Image image = null;
    private int width, height;
    private Border highLightBorder;
    private ArenaActionListener arenaActionListener;
    private boolean selectedForChange = false;
    private Cards.card card;

    public CardPanel(ArenaActionListener arenaActionListener) {
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

        highLightBorder = BorderFactory.createLineBorder(Color.RED, 15);

        width = GUIConfig.initialCardArenaXLocation;
        height = GUIConfig.initialCardArenaYLocation;
        this.arenaActionListener = arenaActionListener;
    }

    public void setVisibility(boolean visible) {
        mainPanel.setVisible(visible);
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

    public Cards.card getCard() {
        return card;
    }

    public void setCard(Cards.card card) {
        this.card = card;
    }

    private void resetImage(){
        image = null;
    }

    public void setLocationAndBound(int x, int y) {
        mainPanel.setBounds(x, y, width, height);
    }

    public boolean isSelectedForChange() {
        return selectedForChange;
    }

    public void toggleBorder(){
        if (selectedForChange){
            mainPanel.setBorder(highLightBorder);
        } else {
            resetBorder();
        }
    }

    private void resetBorder(){
        mainPanel.setBorder(null);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void reset(){
        resetBorder();
        resetImage();
        selectedForChange = false;
        setVisibility(false);
    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        selectedForChange = !selectedForChange;
        toggleBorder();
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
