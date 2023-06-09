package model;

// represents temporary selector that holds an item for the user to move around
public class Hand {

    private Slot hand;
    private EmptySlot blank = new EmptySlot();

    public Hand() {
        this.hand = blank;
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: adds given amount of an item into hand from inventory:
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
                holdIntoEmptyHand(inventory, slotNum, amount, targetName, targetID, targetMaxSize);
                return true;
            }
        } else if (targetSlot.getStackCount() < amount
                || targetID != hand.getItemID()
                || hand.getMaxStackSize() - hand.getStackCount() < amount) {
            return false;
        } else {
            holdIntoNonEmptyHand(inventory, slotNum, amount);
            return true;
        }
    }

    // REQUIRES: slotNum, amount are valid
    // EFFECTS: handles case where an item is held from an empty hand
    private void holdIntoNonEmptyHand(Inventory inventory, int slotNum, int amount) {
        hand.increaseStackCount(amount);
        inventory.removeItem(slotNum, amount);
        EventLog.getInstance().logEvent(new Event(amount + " item(s) picked up at slot " + slotNum));
    }

    // REQUIRES: slotNum, amount, targetID, targetMaxSize are valid
    // EFFECTS: handles case where an item is held from a non-empty hand
    private void holdIntoEmptyHand(Inventory inventory, int slotNum, int amount,
                                   String targetName, int targetID, int targetMaxSize) {
        hand = new FilledSlot(targetName, targetID, amount, targetMaxSize);
        inventory.removeItem(slotNum, amount);
        EventLog.getInstance().logEvent(new Event(amount + " item(s) picked up at slot " + slotNum));
    }

    // REQUIRES: hand is holding an item, amount > 0
    // MODIFIES: this
    // EFFECTS: puts or swap given amount of an item from hand into given slot in inventory:
    //          if hand is empty, return false (target: empty or filled)
    //          if not enough items in hand, return false (target: empty or filled)
    //          if dropping would exceed target item's max stack count, return false (target: filled)
    //          if amount != hand's stackSize, return false (target: filled)
    //          if hand and target items are different, try to swap:
    //              if amount == hand's stackSize, return true (target: filled)
    //              otherwise false
    //          if item can be added to inventory, remove requested amount of items from hand and add them to inv
    //              (target: empty)
    public Boolean drop(Inventory inventory, int slotNum, int amount) {
        Slot targetSlot = inventory.getNthSlot(slotNum);
        int targetID = targetSlot.getItemID();
        int targetMaxSize = targetSlot.getMaxStackSize();
        int targetSize = targetSlot.getStackCount();

        if (!(targetSlot instanceof EmptySlot) && targetMaxSize - targetSize < amount
                && targetSlot.getItemID() == hand.getItemID()) {
            return false;
        } else if (hand instanceof EmptySlot || hand.getStackCount() < amount) {
            return false;
        } else if (targetID != hand.getItemID() && !(targetSlot instanceof EmptySlot)) {
            return swap(inventory, slotNum, amount);
        } else {
            return putItemsIn(inventory, slotNum, amount);
        }
    }

    // EFFECTS: checks to see if item in hand can be swapped with given slot in inventory
    //          if given amount isn't the full amount in hand, return false
    //          otherwise swap and return true
    private Boolean swap(Inventory inventory, int slotNum, int amount) {
        Slot targetSlot = inventory.getNthSlot(slotNum);
        String targetName = targetSlot.getName();
        int targetID = targetSlot.getItemID();
        int targetMaxSize = targetSlot.getMaxStackSize();
        int targetSize = targetSlot.getStackCount();

        if (amount == hand.getStackCount()) {
            inventory.setSlot(slotNum, hand.getName(), hand.getItemID(),
                    hand.getStackCount(), hand.getMaxStackSize());
            hand = new FilledSlot(targetName, targetID, targetSize, targetMaxSize);
            EventLog.getInstance().logEvent(new Event("Stack in hand swapped with stack in slot " + slotNum));
            return true;
        } else {
            return false;
        }

    }

    // EFFECTS: else statement of drop; removes requested amount of items from hand and adds to inventory
    private Boolean putItemsIn(Inventory inventory, int slotNum, int amount) {
        Item item = new Item(hand.getName(), hand.getItemID(), hand.getMaxStackSize());
        item.setStackCount(amount);
        inventory.insertItem(slotNum, item);
        hand.decreaseStackCount(amount);
        if (hand.getStackCount() <= 0) {
            this.hand = blank;
        }
        EventLog.getInstance().logEvent(new Event(amount + " item(s) taken from hand"));
        return true;
    }

    // EFFECTS: turns the hand into a readable string
    public String handTextView() {
        if (hand instanceof EmptySlot) {
            return "Hand: [ ]";
        } else {
            return "Hand: [" + hand.getName() + " " + getHeldAmount() + "x]";
        }
    }

    // EFFECTS: clears hand
    public void clearHand() {
        hand = blank;
        EventLog.getInstance().logEvent(new Event("Hand cleared"));
    }

    public Boolean isEmpty() {
        return (hand instanceof EmptySlot);
    }



    public Slot getHand() {
        return hand;
    }

    public int getHeldAmount() {
        return hand.getStackCount();
    }

}
