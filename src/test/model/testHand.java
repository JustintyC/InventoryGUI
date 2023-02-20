package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class testHand {

    Hand testHand;
    Item item1;
    Item item2;
    Item item3;
    Item item69;
    Inventory inventoryTest;


    @BeforeEach
    void testSetUp() {
        testHand = new Hand();
        item1 = new Item("ur mom", 1, 4);
        item2 = new Item("a", 2, 99);
        item3 = new Item("natural recursion", 3, 3);
        item69 = new Item("69", 69, 69);
        inventoryTest = new Inventory();
    }

    @Test
    void testHoldSingleItem() {
        // 1 item
        inventoryTest.insertItem(0, item1);
        inventoryTest.insertItem(1, item2);
        assertTrue(testHand.hold(inventoryTest, 0, 1));
        assertEquals(testHand.getHand().getItemID(), 1);
        assertEquals(testHand.getHand().getStackCount(), 1);
        assertEquals(inventoryTest.getNthSlot(0).getName(), " ");

        // different item
        assertFalse(testHand.hold(inventoryTest, 1, 1));
    }

    @Test
    void testHoldMultipleSameItem() {
        inventoryTest.insertItem(0, item1);
        inventoryTest.insertItem(0, item1);
        inventoryTest.insertItem(0, item1);
        inventoryTest.insertItem(0, item1);
        assertTrue(testHand.hold(inventoryTest, 0, 3));
        assertEquals(1, testHand.getHand().getItemID());
        assertEquals(3, testHand.getHand().getStackCount());
        assertEquals("ur mom" ,inventoryTest.getNthSlot(0).getName());

        assertTrue(testHand.hold(inventoryTest, 0, 1));
        assertEquals(4, testHand.getHand().getStackCount());
        assertTrue(inventoryTest.getNthSlot(0) instanceof EmptySlot);
        assertFalse(testHand.hold(inventoryTest, 0, 1));

        inventoryTest.insertItem(0, item1);
        assertFalse(testHand.hold(inventoryTest, 0, 1));

    }

    @Test
    void testHoldDifferentItem() {
        inventoryTest.insertItem(0, item1);
        inventoryTest.insertItem(0, item1);
        inventoryTest.insertItem(1, item2);
        testHand.hold(inventoryTest, 0, 1);

        assertFalse(testHand.hold(inventoryTest, 1, 1));
    }

    @Test
    void testHoldInvalidAmounts() {
        inventoryTest.insertItem(3, item3);
        inventoryTest.insertItem(3, item3);
        assertFalse(testHand.hold(inventoryTest, 3, 3));
        inventoryTest.insertItem(3, item3);
        assertFalse(inventoryTest.insertItem(3, item3));

        assertFalse(testHand.hold(inventoryTest, 3, 5));
        assertFalse(testHand.hold(inventoryTest, 3, 10000));
        assertTrue(testHand.hold(inventoryTest, 3, 3));
        assertFalse(testHand.hold(inventoryTest, 3, 1));
    }


}
