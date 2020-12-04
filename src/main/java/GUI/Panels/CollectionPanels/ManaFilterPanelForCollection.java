package GUI.Panels.CollectionPanels;

import GUI.Panels.CollectionPanels.CollectionPanel;

import javax.swing.*;
import java.awt.*;

public class ManaFilterPanelForCollection {
    private JPanel mainPanel;
    private JButton[] manaFilterButtons;

    public ManaFilterPanelForCollection(CollectionPanel collectionPanel) {
        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        completeManaFilterPanel(collectionPanel);
    }

    private void completeManaFilterPanel(CollectionPanel collectionPanelMainClass) {
        JLabel manaFilter = new JLabel("Filter by Mana");
        mainPanel.add(manaFilter);
        manaFilterButtons = new JButton[12];
        for (int i = 0; i < 11; i++) {
            manaFilterButtons[i] = new JButton("" + i);
            manaFilterButtons[i].addActionListener(collectionPanelMainClass.getCollectionGeneralListener());
            mainPanel.add(manaFilterButtons[i]);
        }
        manaFilterButtons[11] = new JButton("None");
        manaFilterButtons[11].addActionListener(collectionPanelMainClass.getCollectionGeneralListener());
        mainPanel.add(manaFilterButtons[11]);

        JButton createNewDeck = new JButton("Create New Deck");
        createNewDeck.addActionListener(collectionPanelMainClass.getCollectionGeneralListener());

        JLabel labelForDistance = new JLabel("         ");
        mainPanel.add(labelForDistance);
        mainPanel.add(createNewDeck);

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JButton[] getManaFilterButtons() {
        return manaFilterButtons;
    }
}
