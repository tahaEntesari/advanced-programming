package Collection.Listeners;

import GUI.Panels.CollectionPanels.CollectionPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static LoggingModule.LoggingClass.logUser;

public class CollectionGeneralListener implements ActionListener {
    private CollectionPanel collectionPanelMainClass;

    public CollectionGeneralListener(CollectionPanel collectionPanelMainClass) {
        this.collectionPanelMainClass = collectionPanelMainClass;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        logUser("Collection->Button clicked: " + actionEvent.getActionCommand());
        switch (actionEvent.getActionCommand()) {
            case "Go":
                collectionPanelMainClass.handleGo();
                break;
            case "Next":
                collectionPanelMainClass.handleNext();
                break;
            case "Previous":
                collectionPanelMainClass.handlePrevious();
                break;
            case "Reset":
                collectionPanelMainClass.resetButtonClicked();
                break;
            case "Disp All":
                collectionPanelMainClass.dispAllclicked();
                break;
            case "Disp Available":
                collectionPanelMainClass.dispAvailableClicked();
                break;
            case "Disp Buyable":
                collectionPanelMainClass.dispBuyableClicked();
                break;
            case "Create New Deck":
                collectionPanelMainClass.createNewDeckFunction();
                break;
            default:
                collectionPanelMainClass.handleDefaultEventHandlerCase(actionEvent);
                break;
        }
    }
}
