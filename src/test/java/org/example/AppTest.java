package org.example;

import static org.example.utils.ConsoleColors.CYAN_BOLD_BRIGHT;
import static org.example.utils.ConsoleColors.RESET;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.example.person.*;
import org.example.files.*;
import org.example.library.*;
import org.example.utils.*;

public class AppTest {
    String mainmenu = "\nWelcome to our library." + "\nPlease select your desired option from the Start Menu:" + "\n1. Manager" + "\n2. Member" + "\n3. Exit>";
    String newline = CYAN_BOLD_BRIGHT + "------------------------------------------" + RESET + "\n";
    String membermenu = "\nMembers Menu" + "\nPlease select your desired menu:" + "\n1. Old Member" + "\n2. New Member" + "\n3. return to Start Menu>";
    String managermenu="\nManagers Menu" + "\nPlease select your desired menu:" + "\n1. Old manager" + "\n2. New Manager" + "\n3. return to Start Menu>";
    String mmenu="\nPlease select your desired menu:" +"\n1. Books Menu" +"\n2. View all rents" +"\n3. View all Members" +"\n4. View all Managers (Only accessible to the Main Manager)" +"\n5. Remove a Manager (Only accessible to the Main Manager)" +"\n6. return to Managers Menu>";
    String bookmembermenu = "\nBooks Menu" +
                "\nPlease select your desired menu:" +
                "\n1. View all Books" +
                "\n2. Rent a Book" +
                "\n3. Return a Book" +
                "\n4. View Rented Books" +
                "\n5. return to Member Menu>";
    String booksmenu="\nBooks Menu" +"\nPlease select your desired menu:" +"\n1. View all Books" +"\n2. Add a new Book" +"\n3. Remove a book" +"\n4. return to Manager Menu>";
    

    public String membermenu(String name) {
        return "\nMember Menu for " + name +
                "\nPlease select your desired menu:" +
                "\n1. Books & Renting" +
                "\n2. Remove this member" +
                "\n3. return to Members Menu>";
    }
    
    @Before
    public void setup() throws IOException {
        String filePath = new java.io.File(".").getCanonicalPath() + "/LibraryFiles/";
        String files[] = new String[]{"books.txt", "managers.txt", "rents.txt", "members.txt"};

        for(String name:files) {
            try {
                // Delete the file
                File file = new File(filePath+name);
                if (file.exists()) {
                    file.delete();
                }

                // Recreate the file
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Files.readFiles();
    }

    @Test
    public void PersonFunctions() {
        int ID = Member.getStaticID();
        Member mem = new Member("Mem1", 98);
        assertEquals("Person-1", "Mem1", mem.getName());
        assertEquals("Person-2", 98, mem.getNationalCode());
        assertEquals("Person-3", ID+". Mem1, with national code of 98", mem.toString());
        assertEquals("Person-4", ID, mem.getID());
    }

    @Test
    public void MemberFunctions() {
        int ID = Member.getStaticID();
        Member mem = new Member("Mem2", 99);
        assertEquals("Member-1", ID+1, Member.getStaticID());
        assertEquals("Member-2", "Member added to system.", mem.save());
        assertEquals("Member-3", "Member was removed from the system.", mem.remove());
        assertEquals("Member-4", ID, Member.getStaticID());
    }

    @Test
    public void ManagerFunctions() {
        int ID = Manager.getStaticID();
        Manager man = new Manager("Mem2", 99);
        assertEquals("Manager-1", ID+1, Manager.getStaticID());
        assertEquals("Manager-2", "Manager added to system.", man.save());
        assertEquals("Manager-3", "Manager was removed from the system.", man.remove());
        assertEquals("Manager-4", ID, Manager.getStaticID());
    }

    @Test
    public void Util(){
        Object[] emptyArray = new Object[0];
        Object[] array = { "Hello" };
        assertEquals("Util-1", "null", ArraysHelper.toString(null));
        assertEquals("Util-2", "", ArraysHelper.toString(emptyArray));
        assertEquals("util-3", "\nHello\n", ArraysHelper.toString(array));
    }

    @Test
    public void BookFunctions() {
        Book book1 = new Book("Skullduggery Pleasant Dark Days", "Derek Landy");
        Book book2 = new Book("Skullduggery Pleasant Faceless Ones", "Derek Landy");
        assertEquals("Book-1", book1.getID()+1, book2.getID());
        assertEquals("Book-2", "Skullduggery Pleasant Dark Days", book1.getName());
        assertEquals("Book-3", "Derek Landy", book1.getAuthor());
        assertEquals("Book-4", false, book1.isBorrowed());
        assertEquals("Book-5", "Book added to system.", book2.save());
        assertEquals("Book-6", "Book was removed from the system.", book2.remove());
        Book book3 = new Book("Hey hello", "Myself");
        assertEquals("Book-7", book1.getID()+1, book3.getID());
        book1.remove();
        assertEquals("Book-8",  book1.getID()+". Skullduggery Pleasant Dark Days, by Derek Landy", book1.toString());
    }

    @Test
    public void RentFunctions() {
        Book book = new Book("Skullduggery Pleasant Dark Days", "Derek Landy");
        Member mem = new Member("Mem1", 98);
        Rent rent = new Rent(mem, book);
        Date dt = new Date();
        assertEquals("Rent-1", true, book.isBorrowed());
        assertEquals("Rent-2", mem, rent.getMember());
        assertEquals("Rent-3", book, rent.getBook());
        assertEquals("Rent-4", dt, rent.getDate());
        assertEquals("Rent-5", "Rent added to system.", rent.save());
        assertEquals("Rent-6", "Rent was removed from the system.", rent.remove());
        assertEquals("Rent-7", false, book.isBorrowed());
        assertEquals("Rent-8", "Mem1 rented Skullduggery Pleasant Dark Days(" + book.getID() + ") at " + rent.getDate() + ".", rent.toString());
    }

    @Test
    public void MyBookFiles() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        MyBookFiles.showBooks();
        assertEquals("bookfiles--1", "No books was found.\n", output.toString());
        output.close();

        Book book = new Book("Skullduggery Pleasant Dark Days", "Derek Landy");
        book.save();
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        MyBookFiles.showBooks();
        assertEquals("bookfiles-0", book.getID() + ". Skullduggery Pleasant Dark Days, by Derek Landy\n", output.toString());
        output.close();

        Book b1= new Book("book1", "Hobbit");
        int ID=b1.getID();
        // assertEquals("bookfiles-5", "No books was found.", MyBookFiles.showBooks());
        assertEquals("bookfiles-1", "Book added to system.", b1.save());
        assertEquals("bookfiles-2", b1, MyBookFiles.getBook("book1", "Hobbit"));
        assertEquals("bookfiles-3", b1, MyBookFiles.getBook(ID));
        assertEquals("bookfiles-4",true, MyBookFiles.checkForBook("book1", "Hobbit"));
        MyBookFiles.readBooks();
        Book b2= new Book("book2", "Hobbit1");
        assertEquals("bookfiles-5", ID+1,b2.getID());
        assertEquals("bookfiles-6",false,MyBookFiles.checkForBook("lkdsfjk", "dklf"));

        Member mem = new Member("Mem1", 98);
        Rent rent = new Rent(mem, b2), rt = new Rent(mem, b1);
        mem.save();
        rent.save();
        rt.save();
        assertEquals("bookfiles-7", "Book was removed from the system.", MyBookFiles.remove(b1));
        assertEquals("bookfiles-8", null, MyRentFiles.getRent(b1.getID()));
        mem.remove();
        b1.remove();
    }

    @Test
    public void MyManagerFiles() throws IOException {
        MyManagerFiles.remove(MyManagerFiles.getManager(3));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        MyManagerFiles.showManagers();
        assertEquals("managerfiles--1", "No manager was found.\n", output.toString());
        output.close();

        Manager man = new Manager("Tim", 456);
        man.save();
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        MyManagerFiles.showManagers();
        assertEquals("managerfiles-0", man.getID() + ". Tim, with national code of 456\n", output.toString());
        output.close();

        MyManagerFiles.readManagers();
        Manager m1=new Manager("Hobbit", 970);
        int ID=m1.getID();
        assertEquals("managerfiles-1", "Manager added to system.", m1.save());
        assertEquals("managerfiles-2", m1,MyManagerFiles.getManager(ID));
        assertEquals("managerfiles-3", true,MyManagerFiles.checkForNationalCode(970));
        assertEquals("managerfiles-4", false,MyManagerFiles.checkForNationalCode(687654));
        MyManagerFiles.readManagers();
        Manager m2=new Manager("Hobbit2", 9948);
        assertEquals("managerfiles-5", ID+1, m2.getID());
    }

    @Test
    public void MyMemberFiles() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        MyMemberFiles.showMembers();
        assertEquals("memberfiles--1", "No member was found.\n", output.toString());
        output.close();
        
        Member mem = new Member("Tim", 456);
        mem.save();
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        MyMemberFiles.showMembers();
        assertEquals("memberfiles-0", mem.getID() + ". Tim, with national code of 456\n", output.toString());
        output.close();

        Member mem1 = new Member("Varun", 987);
        int ID = mem1.getID();
        assertEquals("memberfiles-1", "Member added to system.", mem1.save());
        assertEquals("memberfiles-2", mem1, MyMemberFiles.getMember(ID));
        assertTrue("memberfiles-3", MyMemberFiles.checkForNationalCode(987));
        assertFalse("memberfiles-4", MyMemberFiles.checkForNationalCode(666));
        MyMemberFiles.readFiles();
        Member mem2 = new Member("Bruh", 111);
        assertEquals("memberfiles-5", ID+1, mem2.getID());
    }

    @Test
    public void MyRentFiles() throws IOException{
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        MyRentFiles.showRents(0);
        assertEquals("rentfiles--1", "No rents was found.\n", output.toString());
        output.close();

        // Manager man = new Manager("Tim", 456);
        Member m1=new Member("Hobbit", 970);
        Book b1=new Book("book1", "its me");
        Rent r1= new Rent(m1, b1);
        r1.save();
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        MyRentFiles.showRents(m1.getID());
        assertEquals("rentfiles-0","Hobbit rented "+b1.getName()+"("+b1.getID()+") at "+r1.getDate()+".\n", output.toString());
        output.close();
        
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        MyRentFiles.showRents(0);
        assertEquals("rentfiles-1", "Hobbit rented "+b1.getName()+"("+b1.getID()+") at "+r1.getDate()+".\n", output.toString());
        output.close();

        assertEquals("rentfiles-2", r1,MyRentFiles.getRent(b1.getID()));
        MyRentFiles.readRents();
        assertEquals("rentfiles-3", r1,MyRentFiles.getRent(b1.getID()));
    }

    @Test
    public void AppCheck1() {
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("App-2", "\nWelcome to our library." + 
        "\nPlease select your desired option from the Start Menu:" + 
        "\n1. Manager" +
        "\n2. Member" +
        "\n3. Exit>" + CYAN_BOLD_BRIGHT + "------------------------------------------" + RESET + "\n" + "EXITING LIBRARY..." + "\n", output.toString());
    }

    @Test
    public void AppCheck2() throws Exception {
        String input = "4\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals("App-2", mainmenu + "Command was not understood.\n>" + newline + "EXITING LIBRARY...\n", output.toString());
    }

    @Test
    public void ManagerCLI1() throws IOException{
        String input="1\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("ManagerCLI-1", mainmenu+newline+managermenu+newline+mainmenu+newline+"EXITING LIBRARY..."+"\n", output.toString());
        output.close();
    }

    @Test
    public void ManagerCLI2() throws IOException{
        String enterName="Enter your name ('0' to return): Enter your nationalCode ('0' to return): ";
        String input="1\n2\nHobbit\n970876\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        int ID=Manager.getStaticID()-1;
        assertEquals("ManagerCLI-1", mainmenu+newline+managermenu+newline+enterName+newline+"Your manager ID is '"+ID+"'. Please memorise it.\nManager added to system.\n"+newline+managermenu+newline+mainmenu+newline+"EXITING LIBRARY..."+"\n", output.toString());
        output.close();
        Manager m1=MyManagerFiles.getManager(ID);
        Files.remove(m1);
    }

    @Test
    public void ManagerCLI3() throws IOException{
        String enterName="Enter your name ('0' to return): ";
        String input="1\n2\n0\n1\n0\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("ManagerCLI-3", mainmenu+newline+managermenu+newline+enterName+"\n"+newline+managermenu+newline+"Enter your Manager ID ('0' to return): "+newline+managermenu+newline+mainmenu+newline+"EXITING LIBRARY..."+"\n", output.toString());
    }

    @Test
    public void ManagerCLI4() throws IOException{
        String enterName="Enter your name ('0' to return): Enter your nationalCode ('0' to return): Please enter a valid number.\n";
        String input="1\n2\nHobbit\nhaa\n1\nhaa\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("ManagerCLI-4",mainmenu+newline+managermenu+newline+enterName+newline+managermenu+newline+"Enter your Manager ID ('0' to return): "+"Please enter a valid number.\n"+newline+managermenu+newline+mainmenu+newline+"EXITING LIBRARY..."+"\n", output.toString());
    }

    @Test
    public void ManagerCLI5() throws IOException{
        Manager m1=new Manager("Hobbit99", 994819);
        m1.save();
        String enterName="Enter your name ('0' to return): Enter your nationalCode ('0' to return): ";
        
        String input="1\n2\nHobbit\n994819\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("ManagerCLI-5", mainmenu+newline+managermenu+newline+enterName+"A manager already exists for this national code.\n"+newline+managermenu+newline+mainmenu+newline+"EXITING LIBRARY..."+"\n", output.toString());
        output.close();
        int ID=Manager.getStaticID()-1;
        Manager m2=MyManagerFiles.getManager(ID);
        Files.remove(m2);
        Files.remove(MyManagerFiles.getManager(2));
    }

    @Test
    public void ManagerCLI6() throws IOException{
        String input="1\n1\n98789789\n5\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("ManagerCLI-6",mainmenu+newline+managermenu+newline+"Enter your Manager ID ('0' to return): "+newline+"No such manager was found.\n"+newline+managermenu+"Command was not understood.\n>"+newline+mainmenu+newline+"EXITING LIBRARY..."+"\n", output.toString());
    }

    @Test
    public void ManagerCLI7() throws IOException{
        Manager m1=new Manager("madhav", 5454);
        m1.save();
        int ID=Manager.getStaticID()-1;
        String input="1\n1\n"+ID+"\n8\n6\n3\n3\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("ManagerCLI-7",mainmenu+newline+managermenu+newline+"Enter your Manager ID ('0' to return): "+newline+"\n"+"Manager Menu for madhav"+mmenu+"Command was not understood.\n>"+newline+managermenu+newline+mainmenu+newline+"EXITING LIBRARY...\n"+newline+managermenu+newline+mainmenu+newline+"EXITING LIBRARY...\n",output.toString());
        m1.remove();
        Files.remove(MyManagerFiles.getManager(3));
    }

    @Test
    public void ManagerCLI8() throws IOException{
        MyBookFiles.remove(MyBookFiles.getBook("Skullduggery Pleasant Dark Days", "Derek Landy"));
        MyBookFiles.remove(MyBookFiles.getBook("Skullduggery Pleasant Dark Days", "Derek Landy"));
        MyBookFiles.remove(MyBookFiles.getBook("Skullduggery Pleasant Dark Days", "Derek Landy"));
        MyBookFiles.remove(MyBookFiles.getBook("book1", "Hobbit"));
        Manager m1=new Manager("madhav", 5454);
        m1.save();
        int ID=Manager.getStaticID()-1;
        String input="1\n1\n"+ID+"\n1\n1\n2\n0\n2\nSomeBook\n0\n2\ncha\nchat\n2\ncha\nchat\n3\n0\n3\ncha\n0\n3\ndskfj\nkdlsfj\n3\ncha\nchat\n4\n6\n3\n3\n"+"4\n6\n3\n3\n"+"4\n6\n3\n3\n"+"4\n6\n3\n3\n"+"4\n6\n3\n3\n6\n3\n3\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("managercli-8", mainmenu+newline+managermenu+newline+"Enter your Manager ID ('0' to return): "+newline+"\nManager Menu for madhav"+mmenu+newline+booksmenu+newline+"No books was found.\n"+newline+booksmenu+newline+"Enter the book's name ('0' to return): "+"\n"+newline+booksmenu+newline+"Enter the book's name ('0' to return): Enter the book's author ('0' to return): "+newline+"Your book, 'SomeBook', written by '0', was added to library." +"\n"+"Book added to system."+"\n"+newline+booksmenu+newline+"Enter the book's name ('0' to return): Enter the book's author ('0' to return): "+newline+"Your book, 'cha', written by 'chat', was added to library." +"\n"+ "Book added to system.\n"+newline+booksmenu+newline+"Enter the book's name ('0' to return): Enter the book's author ('0' to return): "+newline+"This book already exists in the library."+"\n"+newline+booksmenu+newline+"Enter the book's name ('0' to return): "+"\n"+newline+booksmenu+newline+"Enter the book's name ('0' to return): Enter the book's author ('0' to return): "+"\n"+newline+booksmenu+newline+"Enter the book's name ('0' to return): Enter the book's author ('0' to return): "+newline+"No such book was found."+"\n"+newline+booksmenu+newline+"Enter the book's name ('0' to return): Enter the book's author ('0' to return): "+newline+"Your book, 'cha', written by 'chat', was removed from the library."+"\n"+"Book was removed from the system.\n"+newline+booksmenu+newline+"\n"+"Manager Menu for madhav"+mmenu+newline+managermenu+newline+mainmenu+newline+"EXITING LIBRARY..."+"\n>"+newline+"\n"+"Manager Menu for madhav"+mmenu+newline+managermenu+newline+mainmenu+newline+"EXITING LIBRARY..."+"\n>"+newline+"\n"+"Manager Menu for madhav"+mmenu+newline+managermenu+newline+mainmenu+newline+"EXITING LIBRARY..."+"\n>"+newline+"\n"+"Manager Menu for madhav"+mmenu+newline+managermenu+newline+mainmenu+newline+"EXITING LIBRARY..."+"\n>"+newline+"\n"+"Manager Menu for madhav"+mmenu+newline+managermenu+newline+mainmenu+newline+"EXITING LIBRARY..."+"\n>"+newline+managermenu+newline+mainmenu+newline+"EXITING LIBRARY..."+"\n"+newline+managermenu+newline+mainmenu+newline+"EXITING LIBRARY..."+"\n", output.toString());
        m1.remove();
    }

    @Test
    public void MembersCLI1() throws IOException {
        String input = "2\n4\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("MemberCLI-1", mainmenu + newline + membermenu + "Command was not understood.\n>" + newline + mainmenu + newline + "EXITING LIBRARY..." + "\n", output.toString());
        output.close();
    }

    @Test
    public void MembersCLI2() throws IOException {
        int ID=Member.getStaticID()-1;
        String input = "2\n2\n0\n2\nMadhav\n0\n2\nMadhav\n880\n" + "1\n" + "0\n1\n" + (ID+1) + "\n3\n3\n3\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("MemberCLI-2", mainmenu + newline + membermenu + newline + "Enter your name ('0' to return): " + newline + membermenu + newline + "Enter your name ('0' to return): " + "Enter your nationalCode ('0' to return): " + newline + membermenu + newline + "Enter your name ('0' to return): " + "Enter your nationalCode ('0' to return): " + newline + "Your Member ID is '" + (ID+1) + "'. Please memorise it.\n" + "Member added to system.\n" + newline + membermenu + newline + "Enter your Member ID ('0' to return): " + newline + membermenu + newline + "Enter your Member ID ('0' to return): " + newline + membermenu("Madhav") + newline + membermenu + newline + mainmenu + newline + "EXITING LIBRARY..." + "\n" + newline + membermenu + newline + mainmenu + newline + "EXITING LIBRARY..." + "\n", output.toString());
        output.close();
        Member m1=MyMemberFiles.getMember(ID+1);
        Files.remove(m1);
    }

    @Test
    public void MembersCLI3() {
        String input = "2\n1\nsdfs\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("membercli-3", mainmenu + newline + membermenu + newline + "Enter your Member ID ('0' to return): " + "Please enter a valid number.\n" + newline + membermenu + newline + mainmenu + newline + "EXITING LIBRARY...\n", output.toString());
    }

    @Test
    public void MembersCLI4() {
        String input = "2\n1\n5\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("membercli-4", mainmenu + newline + membermenu + newline + "Enter your Member ID ('0' to return): " + newline + "No such member was found.\n" + newline + membermenu + newline + mainmenu + newline + "EXITING LIBRARY...\n", output.toString());
    }

    @Test
    public void MembersCLI5() {
        int ID=Member.getStaticID();
        String input = "2\n2\nHey\n123\n2\nHey\n123\n1\n" + ID + "\n2\n" + ID + "\n3\n3\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("membercli-5", mainmenu + newline + membermenu + newline + "Enter your name ('0' to return): " + "Enter your nationalCode ('0' to return): " + newline + "Your Member ID is '" + ID + "'. Please memorise it.\n" + "Member added to system.\n" + newline + membermenu + newline + "Enter your name ('0' to return): " + "Enter your nationalCode ('0' to return): " + "A member already exists for this national code.\n" + newline + membermenu + newline + "Enter your Member ID ('0' to return): " + newline + membermenu("Hey") + newline + "Enter your Member ID ('0' to return): " + newline + "The member 'Hey', with the national code of '123' and ID of '" + ID + "', was removed from the system. Its rented books were removed as well.\n" + "Member was removed from the system.\n" + newline + membermenu + newline + mainmenu + newline + "EXITING LIBRARY...\n" + newline + membermenu + newline + mainmenu + newline + "EXITING LIBRARY...\n", output.toString());
        Member m1=MyMemberFiles.getMember(ID);
        Files.remove(m1);
    }

    @Test
    public void MembersCLI6() {
        int ID=Member.getStaticID();
        String input = "2\n2\nHey\n123\n1\n" + ID + "\n4\n2\n0\n2\n1000\n3\n3\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("membercli-6", mainmenu + newline + membermenu + newline + "Enter your name ('0' to return): " + "Enter your nationalCode ('0' to return): " + newline + "Your Member ID is '" + ID + "'. Please memorise it.\n" + "Member added to system.\n" + newline + membermenu + newline + "Enter your Member ID ('0' to return): " + newline + membermenu("Hey") + "Command was not understood.\n>" + newline + "Enter your Member ID ('0' to return): " + newline + membermenu("Hey") + newline + "Enter your Member ID ('0' to return): " + newline + "No such member was found.\n" + newline + membermenu + newline + mainmenu + newline + "EXITING LIBRARY...\n" + newline + membermenu + newline + mainmenu + newline + "EXITING LIBRARY...\n", output.toString());
        Member m1=MyMemberFiles.getMember(ID);
        Files.remove(m1);
    }

    @Test
    public void MembersCLI7() {
        MyBookFiles.remove(MyBookFiles.getBook("SomeBook", "0"));
        MyBookFiles.remove(MyBookFiles.getBook("SomeBook", "0"));
        int ID=Member.getStaticID();
        String input = "2\n2\nHey\n122\n1\n" + ID + "\n1\n1\n4\n5\n3\n3\n3\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("membercli-7", mainmenu + newline + membermenu + newline + "Enter your name ('0' to return): " + "Enter your nationalCode ('0' to return): " + newline + "Your Member ID is '" + ID + "'. Please memorise it.\n" + "Member added to system.\n" + newline + membermenu + newline + "Enter your Member ID ('0' to return): " + newline + membermenu("Hey") + newline + bookmembermenu + newline + "No books was found.\n" + newline + bookmembermenu + newline + "No rents was found.\n" + newline + bookmembermenu + newline + membermenu("Hey") + newline + membermenu + newline + mainmenu + newline + "EXITING LIBRARY...\n" + newline + membermenu + newline + mainmenu + newline + "EXITING LIBRARY...\n", output.toString());
        Member m1=MyMemberFiles.getMember(ID);
        Files.remove(m1);
    }

    @Test
    public void membercli8() {
        int ID = Member.getStaticID();
        String input = "2\n2\nHey\n1229\n1\n" + ID + "\n1\n2\n0\n2\n100\n3\n0\n3\n10\n5\n3\n3\n3\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("membercli-8", mainmenu + newline + membermenu + newline + "Enter your name ('0' to return): " + "Enter your nationalCode ('0' to return): " + newline + "Your Member ID is '" + ID + "'. Please memorise it.\n" + "Member added to system.\n" + newline + membermenu + newline + "Enter your Member ID ('0' to return): " + newline + membermenu("Hey") + newline + bookmembermenu + newline + "Enter the book's ID ('0' to return): " + newline + bookmembermenu + newline + "Enter the book's ID ('0' to return): " + "MembersCLI.addRent() failed\n" + newline + bookmembermenu + newline + "Enter the rented book's ID ('0' to return): " + newline + bookmembermenu + newline + "Enter the rented book's ID ('0' to return): " + newline + "MembersCLI.removeRent() failed\n" + newline + bookmembermenu + newline + membermenu("Hey") + newline + membermenu + newline + mainmenu + newline + "EXITING LIBRARY...\n" + newline + membermenu + newline + mainmenu + newline + "EXITING LIBRARY...\n", output.toString());
        Member m1=MyMemberFiles.getMember(ID);
        Files.remove(m1);
    }

    @Test
    public void membercli9() {
        int ID = Member.getStaticID();
        String input = "2\n2\nHey\n1234\n1\n" + ID + "\n1\n6\n5\n3\n3\n3\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            App.main(new String[]{});
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals("membercli-9", mainmenu + newline + membermenu + newline + "Enter your name ('0' to return): " + "Enter your nationalCode ('0' to return): " + newline + "Your Member ID is '" + ID + "'. Please memorise it.\n" + "Member added to system.\n" + newline + membermenu + newline + "Enter your Member ID ('0' to return): " + newline + membermenu("Hey") + newline + bookmembermenu + "Command was not understood.\n>" + newline + membermenu("Hey") + newline + membermenu + newline + mainmenu + newline + "EXITING LIBRARY...\n" + newline + membermenu + newline + mainmenu + newline + "EXITING LIBRARY...\n", output.toString());
        Member m1=MyMemberFiles.getMember(ID);
        Files.remove(m1);
    }

    @After
    public void teardown() {

    }
}