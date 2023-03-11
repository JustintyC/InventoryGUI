package persistence;

import model.EmptySlot;
import model.Inventory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Based on https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo in JsonReaderTest
public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Inventory inventory = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyInventory() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyInventory.json");
        try {
            Inventory inventory = reader.read();
            int size = inventory.getListSize();
            for (int i = 0; i < size; i++) {
                assertTrue(inventory.getNthSlot(i) instanceof EmptySlot);
            }
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralInventory() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralInventory.json");
        try {
            Inventory inventory = reader.read();

            assertEquals(3, inventory.getNthSlot(0).getItemID());
            assertEquals(2, inventory.getNthSlot(0).getStackCount());
            assertEquals("Ball", inventory.getNthSlot(0).getName());
            assertEquals(10, inventory.getNthSlot(0).getMaxStackSize());

            assertEquals(2, inventory.getNthSlot(1).getItemID());
            assertEquals(2, inventory.getNthSlot(1).getStackCount());

            assertEquals(1, inventory.getNthSlot(2).getItemID());
            assertEquals(1, inventory.getNthSlot(2).getStackCount());

            assertTrue(inventory.getNthSlot(3) instanceof EmptySlot);

            assertEquals(6, inventory.getNthSlot(4).getItemID());
            assertEquals(1, inventory.getNthSlot(4).getStackCount());

            assertEquals(8, inventory.getNthSlot(5).getItemID());
            assertEquals(1, inventory.getNthSlot(5).getStackCount());

            assertTrue(inventory.getNthSlot(6) instanceof EmptySlot);

            assertEquals(10, inventory.getNthSlot(7).getItemID());
            assertEquals(3, inventory.getNthSlot(7).getStackCount());
            assertEquals("Suspicious Papers", inventory.getNthSlot(7).getName());
            assertEquals(20, inventory.getNthSlot(7).getMaxStackSize());

            assertEquals(9, inventory.getNthSlot(8).getItemID());
            assertEquals(1, inventory.getNthSlot(8).getStackCount());

            assertTrue(inventory.getNthSlot(9) instanceof EmptySlot);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
