package model;

public interface Slot {
    String getName();

    int getItemID();

    int getStackCount();

    int getMaxStackSize();

    void increaseStackCount(int n);

    void decreaseStackCount(int amount);
}
