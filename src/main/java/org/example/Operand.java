package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Operand {
    long value;
    List<Character> digits = new ArrayList<>();

    void updateValue() {
        String str = digits.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
        value = Long.parseLong(str);
    }

    @Override
    public String toString() {
        return digits.stream().collect(Collectors.joining());
    }
}
