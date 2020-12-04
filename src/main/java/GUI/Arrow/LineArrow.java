package GUI.Arrow;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class LineArrow {

    int x;
    int y;
    int endX;
    int endY;
    Color color;
    int thickness;

    public LineArrow(int x, int y, int x2, int y2, Color color) {
        this.x = x;
        this.y = y;
        this.endX = x2;
        this.endY = y2;

        this.color = color;
        this.thickness = 5;
    }

    public LineArrow(){
        x = y = endY = endX = 50;
        thickness = 5;
        color = Color.RED;
    }

    public void setBase(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setHead(int x, int y){
        this.endX = x;
        this.endY = y;
    }

    public void setHead(Point p){
        this.endX = p.x;
        this.endY = p.y - 50;
    }

    public void setBase(Point p){
        this.x = p.x;
        this.y = p.y - 50;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));
        g2.drawLine(x, y, endX, endY);;
//        drawArrowHead(g2);
        g2.dispose();
    }
}