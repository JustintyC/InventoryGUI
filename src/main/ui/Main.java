package ui;

import model.Inventory;
import model.Slot;

import java.util.LinkedList;
import java.util.Scanner;

// DESCRIPTION
public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        System.out.println(inventory.textView());
        System.out.println("Available commands: AddItem, RemoveItem, ViewInventory, Quit");

        while (true) {
            System.out.println("What would you like to do?");
            String instruction = scanner.next();

            if (instruction.equals("Quit")) {
                System.out.println("Quitting...");
                break;
            } else {
                handleInput(instruction, inventory);
            }

        }
    }

    // EFFECTS: Produces results according to inputs.
    private static void handleInput(String input, Inventory inventory) {
        if (input.equals("AddItem")) {
            System.out.println("Stub for AddItem");
        } else if (input.equals("RemoveItem")) {
            System.out.println("Stub for RemoveItem");
        } else if (input.equals("ViewInventory")) {
            System.out.println(inventory.textView());
        } else {
            System.out.println("Unknown command. Please enter one of: AddItem, RemoveItem, ViewInventory, Quit");
        }
    }


}
