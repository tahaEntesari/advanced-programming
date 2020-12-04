package GUI.Panels.ShopPanels;

import Card.InitiateCards;
import GUI.Panels.CollectionPanels.CollectionPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBarPanelShop {
    private JPanel mainPanel;
    private JButton[] heroClassesButtons;
    private JButton displayBuyable, displaySellable;
    private JTextField searchTextField;

    public ToolBarPanelShop(ShopPanel shopPanel) {
        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        completeCollectionToolbarPanel(shopPanel);
    }

    public void completeCollectionToolbarPanel(ShopPanel shopPanel) {
        String[] heroClasses = InitiateCards.getHeroClass();
        heroClassesButtons = new JButton[heroClasses.length];
        for (int i = 0; i < heroClasses.length; i++) {
            heroClassesButtons[i] = new JButton(heroClasses[i].substring(0, shopPanel.getNumberOfCharactersOfHeroNameToDisplay()));
            heroClassesButtons[i].addActionListener(shopPanel.getShopGeneralListener());
            heroClassesButtons[i].setOpaque(true);
            heroClassesButtons[i].setBorderPainted(false);
            mainPanel.add(heroClassesButtons[i]);
        }
        JLabel searchLabel = new JLabel("Search: ");
        searchTextField = new JTextField(10);
        searchTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                shopPanel.handleGo();
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

        nextPage.addActionListener(shopPanel.getShopGeneralListener());
        previousPage.addActionListener(shopPanel.getShopGeneralListener());
        searchButton.addActionListener(shopPanel.getShopGeneralListener());
        resetSearch.addActionListener(shopPanel.getShopGeneralListener());

        displayBuyable = new JButton("Display Buyable");
        displayBuyable.addActionListener(shopPanel.getShopGeneralListener());

        displaySellable = new JButton("Display Sellable");
        displaySellable.addActionListener(shopPanel.getShopGeneralListener());

        mainPanel.add(displayBuyable);
        mainPanel.add(displaySellable);

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JButton[] getHeroClassesButtons() {
        return heroClassesButtons;
    }

    public JButton getDisplaySellable() {
        return displaySellable;
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
