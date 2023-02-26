package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.basic.BasicTextFieldUI;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {

    Hand testHand;
    Item item1;
    Item item2;
    Item item3;
    Item item5;
    Item item69;
    Inventory inventoryTest;


    @BeforeEach
    void testSetUp() {
        testHand = new Hand();
        item1 = new Item("sword", 1, 4);
        item2 = new Item("a", 2, 99);
        item3 = new Item("natural recursion", 3, 3);
        item5 = new Item("5", 5, 5);
        item69 = new Item("69", 69, 69);
        inventoryTest = new Inventory();
    }

    @Test
    void testHoldSingleItem() {
        // 1 item
        inventoryTest.insertItem(0, item1);
        inventoryTest.insertItem(1, item2);
        assertFalse(testHand.hold(inventoryTest, 0, 2));
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
        assertEquals("sword" ,inventoryTest.getNthSlot(0).getName());

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
        assertTrue(testHand.hold(inventoryTest, 0, 1));
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

    @Test
    void testHoldTargetAmountVariation() {
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        assertFalse(testHand.hold(inventoryTest, 1, 3));
        assertTrue(testHand.hold(inventoryTest, 1, 2));
        inventoryTest.insertItem(1, item2);
        assertTrue(testHand.hold(inventoryTest, 1, 1));
    }

    @Test
    void testHoldThatOneYellowLine43() {
        // target is not empty
        // target's stack count is greater than requested amount
        // target ID doesn't match
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(2, item69);
        assertTrue(testHand.hold(inventoryTest, 2, 1));

        assertFalse(testHand.hold(inventoryTest, 1, 0));
        assertFalse(testHand.hold(inventoryTest, 1, 1));
        assertFalse(testHand.hold(inventoryTest, 1, 2));
        assertFalse(testHand.hold(inventoryTest, 1, 3));
    }

    @Test
    void testHoldNotEnoughSpaceInHandToHold() {
        inventoryTest.insertItem(1, item3);
        inventoryTest.insertItem(1, item3);
        inventoryTest.insertItem(1, item3);
        assertTrue(testHand.hold(inventoryTest, 1, 2));

        inventoryTest.insertItem(1, item3);
        assertFalse(testHand.hold(inventoryTest, 1, 3));
        assertTrue(testHand.hold(inventoryTest, 1, 1));
    }

    @Test
    void testHoldTargetSlotNotEnoughItemsToHold() {
        inventoryTest.insertItem(1, item3);
        inventoryTest.insertItem(1, item3);
        assertFalse(testHand.hold(inventoryTest, 1, 4));
        assertFalse(testHand.hold(inventoryTest, 1, 3));
        assertTrue(testHand.hold(inventoryTest, 1, 2));
        assertFalse(testHand.hold(inventoryTest, 1, 2));
        assertFalse(testHand.hold(inventoryTest, 1, 1));

    }

    @Test
    void testHoldTargetItemDifferentFromHandItem() {
        inventoryTest.insertItem(1, item1);
        inventoryTest.insertItem(2, item2);
        testHand.hold(inventoryTest, 1, 1);

        assertFalse(testHand.hold(inventoryTest, 2, 1));
        inventoryTest.removeItem(2, 1);
        inventoryTest.insertItem(2, item1);
        assertTrue(testHand.hold(inventoryTest, 2, 1));
    }

    @Test
    void test2HoldThatOneYellowLine43() {
        // target is empty
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(2, item69);
        testHand.hold(inventoryTest, 1, 3);

        assertFalse(testHand.hold(inventoryTest, 5, 1));
    }

    @Test
    void testHoldAll3FalseHandNotEmpty() {
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);

        testHand.hold(inventoryTest, 1, 2);
        assertTrue(testHand.hold(inventoryTest, 1, 2));
        assertTrue(testHand.hold(inventoryTest, 1, 1));
    }

    @Test
    void testHoldFirstFalseHandNotEmpty() {
        // NOT: target has enough items
        // ID matches
        // hand has enough space
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);

        testHand.hold(inventoryTest, 1, 3);
        assertFalse(testHand.hold(inventoryTest, 1, 5));
        assertFalse(testHand.hold(inventoryTest, 1, 4));
        assertFalse(testHand.hold(inventoryTest, 1, 3));
        assertTrue(testHand.hold(inventoryTest, 1, 2));
    }

    @Test
    void testHoldSecondFalseHandNotEmpty() {
        // target has enough items
        // NOT: ID matches
        // hand has enough space
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);

        inventoryTest.insertItem(2, item69);
        inventoryTest.insertItem(2, item69);
        inventoryTest.insertItem(2, item69);
        inventoryTest.insertItem(2, item69);
        inventoryTest.insertItem(2, item69);

        testHand.hold(inventoryTest, 1, 2);
        assertFalse(testHand.hold(inventoryTest, 2, 1));
        assertFalse(testHand.hold(inventoryTest, 2, 2));
        assertFalse(testHand.hold(inventoryTest, 2, 3));
        assertFalse(testHand.hold(inventoryTest, 2, 4));
        assertFalse(testHand.hold(inventoryTest, 2, 5));

    }

    @Test
    void testHoldThirdFalseHandNotEmpty() {
        // target has enough items
        // ID matches
        // NOT: hand has enough space
        inventoryTest.insertItem(1, item3);
        inventoryTest.insertItem(1, item3);
        inventoryTest.insertItem(1, item3);
        inventoryTest.insertItem(2, item3);
        inventoryTest.insertItem(2, item3);
        testHand.hold(inventoryTest, 1, 2);

        assertFalse(testHand.hold(inventoryTest, 2, 2));
        assertTrue(testHand.hold(inventoryTest, 2, 1));
        assertFalse(testHand.hold(inventoryTest, 2, 1));
    }

    @Test
    void testHoldFirstAndSecondFalseHandNotEmpty() {
        // NOT: target has enough items
        // NOT: ID matches
        // hand has enough space
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);
        inventoryTest.insertItem(1, item2);

        inventoryTest.insertItem(2, item69);
        inventoryTest.insertItem(2, item69);
        inventoryTest.insertItem(2, item69);
        inventoryTest.insertItem(2, item69);
        inventoryTest.insertItem(2, item69);

        testHand.hold(inventoryTest, 1, 3);
        assertFalse(testHand.hold(inventoryTest, 2, 6));
        assertFalse(testHand.hold(inventoryTest, 2, 7));
        assertFalse(testHand.hold(inventoryTest, 2, 8));
        assertFalse(testHand.hold(inventoryTest, 2, 5));
        assertFalse(testHand.hold(inventoryTest, 1, 3));

    }

    @Test
    void testHoldSecondAndThirdFalseHandNotEmpty() {
        // target has enough items
        // NOT: ID matches
        // NOT: hand has enough space
        inventoryTest.insertItem(1, item3);
        inventoryTest.insertItem(1, item3);
        inventoryTest.insertItem(1, item3);

        inventoryTest.insertItem(2, item69);
        inventoryTest.insertItem(2, item69);
        inventoryTest.insertItem(2, item69);
        inventoryTest.insertItem(2, item69);
        inventoryTest.insertItem(2, item69);

        testHand.hold(inventoryTest, 1, 2);
        assertFalse(testHand.hold(inventoryTest, 2, 2));
        assertFalse(testHand.hold(inventoryTest, 2, 3));
        assertFalse(testHand.hold(inventoryTest, 2, 4));
        assertFalse(testHand.hold(inventoryTest, 2, 5));

        assertFalse(testHand.hold(inventoryTest, 2, 1));
    }

    @Test
    void testHoldFirstAndThirdFalseHandNotEmpty() {
        // NOT: target has enough items
        // ID matches
        // NOT: hand has enough space
        inventoryTest.insertItem(1, item5);
        inventoryTest.insertItem(1, item5);
        inventoryTest.insertItem(1, item5);
        inventoryTest.insertItem(1, item5);
        inventoryTest.insertItem(1, item5);

        inventoryTest.insertItem(2, item5);
        inventoryTest.insertItem(2, item5);
        inventoryTest.insertItem(2, item5);
        inventoryTest.insertItem(2, item5);
        inventoryTest.insertItem(2, item5);

        testHand.hold(inventoryTest, 1, 4);
        assertFalse(testHand.hold(inventoryTest, 2, 6));
        assertFalse(testHand.hold(inventoryTest, 2, 7));
        assertFalse(testHand.hold(inventoryTest, 2, 8));

        assertFalse(testHand.hold(inventoryTest, 2, 5));
        assertFalse(testHand.hold(inventoryTest, 2, 4));
        assertFalse(testHand.hold(inventoryTest, 2, 3));
        assertFalse(testHand.hold(inventoryTest, 2, 2));
    }

    @Test
    void testHoldAllFalseHandNotEmpty() {
        // NOT: target has enough items
        // NOT: ID matches
        // NOT: hand has enough space
        inventoryTest.insertItem(1, item5);
        inventoryTest.insertItem(1, item5);
        inventoryTest.insertItem(1, item5);
        inventoryTest.insertItem(1, item5);
        inventoryTest.insertItem(1, item5);

        inventoryTest.insertItem(2, item5);
        inventoryTest.insertItem(2, item5);
        inventoryTest.insertItem(2, item5);
        inventoryTest.insertItem(2, item5);
        inventoryTest.insertItem(2, item5);

        inventoryTest.insertItem(3, item3);
        inventoryTest.insertItem(3, item3);
        inventoryTest.insertItem(3, item3);

        testHand.hold(inventoryTest, 2, 3);

        assertFalse(testHand.hold(inventoryTest, 3, 4));
        assertFalse(testHand.hold(inventoryTest, 3, 5));
    }

    @Test
    void testHoldAllTrueHandNotEmpty() {
        // target has enough items
        // ID matches
        // hand has enough space
        inventoryTest.insertItem(1, item5);
        inventoryTest.insertItem(1, item5);
        inventoryTest.insertItem(1, item5);
        inventoryTest.insertItem(1, item5);
        inventoryTest.insertItem(1, item5);
        testHand.hold(inventoryTest, 1, 1);

        assertTrue(testHand.hold(inventoryTest, 1, 1));
        assertTrue(testHand.hold(inventoryTest, 1, 2));
        assertTrue(testHand.hold(inventoryTest, 1, 1));
    }

    @Test
    void testDropSingleItem() {
        inventoryTest.insertItem(1, item1);
        testHand.hold(inventoryTest, 1, 1);
        assertTrue(testHand.drop(inventoryTest, 2, 1));
        assertEquals(-1, testHand.getHand().getItemID());
        assertEquals(1, inventoryTest.getNthSlot(2).getItemID());
        assertEquals(1, inventoryTest.getNthSlot(2).getStackCount());
    }

    @Test
    void testDropMultipleItemsIntoEmpty() {
        inventoryTest.insertItem(1, item1);
        inventoryTest.insertItem(1, item1);
        inventoryTest.insertItem(1, item1);
        testHand.hold(inventoryTest, 1, 3);

        assertTrue(testHand.drop(inventoryTest, 1, 3));
        assertTrue(testHand.getHand() instanceof EmptySlot);
        assertEquals(3, inventoryTest.getNthSlot(1).getStackCount());
        assertEquals(1, inventoryTest.getNthSlot(1).getItemID());
    }

    @Test
    void testDropMultipleItemsIntoSameItem() {
        inventoryTest.insertItem(2, item1);
        inventoryTest.insertItem(2, item1);
        inventoryTest.insertItem(2, item1);
        inventoryTest.insertItem(2, item1);
        testHand.hold(inventoryTest, 2, 3);

        assertTrue(testHand.drop(inventoryTest, 2, 2));
        assertEquals(1, testHand.getHeldAmount());
        assertEquals(3, inventoryTest.getNthSlot(2).getStackCount());
    }

    @Test
    void testDropSingleItemIntoDifferentSingleItem() {
        inventoryTest.insertItem(1, item1);
        inventoryTest.insertItem(2, item2);
        testHand.hold(inventoryTest, 2, 1);

        assertTrue(testHand.drop(inventoryTest, 1, 1));
        assertEquals(1, testHand.getHand().getItemID());
        assertEquals(2, inventoryTest.getNthSlot(1).getItemID());
        assertEquals("sword", testHand.getHand().getName());
        assertEquals("a", inventoryTest.getNthSlot(1).getName());
    }

    @Test
    void testDropMultipleItemIntoDifferentItems() {
        inventoryTest.insertItem(1, item1);
        inventoryTest.insertItem(1, item1);

        inventoryTest.insertItem(2, item2);
        inventoryTest.insertItem(2, item2);
        inventoryTest.insertItem(2, item2);

        testHand.hold(inventoryTest, 2, 2);

        assertTrue(testHand.drop(inventoryTest, 1, 2));
        assertEquals(2, inventoryTest.getNthSlot(1).getItemID());
        assertEquals(2, inventoryTest.getNthSlot(1).getStackCount());

        assertEquals(2, inventoryTest.getNthSlot(2).getItemID());
        assertEquals(1, inventoryTest.getNthSlot(2).getStackCount());

        assertEquals(1, testHand.getHand().getItemID());
        assertEquals(2, testHand.getHeldAmount());
    }

    @Test
    void testDropEmptyHand() {
        assertFalse(testHand.drop(inventoryTest, 1, 1));
    }

    @Test
    void testDropNotEnoughItemsInHand() {
        inventoryTest.insertItem(1, item1);
        inventoryTest.insertItem(2, item2);
        testHand.hold(inventoryTest, 2, 1);

        assertFalse(testHand.drop(inventoryTest, 1, 2));
        assertFalse(testHand.drop(inventoryTest, 5, 2));
        assertTrue(testHand.drop(inventoryTest, 5, 1));

    }

    @Test
    void testDropWouldExceedMaxStackCount() {
        inventoryTest.insertItem(5, item1);
        inventoryTest.insertItem(5, item1);
        inventoryTest.insertItem(5, item1);
        inventoryTest.insertItem(5, item1);
        testHand.hold(inventoryTest, 5, 4);
        inventoryTest.insertItem(5, item1);
        assertFalse(testHand.drop(inventoryTest, 5, 4));
        assertTrue(testHand.drop(inventoryTest, 6, 4));
    }

    @Test
    void testDropSwapButNotWithFullHandAmount() {
        inventoryTest.insertItem(2, item1);
        inventoryTest.insertItem(2, item1);
        inventoryTest.insertItem(2, item1);
        inventoryTest.insertItem(2, item1);
        inventoryTest.insertItem(3, item3);
        inventoryTest.insertItem(3, item3);
        testHand.hold(inventoryTest, 2, 3);

        assertFalse(testHand.drop(inventoryTest, 3, 1));
        assertFalse(testHand.drop(inventoryTest, 3, 2));
        assertTrue(testHand.drop(inventoryTest, 3, 3));
    }

    @Test
    void testHandTextView() {
        assertEquals("Hand: [ ]", testHand.handTextView());

        inventoryTest.insertItem(0, item1);
        testHand.hold(inventoryTest, 0, 1);
        assertEquals("Hand: [sword 1x]", testHand.handTextView());
    }


}
