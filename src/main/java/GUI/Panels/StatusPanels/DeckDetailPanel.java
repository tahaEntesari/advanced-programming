package GUI.Panels.StatusPanels;

import Configurations.GUIConfig;
import Deck.Deck;
import GUI.UtilityFunctions.GetImageLocation;
import GameState.GameState;
import Hero.Hero;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DeckDetailPanel {
    private JPanel mainPanel;
    private Deck deck;
    private JLabel deckName;
    private JLabel winRatio;
    private JLabel numberOfWins;
    private JLabel numberOfGames;
    private JLabel averageManaCost;
    private Image mostUsedCardImage = null;

    public DeckDetailPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        deckName = new JLabel();
        winRatio = new JLabel();
        numberOfGames = new JLabel();
        numberOfWins = new JLabel();
        averageManaCost = new JLabel();

        JPanel northPanel = new JPanel();
        JPanel southPanel = new JPanel();
        JPanel eastPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(mostUsedCardImage, 0, 0, null);
            }
        };
        mainPanel.add(northPanel, BorderLayout.WEST);
        mainPanel.add(southPanel, BorderLayout.EAST);
        mainPanel.add(eastPanel, BorderLayout.CENTER);

        eastPanel.setSize(new Dimension(GUIConfig.statusPanelHeroImageWidth, GUIConfig.statusPanelHeroImageHeight));

        northPanel.setLayout(new FlowLayout());
        northPanel.add(deckName);


        BoxLayout boxLayout = new BoxLayout(southPanel, BoxLayout.Y_AXIS);
        southPanel.setLayout(boxLayout);

        southPanel.add(winRatio);
        southPanel.add(numberOfWins);
        southPanel.add(numberOfGames);
        southPanel.add(averageManaCost);
    }

    public void updatePanel(){
        deckName.setText(deck.getHero() + ": " + deck.getDeckName());
        winRatio.setText("Win ratio: " + deck.getRatioOfWinsWithThisDeck());
        numberOfWins.setText("Number Of Wins: " + deck.getNumberOfWinsWithThisDeck());
        numberOfGames.setText("Number Of Games: " + deck.getNumberOfGamesWithThisDeck());
        averageManaCost.setText("Average Mana Cost: " + deck.getDeckAverageManaCost());
        try {
            String mostUsedCardImageFileLocation = GetImageLocation.getCardImageLocation(deck.getMostUsedCard());
            mostUsedCardImage = ImageIO.read(new File(mostUsedCardImageFileLocation))
//                    .getScaledInstance(
//                            GUIConfig.statusPanelHeroImageWidth, GUIConfig.statusPanelHeroImageHeight, Image.SCALE_SMOOTH)
            ;


        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPanel.repaint();
        mainPanel.revalidate();
    }


    public void setDeck(Deck deck) {
        this.deck = deck;
        updatePanel();
    }
    public void setDeck(String deckHero, String deckName){
        for (Hero hero : GameState.getInstance().getUser().getHeroes()) {
            if(hero.getName().equals(deckHero)){
                for (Deck deck : hero.getDecks()) {
                    if(deck.getDeckName().equals(deckName)){
                        setDeck(deck);
                        return;
                    }
                }
            }
        }
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }
}
