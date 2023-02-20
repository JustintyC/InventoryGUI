package ui;

import exceptions.EmptySlotException;
import exceptions.InvalidItemIDException;
import model.*;

import java.util.LinkedList;
import java.util.Scanner;

// DESCRIPTION
public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        System.out.println("Available commands: AddItem, RemoveItem, CreateItem, SlotInfo, ViewInventory, Quit");

        while (true) {
            System.out.println("-------------------------------------");
            System.out.println(inventory.textView());
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

    // REQUIRES: max stack size input < 0
    // EFFECTS: Produces results according to inputs.
    private static void handleInput(String input, Inventory inventory) {
        switch (input) {
            case "AddItem":
                doAddItem(inventory);
                break;
            case "RemoveItem":
                doRemoveItem(inventory);
                break;
            case "CreateItem":
                doCreateItem(inventory);
                break;
            case "SlotInfo":
                doSlotInfo(inventory);
                break;
            case "ViewInventory":
                System.out.println(inventory.textView());
                break;
            default:
                System.out.println("Unknown command. Please enter one of: AddItem, RemoveItem, CreateItem, "
                        + "SlotInfo, ViewInventory, Quit");
                break;
        }
    }

    // MODIFIES: inventory
    // EFFECTS: prompts user and adds an item to inventory
    private static void doAddItem(Inventory inventory) {
        ItemBank itemBank = inventory.getItemBank();

        System.out.println("Please enter slot number to add item into: ");
        int slot = Integer.parseInt(scanner.next()) - 1;
        System.out.println("Please enter item ID of the item you wish to add: ");
        int id = Integer.parseInt(scanner.next());
        try {
            Item item = itemBank.findItem(id);
            if (!inventory.insertItem(slot, item)) {
                System.out.println("Cannot add item to given slot.");
            } else {
                System.out.println("Item successfully added.");
            }
        } catch (InvalidItemIDException e) {
            System.out.println("Cannot find item with given item ID.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid slot number.");
        }
    }

    // MODIFIES: inventory
    // EFFECTS: prompts user and removes an item from inventory
    private static void doRemoveItem(Inventory inventory) {
        try {
            System.out.println("Please enter slot number to remove item from: ");
            int slotNum = Integer.parseInt(scanner.next()) - 1;
            if (inventory.getNthSlot(slotNum) instanceof EmptySlot) {
                throw new EmptySlotException();
            } else {
                Slot slot = inventory.getNthSlot(slotNum);
                System.out.println("This slot has " + slot.getStackCount() + " of " + slot.getName());
            }
            System.out.println("Please enter amount of this item to remove: ");
            int amount = Integer.parseInt(scanner.next());
            if (!inventory.removeItem(slotNum, amount)) {
                System.out.println("Not enough items to remove!");
            } else {
                System.out.println("Item successfully removed.");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid slot number.");
        } catch (EmptySlotException e) {
            System.out.println("This slot is empty!");
        }
    }

    // EFFECTS: provides slot info of given slot
    private static void doSlotInfo(Inventory inventory) {
        System.out.println("Please enter slot number: ");
        int slotNum = Integer.parseInt(scanner.next()) - 1;
        Slot slot = inventory.getNthSlot(slotNum);
        if (slot instanceof EmptySlot) {
            System.out.println("This slot is empty.");
        } else {
            System.out.println("Item name: " + slot.getName());
            System.out.println("Item ID: " + slot.getItemID());
            System.out.println("Item amount: " + slot.getStackCount());
            System.out.println("Max stack size: " + slot.getMaxStackSize());
        }
    }

    // MODIFIES: inventory.itemBank
    // EFFECTS: prompts user and creates a new item
    private static void doCreateItem(Inventory inventory) {
        System.out.println("Please enter the item's name: ");
        String name = scanner.next();
        System.out.println("Please enter the item's maximum stack count: ");
        int maxStack = Integer.parseInt(scanner.next());
        inventory.createItem(name, maxStack);
        System.out.println("Item successfully created.");
    }




}
