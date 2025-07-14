package com.github.lotqwerty.lottweaks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LotTweaksTest {

    @Test
    public void testSimpleAssertion() {
        assertEquals(1, 1);
    }

    @Test
    public void testModConstants() {
        assertEquals("lottweaks", LotTweaks.MODID);
        assertEquals("LotTweaks", LotTweaks.NAME);
    }
}