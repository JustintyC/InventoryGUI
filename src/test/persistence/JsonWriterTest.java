package persistence;

import exceptions.InvalidItemIDException;
import model.EmptySlot;
import model.Inventory;
import model.Item;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// Based on https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo in JsonWriterTest
public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Inventory inventory = new Inventory();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyInventory() {
        try {
            Inventory inventory = new Inventory();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyInventory.json");
            writer.open();
            writer.write(inventory);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyInventory.json");
            inventory = reader.read();
            int size = inventory.getListSize();
            for (int i = 0; i < size; i++) {
                assertTrue(inventory.getNthSlot(i) instanceof EmptySlot);
            }
            assertEquals(1, inventory.getItemBank().getNextID());
            try {
                inventory.getItemBank().findItem(1);
                fail("Unexpected item found in empty itemBank");
            } catch (InvalidItemIDException e) {
                // pass
            }

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralInventory() {
        try {
            Inventory inventory = new Inventory();
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralInventory.json");
            inventory.createItem("item1", 5);
            inventory.createItem("item2", 5);
            Item item1 = new Item("item1", 1, 5);
            Item item2 = new Item("item2", 2, 5);
            inventory.insertItem(0, item1);
            inventory.insertItem(0, item1);
            inventory.insertItem(0, item1);
            inventory.insertItem(2, item1);
            inventory.insertItem(5, item1);
            inventory.insertItem(1, item2);
            inventory.insertItem(1, item2);
            writer.open();
            writer.write(inventory);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralInventory.json");
            inventory = reader.read();

            assertEquals(1, inventory.getNthSlot(0).getItemID());
            assertEquals(3, inventory.getNthSlot(0).getStackCount());
            assertEquals("item1", inventory.getNthSlot(0).getName());
            assertEquals(5, inventory.getNthSlot(0).getMaxStackSize());

            assertEquals(2, inventory.getNthSlot(1).getItemID());
            assertEquals(2, inventory.getNthSlot(1).getStackCount());
            assertEquals("item2", inventory.getNthSlot(1).getName());
            assertEquals(5, inventory.getNthSlot(1).getMaxStackSize());

            assertEquals(1, inventory.getNthSlot(2).getItemID());
            assertEquals(1, inventory.getNthSlot(2).getStackCount());
            assertEquals("item1", inventory.getNthSlot(2).getName());
            assertEquals(5, inventory.getNthSlot(2).getMaxStackSize());

            assertTrue(inventory.getNthSlot(3) instanceof EmptySlot);

            assertTrue(inventory.getNthSlot(4) instanceof EmptySlot);

            assertEquals(1, inventory.getNthSlot(5).getItemID());
            assertEquals(1, inventory.getNthSlot(5).getStackCount());

            assertTrue(inventory.getNthSlot(6) instanceof EmptySlot);

            assertTrue(inventory.getNthSlot(7) instanceof EmptySlot);

            assertTrue(inventory.getNthSlot(8) instanceof EmptySlot);

            assertTrue(inventory.getNthSlot(9) instanceof EmptySlot);

            assertEquals("item1", inventory.getItemBank().findItem(1).getItemName());
            assertEquals("item2", inventory.getItemBank().findItem(2).getItemName());

        } catch (IOException e) {
            fail("Unexpected IOException");
        } catch (InvalidItemIDException e) {
            fail("Unexpected InvalidItemIdException");
        }
    }
}
