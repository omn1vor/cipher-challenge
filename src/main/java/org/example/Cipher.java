package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Cipher {
    private final String expr;
    private final Deque<Operand> operands = new ArrayDeque<>();
    private final Deque<Character> operators = new ArrayDeque<>();

    public Cipher(String expr) {
        this.expr = expr;
        checkExpression();
        parse();
    }

    public String encode() {
        Map<Character, Character> map = new HashMap<>();
        Random rnd = new Random();
        Set<Character> numbers = operands.stream()
                .flatMap(operand -> operand.digits.stream())
                .collect(Collectors.toSet());
        for (var num : numbers) {
            map.put(num, getFreeLetter(map.keySet(), rnd));
        }
        for (var operand : operands) {
            operand.digits = operand.digits.stream()
                    .map(map::get)
                    .collect(Collectors.toList());
        }

        StringBuilder sb = new StringBuilder();
        while (!operators.isEmpty()) {
            sb.append(operands.pollFirst());
            sb.append(operators.pollFirst());
            sb.append(operands.pollFirst());
        }
        return sb.toString();
    }

    private Character getFreeLetter(Set<Character> usedLetters, Random rnd) {
        int starting = 'a';
        int range = 'z' - starting + 1;
        char c = (char) (starting + rnd.nextInt(range));
        while (usedLetters.contains(c)) {
            c = (char) (starting + rnd.nextInt(range));
        }
        return c;
    }

    private void parse() {
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == ' ') {
                continue;
            }
            if (Character.isDigit(c)) {
                Operand operand = new Operand();
                while (i < expr.length() && Character.isDigit(expr.charAt(i))) {
                    operand.digits.add(c);
                    i++;
                }
                i--;
                operand.updateValue();
                operands.push(operand);
            } else {
                operators.push(c);
            }
        }
    }

    private void checkExpression() {
        if (!expr.matches("\\d+(?:\\s*[+\\-]\\s*\\d+)*\\s*=\\s*\\d+")) {
            throw new IllegalArgumentException("Expecting expression like this one: '321-111=210'");
        }
    }
}
