package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.LinkedList;

// represents the whole inventory
public class Inventory implements Writable {

    private int inventorySize = 10;

    private LinkedList<Slot> inventory;
    private EmptySlot blank = new EmptySlot();
    ItemBank itemBank;


    // REQUIRES: inventorySize > 0
    // EFFECTS: Constructs an empty inventory with size = inventorySize
    public Inventory() {
        LinkedList<Slot> tempList = new LinkedList<>();
        for (int i = 1; i <= inventorySize; i++) {
            tempList.add(blank);
        }
        this.inventory = tempList;
        itemBank = new ItemBank();
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

    // EFFECTS: turn an inventory into a readable string
    public String textView() {

        String inventoryString = "";

        for (int i = 0; i < this.inventory.size(); i++) {
            if (inventory.get(i) instanceof EmptySlot) {
                inventoryString = inventoryString + "[ ]";
            } else {
                inventoryString = inventoryString + "[" + inventory.get(i).getName()
                        + " " + inventory.get(i).getStackCount() + "x" + "]";
            }
        }

        return inventoryString;
    }

    // MODIFIES: itemBank
    // EFFECTS: Creates a new item with a unique ID and adds to itemBank, then returns statement with its ID
    public String createItem(String itemName, int maxStackSize) {
        int nextID = itemBank.getNextID();
        itemBank.add(new Item(itemName, nextID, maxStackSize));
        return "This item's ID is " + nextID + ".";
    }

    // REQUIRES: given ID is unique (no other item in itemBank has it)
    // MODIFIES: itemBank
    // EFFECTS: Creates a new item with given ID and adds to itemBank
    public void createSpecificItem(String itemName, int id, int maxStackSize) {
        itemBank.add(new Item(itemName, id, maxStackSize));
    }

    // REQUIRES: every slot in inventory is legal
    // EFFECTS: organizes inventory by stacking up as many items as possible to the front of the inventory
    public void organize() {
        int slotNum = 0;

        for (Slot slot : inventory) {
            if (!(slot instanceof EmptySlot)) {
                String slotName = slot.getName();
                int slotID = slot.getItemID();
                int slotStackCount = slot.getStackCount();
                int slotMaxSize = slot.getMaxStackSize();
                Item tempItem = new Item(slotName, slotID, slotMaxSize);
                tempItem.setStackCount(slotStackCount);
                removeItem(slotNum, slotStackCount);

                int i = 0;
                while (!insertItem(i, tempItem)) {
                    i++;
                }
            }
            slotNum++;
        }
    }

    // MODIFIES: this
    // REQUIRES: size > 0
    // EFFECTS: adds extra slots to inventory
    public void increaseSlots(int size) {
        for (int i = inventorySize; i < inventorySize + size; i++) {
            inventory.add(blank);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds given slot to end of inventory
    public void addSlot(Slot slot) {
        inventory.add(slot);
    }

    // Based on https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo in WorkRoom.toJson
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("inventory", slotsToJson());
        json.put("itemBank", itemBank.toJsonArray());
        return json;
    }

    // EFFECTS: returns slots in this inventory as a JSON array
    // Based on https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo in WorkRoom.thingiesToJson
    private JSONArray slotsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Slot slot : inventory) {
            jsonArray.put(slot.toJson());
        }

        return jsonArray;
    }



    // REQUIRES: item ID has never been used before, stackCount and maxStackSize > 0, slotNum is within range of list
    // MODIFIES: this
    // EFFECTS: sets given slot to a new slot with given information
    public void setSlot(int slotNum, String itemName, int id, int stackCount, int maxStackSize) {
        inventory.set(slotNum, new FilledSlot(itemName, id, stackCount, maxStackSize));
    }

    // REQUIRES: 0 >= n > inventorySize
    public Slot getNthSlot(int n) {
        return inventory.get(n);
    }

    public int getListSize() {
        return inventory.size();
    }

    public ItemBank getItemBank() {
        return itemBank;
    }

}
