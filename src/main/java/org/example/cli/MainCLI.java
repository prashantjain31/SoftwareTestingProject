package org.example.cli;

import java.util.Scanner;
import static org.example.utils.ConsoleColors.*;

public class MainCLI {

    public static void startMenu(Scanner scanner) {

        System.out.print("\nWelcome to our library." + 
            "\nPlease select your desired option from the Start Menu:" + 
            "\n1. Manager" +
            "\n2. Member" +
            "\n3. Exit");
        String input;
        
        outer:
        while (true) {
            System.out.print(">");

            input = scanner.nextLine();

            switch (input) {
                case "1":
                    newLine();
                    ManagersCLI.managersMenu(scanner);
                    break outer;
                case "2":
                    newLine();
                    MembersCLI.MembersMenu(scanner);
                    break outer;
                case "3":
                    newLine();
                    System.out.println("EXITING LIBRARY...");
                    break outer;
                case "":
                    continue;
                default:
                    System.out.println("Command was not understood.");
            }
        }
    }

    public static void newLine() {
        System.out.println(CYAN_BOLD_BRIGHT + "------------------------------------------" + RESET);
    }

    public static String checkBeforeContinue(String input) {
        if (input.equals("0"))
            return "";
        else {
            try {
                // Attempt to parse the input as a double
                Double.parseDouble(input);
            } catch (NumberFormatException e) {
                // Handle the case where input is not a valid number
                return "Please enter a valid number.";
            }
            return null;
        }
    }
}
