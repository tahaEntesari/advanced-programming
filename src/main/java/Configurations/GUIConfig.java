package Configurations;

import java.awt.*;

public class GUIConfig {

    public static final int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public static final int topDeckButtonWidth = 150;
    public static final String statusPanel = "statusPanel";
    public static final String logInPanel = "LogIn";
    public static final String settingsPanel = "settingsPanel";
    public static final String collectionPanel = "collectionPanel";
    public static final String shopPanel = "shopPanel";
    public static final String gameMainPanel = "gameMainPanel";
    public static final String playPanel = "playPanel";
    public static final int cardWidthShopPanel = width * 1 / 4;
    public static final int cardHeightShopPanel = height * 1 / 2;
    public static final int cardWidthCollectionPanel = width * 1 / 5;
    public static final int cardHeightCollectionPanel = height * 9 / 20;
    public static final int deckSinglePanelWidthCollection = cardWidthCollectionPanel * 9 / 10;
    public static final int deckSinglePanelHeightCollection = height / 10;
    public static final int deckWidthTopDeckStatusPanel = width * 1 / 3;
    public static final int deckHeightTopDeckStatusPanel = height * 1 / 11;
    public static final int statusPanelHeroImageWidth = width * 1 / 3;
    public static final int statusPanelHeroImageHeight = height * 7 / 8;
    public static final int playPanelThisPlayerManaLocationWidth = width * 2 / 3;
    public static final int playPanelThisPlayerManaLocationHeight = 30;
    public static final int playPanelOpponentManaLocationY = 40;
    public static final int playedCardWidth = GUIConfig.width / 12;
    public static final int playedCardHeight = GUIConfig.height / 6;
    public static final int arenaHeroImageWidth = GUIConfig.width * 5 / 32;
    public static final int arenaHeroImageHeight = GUIConfig.height / 4;
    public static final int arenaHeroImageX = GUIConfig.width / 2 - arenaHeroImageWidth / 2;
    public static final int arenaHeroImageY = GUIConfig.playedCardHeight;
    public static final int arenaHeroImageYOpponent = 60;
    public static final int handDisplayCardWidth = cardWidthShopPanel * 8 / 20;
    public static final int handDisplayCardHeight = cardHeightShopPanel * 11 / 20;
    public static final int handDisplayCardDifferences = 50;

    public static final int enlargedHandViewXlocation = 200;
    public static final int enlargedHandViewYLocation = playedCardHeight * 2 / 3;
    public static final int enlargedHandViewWidth = cardWidthShopPanel;
    public static final int enlargedHandViewHeight = cardHeightShopPanel;

    public static final int heroPowerWidth = 100;
    public static final int heroPowerHeight = 150;
    public static final int heroPowerXLocation = arenaHeroImageX + arenaHeroImageWidth;
    public static final int heroPowerYLocation = arenaHeroImageY * 5 / 4;
    public static final int heroPowerYLocationOpponent = 60;

    public static final int endTurnPanelWidth = 150;
    public static final int endTurnPanelHeight = 100;
    public static final int endTurnPanelXLocation = width - endTurnPanelWidth * 8 / 5;
    public static final int endTurnPanelYLocation = height / 2 - endTurnPanelHeight * 11 / 12;


    public static final int spellViewXlocation = width / 2 - 200;
    public static final int spellViewYLocation = height * 3 / 8;
    public static final int spellViewWidth = cardWidthShopPanel / 2;
    public static final int spellViewHeight = cardHeightShopPanel / 2;

    public static final int initialCardArenaXLocation = width / 3;
    public static final int initialCardArenaYLocation = height * 2 / 3;

}
