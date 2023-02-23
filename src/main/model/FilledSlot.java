package model;

// represents a non-empty slot
public class FilledSlot extends Slot {

    private String itemName; // item's name
    private int itemID;      // item's unique ID
    private int stackCount;  // item's current stack count
    private int maxStackSize;// item's maximum stack count


    // EFFECTS: constructs an item with name, ID, stackSize, and size of maximum stack
    public FilledSlot(String itemName, int id, int stackCount, int maxStackSize) {
        this.itemName = itemName;
        this.itemID = id;
        this.stackCount = stackCount;
        this.maxStackSize = maxStackSize;
    }


    public String getItemName() {
        return itemName;
    }

    @Override
    public String getName() {
        return itemName;
    }

    @Override
    public int getItemID() {
        return itemID;
    }

    @Override
    public int getStackCount() {
        return stackCount;
    }

    @Override
    public int getMaxStackSize() {
        return maxStackSize;
    }

    // MODIFIES: this
    // EFFECTS: increases stack count by n amount
    @Override
    public void increaseStackCount(int n) {
        this.stackCount = stackCount + n;
    }

    // MODIFIES: this
    // EFFECTS: decreases stack count by amount
    @Override
    public void decreaseStackCount(int amount) {
        this.stackCount = stackCount - amount;
    }
}
