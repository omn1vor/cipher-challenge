package org.example;

import java.util.*;

public class Encoder {
    private final Map<Character, Character> map = new HashMap<>();

    public Encoder() {
        initializeMap();
    }

    public String encode(String expr) {
        checkExpression(expr);

        StringBuilder sb = new StringBuilder();
        for (char c : expr.toCharArray()) {
            sb.append(Character.isDigit(c) ? map.get(c) : c);
        }
        return sb.toString();
    }

    public void shuffle() {
        initializeMap();
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

    private void checkExpression(String expr) {
        if (!expr.matches("\\d+(?:\\s*[+\\-]\\s*\\d+)*\\s*=\\s*\\d+(?:\\s*[+\\-]\\s*\\d+)*")) {
            throw new IllegalArgumentException("Expecting expression with addition and/or subtraction, " +
                    "like this one: '321-111=210'");
        }
    }
}
