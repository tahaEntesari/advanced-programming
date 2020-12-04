package GUI.Panels.PlayPanel.SelectionPanels;

import Configurations.GUIConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseBetweenTwoChoices implements ActionListener {
    private JPanel mainPanel;
    private JButton button1, button2;
    private boolean button1Selected, button2Selected;

    public ChooseBetweenTwoChoices() {
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(100, 200, 0, 200));
        mainPanel.setOpaque(true);
        mainPanel.setVisible(false);
        mainPanel.setLayout(new BorderLayout());

        button1 = new JButton("Give a random minion +1/+1");
        button2 = new JButton("Add a card to your hand");
        button1.addActionListener(this);
        button2.addActionListener(this);

        mainPanel.add(button1, BorderLayout.WEST);
        mainPanel.add(button2, BorderLayout.EAST);

//        button1.setBounds(GUIConfig.initialCardArenaXLocation, GUIConfig.initialCardArenaYLocation - 200,
//                GUIConfig.initialCardArenaXLocation, 100);
//        button2.setBounds(GUIConfig.initialCardArenaXLocation, GUIConfig.initialCardArenaYLocation + 200,
//                GUIConfig.initialCardArenaXLocation, 100);

    }

    public boolean isFinished(){
        return button1Selected || button2Selected;
    }

    public boolean getSelectedButtonAndReset(){
        boolean result = button1Selected;
        button1Selected = false;
        button2Selected = false;
        return result;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setVisibility(boolean visibility){
        mainPanel.setVisible(visibility);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand().equals("Give a random minion +1/+1")) {
            button1Selected = true;
        } else if (actionEvent.getActionCommand().equals("Add a card to your hand")) {
            button2Selected = true;
        }
        mainPanel.setVisible(false);
    }
}
