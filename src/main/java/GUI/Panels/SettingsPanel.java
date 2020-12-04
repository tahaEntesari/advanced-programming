package GUI.Panels;

import GUI.ActionHandler;
import GUI.Mapper;
import GUI.MyPaintComponent;
import GameState.GameState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import static LoggingModule.LoggingClass.logUser;

public class SettingsPanel implements MouseListener {
    private JLayeredPane settingsPanel;
    private MyPanel myPanel;
    private Mapper mapper = Mapper.getInstance();

    public SettingsPanel(ActionHandler actionHandler, String imageFileLocation){
        myPanel = new MyPanel(actionHandler, 2, imageFileLocation);
        settingsPanel = myPanel.getCenterPanel();
        settingsPanel.setLayout(null);
        compleChangeBattleground();
    }

    public void compleChangeBattleground(){
        Image[] image = new Image[3];
        String imageFileLocation;
        int width = 500;
        int height = 400;
        for (int i = 0; i < 3; i++) {
            imageFileLocation = "./assets/arena" + (i + 1) + ".jpg";
            try {
                image[i] = ImageIO.read(new File(imageFileLocation)).getScaledInstance(
                        width, height, Image.SCALE_SMOOTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JPanel[] panels = new JPanel[3];
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            panels[i] = new JPanel(){
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(image[finalI],0,0,null);
                }
            };
            panels[i].setName("" + i);
            panels[i].addMouseListener(this);
        }
        settingsPanel.add(panels[0]);
        panels[0].setBounds(0,0,width,height);
        settingsPanel.add(panels[1]);
        panels[1].setBounds(width, 0, width, height);
        settingsPanel.add(panels[2]);
        panels[2].setBounds(width * 2, 0, width, height);

    }


    public void changeBattleground(int index){
        mapper.changeBattleGroundWallpaper(index);
    }

    public JPanel getMainPanel(){
        return myPanel.getMainPanel();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        logUser("Changed battleground background");

        if (JOptionPane.showConfirmDialog(settingsPanel, "Change background?", "",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            changeBattleground(mouseEvent.toString().charAt(mouseEvent.toString().length() - 1) - '0');
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
