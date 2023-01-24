package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Operand {
    List<Character> digits = new ArrayList<>();

    Character getLastDigit(int offset) {
        int index = digits.size() - 1 - offset;
        if (index < 0) {
            return null;
        }
        return digits.get(index);
    }

    long getValue(Map<Character, Integer> map, int offset) {
        long result = 0;
        for (int i = 0; i <= offset; i++) {
            Character character = getLastDigit(i);
            if (character == null) {
                continue;
            }
            result += map.get(character) * (long) Math.pow(10, i);
        }
        return result;
    }

    @Override
    public String toString() {
        return digits.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
