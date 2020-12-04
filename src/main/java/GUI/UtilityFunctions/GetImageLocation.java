package GUI.UtilityFunctions;

public class GetImageLocation {
    public static String getCardImageLocation(String cardName){
        String imageFileName = "";
        String baseLocation = "./assets/";
        imageFileName = cardName + ".png";
//        switch (cardName){
//            case "Arcane Missiles":
//                imageFileName = "Arcane Missiles.png";
//                break;
//        }
        return baseLocation + imageFileName;
    }
    public static String getHeroImageForDeckBackGroundLocation(String heroName){
        String imageFileLocation = heroName.toLowerCase() + "DeckBackground.jpg";
        String baseLocation = "./assets/";
        return baseLocation + imageFileLocation;
    }

    public static String getHeroImageForArena(String heroName){
        String imageFileLocation = heroName + ".png";
        String baseLocation = "./assets/";
        return baseLocation + imageFileLocation;
    }

}
