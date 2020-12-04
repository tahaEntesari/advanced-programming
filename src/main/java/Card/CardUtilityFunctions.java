package Card;

import Configurations.Main_config_file;

import java.io.File;

public class CardUtilityFunctions {
    public static boolean cardExists(String cardName){
        String fileLocation = Main_config_file.returnCardSaveDataLocation(cardName);
        File tempFile = new File(fileLocation);
        return tempFile.exists();
    }
    public static int getCardPrice(String cardName){
        String fileLocation = Main_config_file.returnCardSaveDataLocation(cardName);
        Cards.card temp = Utility.SerializationFunctions.cardDeserialize(fileLocation);
        return temp.getPrice();
    }
    public static Cards.card getCardObjectFromName(String cardName){
        return Utility.SerializationFunctions.cardDeserialize(Main_config_file.returnCardSaveDataLocation(cardName));
    }
}
