package Status;

import Configurations.GameConfig;
import Deck.Deck;
import GameState.GameState;
import Hero.Hero;
import User.User;

import java.util.ArrayList;
import java.util.Collections;

public class GetTopDecks {
    public static ArrayList<Deck> getTopDecks(){
        ArrayList<Deck> topDecks = new ArrayList<>();
        ArrayList<Deck> temp = new ArrayList<>();
        User user = GameState.getInstance().getUser();
        for (Hero hero : user.getHeroes()) {
            temp.addAll(hero.getDecks());
        }
        Collections.sort(temp);
        for (int i = 0; i < temp.size() && i < GameConfig.numberOfTopDecks; i++) {
            topDecks.add(temp.get(i));
        }
        return topDecks;
    }

}
