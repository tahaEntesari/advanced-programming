package GUI.Crosshair;

import java.awt.*;

public class Crosshair {
    private int x, y;
    private Color color;
    private int thickness;
    private int width;

    public Crosshair() {
        x = y = 0;
        color = Color.YELLOW;
        thickness = 5;
        width = 20;

    }

    public void setPoint(Point p){
        x = p.x - 7;
        y = p.y - 30;
    }

    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));
        g2.drawLine(x - width, y, x + width, y);;
        g2.drawLine(x, y - width, x, y + width);;
        g2.dispose();
    }
}
