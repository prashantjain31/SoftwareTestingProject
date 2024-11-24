package org.example.cli;

import org.example.files.MyBookFiles;
import org.example.files.MyManagerFiles;
import org.example.files.MyMemberFiles;
import org.example.files.MyRentFiles;
import org.example.library.Book;
import org.example.person.Manager;
import org.example.utils.ArraysHelper;

import java.util.Objects;
import java.util.Scanner;

public class ManagersCLI extends MainCLI {
    private static Manager manager = null;

    public static void managersMenu(Scanner scanner) {
        System.out.print("\nManagers Menu" +
                "\nPlease select your desired menu:" +
                "\n1. Old manager" +
                "\n2. New Manager" +
                "\n3. return to Start Menu");

        String input;

        outer:
        while (true) {
            System.out.print(">");

            input = scanner.nextLine();

            switch (input) {
                case "1":
                    newLine();
                    System.out.print("Enter your Manager ID ('0' to return): ");
                    String ID = scanner.nextLine();

                    String check = checkBeforeContinue(ID);
                    if (check != null) {
                        if (check.equals(""))
                            System.out.print(check);
                        else
                            System.out.println(check);
                        newLine();
                        managersMenu(scanner);
                        break outer;
                    } else {
                        manager = MyManagerFiles.getManager(Integer.parseInt(ID));
                        newLine();
                    }

                    if (manager != null)
                        managerMenu(manager, scanner);
                    else
                        System.out.println("No such manager was found.");
                    newLine();

                    managersMenu(scanner);
                    break outer;
                case "2":
                    newLine();
                    System.out.println(addManager(scanner));
                    newLine();
                    managersMenu(scanner);
                    break outer;
                case "3":
                    newLine();
                    MainCLI.startMenu(scanner);
                    break outer;
                case "":
                    continue;
                default:
                    System.out.println("Command was not understood.");
            }
        }
    }

    public static void managerMenu(Manager manager, Scanner scanner) {
        System.out.printf("\nManager Menu for " +
                manager.getName() +
                "\nPlease select your desired menu:" +
                "\n1. Books Menu" +
                "\n2. View all rents" +
                "\n3. View all Members" +
                "\n4. View all Managers (Only accessible to the Main Manager)" +
                "\n5. Remove a Manager (Only accessible to the Main Manager)" +
                "\n6. return to Managers Menu");
        String input;

        outer:
        while (true) {
            System.out.print(">");

            input = scanner.nextLine();

            switch (input) {
                case "1":
                    newLine();
                    booksMenu(scanner);
                    break;
                case "2":
                    newLine();
                    MyRentFiles.showRents(0);
                    newLine();
                    managerMenu(manager, scanner);
                    break;
                case "3":
                    newLine();
                    MyMemberFiles.showMembers();
                    newLine();
                    managerMenu(manager, scanner);
                    break;
                case "4":
                    newLine();
                    if (manager.getID() == 1)
                        MyManagerFiles.showManagers();
                    else
                        System.out.println("This list is only accessible to the Main Manager.");
                    newLine();
                    managerMenu(manager, scanner);
                    break;
                case "5":
                    newLine();
                    if (manager.getID() == 1)
                        System.out.println(removeManager(scanner));
                    else
                        System.out.println("This list is only accessible to the Main Manager.");
                    //  The main manager is the manager with the ID of '1'
                    newLine();
                    managersMenu(scanner);
                    break;
                case "6":
                    newLine();
                    ManagersCLI.managersMenu(scanner);
                    break outer;
                case "":
                    continue;
                default:
                    System.out.println("Command was not understood.");
            }
        }
        // scanner.close();
    }

    public static void booksMenu(Scanner scanner) {
        System.out.print("\nBooks Menu" +
                "\nPlease select your desired menu:" +
                "\n1. View all Books" +
                "\n2. Add a new Book" +
                "\n3. Remove a book" +
                "\n4. return to Manager Menu");
        String input;

        outer:
        while (true) {
            System.out.print(">");

            input = scanner.nextLine();

            switch (input) {
                case "1":
                    newLine();
                    MyBookFiles.showBooks();
                    newLine();
                    booksMenu(scanner);
                    break outer;
                case "2":
                    newLine();
                    System.out.println(addBook(scanner));
                    newLine();
                    booksMenu(scanner);
                    break outer;
                case "3":
                    newLine();
                    System.out.println(removeBook(scanner));
                    newLine();
                    booksMenu(scanner);
                    break;
                case "4":
                    newLine();
                    managerMenu(manager, scanner);
                    break outer;
                case "":
                    continue;
                default:
                    System.out.println("Command was not understood.");
            }
        }
    }

    public static String addManager(Scanner scanner) {
        try {
            System.out.print("Enter your name ('0' to return): ");
            String name = scanner.nextLine();

            if (Objects.equals(name, "0")) {
                return "";
            }

            System.out.print("Enter your nationalCode ('0' to return): ");
            String nationalCode = scanner.nextLine();

            String check = checkBeforeContinue(nationalCode);
            if (check != null) {
                return check;
            }

            if (MyManagerFiles.checkForNationalCode(Long.parseLong(nationalCode))) {
                return "A manager already exists for this national code.";
            }

            Manager manager = new Manager(name, Long.parseLong(nationalCode));

            newLine();
            System.out.printf("Your manager ID is '%s'. Please memorise it.%n", manager.getID());
            return manager.save();
        } catch (Exception e) {
            return e.getMessage() + "\n" + ArraysHelper.toString(e.getStackTrace());
        }
    }

    public static String removeManager(Scanner scanner) {
        try {
            System.out.print("Enter the manager's ID ('0' to return): ");
            String ID = scanner.nextLine();

            String check = checkBeforeContinue(ID);
            if (check != null) {
                return check;
            }

            newLine();

            if (true) {
                Manager manager = MyManagerFiles.getManager(Integer.parseInt(ID));
                if (manager != null) {
                    System.out.printf("The manager '%s', with the national code of '%s' and ID of '%s', " +
                            "was removed from the system.%n", manager.getName(), manager.getNationalCode(), manager.getID());
                    return manager.remove();
                } else {
                    return "No such manager was found.";
                }
            }
        } catch (Exception e) {
            return e.getMessage() + "\n" + ArraysHelper.toString(e.getStackTrace());
        }
        return null;
    }

    public static String addBook(Scanner scanner) {
        try {
            System.out.print("Enter the book's name ('0' to return): ");
            String name = scanner.nextLine();

            if (Objects.equals(name, "0")) {
                return "";
            }

            System.out.print("Enter the book's author ('0' to return): ");
            String author = scanner.nextLine();

            if (Objects.equals(name, "0")) {
                return "";
            }

            newLine();

            if (MyBookFiles.checkForBook(name, author)) {
                return "This book already exists in the library.";
            }

            Book book = new Book(name, author);

            System.out.printf("Your book, '%s', written by '%s', was added to library.%n", book.getName(), book.getAuthor());
            return book.save();
        } catch (Exception e) {
            return e.getMessage() + "\n" + ArraysHelper.toString(e.getStackTrace());
        }
    }

    public static String removeBook(Scanner scanner) {
        try {
            System.out.print("Enter the book's name ('0' to return): ");
            String name = scanner.nextLine();

            if (Objects.equals(name, "0")) {
                return "";
            }

            System.out.print("Enter the book's author ('0' to return): ");
            String author = scanner.nextLine();

            if (Objects.equals(author, "0")) {
                return "";
            }

            newLine();

            Book book = MyBookFiles.getBook(name, author);

            if (book != null) {
                System.out.printf("Your book, '%s', written by '%s', was removed from the library.%n", name, author);
                return book.remove();
            } else {
                return "No such book was found.";
            }
        } catch (Exception e) {
            return e.getMessage() + "\n" + ArraysHelper.toString(e.getStackTrace());
        }
    }

}
