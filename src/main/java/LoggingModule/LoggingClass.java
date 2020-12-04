package LoggingModule;
import Configurations.Main_config_file;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class LoggingClass {
    private static final Logger main_logger = Logger.getLogger("main_logger");
    private static FileHandler fh;
    private static boolean userLoggerIsOpen = false;

    static File file;
    static FileWriter fr;

    static SimpleFormatter formatter = new SimpleFormatter();
    static {
        try {
            fh = new FileHandler(returnMainLoggerPath());
            main_logger.addHandler(fh);
            fh.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }
    }
    private LoggingClass(){}
    public static void openUserLogger(String username, long userID) {
        file = new File(returnUserLoggerPath(username, userID));
        try {
            fr = new FileWriter(file, true);
            fr.write("user logger opened.");
            main_logger.info("user logger opened");
            userLoggerIsOpen = true;
        } catch (IOException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }

    }
    public static String returnUserLoggerPath(String username, long userID){
        return Main_config_file.getLogFolder() + "\\" + username + "-" + userID + ".log";
    }
    public static String returnMainLoggerPath(){
        return Main_config_file.getLogFolder() + "\\mainlog" +
                "_" + LocalDateTime.now().toString().substring(5,10) + "_"
                + LocalDateTime.now().toString().substring(11,13) + "_"
                + LocalDateTime.now().toString().substring(14,16) + ".log";
    }
    public static boolean logUserIsOpen(){
        return userLoggerIsOpen;
    }
    public static void logUser(String message){
        try {
            fr.write( LocalDateTime.now().toString() + "\n" + message + "\n");
            System.out.println(message);
            fr.flush();
        } catch (IOException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }

    }
    public static void closeUserLogger(){
        try {
            fr.close();
            userLoggerIsOpen = false;
        } catch (IOException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }
    }
    public static void closeMainLogger(){
        main_logger.removeHandler(fh);
    }
    public static Logger getMainLoggerInstance(){
        return main_logger;
    }


}