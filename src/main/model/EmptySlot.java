package model;

public class EmptySlot implements Slot {


    // EFFECTS: constructs an empty slot
    public EmptySlot() {

    }

    @Override
    public int getItemID() {
        return -1;
    }

    @Override
    public int getStackCount() {
        return -1;
    }

    @Override
    public int getMaxStackSize() {
        return -1;
    }

    @Override
    public void increaseStackCount(int n) {
        // this should never run
    }

    @Override
    public void decreaseStackCount(int amount) {

    }

}
