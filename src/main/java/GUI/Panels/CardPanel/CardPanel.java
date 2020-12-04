package GUI.Panels.CardPanel;

import Card.Cards;
import GUI.UtilityFunctions.GetImageLocation;
import GUI.UtilityFunctions.ToolTip;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class CardPanel implements MouseListener {
    protected JPanel mainPanel;
    protected  String cardName;
    private Image image = null;
    private int width, height;
    private boolean availability = true;
    private Image secondImage = null;
    private boolean showingCardBack = false;
    private Cards.card card = null;


    public CardPanel(int width, int height){
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                myDrawImage(g);

            }
        };
        mainPanel.addMouseListener(this);

        image = null;
        this.width = width;
        this.height = height;
        mainPanel.setPreferredSize(new Dimension(width, height));


        try {
            secondImage = ImageIO.read(new File(GetImageLocation.getCardImageLocation("cardBackClassic"))).getScaledInstance(
                    width, height * 88 / 100, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void myDrawImage(Graphics g) {
        if (availability)
            g.drawImage(image, 0, 0, null);
        else{
            g.drawImage(image, 0, 0, Color.GRAY, null);
        }
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public void setImage(String imageFileLocation) {
        try {
            image = ImageIO.read(new File(imageFileLocation)).getScaledInstance(
                    width, height * 88 / 100, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCardBack(boolean showBack){
        if ((showBack && !showingCardBack) || (!showBack && showingCardBack)) {
            Image temp = image;
            image = secondImage;
            secondImage = temp;
            showingCardBack = !showingCardBack;
        }
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setCard(Cards.card card) {
        this.card = card;
        setCardName(card.getName());
        setImageFromCardName(card.getName());
    }

    public void setOpacity(){
        mainPanel.setOpaque(false);
    }

    public void setVisibility(boolean visible){
        mainPanel.setVisible(visible);
    }

    public void refresh(){

        mainPanel.repaint();
        mainPanel.revalidate();
    }

    public void setLocation(int x, int y){
        mainPanel.setBounds(x, y, width, height);
    }

    public void setImageFromCardName(String cardName){
        setImage(GetImageLocation.getCardImageLocation(cardName));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Cards.card getCard() {
        return card;
    }

    public void setTooltip(){
        ToolTip.setTextToolTip(mainPanel, card);
    }

    public void reset(){
        try {
            secondImage = ImageIO.read(new File(GetImageLocation.getCardImageLocation("cardBackClassic"))).getScaledInstance(
                    width, height * 88 / 100, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        showingCardBack = false;
        card = null;
        setVisibility(false);
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
