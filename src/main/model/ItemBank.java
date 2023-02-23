package model;

import exceptions.InvalidItemIDException;

import java.util.ArrayList;
import java.util.List;

// stores all created items
public class ItemBank {

    List<Item> bank;

    // EFFECTS: Constructs an empty item bank
    public ItemBank() {
        bank = new ArrayList<>();
    }

    // EFFECTS: gets the next item ID that is not taken
    public int getNextID() {
        int rsf = 0;

        for (Item item : this.bank) {
            if (item.getItemID() > rsf) {
                rsf = item.getItemID();
            }
        }

        return rsf + 1;
    }

    // EFFECTS: finds item with given item ID
    public Item findItem(int id) throws InvalidItemIDException {
        for (Item item : bank) {
            if (item.getItemID() == id) {
                return item;
            }
        }
        throw new InvalidItemIDException();
    }

    // REQUIRES: No other item in bank has same ItemID as given item
    // MODIFIES: this
    // EFFECTS: Adds given item to item bank
    public void add(Item item) {
        bank.add(item);
    }

    // EFFECTS: Returns item of given index
    public Item get(int index) {
        return bank.get(index);
    }
}
