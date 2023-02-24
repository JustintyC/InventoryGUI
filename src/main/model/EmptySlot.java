package model;

import exceptions.EmptySlotException;

// represents an empty slot
public class EmptySlot extends Slot {


    // EFFECTS: constructs an empty slot
    public EmptySlot() {

    }

    @Override
    public String getName() {
        return " ";
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
    }

    @Override
    public void decreaseStackCount(int amount) {
    }

}
