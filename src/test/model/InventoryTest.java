package model;

import exceptions.InvalidItemIDException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    //private int inventorySize = Inventory.inventorySize;
    Inventory inventoryTest;
    Item item1;
    Item item2;
    Item item3;
    Item item4;
    int inventorySize;


    @BeforeEach
    void setUp() {
        inventoryTest = new Inventory();
        inventorySize = inventoryTest.getListSize();

        item1 = new Item("Nuke", 1, 1);
        item2 = new Item("Sword", 2, 1);
        item3 = new Item("Shork", 3, 99);
        item4 = new Item("Skull", 4, 99);
    }

    @Test
    void testConstructor() {
       assertEquals(inventorySize, inventoryTest.getListSize());
    }

    @Test
    void testInsertItem() {
        inventoryTest.insertItem(0, item1);
        assertEquals(1, inventoryTest.getNthSlot(0).getItemID());
        assertFalse(inventoryTest.insertItem(0, item1));

        inventoryTest.insertItem(7, item1);
        assertEquals(1, inventoryTest.getNthSlot(7).getItemID());
        assertFalse(inventoryTest.insertItem(7, item3));

        inventoryTest.insertItem(5, item3);
        inventoryTest.insertItem(5, item3);
        assertEquals(2, inventoryTest.getNthSlot(5).getStackCount());
        inventoryTest.insertItem(5, item3);
        assertEquals(3, inventoryTest.getNthSlot(5).getStackCount());
        inventoryTest.insertItem(5, item3);
        inventoryTest.insertItem(5, item3);
        assertEquals(5, inventoryTest.getNthSlot(5).getStackCount());
        assertFalse(inventoryTest.insertItem(5, item4));

        for (int i = 6; i <= 98; i++) {
            inventoryTest.insertItem(5, item3);
        }
        assertEquals(98, inventoryTest.getNthSlot(5).getStackCount());
        inventoryTest.insertItem(5, item3);
        assertEquals(99, inventoryTest.getNthSlot(5).getStackCount());
        assertFalse(inventoryTest.insertItem(5, item3));
    }

    @Test
    void testRemoveItem() {
        assertFalse(inventoryTest.removeItem(3, 1));

        inventoryTest.insertItem(0, item1);
        assertFalse(inventoryTest.removeItem(0, 2));
        inventoryTest.removeItem(0, 1);
        assertTrue(inventoryTest.getNthSlot(0) instanceof EmptySlot);

        for (int i = 1; i <= 10; i++) {
            inventoryTest.insertItem(4, item4);
        }

        inventoryTest.removeItem(4, 1);
        assertEquals(inventoryTest.getNthSlot(4).getStackCount(), 9);
        inventoryTest.removeItem(4, 6);
        assertEquals(inventoryTest.getNthSlot(4).getStackCount(), 3);
        inventoryTest.removeItem(4, 3);
        assertTrue(inventoryTest.getNthSlot(4) instanceof EmptySlot);
        assertFalse(inventoryTest.removeItem(4, 5));
    }

    @Test
    void testAddSlots() {
        int originalSize = inventorySize;
        inventoryTest.addSlots(10);
        assertEquals(10 + originalSize, inventoryTest.getListSize());
    }

    @Test
    void testTextView() {
        assertEquals("[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]", inventoryTest.textView());
        inventoryTest.insertItem(3, item1);
        assertEquals("[ ][ ][ ][Nuke 1x][ ][ ][ ][ ][ ][ ]", inventoryTest.textView());
        inventoryTest.insertItem(5, item4);
        inventoryTest.insertItem(5, item4);
        inventoryTest.insertItem(5, item4);
        assertEquals("[ ][ ][ ][Nuke 1x][ ][Skull 3x][ ][ ][ ][ ]", inventoryTest.textView());
        inventoryTest.removeItem(5, 3);
        inventoryTest.removeItem(3, 1);
        assertEquals("[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]", inventoryTest.textView());

        inventoryTest.addSlots(10);
        assertEquals("[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]", inventoryTest.textView());
    }

    @Test
    void testCreateItem() {
        assertEquals("This item's ID is " + "1" + ".",
                inventoryTest.createItem("testItem", 5));
        try {
            assertEquals("testItem", inventoryTest.itemBank.findItem(1).getItemName());
        } catch (InvalidItemIDException e) {

        } catch (IndexOutOfBoundsException e) {

        }

        assertEquals("This item's ID is " + "2" + ".",
                inventoryTest.createItem("testItem2", 10));
        try {
            assertEquals("testItem2", inventoryTest.itemBank.findItem(2).getItemName());
        } catch (InvalidItemIDException e) {

        } catch (IndexOutOfBoundsException e) {

        }

    }

    @Test
    void testOrganizeEmpty() {
        inventoryTest.organize();
        assertTrue(inventoryTest.getNthSlot(1) instanceof EmptySlot);
        assertTrue(inventoryTest.getNthSlot(2) instanceof EmptySlot);
        assertTrue(inventoryTest.getNthSlot(3) instanceof EmptySlot);

    }

    @Test
    void testOrganize1Item() {
        inventoryTest.insertItem(3, item4);
        inventoryTest.organize();
        assertEquals(4, inventoryTest.getNthSlot(0).getItemID());
        assertEquals(-1, inventoryTest.getNthSlot(3).getItemID());
    }

    @Test
    void testOrganizeMultipleItems() {
        inventoryTest.insertItem(0, item4);
        inventoryTest.insertItem(1, item4);
        inventoryTest.insertItem(3, item1);
        inventoryTest.insertItem(5, item1);
        inventoryTest.organize();

        assertEquals(4, inventoryTest.getNthSlot(0).getItemID());
        assertEquals(2, inventoryTest.getNthSlot(0).getStackCount());
        assertEquals(1, inventoryTest.getNthSlot(1).getItemID());
        assertEquals(1, inventoryTest.getNthSlot(2).getItemID());
        assertTrue(inventoryTest.getNthSlot(3) instanceof EmptySlot);
        assertTrue(inventoryTest.getNthSlot(5) instanceof EmptySlot);
    }

    @Test
    void testOrganizeMultipleTimes() {
        inventoryTest.insertItem(5, item2);
        inventoryTest.insertItem(7, item3);
        inventoryTest.insertItem(8, item3);
        inventoryTest.insertItem(8, item3);
        inventoryTest.insertItem(8, item3);
        inventoryTest.organize();

        assertEquals(2, inventoryTest.getNthSlot(0).getItemID());
        assertEquals(3, inventoryTest.getNthSlot(1).getItemID());
        assertEquals(4, inventoryTest.getNthSlot(1).getStackCount());

        inventoryTest.insertItem(2, item4);
        inventoryTest.insertItem(3, item4);
        inventoryTest.insertItem(4, item4);
        inventoryTest.insertItem(5, item4);
        inventoryTest.insertItem(6, item3);
        inventoryTest.insertItem(7, item4);
        inventoryTest.insertItem(8, item4);
        inventoryTest.insertItem(9, item4);
        inventoryTest.organize();

        assertEquals(4, inventoryTest.getNthSlot(2).getItemID());
        assertEquals(7, inventoryTest.getNthSlot(2).getStackCount());
        assertEquals(5, inventoryTest.getNthSlot(1).getStackCount());
    }

    @Test
    void testSlotGetNameBecauseJacocoWantsIt() {
        inventoryTest.insertItem(0, item4);
        assertEquals("Skull", inventoryTest.getNthSlot(0).getName());
    }

    @Test
    void testGetItemBank() {
        assertEquals(0, inventoryTest.getItemBank().bank.size());

        inventoryTest.createItem("A", 5);
        assertEquals(1, inventoryTest.getItemBank().bank.size());

        inventoryTest.createItem("B", 56);
        assertEquals(2, inventoryTest.getItemBank().bank.size());

    }


}