package GUI.UtilityFunctions;

import Card.Cards;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToolTip {

    public static void setTextToolTip(JComponent component, Object object){
        if (object == null){
            component.setToolTipText(null);
            return;
        }
        component.setToolTipText(GUI.UtilityFunctions.StringToJLabel.correctFormat(object.toString()));
        setTimeout(component);
    }

    public static void setTextToolTip(JComponent component, String cardName){
        component.setToolTipText(
                GUI.UtilityFunctions.StringToJLabel.correctFormat(
                        Card.CardUtilityFunctions.getCardObjectFromName(cardName).superPrint()));
        setTimeout(component);
    }
    public static void setTimeout(JComponent component){
        final int defaultDismissTimeout = ToolTipManager.sharedInstance().getDismissDelay();

        component.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent me) {
                ToolTipManager.sharedInstance().setDismissDelay(60000);
            }

            public void mouseExited(MouseEvent me) {
                ToolTipManager.sharedInstance().setDismissDelay(defaultDismissTimeout);
            }
        });
    }

}
