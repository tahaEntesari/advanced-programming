package Passive;

import java.io.Serializable;
import java.util.ArrayList;

public class Passive{
    private ArrayList<String> name;
    private ArrayList<String> description;

    public Passive() {
        name = new ArrayList<>();
        description = new ArrayList<>();

        name.add("Twice Draw");
        description.add("You can draw two cards each turn");

        name.add("Off Cards");
        description.add("Every card that you play costs 1 less mana.");

        name.add("Warriors");
        description.add("Every minion that you have played that dies adds 2 to your armour");

        name.add("Free Power");
        description.add("Mana cost for your hero power reduces by 1 and you get to use it twice in each round");

            name.add("Mana Jump");
        description.add("You start the game with 2 mana instead of 1");
    }

    public ArrayList<String> getName() {
        return name;
    }

    public ArrayList<String> getDescription() {
        return description;
    }
}
