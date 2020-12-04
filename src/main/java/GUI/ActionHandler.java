package GUI;

import java.awt.event.ActionEvent;
import java.util.TimerTask;

import static LoggingModule.LoggingClass.logUser;

public class ActionHandler extends TimerTask {
    ActionEvent actionEvent;
    MainFrame mainFrame;
    ActionHandler(){}
    ActionHandler(MainFrame mainFrame){
        this.mainFrame = mainFrame;
    }

    public void setActionEvent(ActionEvent actionEvent) {
        logUser("Button click: " + actionEvent.getActionCommand());
        this.actionEvent = actionEvent;
    }

    @Override
    public void run() {
        switch (getActionEventAndRemoveIt()){
            case "Exit":
                mainFrame.exit();
                break;
            case "Back":
                mainFrame.backToMainPanel();
                break;
            case "Play":
                mainFrame.goToPanel("playPanel");
                break;
            case "Status":
                mainFrame.goToPanel("statusPanel");
                break;
            case "Shop":
                mainFrame.goToPanel("shopPanel");
                break;
            case "Collection":
                mainFrame.goToPanel("collectionPanel");
                break;
            case "Settings":
                mainFrame.goToPanel("settingsPanel");
                break;
            case "GameFinished":
                mainFrame.backToMainPanel();
                break;
            case "Null":
                break;
        }
    }


    public String getActionEventAndRemoveIt(){
        String temp;
        if (actionEvent!= null) {
            temp = actionEvent.getActionCommand();
            actionEvent = null;
        } else {
            temp = "Null";
        }
        return temp;
    }
}
