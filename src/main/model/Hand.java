package model;

import java.util.LinkedList;
import java.util.List;

// "selector" that holds an item for the user to move around
public class Hand {

    private Slot hand;

    public Hand() {
        this.hand = new EmptySlot();
    }

    // REQUIRES: hand is empty
    // MODIFIES: this
    // EFFECTS: adds an item into hand from inventory:
    //          if targeted slot in inventory is empty, return false
    //          if targeted slot in inventory doesn't have enough items, return false
    //          if hand is empty, add items to hand + remove items from inventory + return true
    //          if hand is occupied:
    //              if items are different, return false
    //              if items are the same but hand doesn't have enough space, return false
    //              if items are the same and hand has enough space, add items to hand and remove items from inventory
    //                      return true
    public Boolean hold(Inventory inventory, int slotNum, int amount) {
        Slot targetSlot = inventory.getNthSlot(slotNum);
        String targetName = targetSlot.getName();
        int targetID = targetSlot.getItemID();
        int targetMaxSize = targetSlot.getMaxStackSize();
        int targetSize = targetSlot.getStackCount();

        if (hand instanceof EmptySlot) {
            if (amount > targetSize) {
                return false;
            } else {
                hand = new FilledSlot(targetName, targetID, amount, targetMaxSize);
                inventory.removeItem(slotNum, amount);
                return true;
            }
        } else if (targetSlot instanceof EmptySlot
                || targetSlot.getStackCount() < amount
                || targetID != hand.getItemID()
                || hand.getMaxStackSize() - hand.getStackCount() < amount) {
            return false;
        } else {
            hand.increaseStackCount(amount);
            inventory.removeItem(slotNum, amount);
            return true;
        }
    }

    // REQUIRES: hand is holding an item
    // MODIFIES: this
    // EFFECTS: removes an item from hand into inventory:
    //          if hand is empty, return false
    //          if item in hand doesn't match targeted slot, return false
    //          if item in hand matches targeted slot:
    //              if item cannot be added to inventory due to size, return false
    //              if item can be added to inventory, remove requested amount of items from hand and add them to inv
    public Boolean drop(Inventory inventory, int slotNum, int amount) {
        return false; //stub
    }

    public Boolean isHolding() {
        return !(hand instanceof EmptySlot);
    }

    public Slot getHand() {
        return hand;
    }

    public int getHeldAmount() {
        return hand.getStackCount();
    }

}
