package org.example;

import java.util.*;

public class Encoder {
    private final Map<Character, Character> map = new HashMap<>();

    public Encoder() {
        initializeMap();
    }

    public String encode(String rawString) {
        checkExpression(rawString);

        StringBuilder sb = new StringBuilder();
        for (char c : rawString.toCharArray()) {
            sb.append(Character.isDigit(c) ? map.get(c) : c);
        }
        return sb.toString();
    }

     private void initializeMap() {
        Random rnd = new Random();
        List<Character> letters = new ArrayList<>('z' - 'a' + 1);
        for (char c = 'a'; c <= 'z'; c++) {
            letters.add(c);
        }

        map.clear();
        for (int i = 0; i < 10; i++) {
            int index = rnd.nextInt(letters.size());
            map.put(Character.forDigit(i, 10), letters.get(index));
            letters.remove(index);
        }
    }

    private void checkExpression(String rawString) {
        if (!rawString.matches("\\d+(?:\\s*[+\\-]\\s*\\d+)*\\s*=\\s*\\d+(?:\\s*[+\\-]\\s*\\d+)*")) {
            String message = String.format("Wrong input format: %s.%nExpecting an equality with addition " +
                    "and/or subtraction, like this one: '321-111=210'", rawString);
            throw new IllegalArgumentException(message);
        }
    }
}
