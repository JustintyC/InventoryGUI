package ui;

import exceptions.InvalidItemIDException;
import model.*;

import java.util.Scanner;

// inventory text UI that handles inputs and produces corresponding information
public class TextUI {
    private static Scanner scanner = new Scanner(System.in); // Scanner from B04-SimpleCalculatorStaterLecLab

    // EFFECTS: Produces results according to inputs.
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public static void handleInput(String input, Inventory inventory, Hand hand, String commandList) {
        switch (input) {
            case "AddItem":
                doAddItem(inventory, hand);
                break;
            case "RemoveItem":
                doRemoveItem(inventory, hand);
                break;
            case "Hold":
                doHoldItem(inventory, hand);
                break;
            case "Drop":
                doDropItem(inventory, hand);
                break;
            case "CreateItem":
                doCreateItem(inventory, hand);
                break;
            case "SlotInfo":
                doSlotInfo(inventory, hand);
                break;
            case "Organize":
                inventory.organize();
                System.out.println("Inventory organized.");
                break;
            case "ViewInventory":
                System.out.println(inventory.textView());
                break;
            default:
                System.out.println("Unknown command. Please enter one of: " + commandList);
                break;
        }
    }

    // MODIFIES: inventory
    // EFFECTS: prompts user and adds an item to inventory
    private static void doAddItem(Inventory inventory, Hand hand) {
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
    private static void doRemoveItem(Inventory inventory, Hand hand) {
        try {
            System.out.println("Please enter slot number to remove item from: ");
            int slotNum = Integer.parseInt(scanner.next()) - 1;
            if (inventory.getNthSlot(slotNum) instanceof EmptySlot) {
                System.out.println("This slot is empty!");
            } else {
                Slot slot = inventory.getNthSlot(slotNum);
                System.out.println("This slot has " + slot.getStackCount() + " of " + slot.getName());
                System.out.println("Please enter amount of this item to remove: ");
                int amount = Integer.parseInt(scanner.next());
                if (!inventory.removeItem(slotNum, amount)) {
                    System.out.println("Not enough items to remove!");
                } else {
                    System.out.println("Item successfully removed.");
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid slot number.");
        }
    }

    // MODIFIES: inventory, hand
    // EFFECTS: prompts user and picks up an item from inventory
    private static void doHoldItem(Inventory inventory, Hand hand) {
        try {
            System.out.println("Please enter target slot number: ");
            int slotNum = Integer.parseInt(scanner.next()) - 1;
            System.out.println("Please enter amount to remove: ");
            int amount = Integer.parseInt(scanner.next());
            if (!hand.hold(inventory, slotNum, amount)) {
                System.out.println("Cannot hold items from targeted slot.");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid slot number.");
        }
    }

    // MODIFIES: inventory, hand
    // EFFECTS: prompts user and drops an item from hand into inventory
    private static void doDropItem(Inventory inventory, Hand hand) {

        try {
            System.out.println("Please enter target slot number: ");
            int slotNum = Integer.parseInt(scanner.next()) - 1;
            System.out.println("Please enter amount to drop: ");
            int amount = Integer.parseInt(scanner.next());
            if (!hand.drop(inventory, slotNum, amount)) {
                System.out.println("Cannot drop items into targeted slot.");
            } else {
                System.out.println("Items successfully dropped.");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid slot number.");
        }
    }

    // EFFECTS: provides slot info of given slot
    private static void doSlotInfo(Inventory inventory, Hand hand) {
        try {
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
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid slot number.");
        }
    }

    // MODIFIES: inventory.itemBank
    // EFFECTS: prompts user and creates a new item
    private static void doCreateItem(Inventory inventory, Hand hand) {
        System.out.println("Please enter the item's name: ");
        String name = scanner.next();
        System.out.println("Please enter the item's maximum stack count: ");
        int maxStack = Integer.parseInt(scanner.next());
        System.out.println(inventory.createItem(name, maxStack));
        System.out.println("Item successfully created.");
    }
}
