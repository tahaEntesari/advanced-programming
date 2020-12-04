package GameState;

import User.User;

import User.UserFunctions;

public class GameState {
    private User user;
    private static GameState instance;

    private GameState() {
    }

    public static GameState getInstance() {
        if (instance == null) instance = new GameState();
        return instance;
    }

    public boolean userLogIn(String username, String password) {
        this.user = UserFunctions.userLogIn(username, password);
        return this.user != null;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getWalletBalance() {
        return user.getWalletBalance();
    }

    public User getUser() {
        return user;
    }

}
