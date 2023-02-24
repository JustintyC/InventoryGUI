package model;

import exceptions.InvalidItemIDException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ItemBankTest {

    ItemBank testItemBank;
    Item item1;
    Item item2;
    Item item3;
    Item item69;

    @BeforeEach
    void testSetUp() {
        testItemBank = new ItemBank();

        item1 = new Item("item1", 1, 1);
        item2 = new Item("item2", 2, 99);
        item3 = new Item("item3", 3, 1);
        item69 = new Item("item69", 69, 69);
    }

    @Test
    void testGetNextID() {
        assertEquals(1, testItemBank.getNextID());

        testItemBank.add(item1);
        testItemBank.add(item2);
        testItemBank.add(item3);
        assertEquals(4, testItemBank.getNextID());

        testItemBank.add(item69);
        assertEquals(70, testItemBank.getNextID());
    }

    @Test
    void testGetNextIDScrambled() {
        assertEquals(1, testItemBank.getNextID());

        testItemBank.add(item3);
        testItemBank.add(item2);
        testItemBank.add(item69);
        testItemBank.add(item1);

        assertEquals(70, testItemBank.getNextID());

    }


    @Test
    void testAdd() {
        List tempList = new ArrayList<Item>();
        tempList.add(item1);

        testItemBank.add(item1);
        assertEquals(item1, testItemBank.get(0));

        testItemBank.add(item2);
        testItemBank.add(item3);

        assertEquals(item2, testItemBank.get(1));
        assertEquals(item3, testItemBank.get(2));
    }

    @Test
    void testFindItem() throws InvalidItemIDException {
        testItemBank.add(item1);
        testItemBank.add(item2);
        testItemBank.add(item3);

        try  {
            assertEquals(item1, testItemBank.findItem(1));
        } catch (InvalidItemIDException e) {
            fail();
        }
        assertEquals(item1, testItemBank.findItem(1));
        assertEquals(item2, testItemBank.findItem(2));

        try {
            testItemBank.findItem(100000000);
            fail("There should not be an item with ID of 100000000");
        } catch (InvalidItemIDException e) {

        }

    }

}
