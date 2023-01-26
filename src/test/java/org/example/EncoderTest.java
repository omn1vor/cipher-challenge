package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EncoderTest {

    Encoder encoder;

    static List<String> equalities() {
        return List.of(
                "12+21=33",
                "321-111=210",
                "11 + 5 = 120 - 4"
        );
    }

    @BeforeEach
    void setUp() {
        encoder = new Encoder();
    }

    @ParameterizedTest
    @MethodSource(value = "equalities")
    @DisplayName("Digits should be converted in such a way that the new symbol is a letter " +
            "and equal symbols remain equal and visa versa")
    void testEncoding(String originalEquality) {
        String encodedEquality = encoder.encode(originalEquality);

        Map<Character, Character> map = new HashMap<>();
        for (int i = 0; i < originalEquality.length(); i++) {
            char original = originalEquality.charAt(i);
            char encoded = encodedEquality.charAt(i);

            if (!Character.isDigit(original)) {
                assertEquals(original, encoded); // non-digits should remain intact
                continue;
            }

            assertTrue(Character.isLetter(encoded)); // digits should be changed to letters

            // gathering an actual map that was used this time (since it's random)
            if (map.containsKey(original)) {
                assertEquals(encoded, map.get(original)); // mapping should be consistent
            } else {
                map.put(original, encoded);
            }
        }
        assertEquals(map.keySet().size(), Set.copyOf(map.values()).size()); // character sets should be equal in size
    }

    @Test
    @DisplayName("Malformed equalities should throw IllegalArgumentException")
    void testMalformedEquality() {
        assertThrows(IllegalArgumentException.class, () -> encoder.encode("12/21=33"));
    }
}