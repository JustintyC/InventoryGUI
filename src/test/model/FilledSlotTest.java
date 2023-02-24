package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilledSlotTest {

    Slot testFilledSlot;

    @BeforeEach
    void testSetUp() {
        testFilledSlot = new FilledSlot("item1", 1, 1, 10);
    }

    @Test
    void testIncreaseStackCount() {
        testFilledSlot.increaseStackCount(1);
        assertEquals(2,testFilledSlot.getStackCount());

        testFilledSlot.increaseStackCount(3);
        assertEquals(5,testFilledSlot.getStackCount());
    }

    @Test
    void testDecreaseStackCount() {
        testFilledSlot.increaseStackCount(9);
        testFilledSlot.decreaseStackCount(1);
        assertEquals(9,testFilledSlot.getStackCount());

        testFilledSlot.decreaseStackCount(3);
        assertEquals(6,testFilledSlot.getStackCount());
    }

}
