package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestInventory {

    private int inventorySize = Inventory.inventorySize;
    Inventory inventoryTest;
    Item nuke;
    Item ur_mom;
    Item shork;
    Item skull;


    @BeforeEach
    void setUp() {
        inventoryTest = new Inventory();

        nuke = new Item("Nuke", 1, 1);
        ur_mom = new Item("ur mom", 2, 1);
        shork = new Item("Shork", 3, 99);
        skull = new Item("Skull", 4, 99);

    }

    @Test
    void testConstructor() {
       assertEquals(inventorySize, inventoryTest.getListSize());
    }

    @Test
    void testInsertItem() {
        inventoryTest.insertItem(0, nuke);
        assertEquals(1, inventoryTest.getNthSlot(0).getItemID());
        assertFalse(inventoryTest.insertItem(0, nuke));

        inventoryTest.insertItem(7, nuke);
        assertEquals(1, inventoryTest.getNthSlot(7).getItemID());
        assertFalse(inventoryTest.insertItem(7, shork));

        inventoryTest.insertItem(5, shork);
        inventoryTest.insertItem(5, shork);
        assertEquals(2, inventoryTest.getNthSlot(5).getStackCount());
        inventoryTest.insertItem(5, shork);
        assertEquals(3, inventoryTest.getNthSlot(5).getStackCount());
        inventoryTest.insertItem(5, shork);
        inventoryTest.insertItem(5, shork);
        assertEquals(5, inventoryTest.getNthSlot(5).getStackCount());
        assertFalse(inventoryTest.insertItem(5, skull));

        for (int i = 6; i <= 98; i++) {
            inventoryTest.insertItem(5, shork);
        }
        assertEquals(98, inventoryTest.getNthSlot(5).getStackCount());
        inventoryTest.insertItem(5, shork);
        assertEquals(99, inventoryTest.getNthSlot(5).getStackCount());
        assertFalse(inventoryTest.insertItem(5, shork));
    }

    @Test
    void testRemoveItem() {
        assertFalse(inventoryTest.removeItem(3, 1));

        inventoryTest.insertItem(0, nuke);
        assertFalse(inventoryTest.removeItem(0, 2));
        inventoryTest.removeItem(0, 1);
        assertTrue(inventoryTest.getNthSlot(0) instanceof EmptySlot);

        for (int i = 1; i <= 10; i++) {
            inventoryTest.insertItem(4, skull);
        }

        inventoryTest.removeItem(4, 1);
        assertEquals(inventoryTest.getNthSlot(4).getStackCount(), 9);
        inventoryTest.removeItem(4, 6);
        assertEquals(inventoryTest.getNthSlot(4).getStackCount(), 3);
        inventoryTest.removeItem(4, 3);
        assertTrue(inventoryTest.getNthSlot(4) instanceof EmptySlot);
        assertFalse(inventoryTest.removeItem(4, 5));
    }

}