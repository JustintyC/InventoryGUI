package model;

public abstract class Slot {

    private String itemName;

    public abstract String getName();

    public abstract int getItemID();

    public abstract int getStackCount();

    public abstract int getMaxStackSize();

    public abstract void increaseStackCount(int n);

    public abstract void decreaseStackCount(int amount);
}
