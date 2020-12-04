package GUI.Panels;

import Configurations.GUIConfig;
import GUI.MapperForDeckButtonLike;
import GUI.Panels.CollectionPanels.CollectionPanel;
import GUI.UtilityFunctions.GetImageLocation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import static LoggingModule.LoggingClass.logUser;

public class DeckButtonlikePanel implements MouseListener {
    private JPanel mainPanel;
    private MapperForDeckButtonLike mapperForDeckButtonLike;
    private String deckName;
    private String deckHero;
    private Image image;
    private int width, height;
    private JLabel deckNameLabel;

    public DeckButtonlikePanel(MapperForDeckButtonLike mapperForDeckButtonLike, int width, int height) {
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                myDrawImage(g);
            }
        };
        this.mapperForDeckButtonLike = mapperForDeckButtonLike;
        this.width = width;
        this.height = height;
        mainPanel.setSize(new Dimension(width, height));
        mainPanel.addMouseListener(this);
        mainPanel.setLayout(new BorderLayout());
        deckNameLabel = new JLabel("");
        deckNameLabel.setForeground(Color.CYAN);
//        deckNameLabel.setOpaque(false);
        mainPanel.add(deckNameLabel, BorderLayout.CENTER);
    }

    public void setDeckName(String deckName, String deckHero) {
        this.deckName = deckName;
        this.deckHero = deckHero;
        deckNameLabel.setText(deckHero + ": " + deckName);
        setImage(deckHero);
    }


    public void setImage(String heroName) {
        String imageFileLocation = GetImageLocation.getHeroImageForDeckBackGroundLocation(heroName);
        try {
            image = ImageIO.read(new File(imageFileLocation)).getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void myDrawImage(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        logUser("Collection -> Show deck: " + deckHero + ": " + deckName);
        mapperForDeckButtonLike.showDeckCards(deckHero, deckName);
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
