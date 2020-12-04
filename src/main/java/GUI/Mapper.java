package GUI;

import Configurations.GUIConfig;
import GameState.GameState;
import User.UserFunctions;


public class Mapper {
    private static Mapper instance;
    private GameState gameState;
    private MainFrame mainFrame;
    private Mapper(){
        gameState = GameState.getInstance();
    }
    public static Mapper getInstance(){
        if (instance == null) instance = new Mapper();
        return instance;
    }

    public static void setMainFrame(MainFrame mainFrame) {
        if(instance == null) instance = new Mapper();
        instance.mainFrame = mainFrame;
    }

    public boolean userLogIn(String username, String password){
        boolean logInResult = gameState.userLogIn(username, password);
        if(logInResult) changeMainFrameCard(GUIConfig.gameMainPanel);
        return logInResult;
    }
    public String userSignUp(String username, String password){
        String signUpResult;
        signUpResult = UserFunctions.userSignUp(username, password, gameState);
        if (signUpResult.equals("Successful"))
            changeMainFrameCard(GUIConfig.gameMainPanel);
        return signUpResult;
    }
    private void changeMainFrameCard(String panelName){
        mainFrame.goToPanel(panelName);
    }
    public void changeBattleGroundWallpaper(int index){
        mainFrame.changeBattlegroundBackground(index);
    }
}
