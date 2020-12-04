package GUI.Arrow;

import javax.swing.*;
import java.awt.*;

public class ArrowDrawer implements Runnable {
    private LineArrow lineArrow;
    private JPanel panel;
    private boolean finished = false;
    private boolean initiated = false;

    public ArrowDrawer() {
        lineArrow = new LineArrow();
        panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                lineArrow.draw(g);
            }
        };
        panel.setVisible(true);
        panel.setOpaque(false);

    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public JPanel getPanel() {
        return panel;
    }

    public boolean isActive(){
        return initiated && !finished;
    }

    public void reset(){
        lineArrow.setHead(0, 0);
        lineArrow.setBase(0, 0);
        finished = false;
        initiated = false;
        panel.setVisible(false);

    }

    @Override
    public void run() {
        Point p;
        panel.setVisible(true);
        while (!finished) {
            if (!initiated) {
                lineArrow.setBase(MouseInfo.getPointerInfo().getLocation());
                initiated = true;
            }
            p = MouseInfo.getPointerInfo().getLocation();
            lineArrow.setHead(p);
            panel.repaint();
            panel.revalidate();
        }
        reset();

    }
}
