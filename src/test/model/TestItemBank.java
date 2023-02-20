package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestItemBank {

    ItemBank testItemBank;
    Item item1;
    Item item2;
    Item item3;
    Item item69;

    @BeforeEach
    void testSetUp() {
        testItemBank = new ItemBank();

        item1 = new Item("ur mom", 1, 1);
        item2 = new Item("a", 2, 99);
        item3 = new Item("natural recursion", 3, 1);
        item69 = new Item("69", 69, 69);
    }

    @Test
    void testGetNextID() {
        assertEquals(0, testItemBank.getNextID());

        testItemBank.add(item1);
        testItemBank.add(item2);
        testItemBank.add(item3);
        assertEquals(4, testItemBank.getNextID());

        testItemBank.add(item69);
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

}
