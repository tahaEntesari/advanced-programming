package GUI.Crosshair;

import javax.swing.*;
import java.awt.*;

public class CrosshairDrawer implements Runnable{
    private JPanel panel;
    private Crosshair crosshair;
    private boolean finished, initiated;

    public CrosshairDrawer(){
        crosshair = new Crosshair();
        panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                crosshair.draw(g);
            }
        };
        panel.setVisible(false);
        panel.setOpaque(false);
        finished = false;
        initiated = false;
    }

    public JPanel getPanel() {
        return panel;
    }

    public boolean isActive(){
        return initiated && !finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void reset(){
        crosshair.setPoint(new Point(0, 0));
        finished = false;
        initiated = false;
        panel.setVisible(false);

    }

    @Override
    public void run() {
        Point p;
        panel.setVisible(true);
        initiated = true;
        while (!finished) {
            p = MouseInfo.getPointerInfo().getLocation();
            crosshair.setPoint(p);
            panel.repaint();
            panel.revalidate();
        }
        reset();

    }
}
