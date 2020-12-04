package GUI.Panels.CollectionPanels;

import Collection.CardTransactionAndDeckModificationFunctions;
import Collection.DrawerFunctions;
import Collection.CollectionEventHandlingFunctions;
import Collection.Listeners.CollectionGeneralListener;
import Collection.Listeners.collectionKeyListener;
import Configurations.GUIConfig;
import GUI.ActionHandler;
import GUI.MapperForDeckButtonLike;
import GUI.Panels.*;
import Deck.Deck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;


public class CollectionPanel{
    private JLayeredPane collectionPanel;
    private MyPanel myPanel;
    private JPanel deckViewMainPanel;
    private CardLayout cardLayoutForDeckView;
    private DeckOverviewPanel deckOverviewPanel;
    private MainCardDisplayPanel cardDisplayPanel;
    private ToolBarPanelCollection collectionToolbarPanel;
    private ManaFilterPanelForCollection manaFilterPanel;
    private DeckDisplayPanel deckDisplayPanel;

    private int displayingPageNumber = 0;
    private int displayingHeroClass = 0;
    private int currentManaFilterButton = 11;
    private ArrayList<String> cardsOnDisplay;
    private String displayingWhichCards = "all";  // "all", "available" and "buyable"
    private int numberOfCharactersOfHeroNameToDisplay = 4;
    private Deck currentChosenDeck;
    private boolean enteredCreateNewDeckMode = false;

    private CollectionEventHandlingFunctions collectionEventHandlingFunctions;
    private DrawerFunctions drawerFunctions;
    private CardTransactionAndDeckModificationFunctions cardTransactionAndDeckModificationFunctions;
    private CollectionGeneralListener collectionGeneralListener;
    private MapperForDeckButtonLike mapperForDeckButtonLike;

    public CollectionPanel(ActionHandler actionHandler, String imageFileLocation) {
        collectionEventHandlingFunctions = new CollectionEventHandlingFunctions(this);
        drawerFunctions = new DrawerFunctions(this);
        cardTransactionAndDeckModificationFunctions = new CardTransactionAndDeckModificationFunctions(this);
        collectionGeneralListener = new CollectionGeneralListener(this);
        mapperForDeckButtonLike = new MapperForDeckButtonLike(this);

        myPanel = new MyPanel(actionHandler, 2, imageFileLocation);
        collectionPanel = myPanel.getCenterPanel();
        collectionPanel.setLayout(new BorderLayout());

        deckViewMainPanel = new JPanel();
        cardLayoutForDeckView = new CardLayout(0, 0);
        deckViewMainPanel.setLayout(cardLayoutForDeckView);


        deckOverviewPanel = new DeckOverviewPanel(this);
        deckDisplayPanel = new DeckDisplayPanel(this);
        JScrollPane decksPanelScrollPane = new JScrollPane(deckViewMainPanel);
        deckViewMainPanel.add(deckOverviewPanel.getMainPanel(), "overview");
        deckViewMainPanel.add(deckDisplayPanel.getDeckDisplayPanel(), "display");

        deckViewMainPanel.setAutoscrolls(true);

        deckViewMainPanel.setMaximumSize(new Dimension(GUIConfig.deckSinglePanelWidthCollection, GUIConfig.cardHeightCollectionPanel * 2));
        decksPanelScrollPane.setMaximumSize(new Dimension(GUIConfig.deckSinglePanelWidthCollection, GUIConfig.cardHeightCollectionPanel * 2));
        deckOverviewPanel.setMaximumSize(new Dimension(GUIConfig.deckSinglePanelWidthCollection, GUIConfig.cardHeightCollectionPanel * 2));


        cardDisplayPanel = new MainCardDisplayPanel(this);

        collectionToolbarPanel = new ToolBarPanelCollection(this);

        manaFilterPanel = new ManaFilterPanelForCollection(this);


        collectionPanel.add(decksPanelScrollPane, BorderLayout.EAST);
        collectionPanel.add(cardDisplayPanel.getMainPanel(), BorderLayout.CENTER);
        collectionPanel.add(collectionToolbarPanel.getMainPanel(), BorderLayout.NORTH);
        collectionPanel.add(manaFilterPanel.getMainPanel(), BorderLayout.SOUTH);

        collectionPanel.addKeyListener(new collectionKeyListener(this));

        cardLayoutForDeckView.show(deckViewMainPanel, "overview");

    }


    public void resetVis() {
        drawerFunctions.resetVis();
    }

    public void refreshPage() {
        drawerFunctions.refreshPage();
    }

    public void colorTheButtons(){
        drawerFunctions.colorTheButtons();
    }

    public void cardsToDraw(ArrayList<String> cardNames) {
        cardDisplayPanel.cardsToDraw(cardNames);
    }

    public void cardsToDraw(String cardName) {
        cardDisplayPanel.cardsToDraw(cardName);
    }

    public void showDeckCards(String heroName, String deckName) {
        drawerFunctions.showDeckCards(heroName, deckName);
    }

    public void showDeckCards() {
        drawerFunctions.showDeckCards();
    }

    public void requestFocus() {
        collectionPanel.requestFocus();
    }

    public void repaint() {
        collectionPanel.repaint();
    }

    public void revalidate() {
        collectionPanel.revalidate();
    }

    /////////////////////////////////////////////// Deck transaction /////////////////////////////////////////////////
    public void removeFromDeck(String cardName) {
        cardTransactionAndDeckModificationFunctions.removeFromDeck(cardName);
    }

    public void addToDeck(String cardName) {
        cardTransactionAndDeckModificationFunctions.addToDeck(cardName);
    }

    public void buyCard(String cardName) {
        cardTransactionAndDeckModificationFunctions.buyCard(cardName);
    }

    /////////////////////////////////////////////// Deck transaction /////////////////////////////////////////////////
    /////////////////////////////////////////////// Event Handling ///////////////////////////////////////////////////
    public void handleGo() {
        collectionEventHandlingFunctions.handleGo();
    }

    public void handleNext() {
        collectionEventHandlingFunctions.handleNext();
    }

    public void handlePrevious() {
        collectionEventHandlingFunctions.handlePrevious();
    }

    public void handleUpDown(boolean up) {
        collectionEventHandlingFunctions.handleUpDown(up);
    }

    public void deckBackButtonClicked() {
        collectionEventHandlingFunctions.deckBackButtonClicked();

    }

    public void deckDeleteButtonClicked() {
        collectionEventHandlingFunctions.deckDeleteButtonClicked();
    }

    public void deckRenameButtonClicked() {
        collectionEventHandlingFunctions.deckRenameButtonClicked();
    }

    public void deckChangeHeroButtonClicked() {
        collectionEventHandlingFunctions.deckChangeHeroButtonClicked();
    }

    public void createNewDeckFunction() {
        collectionEventHandlingFunctions.createNewDeckFunction();
    }

    public void setCurrentDeckAsHerosMainDeckClicked() {
        collectionEventHandlingFunctions.setCurrentDeckAsHerosMainDeckClicked();
    }

    public boolean backToMainPanelCommandIssued() {
        return collectionEventHandlingFunctions.backToMainPanelCommandIssued();
    }

    public void resetButtonClicked() {
        collectionEventHandlingFunctions.resetButtonClicked();
    }

    public void dispAllclicked() {
        collectionEventHandlingFunctions.dispAllclicked();
    }

    public void dispAvailableClicked() {
        collectionEventHandlingFunctions.dispAvailableClicked();
    }

    public void dispBuyableClicked() {
        collectionEventHandlingFunctions.dispBuyableClicked();
    }

    public void handleDefaultEventHandlerCase(ActionEvent actionEvent) {
        collectionEventHandlingFunctions.handleDefaultEventHandlerCase(actionEvent);
    }


    /////////////////////////////////////////////// Event Handling ///////////////////////////////////////////////////
    ////////////////////////////////////////Getters. Might need to delete unnecessary //////////////////////////////////


    public JLayeredPane getCollectionPanel() {
        return collectionPanel;
    }

    public JPanel getMainPanel() {
        return myPanel.getMainPanel();
    }

    public JPanel getDeckViewMainPanel() {
        return deckViewMainPanel;
    }

    public CardLayout getCardLayoutForDeckView() {
        return cardLayoutForDeckView;
    }

    public DeckDisplayPanel getDeckDisplayPanel() {
        return deckDisplayPanel;
    }


    public int getDisplayingPageNumber() {
        return displayingPageNumber;
    }

    public int getDisplayingHeroClass() {
        return displayingHeroClass;
    }

    public int getCurrentManaFilterButton() {
        return currentManaFilterButton;
    }

    public ArrayList<String> getCardsOnDisplay() {
        return cardsOnDisplay;
    }

    public String getDisplayingWhichCards() {
        return displayingWhichCards;
    }

    public int getNumberOfCharactersOfHeroNameToDisplay() {
        return numberOfCharactersOfHeroNameToDisplay;
    }

    public Deck getCurrentChosenDeck() {
        return currentChosenDeck;
    }

    public boolean isEnteredCreateNewDeckMode() {
        return enteredCreateNewDeckMode;
    }

    public CollectionGeneralListener getCollectionGeneralListener() {
        return collectionGeneralListener;
    }

    public ToolBarPanelCollection getCollectionToolbarPanel() {
        return collectionToolbarPanel;
    }

    public ManaFilterPanelForCollection getManaFilterPanel() {
        return manaFilterPanel;
    }

    public DeckOverviewPanel getDeckOverviewPanel() {
        return deckOverviewPanel;
    }

    public MapperForDeckButtonLike getMapperForDeckButtonLike() {
        return mapperForDeckButtonLike;
    }

    //////////////////////////////////////////////// Setters /////////////////////////////////////////////////////////
    public void setDisplayingHeroClass(int displayingHeroClass) {
        this.displayingHeroClass = displayingHeroClass;
    }

    public void setDisplayingWhichCards(String displayingWhichCards) {
        this.displayingWhichCards = displayingWhichCards;
    }

    public void setDisplayingPageNumber(int displayingPageNumber) {
        this.displayingPageNumber = displayingPageNumber;
    }

    public void setEnteredCreateNewDeckMode(boolean enteredCreateNewDeckMode) {
        this.enteredCreateNewDeckMode = enteredCreateNewDeckMode;
    }

    public void setCurrentManaFilterButton(int currentManaFilterButton) {
        this.currentManaFilterButton = currentManaFilterButton;
    }

    public void setCurrentChosenDeck(Deck currentChosenDeck) {
        this.currentChosenDeck = currentChosenDeck;
    }

    public void setCardsOnDisplay(ArrayList<String> cardsOnDisplay) {
        this.cardsOnDisplay = cardsOnDisplay;
    }

}
