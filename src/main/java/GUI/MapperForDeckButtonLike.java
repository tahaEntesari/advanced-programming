package GUI;

import GUI.Panels.CollectionPanels.CollectionPanel;
import GUI.Panels.StatusPanels.StatusPanel;

public class MapperForDeckButtonLike {
    private StatusPanel statusPanel = null;
    private CollectionPanel collectionPanel = null;

    public MapperForDeckButtonLike(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    public MapperForDeckButtonLike(CollectionPanel collectionPanel) {
        this.collectionPanel = collectionPanel;
    }

    public void showDeckCards(String deckHero, String deckName){
        if (statusPanel == null){
            collectionPanel.showDeckCards(deckHero, deckName);
        } else {
            statusPanel.showDeckCards(deckHero, deckName);
        }
    }

}
