package Status;

import GUI.Panels.StatusPanels.StatusPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatusGeneralListener implements ActionListener {
    private StatusPanel statusPanel;

    public StatusGeneralListener(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}
