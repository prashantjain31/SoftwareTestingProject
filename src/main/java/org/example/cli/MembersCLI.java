package org.example.cli;

import org.example.files.MyBookFiles;
import org.example.files.MyMemberFiles;
import org.example.files.MyRentFiles;
import org.example.library.Rent;
import org.example.person.Member;
import org.example.utils.ArraysHelper;

import java.util.Scanner;

public class MembersCLI extends MainCLI {
    private static Member member;

    public static void MembersMenu(Scanner scanner) {
        System.out.print("\nMembers Menu" +
                "\nPlease select your desired menu:" + 
                "\n1. Old Member" +
                "\n2. New Member" +
                "\n3. return to Start Menu");

        String input;

        outer:
        while (true) {
            System.out.print(">");

            input = scanner.nextLine();

            switch (input) {
                case "1":
                    newLine();
                    System.out.print("Enter your Member ID ('0' to return): ");
                    String ID = scanner.nextLine();

                    String check = checkBeforeContinue(ID);
                    if (check != null) {
                        if (check.equals(""))
                            System.out.print(check);
                        else
                            System.out.println(check);
                        newLine();
                        MembersMenu(scanner);
                        break outer;
                    } else {
                        member = MyMemberFiles.getMember(Integer.parseInt(ID));
                        newLine();
                    }

                    if (member != null)
                        MemberMenu(member, scanner);
                    else
                        System.out.println("No such member was found.");
                    newLine();

                    MembersMenu(scanner);
                    break outer;
                case "2":
                    newLine();
                    String answer = addMember(scanner);
                    if (answer != null && !answer.equals(""))
                        System.out.println(answer);
                    newLine();
                    MembersMenu(scanner);
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

    public static void MemberMenu(Member member, Scanner scanner) {
        System.out.printf("\nMember Menu for " + member.getName() +
                "\nPlease select your desired menu:" +
                "\n1. Books & Renting" +
                "\n2. Remove this member" +
                "\n3. return to Members Menu");
        String input;

        outer:
        while (true) {
            System.out.print(">");

            input = scanner.nextLine();

            switch (input) {
                case "1":
                    newLine();
                    booksMenu(member, scanner);
                    break outer;
                case "2":
                    newLine();
                    String answer = removeMember(scanner);
                    if (answer != null && !answer.equals(""))
                        System.out.println(answer);
                    else {
                        newLine();
                        MemberMenu(member, scanner);
                        break outer;
                    }
                    newLine();
                    MembersMenu(scanner);
                    break outer;
                case "3":
                    newLine();
                    MembersCLI.MembersMenu(scanner);
                    break outer;
                case "":
                    continue;
                default:
                    System.out.println("Command was not understood.");
            }
        }
    }

    public static void booksMenu(Member member, Scanner scanner) {
        System.out.print("\nBooks Menu" +
                "\nPlease select your desired menu:" +
                "\n1. View all Books" +
                "\n2. Rent a Book" +
                "\n3. Return a Book" +
                "\n4. View Rented Books" +
                "\n5. return to Member Menu");
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
                    booksMenu(member, scanner);
                    break outer;
                case "2":
                    newLine();
                    String answer = addRent(member, scanner);
                    if (answer != null && !answer.equals(""))
                        System.out.println(answer);
                    newLine();
                    booksMenu(member, scanner);
                    break outer;
                case "3":
                    newLine();
                    answer = removeRent(scanner);
                    if (answer != null && !answer.equals(""))
                        System.out.println(answer);
                    newLine();
                    booksMenu(member, scanner);
                    break outer;
                case "4":
                    newLine();
                    MyRentFiles.showRents(member.getID());
                    newLine();
                    booksMenu(member, scanner);
                    break outer;
                case "5":
                    newLine();
                    MemberMenu(member, scanner);
                    break outer;
                case "":
                    continue;
                default:
                    System.out.println("Command was not understood.");
            }
        }
    }

    public static String addMember(Scanner scanner) {
        try {
            System.out.print("Enter your name ('0' to return): ");
            String name = scanner.nextLine();

            if (name.equals("0")) {
                return "";
            }

            System.out.print("Enter your nationalCode ('0' to return): ");
            String nationalCode = scanner.nextLine();

            String check = checkBeforeContinue(nationalCode);
            if (check != null) {
                return check;
            }

            if (MyMemberFiles.checkForNationalCode(Long.parseLong(nationalCode))) {
                return "A member already exists for this national code.";
            }

            Member Member = new Member(name, Long.parseLong(nationalCode));

            newLine();
            System.out.printf("Your Member ID is '%s'. Please memorise it.%n",Member.getID());
            return Member.save();
        }
        catch (Exception e){
            return e.getMessage() + "\n" + ArraysHelper.toString(e.getStackTrace());
        }
    }

    public static String removeMember(Scanner scanner) {
        try {
            System.out.print("Enter your Member ID ('0' to return): ");
            String ID = scanner.nextLine();

            String check = checkBeforeContinue(ID);
            if (check != null) {
                return check;
            }

            newLine();

            Member member = MyMemberFiles.getMember(Integer.parseInt(ID));
            if (member != null) {
                System.out.printf("The member '%s', with the national code of '%s' and ID of '%s', " +
                        "was removed from the system. Its rented books were removed as well.%n", member.getName(), member.getNationalCode(), member.getID());
                return member.remove();
            } else {
                return "No such member was found.";
            }
        }
        catch(Exception e) {
            return e.getMessage() + "\n" + ArraysHelper.toString(e.getStackTrace());
        }
    }

    public static String addRent(Member member, Scanner scanner) {
        try {
            System.out.print("Enter the book's ID ('0' to return): ");
            String ID = scanner.nextLine();

            String check = checkBeforeContinue(ID);
            if (check != null) {
                return check;
            }

            if (MyBookFiles.getBook(Integer.parseInt(ID)).isBorrowed())
                System.out.println("This Book is already rented from the library.");
            else {
                Rent rent = new Rent(member, MyBookFiles.getBook(Integer.parseInt(ID)));
                rent.getBook().setBorrowed(true);
                newLine();
                System.out.printf("Your book, '%s', written by '%s', was rented from the library in '%s'.%n",
                        rent.getBook().getName(), rent.getBook().getAuthor(), rent.getDate());
                return rent.save();
            }
        }
        catch(Exception e) {
            return "MembersCLI.addRent() failed";
        }
        return null;
    }

    public static String removeRent(Scanner scanner) {
        try {
            System.out.print("Enter the rented book's ID ('0' to return): ");
            String ID = scanner.nextLine();

            String check = checkBeforeContinue(ID);
            if (check != null) {
                return check;
            }

            Rent rent = MyRentFiles.getRent(Integer.parseInt(ID));

            newLine();
            System.out.printf("The book '%s' written by '%s', was returned to the library.%n",
                    rent.getBook().getName(), rent.getBook().getAuthor());
            return rent.remove();
        }
        catch(Exception e) {
            return "MembersCLI.removeRent() failed";
        }
    }

}
