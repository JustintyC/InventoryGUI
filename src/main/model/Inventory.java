package model;

import java.util.LinkedList;

public class Inventory {

    public static final int inventorySize = 10;

    private LinkedList<Slot> inventory;
    private EmptySlot blank = new EmptySlot();

    // REQUIRES: inventorySize > 0
    // EFFECTS: Constructs an empty inventory with size = inventorySize
    public Inventory() {
        LinkedList<Slot> tempList = new LinkedList<>();

        for (int i = 1; i <= inventorySize; i++) {
            tempList.add(blank);
        }

        this.inventory = tempList;

    }

    // REQUIRES: 0 >= slotNumber > inventorySize
    // MODIFIES: this, inventory.get(slotNumber)
    // EFFECTS: attempts to insert an item into the slotNumber^th slot of inventory.
    //          if the slot is an EmptySlot, create FilledSlot with item data and return true. (1)
    //          else (the slot is a FilledSlot):
    //             if the FilledSlot has a different item ID from the current item, return false (2)
    //             if the FilledSlot's stackCount > maxStackSize after adding item, return false (3)
    //             else add item's stackSize to the FilledSlot's stackCount, return true (4)
    public Boolean insertItem(int slotNumber, Item item) {
        Slot currentSlot = inventory.get(slotNumber);
        int insertAmount = item.getStackCount();

        String itemName = item.getItemName();
        int itemID = item.getItemID();
        int stackCount = item.getStackCount();
        int maxStackSize = item.getMaxStackSize();

        if (currentSlot instanceof EmptySlot) { // (1)
            Slot newFilledSlot = new FilledSlot(itemName, itemID, stackCount, maxStackSize);
            inventory.set(slotNumber, newFilledSlot);
            return true;
        } else if (currentSlot.getItemID() != item.getItemID()) { // (2)
            return false;
        } else if (currentSlot.getStackCount() + insertAmount > currentSlot.getMaxStackSize()) { // (3)
            return false;
        } else { // (4)
            inventory.get(slotNumber).increaseStackCount(insertAmount);
            return true;
        }
    }

    // REQUIRES:  0 >= slotNumber > inventorySize, amount > 0
    // MODIFIES: this, inventory.get(slotNumber)
    // EFFECTS: attempts to remove amount number of items from slotNumber^th slot of inventory.
    //          if the slot is an EmptySlot, return false
    //          else (the slot is a FilledSlot):
    //          if the FilledSlot's stackCount - amount is < 0, return false
    //          else subtract amount from FilledSlot's stackCount
    //              ** if the FilledSlot's stackCount = 0, replace the FilledSlot with an EmptySlot
    //              return true
    public Boolean removeItem(int slotNumber, int amount) {
        Slot currentSlot = inventory.get(slotNumber);

        if (currentSlot instanceof EmptySlot) {
            return false;
        } else if (currentSlot.getStackCount() - amount < 0) {
            return false;
        } else {
            currentSlot.decreaseStackCount(amount);
            if (currentSlot.getStackCount() == 0) {
                inventory.set(slotNumber, blank);
            }
            return true;
        }
    }

    // REQUIRES: 0 >= n > inventorySize
    public Slot getNthSlot(int n) {
        return inventory.get(n);
    }

    public LinkedList<Slot> getInventory() {
        return inventory;
    }

    public int getListSize() {
        return inventory.size();
    }

}
