package model;

// represents an item with its name, unique ID, current stack count, and max stack size
public class Item {

    private String itemName; // item's name
    private int itemID;      // item's unique ID
    private int stackCount;  // item's current stack count
    private int maxStackSize;// item's maximum stack count


    // EFFECTS: constructs an item with name, ID, stackSize of 1, and size of maximum stack
    public Item(String itemName, int id, int maxStackSize) {
        this.itemName = itemName;
        this.itemID = id;
        this.stackCount = 1;
        this.maxStackSize = maxStackSize;
    }

    // REQUIRES: maxStackSize >= count > 0
    // EFFECTS: sets the item's stackCount to given int
    public void setStackCount(int count) {
        this.stackCount = count;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemID() {
        return itemID;
    }

    public int getStackCount() {
        return stackCount;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

}
