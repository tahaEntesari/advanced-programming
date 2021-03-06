package User;

import Configurations.Main_config_file;
import GameState.GameState;
import LoggingModule.LoggingClass;
import Utility.FileFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.logging.Logger;

import static LoggingModule.LoggingClass.logUser;
import static LoggingModule.LoggingClass.openUserLogger;

public class UserFunctions {
    private static Logger main_logger = LoggingClass.getMainLoggerInstance();

    public static String userSignUp(String username, String password, GameState gameState) {
        String signUpResult;
        if (!PasswordUsername.CheckUsernameValidity(username)) {
            signUpResult = "Invalid Username";
        } else if (!PasswordUsername.PasswordFormatCheck(password)) {
            signUpResult = "Invalid password format.\n At least 8 letters with\n" +
                    "both uppercase and lowercase English letters.";
        } else {
            signUpResult = "Successful";
            User currentUser = createUser.createAUser(username, password);
            gameState.setUser(currentUser);
            logUser("The current Balance of the user " + currentUser.getWalletBalance());
            logUser("Your current hero: " + currentUser.getCurrentHeroName() +
                    " with hero level: " + currentUser.getHeroLevel());
        }
        return signUpResult;
    }


    public static User userLogIn(String username, String password) {
        long userID = findUserID(username);
        if (userID == -1) {
            main_logger.info("Error. No such user; or user has been deleted.");
            return null;
        }
        String passwordvalid = PasswordUsername.PasswordValidityCheck(username, userID, password);
        if (passwordvalid.equals("Incorrect")) {
            main_logger.info("Password incorrect.");
            return null;
        }
        if (passwordvalid.equals("UserNonExistent")) {
            main_logger.info("What the hell!? I found this user's userID through searching the" +
                    " user list but now I couldn't find it.");
            return null;
        }

        User returnUser = null;

        returnUser = User.deserializeUser(username, userID);
        if (returnUser == null) return null;
        // instantiate the user logger.

        openUserLogger(username, userID);
        try {
            logUser("Log in.");
        } catch (Exception e) {
            main_logger.info("The user logger could not be instantiated.");
            main_logger.info(e.getMessage());
            return null;
        }
        //LOAD THE GOD DAMN USER DATA.
        return returnUser;
    }

    public static User userLogOut(User user) {
        logUser("Log out.");
        // SAVE THE USER DATA. ALSO SAVE THE LOG OUT TIME.
        user.serializeUser();
        LoggingClass.closeUserLogger();
        return null;
    }

    public static long findUserID(String username) {
        JSONArray array = getUserArray();
        for (int i = 0; i < array.size(); i++) {
            JSONObject temp_user = (JSONObject) array.get(i);
            if (temp_user.get("Username").equals(username) && temp_user.get("Deleted At").equals("None")) {
                return (long) temp_user.get("UserID");
            }
        }
        return -1;
    }

    public static JSONArray getUserArray() {
        return FileFunctions.loadJsonArray(Main_config_file.getUser_list_location());
    }

    public static JSONObject getUserFromArray(String username, long userID) {
        JSONArray array = UserFunctions.getUserArray();
        JSONObject result = null;
        boolean flag = false;
        for (int i = 0; i < array.size(); i++) {
            JSONObject temp_user = (JSONObject) array.get(i);
            if (temp_user.get("Username").equals(username) && (((long) temp_user.get("UserID")) == userID)
                    && temp_user.get("Deleted At").equals("None")) {
                flag = true;
                result = (JSONObject) array.get(i);
                break;
            }
        }
        if (!flag) main_logger.info("Error. Could not find the user in the user list.");
        return result;
    }

    public static JSONObject getUserFromArray(JSONArray array, String username, long userID) {
        JSONObject result = null;
        boolean flag = false;
        for (int i = 0; i < array.size(); i++) {
            JSONObject temp_user = (JSONObject) array.get(i);
            if (temp_user.get("Username").equals(username) && (((long) temp_user.get("UserID")) == userID)
                    && temp_user.get("Deleted At").equals("None")) {
                flag = true;
                result = (JSONObject) array.get(i);
                break;
            }
        }
        if (!flag) main_logger.info("Error. Could not find the user in the user list.");
        return result;
    }

}