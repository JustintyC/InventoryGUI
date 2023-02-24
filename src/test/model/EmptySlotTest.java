package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmptySlotTest {

    EmptySlot blank;

    @BeforeEach
    void testSetUp() {
        blank = new EmptySlot();
    }

    @Test
    void testIncreaseStackCount() {
        blank.increaseStackCount(1);
        assertEquals(-1,blank.getStackCount());

        blank.increaseStackCount(1000000);
        assertEquals(-1,blank.getStackCount());
    }

    @Test
    void testDecreaseStackCount() {
        blank.decreaseStackCount(1);
        assertEquals(-1,blank.getStackCount());

        blank.decreaseStackCount(1000000);
        assertEquals(-1,blank.getStackCount());
    }

}
