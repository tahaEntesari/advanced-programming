package Collection.Listeners;

import GUI.Panels.CollectionPanels.CollectionPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeckDisplayButtonActionListener implements ActionListener {
    private CollectionPanel collectionPanel;

    public DeckDisplayButtonActionListener(CollectionPanel collectionPanel) {
        this.collectionPanel = collectionPanel;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String command = actionEvent.getActionCommand();
        String cardName = command.substring(0, command.indexOf(':'));
        collectionPanel.removeFromDeck(cardName);
    }
}
