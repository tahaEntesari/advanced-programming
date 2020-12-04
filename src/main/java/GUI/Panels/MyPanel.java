package GUI.Panels;

import Configurations.GUIConfig;
import GUI.ActionHandler;
import GUI.MyPaintComponent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MyPanel {
    private JPanel mainPanel;
    private ToolbarPanel toolbarPanel;
    private ActionHandler actionHandler;
    private JLayeredPane centerPanel;
    private Image image;
    public MyPanel(ActionHandler actionHandler, int numberOfKeysForToolbarPanel, String imageFileLocation){
        this.actionHandler = actionHandler;
        if (imageFileLocation.equals("")) image = null;
        else{
            try {
                image = ImageIO.read(new File(imageFileLocation)).getScaledInstance(
                        GUIConfig.width, GUIConfig.height, Image.SCALE_SMOOTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mainPanel = new JPanel();
        centerPanel = new JLayeredPane(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        toolbarPanel = new ToolbarPanel(this.actionHandler, numberOfKeysForToolbarPanel);
        mainPanel.setLayout(new BorderLayout());
//        mainPanel.add(toolbarPanel.getToolbarPanel(), BorderLayout.NORTH);
        mainPanel.add(toolbarPanel.getOverlayPanel(), BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }

    public void changeImage(String imageFileLocation){
        try {
            image = ImageIO.read(new File(imageFileLocation)).getScaledInstance(
                    GUIConfig.width, GUIConfig.height, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
    public JPanel getTransparentPanel(){
        return toolbarPanel.getTransparentPanel();
    }

    public JLayeredPane getCenterPanel() {
        return centerPanel;
    }

    public ActionHandler getActionHandler() {
        return actionHandler;
    }
}
