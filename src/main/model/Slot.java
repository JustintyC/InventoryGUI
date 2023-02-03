package model;

public interface Slot {
    int getItemID();

    int getStackCount();

    int getMaxStackSize();

    void increaseStackCount(int n);

    void decreaseStackCount(int amount);
}
