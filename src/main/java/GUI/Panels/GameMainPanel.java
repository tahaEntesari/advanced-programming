package GUI.Panels;

import GUI.ActionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameMainPanel implements ActionListener {
    private JLayeredPane centerPanel;
    private MyPanel myPanel;
    public GameMainPanel(ActionHandler actionHandler, String imageFileLocation){
        myPanel = new MyPanel(actionHandler, 1, imageFileLocation);
        centerPanel = myPanel.getCenterPanel();
        /////////////// RestOfPanel Panel ////////////////////
        centerPanel.setLayout(new GridLayout(3,2));
        completeRestOfPanel();
    }
    private void completeRestOfPanel(){
        JButton play = new JButton("Play");
        JButton status = new JButton("Status");
        JButton shop = new JButton("Shop");
        JButton collection = new JButton("Collection");
        JButton settings = new JButton("Settings");
        Font font = new Font("Helvetica", Font.ITALIC, 25);
        JButton []buttons = {play, status, shop, collection, settings};
        for (JButton button : buttons) {
            button.addActionListener(this);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setForeground(Color.BLUE);
            button.setFont(font);
//            button.setBorderPainted(false);
        }
        centerPanel.add(play);
        centerPanel.add(status);
        centerPanel.add(shop);
        centerPanel.add(collection);
        centerPanel.add(settings);

    }

    public JPanel getMainPanel(){
        return myPanel.getMainPanel();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        myPanel.getActionHandler().setActionEvent(actionEvent);
    }
}
