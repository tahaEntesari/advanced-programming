package GUI.Panels.PlayPanel;

import GUI.Panels.CollectionPanels.CollectionPanel;

import javax.swing.*;
import java.awt.*;

public class ManaPanel {
    private JPanel mainPanel;
    private JButton[] manaButtons;
    private int currentMana;
    private int currentRound;

    public ManaPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        mainPanel.setOpaque(false);
        completeManaPanel();
        currentRound = 1;
        setCurrentMana(1);
    }

    private void completeManaPanel() {
        manaButtons = new JButton[11];
        manaButtons[0] = new JButton(currentMana + " / " + currentRound);

        for (int i = 1; i < 11; i++) {
            manaButtons[i] = new JButton("" + i);
            mainPanel.add(manaButtons[i]);
        }
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
        manaButtons[0].setText(currentMana + " / " + currentRound);
        colorTheButtons();
    }

    public void executeRound(){
        if (this.currentRound < 10)
            this.currentRound++;
        setCurrentMana(this.currentRound);
    }
    public void colorTheButtons(){
        for (int i = 1; i < 11; i++) {
            Color color = i <= currentMana? Color.BLUE: Color.WHITE;
            manaButtons[i].setBackground(color);
        }
    }

    public void reset(){
        this.currentRound = 1;
        setCurrentMana(1);

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
