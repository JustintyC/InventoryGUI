package model;

// DESCRIPTION
public class Item implements Slot {

    private String itemName;
    private int itemID;
    private int stackCount;
    private int maxStackSize;


    // EFFECTS: constructs an item with name, ID, stackSize of 1, and size of maximum stack
    public Item(String itemName, int id, int maxStackSize) {
        this.itemName = itemName;
        this.itemID = id;
        this.stackCount = 1;
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

    @Override
    public void increaseStackCount(int n) {

    }

    @Override
    public void decreaseStackCount(int amount) {

    }

}
