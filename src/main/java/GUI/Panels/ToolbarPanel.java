package GUI.Panels;

import GUI.ActionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ToolbarPanel implements ActionListener{
    private JPanel toolbarPanel;
    private ActionHandler actionHandler;
    private JPanel overlayPanel;
    private JPanel transparentPanel;
    public ToolbarPanel(ActionHandler actionHandler, int numberOfKeys) {
        /*
        if numberOfKeys is 1, then the toolbar will only contain the "exit" key.
         */
        this.actionHandler = actionHandler;
        overlayPanel = new JPanel();
        overlayPanel.setLayout(new OverlayLayout(overlayPanel));
        transparentPanel = new JPanel();
        toolbarPanel = new JPanel();

        overlayPanel.add(transparentPanel, BorderLayout.WEST);
        overlayPanel.add(toolbarPanel, BorderLayout.EAST);
        toolbarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton exitButton = new JButton("Exit");

        exitButton.setContentAreaFilled(false);
        exitButton.setIcon(new ImageIcon(
                new BufferedImage(10,5,BufferedImage.TYPE_INT_RGB)));
        exitButton.setRolloverIcon(new ImageIcon(
                new BufferedImage(10,5,BufferedImage.TYPE_INT_ARGB)));
        exitButton.setBorder(null);

        exitButton.addActionListener(this);
        toolbarPanel.add(exitButton);
        if (numberOfKeys>1) {
            JButton backButtton = new JButton("Back");
            backButtton.addActionListener(this);
            toolbarPanel.add(backButtton);

            backButtton.setContentAreaFilled(false);
            backButtton.setIcon(new ImageIcon(
                    new BufferedImage(10,5,BufferedImage.TYPE_INT_RGB)));
            backButtton.setRolloverIcon(new ImageIcon(
                    new BufferedImage(10,5,BufferedImage.TYPE_INT_ARGB)));
            backButtton.setBorder(null);
        }
    }

    public JPanel getOverlayPanel() {
        return overlayPanel;
    }

    public JPanel getTransparentPanel(){
        return transparentPanel;
    }

    public ActionHandler getActionHandler() {
        return actionHandler;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        actionHandler.setActionEvent(actionEvent);
    }
}
