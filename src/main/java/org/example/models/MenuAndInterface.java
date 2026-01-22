package org.example.models;

public class MenuAndInterface {

    public static boolean checkChoiceMenu(String choice) {
        return !choice.toUpperCase().contains("LOG") && !choice.toUpperCase().contains("SIG")
                && !choice.toUpperCase().contains("EXI");
    }

    public static boolean checkChoiceLoggedMenu(String choice) {
        return !choice.toUpperCase().contains("VIEW") && !choice.toUpperCase().contains("RESET")
                && !choice.toUpperCase().contains("EXI");
    }

    public static int menu (){
        String choose;
        int returnMenu = 0;
        printCenteredMessage("Menu Page");

        do {
            IO.print("\tSelect:\n\t- Log -> Login\n\t- Sig -> Sign-in\n\t- Exi -> Exit\n\n\tEnter your choice: ");
            choose = IO.readln();

            if (checkChoiceMenu(choose)){
                IO.println("\nWrong choice, try again.\n");
            }

        } while (checkChoiceMenu(choose));

        if (choose.toUpperCase().contains("SIG"))
            returnMenu = 2;
        else if (choose.toUpperCase().contains("LOG"))
            returnMenu = 1;

        return returnMenu;
    }

    public static void printCenteredMessage(String message){
        int times = message.length() + 8;
        IO.print("\n\t\t\t");
        for (int i = 0; i < times; i++) {
            IO.print("-");
        }
        System.out.printf("\n\t\t\t--- %s ---\n\t\t\t", message);
        for (int j = 0; j < times; j++) {
            IO.print("-");
        }
        IO.print("\n\n");
    }

    public static int loggedMenu (){
        String choose;
        int returnMenu = 0;
        printCenteredMessage("Logged Page");

        do {
            IO.print("\tSelect:\n\t- View -> View profile\n\t- Reset -> Reset password\n\t- Exi -> Exit\n\n\tEnter your choice: ");
            choose = IO.readln();

            if (checkChoiceLoggedMenu(choose)){
                IO.println("\nWrong choice, try again.\n");
            }

        } while (checkChoiceLoggedMenu(choose));

        if (choose.toUpperCase().contains("VIEW"))
            returnMenu = 2;
        else if (choose.toUpperCase().contains("RESET"))
            returnMenu = 1;

        return returnMenu;
    }

    public static void userConfirmNextPage(){
        IO.print("\n\t Press anything to continue.");
        IO.readln();
    }

    public static void closeProgramMessage(){
        IO.println("\n\tClosing program...");
        userConfirmNextPage();
    }
}
