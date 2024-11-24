package org.example;

import java.io.IOException;
import java.util.Scanner;

import org.example.cli.MainCLI;
import org.example.files.Files;

public class App {
    public static void main(String[] args) throws IOException{
        Files.readFiles();

        Scanner sc = new Scanner(System.in);

        MainCLI.startMenu(sc);
    }
}
