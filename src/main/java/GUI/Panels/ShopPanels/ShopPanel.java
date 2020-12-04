package GUI.Panels.ShopPanels;

import Card.CardUtilityFunctions;
import Configurations.GUIConfig;
import GUI.ActionHandler;
import GUI.Panels.CardPanel.CardPanelForShop;
import GUI.Panels.MyPanel;
import GUI.UtilityFunctions.GetImageLocation;
import GameState.GameState;
import Shop.*;
import PanelFunctions.ShopAndCollectionVisualizationFunctions;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ShopPanel {
    private GameState gameState;
    private JLayeredPane mainShopPanel;
    private ToolBarPanelShop shopToolbarPanel;
    private MainCardDisplayPanel cardDisplayPanel;
    private JPanel diamondPanel;
    private JLabel diamondAmount;
    private MyPanel myPanel;
    private CardPanelForShop[] cardPanels = new CardPanelForShop[8];
    private JButton[] heroClassesButtons;
    private JTextField searchTextField;
    private JButton searchButton, resetSearch, nextPage, previousPage, displayBuyable, displaySellable;
    private int displayingPageNumber = 0;
    private int displayingHeroClass = 0;
    private ArrayList<String> cardsOnDisplay;
    private String displayingBuyableOrSellable = "buy";  // "buy" and "sell" are valid!
    private int numberOfCharactersOfHeroNameToDisplay = 4;

    private boolean sorted = false;

    private ShopEventHandlerFunctions shopEventHandlerFunctions;
    private ShopGeneralListener shopGeneralListener;
    private DrawerFunctions drawerFunctions;

    public ShopPanel(ActionHandler actionHandler, String imageFileLocation) {
        shopEventHandlerFunctions = new ShopEventHandlerFunctions(this);
        shopGeneralListener = new ShopGeneralListener(this);
        drawerFunctions = new DrawerFunctions(this);

        gameState = GameState.getInstance();
        myPanel = new MyPanel(actionHandler, 2, imageFileLocation);
        mainShopPanel = myPanel.getCenterPanel();
//        diamondPanel = new JPanel();
        diamondPanel = myPanel.getTransparentPanel();
        diamondAmount = new JLabel("0");
        /////////////// Main Panel ////////////////////
        cardDisplayPanel = new MainCardDisplayPanel(this);
        mainShopPanel.setLayout(new BorderLayout());
        shopToolbarPanel = new ToolBarPanelShop(this);

        mainShopPanel.add(shopToolbarPanel.getMainPanel(), BorderLayout.NORTH);
        mainShopPanel.add(cardDisplayPanel.getMainPanel(), BorderLayout.CENTER);
        mainShopPanel.addKeyListener(new shopKeyListener(this));

        /////////////// diamond Panel ////////////////////
        JLabel diamond = new JLabel("Diamonds: ");
        diamondPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        diamondPanel.add(diamond);
        diamondPanel.add(diamondAmount);
    }

    public void resetVis() {
        drawerFunctions.resetVis();
    }

    public void refreshPage() {
        drawerFunctions.refreshPage();
    }

    public void colorTheButtons() {
        drawerFunctions.colorTheButtons();
    }

    public void setDiamondAmount() {
        diamondAmount.setText("" + gameState.getWalletBalance());
    }

    public void cardsToDraw() {
        cardDisplayPanel.cardsToDraw(cardsOnDisplay);
    }

    public void cardsToDraw(String cardName) {
        cardDisplayPanel.cardsToDraw(cardName);
    }

    public void issueTransaction(String cardName) {
        ShopTransactionFunctions.issueTransaction(cardName, this);
    }

    public void handleGo() {
        shopEventHandlerFunctions.handleGo(gameState.getUser());
    }

    public void handleNext() {
        shopEventHandlerFunctions.handleNext();
    }

    public void handlePrevious() {
        shopEventHandlerFunctions.handlePrevious();
    }

    public void handleUpDown(boolean up) {
        shopEventHandlerFunctions.handleUpDown(up);
    }

    public void repaint() {
        mainShopPanel.repaint();
    }

    public void revalidate() {
        mainShopPanel.revalidate();
    }

    public void requestFocus() {
        mainShopPanel.requestFocus();
    }

    ////////////////////////////////////////// Getters ///////////////////////////////////////////////////

    public JPanel getMainPanel() {
        return myPanel.getMainPanel();
    }

    public int getDisplayingPageNumber() {
        return displayingPageNumber;
    }

    public int getDisplayingHeroClass() {
        return displayingHeroClass;
    }

    public ArrayList<String> getCardsOnDisplay() {
        return cardsOnDisplay;
    }

    public String getDisplayingBuyableOrSellable() {
        return displayingBuyableOrSellable;
    }

    public int getNumberOfCharactersOfHeroNameToDisplay() {
        return numberOfCharactersOfHeroNameToDisplay;
    }

    public ShopGeneralListener getShopGeneralListener() {
        return shopGeneralListener;
    }

    public ToolBarPanelShop getShopToolbarPanel() {
        return shopToolbarPanel;
    }
    ///////////////////////////////////////// Setters //////////////////////////////////////////////////////


    public void setDisplayingPageNumber(int displayingPageNumber) {
        this.displayingPageNumber = displayingPageNumber;
    }

    public void setDisplayingHeroClass(int displayingHeroClass) {
        this.displayingHeroClass = displayingHeroClass;
    }

    public void setDisplayingBuyableOrSellable(String displayingBuyableOrSellable) {
        this.displayingBuyableOrSellable = displayingBuyableOrSellable;
    }

    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    public void setCardsOnDisplay(ArrayList<String> cardsOnDisplay) {
        this.cardsOnDisplay = cardsOnDisplay;
    }
}
