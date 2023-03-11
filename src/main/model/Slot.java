package model;

import org.json.JSONObject;
import persistence.Writable;

// represents a slot in the inventory
public abstract class Slot implements Writable {

    public String getName() {
        return " ";
    }

    public int getItemID() {
        return -1;
    }

    public int getStackCount() {
        return -1;
    }

    public int getMaxStackSize() {
        return -1;
    }

    public void increaseStackCount(int n) {
    }

    public void decreaseStackCount(int amount) {
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", getName());
        json.put("itemID", getItemID());
        json.put("stackCount", getStackCount());
        json.put("maxStackSize", getMaxStackSize());
        return json;
    }
}
