package ui;

import model.*;

import java.util.Scanner;

// main text UI file
public class Main {
    private static Scanner scanner = new Scanner(System.in); // Scanner from B04-SimpleCalculatorStaterLecLab

    public static void main(String[] args) {
        String commandList = "AddItem, RemoveItem, CreateItem, Hold, Drop, SlotInfo, Organize, ViewInventory, Quit";
        Inventory inventory = new Inventory();
        Hand hand = new Hand();
        System.out.println("Available commands: " + commandList);


        inventory.getItemBank().add(new Item("UBC Card", 1, 1));
        inventory.getItemBank().add(new Item("Pencil", 2, 5));

        while (true) {
            System.out.println("-------------------------------------");
            System.out.println(inventory.textView());
            System.out.println(hand.handTextView());
            System.out.println("What would you like to do?");
            String instruction = scanner.next();

            if (instruction.equals("Quit")) {
                System.out.println("Quitting...");
                break;
            } else {
                TextUI.handleInput(instruction, inventory, hand, commandList);
            }

        }
    }






}
