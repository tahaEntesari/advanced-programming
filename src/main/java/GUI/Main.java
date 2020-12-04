package GUI;

import Card.InitiateCards;
import Configurations.GUIConfig;
import Configurations.Main_config_file;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Main_config_file.createRequiredDirectories();
        File file = new File(Main_config_file.returnCardSaveDataLocation("Fireball"));
        if(!file.exists())
            InitiateCards.InstantiateAllCards();

        final MainFrame[] mainFrame = new MainFrame[1];
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainFrame[0] = new MainFrame(GUIConfig.width, GUIConfig.height);
            }
        });

    }
}
