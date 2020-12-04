package Collection.Listeners;

import GUI.Panels.CollectionPanels.CollectionPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CollectionDeckActionListener implements ActionListener {
    CollectionPanel collectionPanel;

    public CollectionDeckActionListener(CollectionPanel collectionPanel) {
        this.collectionPanel = collectionPanel;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()){
            case "Rename Deck":
                collectionPanel.deckRenameButtonClicked();
                break;
            case "Change Hero":
                collectionPanel.deckChangeHeroButtonClicked();
                break;
            case "Back":
                collectionPanel.deckBackButtonClicked();
                break;
            case "Delete Deck":
                collectionPanel.deckDeleteButtonClicked();
                break;
            case "Set as main deck":
                collectionPanel.setCurrentDeckAsHerosMainDeckClicked();
                break;
        }
    }
}
