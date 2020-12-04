package GUI.Panels.PlayPanel.MovingPanel;

import Configurations.GUIConfig;
import GUI.UtilityFunctions.GetImageLocation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MovingCard {
    private JPanel panel;
    private Image image;
    private int width, height;
    private boolean visible;

    public MovingCard(){
        panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        image = null;
        width = GUIConfig.playedCardWidth;
        height = GUIConfig.playedCardHeight;
        panel.setOpaque(false);
        panel.setVisible(false);
        panel.setPreferredSize(new Dimension(width, height));
    }

    public JPanel getPanel(){
        return panel;
    }

    public void setImage(String cardName){
        String imageFileLocation = GetImageLocation.getCardImageLocation(cardName);
        try {
            image = ImageIO.read(new File(imageFileLocation)).getScaledInstance(
                    width, height, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setVisibility(true);
    }

    public void setLocation(Point point){
        point.setLocation(point.x - 20, point.y - 70);
        panel.setLocation(point);
    }

    public void setVisibility(boolean visibility){
        visible = visibility;
        panel.setVisible(visible);
        if (!visible) image = null;
    }
}
