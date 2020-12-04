package GUI.Panels.PlayPanel;

import Card.InitiateCards;
import GameState.GameState;
import Passive.Passive;
import User.User;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayPanelPopUp {
    public static String chooseHeroPopUp() {
        String[] choices = new String[InitiateCards.getHeroClass().length - 1];
        for (int i = 1; i < InitiateCards.getHeroClass().length; i++) {
            choices[i - 1] = InitiateCards.getHeroClass()[i];
        }
        String input = (String) JOptionPane.showInputDialog(null, "Choose Your Hero",
                "Herooooo hero hero Herooooo hero hero ....", JOptionPane.QUESTION_MESSAGE, null,
                choices, // Array of choices
                choices[0]); // Initial choice
        return input;
    }

    public static String choosePassive() {
        Passive passive = new Passive();
        ArrayList<String> names = new ArrayList<>(passive.getName());
        ArrayList<String> descriptions = new ArrayList<>(passive.getDescription());
        String[] choices = new String[3];
        Random random = new Random(System.nanoTime());
        for (int i = 0; i < choices.length; i++) {
            int index = random.nextInt(names.size());
            choices[i] = names.remove(index) + ": " + descriptions.remove(index);
        }
        String input = (String) JOptionPane.showInputDialog(null, "Choose Your Passive",
                "Passive", JOptionPane.QUESTION_MESSAGE, null,
                choices, // Array of choices
                choices[0]); // Initial choice

        if (input != null)
            input = input.substring(0, input.indexOf(":"));
        return input;
    }


}
