//package CLI;
//
//import java.util.*;
//
//import static ColorCommandLine.ColorCommandLine.*;
//
//public class Commands {
//    /*
//    // singleton class
//    private final static Commands instance = new Commands();
//
//    private Commands(){}
//    public static Commands getInstance(){
//        return instance;
//    }
//    */
//    private static Hashtable<CLI.locations, String[]> validCommands = new Hashtable<CLI.locations, String[]>();
//    private static Hashtable<String, String > help = new Hashtable<>();
//    static {
//        // HANDLE "HEARTHSTONE HELP" AND "EXIT -A".
//        //validCommands.put(CLI.locations.preGameEntry, new String[]{""});
//        validCommands.put(CLI.locations.enterCredentials, new String[]{"back"});
//        validCommands.put(CLI.locations.gameEntry, new String[]{"y", "n", "back"});
//        validCommands.put(CLI.locations.createUser, new String[]{"back", "user-pass -help"});
//        validCommands.put(CLI.locations.userPanel, new String[]{"back", "exit", "store", "collection", "hero",
//                "cardEnhFab", "userSet", "play", "wheelOfFortune"});
//        validCommands.put(CLI.locations.hero, new String[]{"chHero", "upHero", "ls", "back", "exit"});
//        validCommands.put(CLI.locations.wheelOfFortune, new String[]{"roll", "back", "exit"});
//        validCommands.put(CLI.locations.userSettings, new String[]{"chPass", "exportData", "importData", "deleteUser",
//                "back", "exit"});
//        validCommands.put(CLI.locations.store, new String[]{"buySingle", "buyPack", "detail", "listBuy", "listSell",
//                "ls", "wallet", "sell", "back", "exit"});
//        validCommands.put(CLI.locations.collectionsAndDeck, new String[]{"add", "remove", "detail", "listDeck",
//                "listAll", "listAddable", "ls", "back", "exit"});
//        validCommands.put(CLI.locations.cardFabricationAndEnhancement, new String[]{"create", "enhance"});
//        // Help object:
//        String user_panel_run_only = "Command can only be run whilst in the user panel.";
//        String provide_card_name = "Provide the card name after the command.";
//
//        help.put("back", "Go back one step");
//        help.put("exit", "Exit current user and go to sign in/up page.");
//        help.put("exit -a", "Exit game.");
//        help.put("y", "Indicating you already have an account and want to sign in.");
//        help.put("n", "Indicating you don't have an account and want to sign up.");
//        help.put("user-pass -help", "Displays the guidelines regarding username and/or password formats.");
//        help.put("store", "Enter the store. " + user_panel_run_only);
//        help.put("collection", "Enter the collection and deck section. " + user_panel_run_only);
//        help.put("hero", "Enter the hero section. " + user_panel_run_only);
//        help.put("cardEnhFab", "Enter the card enhancement and fabrication section. " + user_panel_run_only);
//        help.put("userSet", "Enter the user settings section. " + user_panel_run_only);
//        help.put("play", "Enter the playground. " + user_panel_run_only);
//        help.put("wheelOfFortune", "Try your chances in the wheel of fortune. " + user_panel_run_only);
//        help.put("chHero", "Change your current hero. Provide the hero name after the command.");
//        help.put("upHero", "Upgrade your current hero.");
//
//        help.put("listBuy", "list the purchaseable cards");
//        help.put("listSell", "list the cards that can be sold.");
//        help.put("ls", "list contents.");
//        help.put("detail", "Print the details of a given card.");
//
//        help.put("listAll", "List all of available cards.");
//        help.put("listDeck", "List the cards currently in you deck");
//        help.put("listAddable", "List cards that are available buy out of your deck and thus " +
//                "can be added to your deck.");
//
//
//
//        help.put("roll", "If valid, it will roll the wheel of fortune. " +
//                "If not, it will show when you can roll the wheel.");
//        help.put("chPass", "Change password of the current user.");
//        help.put("deleteUser", "Delete the current user without exporting the data.");
//        help.put("exportData", "Export the current user's data.");
//        help.put("importData", "Import user data.");
//        help.put("buySingle", "Buy a single card in the store. " + provide_card_name);
//        help.put("buyPack", "Buy a pack of cards. " + provide_card_name);
//        help.put("wallet", "Show wallet balance.");
//        help.put("sell", "sell a card. " + provide_card_name);
//        help.put("add", "Add a card to your deck. " + provide_card_name);
//        help.put("remove", "Remove a card from your deck. " + provide_card_name);
//        help.put("create", "Create a card.");
//        help.put("enhance", "Enhance a card through spending gold. " + provide_card_name);
//
//    }
//    public static ArrayList<String> getCurrentLevelCommands(CLI.locations location){
//        ArrayList<String> array = new ArrayList<>();
//        Collections.addAll(array, validCommands.get(location));
//        return array;
//    }
//    public static void printAllCommands(){
//        System.out.println("Hearthstone help:");
//        for(CLI.locations myIterator: CLI.locations.values()){
//            if (myIterator.equals(CLI.locations.preGameEntry)) continue;
//            System.out.println("\n\n" + RED +  myIterator + RESET + ":");
//            printValidCommands(myIterator);
//        }
//    }
//    public static void printValidCommandsInThisLevel(CLI.locations location){
//        System.out.println("The valid commands in this level:");
//        printValidCommands(location);
//    }
//    public static void  printValidCommands(CLI.locations location){
//        String [] commands = validCommands.get(location);
//        String helpString;
//        for(String str:commands){
//            helpString = help.get(str);
//            if (helpString.charAt(0) == '-') continue;
//            System.out.println(CYAN + str + RESET + ":\t\t\t\t" + helpString);
//        }
//    }
//}
