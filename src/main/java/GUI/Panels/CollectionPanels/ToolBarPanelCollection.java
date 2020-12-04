package GUI.Panels.CollectionPanels;

import Card.InitiateCards;
import GUI.Panels.CollectionPanels.CollectionPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBarPanelCollection {
    private JPanel mainPanel;
    private JButton[] heroClassesButtons;
    private JButton displayAll, displayAvailable, displayBuyable;
    private JTextField searchTextField;

    public ToolBarPanelCollection(CollectionPanel collectionPanel) {
        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        completeCollectionToolbarPanel(collectionPanel);
    }

    public void completeCollectionToolbarPanel(CollectionPanel collectionPanelMainClass) {
        String[] heroClasses = InitiateCards.getHeroClass();
        heroClassesButtons = new JButton[heroClasses.length];
        for (int i = 0; i < heroClasses.length; i++) {
            heroClassesButtons[i] = new JButton(heroClasses[i].substring(0, collectionPanelMainClass.getNumberOfCharactersOfHeroNameToDisplay()));
            heroClassesButtons[i].addActionListener(collectionPanelMainClass.getCollectionGeneralListener());
            heroClassesButtons[i].setOpaque(true);
            heroClassesButtons[i].setBorderPainted(false);
            mainPanel.add(heroClassesButtons[i]);
        }
        JLabel searchLabel = new JLabel("Search: ");
        searchTextField = new JTextField(10);
        searchTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                collectionPanelMainClass.handleGo();
            }
        });
        JButton searchButton = new JButton("Go");
        JButton resetSearch = new JButton("Reset");
        mainPanel.add(searchLabel);
        mainPanel.add(searchTextField);
        mainPanel.add(searchButton);
        mainPanel.add(resetSearch);
        JButton nextPage = new JButton("Next");
        JButton previousPage = new JButton("Previous");
        mainPanel.add(previousPage);
        mainPanel.add(nextPage);

        nextPage.addActionListener(collectionPanelMainClass.getCollectionGeneralListener());
        previousPage.addActionListener(collectionPanelMainClass.getCollectionGeneralListener());
        searchButton.addActionListener(collectionPanelMainClass.getCollectionGeneralListener());
        resetSearch.addActionListener(collectionPanelMainClass.getCollectionGeneralListener());

        displayBuyable = new JButton("Disp Buyable");
        displayBuyable.addActionListener(collectionPanelMainClass.getCollectionGeneralListener());

        displayAll = new JButton("Disp All");
        displayAll.addActionListener(collectionPanelMainClass.getCollectionGeneralListener());

        displayAvailable = new JButton("Disp Available");
        displayAvailable.addActionListener(collectionPanelMainClass.getCollectionGeneralListener());

        mainPanel.add(displayAll);
        mainPanel.add(displayAvailable);
        mainPanel.add(displayBuyable);

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JButton[] getHeroClassesButtons() {
        return heroClassesButtons;
    }

    public JButton getDisplayAll() {
        return displayAll;
    }

    public JButton getDisplayAvailable() {
        return displayAvailable;
    }

    public JButton getDisplayBuyable() {
        return displayBuyable;
    }

    public JTextField getSearchTextField() {
        return searchTextField;
    }

    public void setSearchTextFieldText(String text) {
        searchTextField.setText(text);
    }
}
