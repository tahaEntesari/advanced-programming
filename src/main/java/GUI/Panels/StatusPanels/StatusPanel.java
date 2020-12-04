package GUI.Panels.StatusPanels;

import GUI.ActionHandler;
import GUI.MapperForDeckButtonLike;
import GUI.Panels.MyPanel;
import Status.StatusGeneralListener;

import javax.swing.*;
import java.awt.*;

public class StatusPanel{
    private JLayeredPane statusPanel;
    private TopDeckPanel topDeckPanel;
    private MyPanel myPanel;
    private DeckDetailPanel deckDetailPanel;

    private StatusGeneralListener statusGeneralListener;
    private MapperForDeckButtonLike mapperForDeckButtonLike;

    private boolean toUpdateTopDecks = true;
    public StatusPanel(ActionHandler actionHandler, String imageFileLocation){
        myPanel = new MyPanel(actionHandler, 2, imageFileLocation);
        statusPanel = myPanel.getCenterPanel();
        statusPanel.setLayout(new GridLayout(1, 2));
        statusGeneralListener = new StatusGeneralListener(this);
        mapperForDeckButtonLike = new MapperForDeckButtonLike(this);
        topDeckPanel = new TopDeckPanel(this);
        deckDetailPanel = new DeckDetailPanel();

        statusPanel.add(deckDetailPanel.getMainPanel());
        statusPanel.add(topDeckPanel.getMainPanel());


    }

    public void resetVis(){
        if (toUpdateTopDecks){
            toUpdateTopDecks = false;
            topDeckPanel.updateDecks();
        }
    }

    public void showDeckCards(String deckHero, String deckName){
        deckDetailPanel.setDeck(deckHero, deckName);
    }

    public JPanel getMainPanel(){
        return myPanel.getMainPanel();
    }

    public StatusGeneralListener getStatusGeneralListener() {
        return statusGeneralListener;
    }

    public MapperForDeckButtonLike getMapperForDeckButtonLike() {
        return mapperForDeckButtonLike;
    }

    public void setToUpdateTopDecks(boolean toUpdateTopDecks) {
        this.toUpdateTopDecks = toUpdateTopDecks;
    }
}
