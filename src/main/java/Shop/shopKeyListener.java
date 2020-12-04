package Shop;

import GUI.Panels.ShopPanels.ShopPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class shopKeyListener implements KeyListener {
    ShopPanel panel;
    public shopKeyListener(ShopPanel shopPanel) {
        this.panel = shopPanel;
    }

    ///////////////////////////////////////////Functions///////////////////////////////////////////////////////////////
    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()){
            case KeyEvent.VK_RIGHT:
                panel.handleNext();
                break;
            case KeyEvent.VK_LEFT:
                panel.handlePrevious();
                break;
            case KeyEvent.VK_UP:
                panel.handleUpDown(true);
                break;
            case KeyEvent.VK_DOWN:
                panel.handleUpDown(false);
                break;
            case KeyEvent.VK_ENTER:
                panel.handleGo();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
