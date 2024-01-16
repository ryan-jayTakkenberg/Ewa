package app.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class HashUtilTests {

    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Test
    void testCorrectHash() {
        String input = "test";

        String hash = HashUtil.hash(input);

        assertEquals(hash, "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08");
    }

    @Test
    void testHashLengthForAnyInput() {
        String randomInput = String.valueOf(Math.random());

        String hash = HashUtil.hash(randomInput);

        assertEquals(hash.length(), 64);
    }

    @Test
    void testDifferentHashes() {
        String input = "test";

        String hash1 = HashUtil.hash(input.toLowerCase());
        String hash2 = HashUtil.hash(input.toUpperCase());

        assertNotEquals(hash1, hash2);
    }

    @Test
    void testInvalidInput() {
        String hash = HashUtil.hash(null);
        assertEquals(hash, "");
    }

}
