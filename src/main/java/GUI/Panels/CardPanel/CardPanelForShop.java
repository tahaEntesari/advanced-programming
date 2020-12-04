package GUI.Panels.CardPanel;

import GUI.Panels.ShopPanels.ShopPanel;

import javax.swing.*;
import java.awt.event.MouseEvent;

import static LoggingModule.LoggingClass.logUser;

public class CardPanelForShop extends CardPanel {
    private ShopPanel shopPanel;
    private int price;
    private String transactionType;
    public CardPanelForShop(int width, int height, ShopPanel shopPanel) {
        super(width, height);
        this.shopPanel = shopPanel;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        logUser("Shop: Clicked on card");
        int transactionOption = JOptionPane.showConfirmDialog(mainPanel, "Do you want to " +
                        transactionType + " this for " + price + "?",
                "Transaction popup", JOptionPane.YES_NO_OPTION);
        if (transactionOption == JOptionPane.YES_OPTION) {
            logUser("Shop: Yes");
            shopPanel.issueTransaction(cardName);
        }
    }
}
