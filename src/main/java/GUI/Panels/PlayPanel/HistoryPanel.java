package GUI.Panels.PlayPanel;

import Configurations.GUIConfig;
import Configurations.GameConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HistoryPanel implements MouseListener {
    private JPanel mainPanel;
    private JPanel dummyPanelBeforeEnlarging;
    private JButton[] historyButtons;
    private int numberOfHistory;

    public HistoryPanel() {
        this.mainPanel = new JPanel();
        dummyPanelBeforeEnlarging = new JPanel();
        dummyPanelBeforeEnlarging.setOpaque(true);
        dummyPanelBeforeEnlarging.setBackground(Color.GREEN);
        dummyPanelBeforeEnlarging.addMouseListener(this);
        completeHistoryPanel();

    }

    private void completeHistoryPanel(){
        mainPanel.setOpaque(false);
        mainPanel.setVisible(false);
        numberOfHistory = GameConfig.numberOfPreviousMovesToShow;
        mainPanel.setLayout(new GridLayout(numberOfHistory + 1, 1));

        JLabel historyLabel = new JLabel("History");
        historyLabel.setForeground(Color.RED);
        mainPanel.add(historyLabel);

        historyButtons = new JButton[numberOfHistory];
        for (int i = 0; i < numberOfHistory; i++) {
            historyButtons[i] = new JButton("");
            mainPanel.add(historyButtons[i]);
        }
    }

    public void addNewActionToHistory(String action){
        for (int i = numberOfHistory - 1; i >0 ; i--) {
            historyButtons[i].setText(historyButtons[i - 1].getText());
        }
        historyButtons[0].setText(GUI.UtilityFunctions.StringToJLabel.correctFormat(action));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JPanel getDummyPanelBeforeEnlarging() {
        return dummyPanelBeforeEnlarging;
    }

    public void setBounds(){
        mainPanel.setBounds(50, 20, 100, GUIConfig.height - 100);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        mainPanel.setVisible(true);
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        mainPanel.setVisible(false);
    }

    public void reset(){
        for (int i = 0; i < numberOfHistory; i++) {
            historyButtons[i].setText("");
        }
    }
}
