package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Operand {
    long value;
    List<Character> digits = new ArrayList<>();

    @Override
    public String toString() {
        return digits.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
