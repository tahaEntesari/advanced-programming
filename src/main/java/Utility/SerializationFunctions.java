package Utility;

import Card.Cards;
import Configurations.Main_config_file;
import LoggingModule.LoggingClass;
import User.User;

import java.io.*;
import java.util.Random;
import java.util.logging.Logger;

public class SerializationFunctions {
    private static Logger main_logger = LoggingClass.getMainLoggerInstance();
    public static void Serialize(Object object, String fileLocation){
        //Saving of object in a file
     try{
            FileOutputStream file = new FileOutputStream(fileLocation);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(object);

            out.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }
        main_logger.info("Object has been serialized");
    }
    public static User userDeserialize(String fileLocation) {
        // You must put the object name like below after you have called this method
        // returnObject = (className) returnObject;
        User returnObject = null;
        try {
            FileInputStream file = new FileInputStream(fileLocation);
            ObjectInputStream in = null;
            in = new ObjectInputStream(file);
            // Method for deserialization of object
             returnObject = (User)in.readObject();
            in.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }
        return returnObject;
    }
    public static Cards.card cardDeserialize(String cardNameOrFileLocation){
        // You must put the object name like below after you have called this method
        // returnObject = (className) returnObject;
        String fileLocation;
        if(cardNameOrFileLocation.contains(".ser"))
            fileLocation = cardNameOrFileLocation;
        else
            fileLocation= Main_config_file.returnCardSaveDataLocation(cardNameOrFileLocation);
        Cards.card returnObject = null;
        try {
            FileInputStream file = new FileInputStream(fileLocation);
            ObjectInputStream in = null;
            in = new ObjectInputStream(file);
            // Method for deserialization of object
            returnObject = (Cards.card)in.readObject();
            Random random = new Random(System.nanoTime());
            returnObject.setRandomField(random.nextInt());
            in.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }
        return returnObject;
    }

    public static Cards.minion minionDeserialize(String cardNameOrFileLocation){
        // You must put the object name like below after you have called this method
        // returnObject = (className) returnObject;
        String fileLocation;
        if(cardNameOrFileLocation.contains(".ser"))
            fileLocation = cardNameOrFileLocation;
        else
            fileLocation= Main_config_file.returnCardSaveDataLocation(cardNameOrFileLocation);
        Cards.minion returnObject = null;
        try {
            FileInputStream file = new FileInputStream(fileLocation);
            ObjectInputStream in = null;
            in = new ObjectInputStream(file);
            // Method for deserialization of object
            returnObject = (Cards.minion)in.readObject();
            Random random = new Random(System.nanoTime());
            returnObject.setRandomField(random.nextInt());
            in.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }
        return returnObject;
    }

    public static Cards.spell spellDeserialize(String cardNameOrFileLocation){
        // You must put the object name like below after you have called this method
        // returnObject = (className) returnObject;
        String fileLocation;
        if(cardNameOrFileLocation.contains(".ser"))
            fileLocation = cardNameOrFileLocation;
        else
            fileLocation= Main_config_file.returnCardSaveDataLocation(cardNameOrFileLocation);
        Cards.spell returnObject = null;
        try {
            FileInputStream file = new FileInputStream(fileLocation);
            ObjectInputStream in = null;
            in = new ObjectInputStream(file);
            // Method for deserialization of object
            returnObject = (Cards.spell)in.readObject();
            Random random = new Random(System.nanoTime());
            returnObject.setRandomField(random.nextInt());
            in.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }
        return returnObject;
    }

    public static Cards.weapon weaponDeserialize(String cardNameOrFileLocation){
        // You must put the object name like below after you have called this method
        // returnObject = (className) returnObject;
        String fileLocation;
        if(cardNameOrFileLocation.contains(".ser"))
            fileLocation = cardNameOrFileLocation;
        else
            fileLocation= Main_config_file.returnCardSaveDataLocation(cardNameOrFileLocation);
        Cards.weapon returnObject = null;
        try {
            FileInputStream file = new FileInputStream(fileLocation);
            ObjectInputStream in = null;
            in = new ObjectInputStream(file);
            // Method for deserialization of object
            returnObject = (Cards.weapon)in.readObject();
            Random random = new Random(System.nanoTime());
            returnObject.setRandomField(random.nextInt());
            in.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }
        return returnObject;
    }

    public static Cards.quest questDeserialize(String cardNameOrFileLocation){
        // You must put the object name like below after you have called this method
        // returnObject = (className) returnObject;
        String fileLocation;
        if(cardNameOrFileLocation.contains(".ser"))
            fileLocation = cardNameOrFileLocation;
        else
            fileLocation= Main_config_file.returnCardSaveDataLocation(cardNameOrFileLocation);
        Cards.quest returnObject = null;
        try {
            FileInputStream file = new FileInputStream(fileLocation);
            ObjectInputStream in = null;
            in = new ObjectInputStream(file);
            // Method for deserialization of object
            returnObject = (Cards.quest)in.readObject();
            in.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }
        return returnObject;
    }

}
