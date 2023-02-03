package ui;

import model.Inventory;
import model.Item;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        System.out.println("Available commands: Quit");

        while (true) {
            System.out.println("What would you like to do?");
            String instruction = scanner.next();

            if (instruction.equals("Quit")) {
                System.out.println("Quitting...");
                break;
            } else {
                System.out.println("Unknown command. Please enter one of: Quit");
            }

        }
    }
}
