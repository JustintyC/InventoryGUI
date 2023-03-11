package ui;

import exceptions.InvalidItemIDException;
import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.uiexceptions.InvalidSaveSlotException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

// inventory text UI that handles inputs and produces corresponding information
public class InventoryApp {
    private Scanner scanner = new Scanner(System.in); // Scanner from B04-SimpleCalculatorStaterLecLab
    Inventory inventory;
    Hand hand;

    // Based on https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private static final String JSON_STORE1 = "./data/inventory1.json";
    private static final String JSON_STORE2 = "./data/inventory2.json";
    private static final String JSON_STORE3 = "./data/inventory3.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: Constructs an instance of inventory and runs application
    public InventoryApp() {
        inventory = new Inventory();
        hand = new Hand();
        inventory.getItemBank().add(new Item("UBC Card", 1, 1));
        inventory.getItemBank().add(new Item("Pencil", 2, 20));
        inventory.getItemBank().add(new Item("Ball", 3, 10));
        inventory.getItemBank().add(new Item("Sensible Footwear", 4, 1));
        inventory.getItemBank().add(new Item("Laptop", 5, 1));
        inventory.getItemBank().add(new Item("Phone", 6, 10));
        inventory.getItemBank().add(new Item("Credit Card", 7, 5));
        inventory.getItemBank().add(new Item("Vodka", 8, 1));
        inventory.getItemBank().add(new Item("Keys", 9, 5));
        inventory.getItemBank().add(new Item("Suspicious Papers", 10, 20));

        jsonWriter = new JsonWriter(JSON_STORE1);
        jsonReader = new JsonReader(JSON_STORE1);
        runInventoryApp();
    }

    // EFFECTS: Initiates an inventory from a video game with console-based user interaction
    private void runInventoryApp() {
        String commandList = "AddItem, RemoveItem, CreateItem, Hold, Drop, SlotInfo, Organize, ViewInventory, Save, "
                + "Load, Quit";
        System.out.println("Available commands: " + commandList);
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
                handleInput(instruction, inventory, hand, commandList);
            }
        }
    }


    // EFFECTS: Produces results according to inputs.
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void handleInput(String input, Inventory inventory, Hand hand, String commandList) {
        switch (input) {
            case "AddItem":
                doAddItem(inventory);
                break;
            case "RemoveItem":
                doRemoveItem(inventory);
                break;
            case "Hold":
                doHoldItem(inventory, hand);
                break;
            case "Drop":
                doDropItem(inventory, hand);
                break;
            case "CreateItem":
                doCreateItem(inventory);
                break;
            case "SlotInfo":
                doSlotInfo(inventory);
                break;
            case "Organize":
                inventory.organize();
                System.out.println("Inventory organized.");
                break;
            case "ViewInventory":
                System.out.println(inventory.textView());
                break;
            case "Save":
                try {
                    doSave();
                } catch (InvalidSaveSlotException e) {
                    System.out.println("Invalid save slot.");
                }
                break;
            case "Load":
                try {
                    doLoad();
                } catch (InvalidSaveSlotException e) {
                    System.out.println("Invalid save slot.");
                }
                break;
            default:
                System.out.println("Unknown command. Please enter one of: " + commandList);
                break;
        }
    }

    // MODIFIES: inventory
    // EFFECTS: prompts user and adds an item to inventory
    private void doAddItem(Inventory inventory) {
        ItemBank itemBank = inventory.getItemBank();

        System.out.println("Please enter slot number to add item into: ");
        int slot = parseInt(scanner.next()) - 1;
        System.out.println("Please enter item ID of the item you wish to add: ");
        int id = parseInt(scanner.next());
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
    private void doRemoveItem(Inventory inventory) {
        try {
            System.out.println("Please enter slot number to remove item from: ");
            int slotNum = parseInt(scanner.next()) - 1;
            if (inventory.getNthSlot(slotNum) instanceof EmptySlot) {
                System.out.println("This slot is empty!");
            } else {
                Slot slot = inventory.getNthSlot(slotNum);
                System.out.println("This slot has " + slot.getStackCount() + " of " + slot.getName());
                System.out.println("Please enter amount of this item to remove: ");
                int amount = parseInt(scanner.next());
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
    private void doHoldItem(Inventory inventory, Hand hand) {
        try {
            System.out.println("Please enter target slot number: ");
            int slotNum = parseInt(scanner.next()) - 1;
            System.out.println("Please enter amount to remove: ");
            int amount = parseInt(scanner.next());
            if (!hand.hold(inventory, slotNum, amount)) {
                System.out.println("Cannot hold items from targeted slot.");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid slot number.");
        }
    }

    // MODIFIES: inventory, hand
    // EFFECTS: prompts user and drops an item from hand into inventory
    private void doDropItem(Inventory inventory, Hand hand) {

        try {
            System.out.println("Please enter target slot number: ");
            int slotNum = parseInt(scanner.next()) - 1;
            System.out.println("Please enter amount to drop: ");
            int amount = parseInt(scanner.next());
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
    private void doSlotInfo(Inventory inventory) {
        try {
            System.out.println("Please enter slot number: ");
            int slotNum = parseInt(scanner.next()) - 1;
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
    private void doCreateItem(Inventory inventory) {
        System.out.println("Please enter the item's name: ");
        String name = scanner.next();
        System.out.println("Please enter the item's maximum stack count: ");
        int maxStack = parseInt(scanner.next());
        System.out.println(inventory.createItem(name, maxStack));
        System.out.println("Item successfully created.");
    }

    // EFFECTS: saves current inventory to file
    // Based on https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private void doSave() throws InvalidSaveSlotException {
        System.out.println("Select save slot: [1] [2] [3]");
        int saveSlotNum = parseInt(scanner.next());
        switch (saveSlotNum) {
            case 1:
                this.jsonWriter = new JsonWriter(JSON_STORE1);
                break;
            case 2:
                this.jsonWriter = new JsonWriter(JSON_STORE2);
                break;
            case 3:
                this.jsonWriter = new JsonWriter(JSON_STORE3);
                break;
            default:
                throw new InvalidSaveSlotException();
        }
        try {
            jsonWriter.open();
            jsonWriter.write(inventory);
            jsonWriter.close();
            System.out.println("Saved current inventory to slot " + saveSlotNum + ".");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file to slot " + saveSlotNum + ".");
        }
    }

    // EFFECTS: load inventory from file
    // Based on https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private void doLoad() throws InvalidSaveSlotException {
        System.out.println("Select save slot: [1] [2] [3]");
        int saveSlotNum = parseInt(scanner.next());
        switch (saveSlotNum) {
            case 1:
                this.jsonReader = new JsonReader(JSON_STORE1);
                break;
            case 2:
                this.jsonReader = new JsonReader(JSON_STORE2);
                break;
            case 3:
                this.jsonReader = new JsonReader(JSON_STORE3);
                break;
            default:
                throw new InvalidSaveSlotException();
        }
        try {
            inventory = jsonReader.read();
            System.out.println("Loaded inventory from slot " + saveSlotNum + ".");
        } catch (IOException e) {
            System.out.println("Unable to read from slot " + saveSlotNum + ".");
        }
    }
}
