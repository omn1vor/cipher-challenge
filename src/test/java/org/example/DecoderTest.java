package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecoderTest {

    Decoder decoder;
    private static Stream<Arguments> equalities() {
        return Stream.of(
                Arguments.of("xwc-ccc=wcm", "xwc-ccc=wcm:" + System.lineSeparator() +
                        "321-111=210; 642-222=420; 963-333=630; 926-666=260" + System.lineSeparator()),
                Arguments.of("xwc+ccc=wcm", "xwc+ccc=wcm:" + System.lineSeparator() +
                        "395+555=950; 197+777=974" + System.lineSeparator()),
                Arguments.of("hff+hff=pf+pf+pf+hpf", "hff+hff=pf+pf+pf+hpf:" + System.lineSeparator() +
                        "200+200=50+50+50+250" + System.lineSeparator())
        );
    }

    @BeforeEach
    void setUp() {
        decoder = new Decoder();
    }

    @ParameterizedTest
    @MethodSource(value = "equalities")
    @DisplayName("Test cases should decode as expected")
    void decode(String equality, String expected) {

        decoder.addEquality(equality);
        assertEquals(expected, decoder.decode());
    }

    @Test
    @DisplayName("Multiple equalities should be decoded as expected")
    void decodeTwoEqualities() {
        decoder.addEquality("xwc - ccc = wcm");
        decoder.addEquality("xwc = wcm + ccc");

        String expected = "xwc - ccc = wcm:" + System.lineSeparator() +
                "321 - 111 = 210; 642 - 222 = 420; 963 - 333 = 630; 926 - 666 = 260" + System.lineSeparator() +
                "xwc = wcm + ccc:" + System.lineSeparator() +
                "321 = 210 + 111; 642 = 420 + 222; 963 = 630 + 333; 926 = 260 + 666" + System.lineSeparator();
        assertEquals(expected, decoder.decode());
    }
}
