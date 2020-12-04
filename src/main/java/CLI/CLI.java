//package CLI;
//
//import Card.InitiateCards;
//import Configurations.Main_config_file;
//import LoggingModule.LoggingClass;
//import User.PasswordUsername;
//import User.User;
//import User.createUser;
//import Utility.ClosestMatch;
//
//import java.util.Scanner;
//import java.util.Stack;
//import java.util.logging.Logger;
//
//import static ColorCommandLine.ColorCommandLine.CYAN_BOLD;
//import static ColorCommandLine.ColorCommandLine.RESET;
//import static LoggingModule.LoggingClass.*;
//import static LoggingModule.LoggingClass.logUser;
//import static User.DeleteUser.deleteUser;
//import static User.PasswordUsername.ChangePassword;
//import static User.PasswordUsername.PasswordValidityCheck;
//import static User.UserFunctions.userLogIn;
//import static User.UserFunctions.userLogOut;
//
//public class CLI {
//    enum locations{
//        preGameEntry, gameEntry, createUser, userPanel, store, collectionsAndDeck, hero,
//        cardFabricationAndEnhancement, userSettings, wheelOfFortune, enterCredentials
//    }
//    private static Logger main_logger;
//    private static String username; // Logged in as.
//
//    static User currentUser = null, cloneUser = null;
//
//    public static boolean inputGeneralCommandHandling(String command, Stack<locations> followedPath){
//        /*
//        The return value of this function determines whether or not the code should use a break
//        in the switch case or rather just continue on.
//         */
//        if (command.equals("Hearthstone help")) {
//            if (logUserIsOpen()) {
//                logUser("Command: Hearthstone help");
//            }
//            Commands.printAllCommands();
//            return true;
//        } else if (command.equals("help")) {
//            if (logUserIsOpen()){
//                logUser("Command: help");
//            }
//            Commands.printValidCommands(followedPath.peek());
//            return true;
//        }else if (command.equals("exit -a")) {
//            if (followedPath.peek().equals(locations.collectionsAndDeck) &&
//                    cloneUser != null && cloneUser.currentNumberOfCardsInDeck() < cloneUser.getDeckSize()){
//                main_logger.info(CYAN_BOLD + "Error." + RESET + " You cannot exit the collection section until you complete your deck");
//                logUser("Command: exit -a\n Error. Deck not at full capacity.");
//                return true;
//            } else if (followedPath.peek().equals(locations.collectionsAndDeck) &&
//                    cloneUser != null && cloneUser.currentNumberOfCardsInDeck() == cloneUser.getDeckSize()){
//                main_logger.info("Deck cards OK.");
//                cloneUser = null;
//            }
//            logUser("Command: exit -a");
//            inputGeneralCommandHandling("exit", followedPath);
//            followedPath.pop();
//            return true;
//        } else if (command.equals("exit")) {
//            if (followedPath.peek().equals(locations.collectionsAndDeck) &&
//                    cloneUser != null && cloneUser.currentNumberOfCardsInDeck() < cloneUser.getDeckSize()){
//                main_logger.info(CYAN_BOLD + "Error." + RESET + " You cannot exit the collection section until you complete your deck");
//                logUser("Command: exit\n Error. Deck not at full capacity.");
//                return true;
//            } else if (followedPath.peek().equals(locations.collectionsAndDeck) &&
//                    cloneUser != null && cloneUser.currentNumberOfCardsInDeck() == cloneUser.getDeckSize()){
//                main_logger.info("Deck cards OK.");
//                cloneUser = null;
//            }
//            logUser("Command: exit");
//            cloneUser = null;
//            if (currentUser != null)
//                currentUser = userLogOut(currentUser);
//            for (int i = followedPath.size(); i>2; i--){
//                followedPath.pop();
//            }
//            return true;
//        } else if(command.equals("back")){
//            if (followedPath.peek().equals(locations.userPanel))
//                return inputGeneralCommandHandling("exit", followedPath);
//            if (followedPath.peek().equals(locations.enterCredentials)){
//                followedPath.pop();
//                main_logger.info("Command: back\nNavigate: Game entry");
//                return true;
//            } if (followedPath.peek().equals(locations.createUser)){
//                followedPath.pop();
//                main_logger.info("Command: back\nNavigate: Game entry");
//                return true;
//            }
//            if (followedPath.peek().equals(locations.collectionsAndDeck) &&
//                    cloneUser != null && cloneUser.currentNumberOfCardsInDeck() < cloneUser.getDeckSize()) {
//                main_logger.info(CYAN_BOLD + "Error." + RESET + " You cannot exit the collection section until you complete your deck");
//                logUser("Command: back\n Error. Deck not at full capacity.");
//                return true;
//            } else if (followedPath.peek().equals(locations.collectionsAndDeck) &&
//                    cloneUser != null && cloneUser.currentNumberOfCardsInDeck() == cloneUser.getDeckSize()){
//                main_logger.info("Deck cards OK.");
//                cloneUser = null;
//            }
//            // ADD IT TO HERE. ALSO ADD A MECHANISM IN THE ABOVE ONES THAT CHECKS CLONEUSER IS NULL AT EVERY PLACE.
//            followedPath.pop();
//            logUser("Command: back\n Navigate " + followedPath.peek());
//            return true;
//        } else {
//            return false;
//        }
//    }
//    static String  removeRedundantSpace(String st){
//        return st.replaceAll("^[ \t]+|[ \t]+$", "");
//    }
//    static String readALine(Scanner scanner, Stack<locations> followedPath, boolean []shouldBreak){
//        System.out.println("Enter your command:");
//        String command = scanner.nextLine();
//        command = removeRedundantSpace(command);
//        shouldBreak[0] = inputGeneralCommandHandling(command, followedPath);
//        return command;
//    }
//
//
//    static void invalidCommandPrint(String commandToHandle, Stack<locations> followedPath){
//        if (logUserIsOpen()) logUser("Invalid command. Try again.");
//        else System.out.println("Invalid command. Try again.");
//        System.out.println("Entered: " +commandToHandle + "\nPerhaps you meant: " + CYAN_BOLD +  ClosestMatch.getClosestMatch(
//                commandToHandle, Commands.getCurrentLevelCommands(followedPath.peek())) + RESET);
//        String [] splittedCommand = commandToHandle.split(" ");
//        if (splittedCommand.length > 1)
//        System.out.println("Or Perhaps you meant: " + CYAN_BOLD + ClosestMatch.getClosestMatch(
//                splittedCommand[0], Commands.getCurrentLevelCommands(followedPath.peek())) + RESET);
//    }
//    static void detailACard(String commandToHandle){
//        String cardName = removeRedundantSpace(commandToHandle.substring(6));
//        try {
//            logUser("Command: detail " + cardName);
//            currentUser.printCardInformation(cardName);
//        } catch (Exception e){
//            logUser("Command: detail + " +cardName + "\nError. Card name incorrect.");
//            System.out.println("Perhaps you meant: " + CYAN_BOLD + ClosestMatch.getClosestMatch(
//                    cardName, currentUser.getAllCards()) + RESET);
//        }
//    }
//
//    public static void main(String[] args) {
//        boolean flag = Main_config_file.createRequiredDirectories();
//        if(!flag){
//            System.out.println(CYAN_BOLD + "Error." + RESET + " Could not create the directories");
//            return;
//        }
//        main_logger =  LoggingClass.getMainLoggerInstance();
//        Scanner scanner = new Scanner(System.in);
//        String commandToHandle;
//        main_logger.info("Should the card objects be instantiated? Enter " + CYAN_BOLD + "y" + RESET  +
//                "if this is a first run or if the cards have changed.\n" +
//                " Else, enter " + CYAN_BOLD + "n" + RESET + ":");
//        commandToHandle = scanner.nextLine();
//        if (commandToHandle.equals("y") || commandToHandle.equals("Y")
//                || commandToHandle.equals("Yes") || commandToHandle.equals("yes")){
//            InitiateCards.InstantiateAllCards();
//            main_logger.info("Instantiated all cards");
//        }
//        commandToHandle = "help";
//        Stack<locations> followedPath = new Stack<>();
//        followedPath.push(locations.preGameEntry);
//        followedPath.push(locations.gameEntry);
//        String  password;
//        boolean []shouldBreak = {false};
//
//
//        System.out.println("\t\t\t\t\tWelcome to my Hearthstone." +
//                "\n Sorry for the inconvenience that you are forced to work" +
//                "with this petty command line\n rather than a beautiful graphic user interface. Still, we must work with" +
//                "what we have.\nTo see the system help, enter \"Hearthstone help\". To exit the game in any stage, " +
//                "enter \"exit -a\". To log out of your user, enter \"exit\"." +
//                "\nIf you already have an account, enter " + CYAN_BOLD + "y" + RESET + ", otherwise enter "  + CYAN_BOLD + "n" + RESET + ":");
//
//        try {
//            outer: while (true) {
//                inner: switch (followedPath.peek()){
//                    case preGameEntry:
//                        main_logger.info(CYAN_BOLD + "Pre Game Entry:" + RESET);
//                        currentUser = null;
//                        break outer;
//                    case gameEntry:
//                        cloneUser = null;
//                        main_logger.info(CYAN_BOLD + "Game Entry:" + RESET);
//                        currentUser = null;
//                        commandToHandle = readALine(scanner, followedPath, shouldBreak);
//                        if (shouldBreak[0]) break;
//                        if (commandToHandle.equals("n") || commandToHandle.equals("no")
//                                || commandToHandle.equals("No") || commandToHandle.equals("N")){
//                            followedPath.push(locations.createUser);
//                        }
//                        else if (commandToHandle.equals("y") || commandToHandle.equals("Y")
//                                || commandToHandle.equals("Yes") || commandToHandle.equals("yes")){
//                            followedPath.push(locations.enterCredentials);
//                        } else {
//                            invalidCommandPrint(commandToHandle, followedPath);
//                        }
//                        break;
//
//                    case enterCredentials:
//                        main_logger.info(CYAN_BOLD + "Enter your credentials" + RESET);
//                        currentUser = null;
//                        System.out.println("Username:");
//                        username = readALine(scanner, followedPath, shouldBreak);
//                        if (shouldBreak[0]) break;
//                        System.out.println("Password:");
//                        password = readALine(scanner, followedPath, shouldBreak);
//                        if (shouldBreak[0]) break;
//                        currentUser = userLogIn(username, password);
//                        if (currentUser != null) {
//                            main_logger.info("User log in successful. Welcome");
//                            followedPath.pop();
//                            followedPath.push(locations.userPanel);
//
//                        } else {
//                            System.out.println(CYAN_BOLD + "Log in unsuccessful. Try again." + RESET);
//                            break;
//                        }
//                        break;
//                    case createUser:
//                        main_logger.info(CYAN_BOLD + "Create user:" + RESET);
//                        currentUser = null;
//                        System.out.println(PasswordUsername.passwordFormat);
//                        System.out.println("Username:");
//                        username = readALine(scanner, followedPath, shouldBreak);
//                        if (shouldBreak[0]) break;
//                        boolean usernameNotTaken = PasswordUsername.CheckUsernameValidity(username);
//                        if (!usernameNotTaken) {
//                            break;
//                        }
//                        System.out.println("Password:");
//                        password = readALine(scanner, followedPath, shouldBreak);
//                        if (shouldBreak[0]) break;
//                        boolean passwordFormatCheck = false;
//                        while (!passwordFormatCheck) {
//                            passwordFormatCheck = PasswordUsername.PasswordFormatCheck(password);
//                            if (passwordFormatCheck) {
//                                followedPath.pop();
//                                followedPath.push(locations.userPanel);
//                                currentUser = createUser.createAUser(username, password);
//                                System.out.println("Password accepted. Welcome to my Hearthstone.");
//                                openUserLogger(currentUser.getUsername(), currentUser.getUserID());
//                                break;
//                            }
//                            password = readALine(scanner, followedPath, shouldBreak);
//                            if (shouldBreak[0]) break;
//                        }
//                        logUser("The current Balance of the user " + currentUser.getWalletBalance());
//                        logUser("Your current hero: " + currentUser.getCurrentHeroName() +
//                                " with hero level: " + currentUser.getHeroLevel());
//                        logUser("The cards in your heroes deck:\n"
//                                + currentUser.getCardsInDeckString());
//
//                        while (true) {
//                            System.out.println("If you wish to see the details of your current cards, enter "  + CYAN_BOLD + "y" + RESET + ", otherwise," +
//                                    " enter "  + CYAN_BOLD + "n" + RESET + " to continue to the user panel");
//                            commandToHandle = scanner.nextLine();
//                            if (commandToHandle.equals("n") || commandToHandle.equals("no")
//                                    || commandToHandle.equals("No") || commandToHandle.equals("N")){
//                                break;
//                            }
//                            else if (commandToHandle.equals("y") || commandToHandle.equals("Y")
//                                    || commandToHandle.equals("Yes") || commandToHandle.equals("yes")){
//                                logUser("Command: view deck cards after user creation.");
//                                currentUser.printDeckCardsWithDetails();
//                                break;
//                            } else {
//                                System.out.println("Invalid input. Try again. Enter " + CYAN_BOLD + "n" + RESET +
//                                        " for No and " + CYAN_BOLD + "y" + RESET + " for Yes.");
//                            }
//                        }
//                        break;
//                    case userPanel:
//                        cloneUser = null;
//                        logUser("User panel:");
//                        System.out.println("Welcome to the user panel. From here you can access the store,\n " +
//                                "your collections, user settings, hero settings, card manipulation " +
//                                "and in future versions, the arena. Enter your command:");
//
//                        commandToHandle = readALine(scanner, followedPath, shouldBreak);
//                        if(shouldBreak[0]) break;
//                        switch (commandToHandle){
//                            case "store":
//                                logUser("Navigation: store");
//                                followedPath.push(locations.store);
//                                break;
//                            case "collection":
//                                logUser("Navigation: collection");
//                                followedPath.push(locations.collectionsAndDeck);
//                                break;
//                            case "hero":
//                                logUser("Navigation: hero");
//                                followedPath.push(locations.hero);
//                                break;
//                            case "cardEnhFab":
//                                logUser("Navigation: cardEnhFab");
//                                followedPath.push(locations.cardFabricationAndEnhancement);
//                                break;
//                            case "userSet":
//                                logUser("Navigation: userSet");
//                                followedPath.push(locations.userSettings);
//                                break;
//                            case "wheelOfFortune":
//                                logUser("Navigation: wheelOfFortune");
//                                followedPath.push(locations.wheelOfFortune);
//                                break;
//                            case "play":
//                                logUser("This section has not yet been implemented.");
//                                break;
//                            default:
//                                invalidCommandPrint(commandToHandle, followedPath);
//                        }
//                        break ;
//                    case store:
//                        logUser("Store:");
//                        commandToHandle = readALine(scanner, followedPath, shouldBreak);
//                        if(shouldBreak[0]) break;
//                        switch (commandToHandle){
//                            case "wallet":
//                                logUser("Command: wallet");
//                                logUser("The current wallet balance is: " + currentUser.getWalletBalance());
//                                break inner;
//                            case "listBuy":
//                                logUser("Command: listBuy");
//                                logUser("The cards that you can buy: \n"
//                                        + currentUser.getComplementAvailableCards());
//                                break inner;
//                            case "listSell":
//                                logUser("Command: listSell");
//                                logUser("The cards that you can sell. Heed attention to the fact that\n " +
//                                        "if a card is currently in your deck, you cannot sell it.");
//                                logUser(currentUser.getAvailableCards().toString());
//                                break inner;
//                            case "ls":
//                                logUser("Command: ls");
//                                logUser("The purchasable cards:\n" + currentUser.getComplementAvailableCards());
//                                logUser("The cards that you can sell:\n" + currentUser.getAvailableCards());
//                                break inner;
//                        }
//                        if(commandToHandle.contains("buySingle")){
//                            String cardName = removeRedundantSpace(commandToHandle.substring(9));
//
//                            int result = currentUser.buyCard(cardName);
//                            switch (result){
//                                case 404:
//                                    logUser("Command: buySingle " + cardName +
//                                            "\nError. Card name is wrong");
//
//                                    System.out.println("Entered: " +commandToHandle + "\nPerhaps you meant: " + CYAN_BOLD + ClosestMatch.getClosestMatch(
//                                            cardName, currentUser.getAllCards()) + RESET);
//                                    break inner;
//                                case 402:
//                                    logUser("Command: buySingle " + cardName +
//                                            "\nError. Insufficient funds");
//                                    break inner;
//                                case 400:
//                                    logUser("Command: buySingle " + cardName +
//                                            "\nError. The card is already in your collection. You high?");
//                                    break inner;
//                                case 200:
//                                    logUser("Command: buySingle " + cardName +
//                                            "\nPurchase successful.");
//                                    currentUser.serializeUser();
//                                    break inner;
//                                default:
//                                    logUser("buyCard returned unexpected result. Exiting");
//                                    followedPath.push(locations.preGameEntry);
//                                    break inner;
//                            }
//                        } else if (commandToHandle.contains("sell")){
//                            String cardName = removeRedundantSpace(commandToHandle.substring(4));
//
//                            int result = currentUser.sellCard(cardName);
//                            switch (result){
//                                case 404:
//                                    logUser("Command: sell " + cardName +
//                                            "\nError. Card name is wrong");
//                                    System.out.println("Entered: " +commandToHandle + "\nPerhaps you meant: " + CYAN_BOLD + ClosestMatch.getClosestMatch(
//                                            cardName, currentUser.getAllCards()) + RESET);
//                                    break inner;
//                                case 403:
//                                    logUser("Command: sell " + cardName +
//                                            "\nError. The card is currently in your deck." +
//                                            " First remove it from your deck.");
//                                    break inner;
//                                case 401:
//                                    logUser("Command: sell " + cardName +
//                                            "\nError. You do not own this card.");
//                                    break inner;
//                                case 200:
//                                    logUser("Command: sell " + cardName +
//                                            "\nTransaction Successful");
//                                    currentUser.serializeUser();
//                                    break inner;
//                                default:
//                                    logUser("sellCard returned unexpected result. Exiting");
//                                    followedPath.push(locations.preGameEntry);
//                                    break inner;
//                            }
//                        } else if (commandToHandle.contains("detail")) {
//                            detailACard(commandToHandle);
//                        }else if (commandToHandle.contains("buyPack")){
//                            logUser("Command: buyPack");
//                            System.out.println("This section has not yet been implemented.");
//                            //currentUser.serializeUser();
//                            break ;
//
//                        } else {
//                            invalidCommandPrint(commandToHandle, followedPath);
//                        }
//                        break ;
//                    case collectionsAndDeck:
//                        logUser("Collection:");
//                        System.out.println("Welcome to the collections. Here you can edit your deck.\n " +
//                                "Your deck has a limit of " + currentUser.getDeckSize() + " and it cannot in any way " +
//                                "exceed this amount.\nThus to add a card to your deck, you must first make an " +
//                                "empty slot.\nTo ensure that your deck stays full, your changes will only be " +
//                                "executed every time the deck is at its full capacity");
//                        commandToHandle = readALine(scanner, followedPath, shouldBreak);
//                        if(shouldBreak[0]) break;
//                        // PAY ATTENTION TO THE LINE BELOW IN FUTURE CHAGNES.
//                        if (cloneUser == null) {
//                            try {
//                                cloneUser = currentUser.clone();
//                            } catch (CloneNotSupportedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        switch (commandToHandle){
//                            case "listDeck":
//                                logUser("Command: listDeck");
//                                logUser(cloneUser.getCardsInDeckString());
//                                break inner;
//                            case "listAll":
//                                logUser("Command: listAll");
//                                cloneUser.printAvailableCards();
//                                break inner;
//                            case "listAddable":
//                                logUser("Command: listAddable");
//                                logUser(cloneUser.getAddableCards().toString());
//                                break inner;
//                            case "ls":
//                                logUser("Command: ls");
//                                logUser("Printing all available cards.");
//                                cloneUser.printAvailableCards();
//                                break inner;
//                        }
//                        if (commandToHandle.contains("detail")){
//                            detailACard(commandToHandle);
//                        } else if(commandToHandle.contains("add")){
//                            String cardName = removeRedundantSpace(commandToHandle.substring(3));
//                            int result = cloneUser.addToDeck(cardName);
//                            switch (result) {
//                                case 404:
//                                    logUser("Command: add " + cardName +
//                                            "\nError. Card name is wrong");
//                                    System.out.println("Entered: " +commandToHandle + "\nPerhaps you meant: " + CYAN_BOLD +ClosestMatch.getClosestMatch(
//                                            cardName, currentUser.getAllCards()) + RESET + "\n");
//                                    break inner;
//                                case 401:
//                                    logUser("Command: add " + cardName + "\nError. You do not own this card.");
//                                    break inner;
//                                case 409:
//                                    logUser("Command: add " + cardName + "\nError. The deck is at full capacity.");
//                                    break inner;
//                                case 403:
//                                    logUser("Command: add " + cardName + "\nError. Maximum instances of card in deck reached.");
//                                    break inner;
//                                case 200:
//                                    logUser("Command: add " + cardName + "\nSuccessful.");
//                                    if (cloneUser.currentNumberOfCardsInDeck() == cloneUser.getDeckSize()) {
//                                        cloneUser.serializeUser();
//                                        currentUser = User.deserializeUser(currentUser.getUsername(), currentUser.getUserID());
//                                    }
//                                    break inner;
//                                default:
//                                    logUser("add returned unexpected result. Exiting");
//                                    followedPath.push(locations.preGameEntry);
//                                    break inner;
//                            }
//
//                        } else if(commandToHandle.contains("remove")){
//                            String cardName = removeRedundantSpace(commandToHandle.substring(6));
//                            int result = cloneUser.removeFromDeck(cardName);
//                            switch (result){
//                                case 404:
//                                    logUser("Command: remove " + cardName +
//                                            "\nError. Card name is wrong");
//                                    System.out.println("Entered: " +commandToHandle + "\nPerhaps you meant: " + CYAN_BOLD + ClosestMatch.getClosestMatch(
//                                            cardName, currentUser.getAllCards()) + RESET);
//                                    break inner;
//                                case 401:
//                                    logUser("Command: remove " + cardName + "\nError. You do not own this card.");
//                                    break inner;
//                                case 406:
//                                    logUser("Command: remove " + cardName + "\nError. The deck is empty.");
//                                    break inner;
//                                case 403:
//                                    logUser("Command: remove " + cardName + "\nError. No instances of card in deck.");
//                                    break inner;
//                                case 200:
//                                    logUser("Command: remove " + cardName + "\nSuccessful. Don't forget to add a card.");
//                                    break inner;
//                                default:
//                                    logUser("remove returned unexpected result. Exiting");
//                                    followedPath.push(locations.preGameEntry);
//                                    break inner;
//                            }
//                        } else {
//                            invalidCommandPrint(commandToHandle, followedPath);
//                        }
//
//                        break ;
//                    case hero:
//                        logUser("Hero:");
//                        commandToHandle = readALine(scanner, followedPath, shouldBreak);
//                        if(shouldBreak[0]) break;
//                        switch (commandToHandle){
//                            case "upHero":
//                                logUser("Command: upHero");
//                                logUser("This section has not yet been implemented.");
//                                break inner;
//                            case "ls":
//                                logUser("Command: ls");
//                                currentUser.printAllHeroesInformation();
//                                logUser("Current hero: " + currentUser.getCurrentHeroName());
//                                break inner;
//                        }
//                        if (commandToHandle.contains("chHero")){
//                            String heroName = removeRedundantSpace(commandToHandle.substring(6));
//                            int result = currentUser.changeHero(heroName);
//                            switch (result){
//                                case 404:
//                                    logUser("Command chHero " + heroName + "\nError. Hero name incorrect.");
//                                    System.out.println("Entered: " +commandToHandle + "\nPerhaps you meant: " + CYAN_BOLD + ClosestMatch.getClosestMatch(
//                                            heroName, currentUser.getHeroNames()) + RESET);
//                                    break inner;
//                                case 400:
//                                    logUser("Command chHero " + heroName + "\nError. Hero already selected.");
//                                    break inner;
//                                case 200:
//                                    logUser("Command chHero " + heroName + "\nSuccessful.");
//                                    currentUser.serializeUser();
//                                    break inner;
//                                default:
//                                    logUser("chHero returned unexpected result. Exiting");
//                                    followedPath.push(locations.preGameEntry);
//                                    break inner;
//                            }
//                        } else {
//                            invalidCommandPrint(commandToHandle, followedPath);
//                        }
//                        break;
//                    case userSettings:
//                        logUser("User settings:");
//                        commandToHandle = readALine(scanner, followedPath, shouldBreak);
//                        if(shouldBreak[0]) break;
//                        String passCheckResult;
//                        switch (commandToHandle){
//                            case "chPass":
//                                System.out.println("Enter the current password: ");
//                                String currentPassword = readALine(scanner, followedPath, shouldBreak);
//                                if(shouldBreak[0]) break inner;
//                                passCheckResult = PasswordValidityCheck(currentUser.getUsername(),
//                                        currentUser.getUserID(), currentPassword);
//                                switch (passCheckResult){
//                                    case "Correct":
//                                        break; // this is correct. must not break inner.
//                                    case "Incorrect":
//                                        logUser("Command: chPass\nError. Password incorrect.");
//                                        break inner;
//                                    default:
//                                        logUser("Password check returned unexpected result. Exiting");
//                                        followedPath.push(locations.preGameEntry);
//                                        break inner;
//                                }
//                                System.out.println("Enter the new password: ");
//                                String newPasswrod = readALine(scanner, followedPath, shouldBreak);
//                                if(shouldBreak[0]) break inner;
//                                int result = ChangePassword(currentUser.getUsername(), currentUser.getUserID(),
//                                        currentPassword, newPasswrod, currentUser);
//                                switch (result){
//                                    case 405:
//                                        logUser("chPass \n Error. New password format is incorrect.");
//                                        break inner;
//                                    case 200:
//                                        logUser("chPass\nSuccessful.");
//                                        currentUser.serializeUser();
//                                        break inner;
//                                    default:
//                                        logUser("chPass\n ChangePassword function returned unexpected result. Exiting");
//                                        followedPath.push(locations.preGameEntry);
//                                }
//                                break inner;
//                            case "deleteUser":
//                                System.out.println("Enter your password:");
//                                String yourPassword = readALine(scanner, followedPath, shouldBreak);
//                                if(shouldBreak[0]) break inner;
//                                passCheckResult = PasswordValidityCheck(currentUser.getUsername(),
//                                        currentUser.getUserID(), yourPassword);
//                                switch (passCheckResult){
//                                    case "Correct":
//                                        break; // this is correct. must not break inner.
//                                    case "Incorrect":
//                                        logUser("Command: deleteUser\nError. Password incorrect.");
//                                        break inner;
//                                    default:
//                                        logUser("Password check returned unexpected result. Exiting");
//                                        followedPath.push(locations.preGameEntry);
//                                        break inner;
//                                }
//                                // RETURN VALUE
//                                deleteUser(currentUser.getUsername(), currentUser.getUserID());
//                                logUser("Command: deleteUser\nSuccessful. Log in again to continue.");
//                                currentUser.serializeUser();
//                                logUser("Command: exit");
//                                currentUser = userLogOut(currentUser);
//                                for (int i = followedPath.size(); i>2; i--){
//                                    followedPath.pop();
//                                }
//                                break inner;
//                            default:
//                                invalidCommandPrint(commandToHandle, followedPath);
//                        }
//                        break;
//                    case cardFabricationAndEnhancement:
//                        logUser("Card enhancement and fabrication:");
//                        System.out.println("This section has not yet been implemented");
//                        followedPath.pop();
//                        break ;
//                    case wheelOfFortune:
//                        logUser("Wheel of Fortune:");
//                        System.out.println("This section has not yet been implemented.");
//                        followedPath.pop();
//                        break ;
//                }
//
//            }
//        } catch (Exception e){
//            e.getStackTrace();
//            main_logger.info(e.getMessage());
//        } finally {
//            // Serialize USER if it is open
//            main_logger.info("Exiting");
//
//            LoggingClass.closeMainLogger();
//        }
//    }
//}
