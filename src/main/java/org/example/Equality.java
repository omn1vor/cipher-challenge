package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Equality {
    private final String rawString;
    private Expression left;
    private Expression right;
    private int maxDigits;

    Equality(String rawString) {
        this.rawString = rawString.toLowerCase();
        checkFormat();
        parse();
    }

    int getMaxDigits() {
        return maxDigits;
    }

    List<Operand> getOperands() {
        List<Operand> operands = new ArrayList<>(left.operands);
        operands.addAll(right.operands);
        return operands;
    }

    boolean check(Map<Character, Integer> map, int offset) {
        long leftResult = left.evaluate(map, offset);
        long rightResult = right.evaluate(map, offset);

        long powerOf10 = (long) Math.pow(10, offset + 1);
        boolean maxDigitCheck = offset + 1 == maxDigits;

        return leftResult % powerOf10 == rightResult % powerOf10
                || !maxDigitCheck && powerOf10 + leftResult % powerOf10 == rightResult % powerOf10
                || !maxDigitCheck && leftResult % powerOf10 == powerOf10 + rightResult % powerOf10;
    }

    String printWithMapping(Map<Character, Integer> map) {
        StringBuilder sb = new StringBuilder();
        for (char c : rawString.toCharArray()) {
            if (Character.isLetter(c)) {
                sb.append(Character.forDigit(map.get(c), 10));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private void parse() {
        String[] sides = rawString.split("=");
        left = new Expression(sides[0]);
        right = new Expression(sides[1]);
        maxDigits = Math.max(left.maxDigits, right.maxDigits);
    }

    private void checkFormat() {
        if (!rawString.matches("[a-z]+(?:\\s*[+\\-]\\s*[a-z]+)*\\s*=\\s*[a-z]+(?:\\s*[+\\-]\\s*[a-z]+)*")) {
            String message = String.format("Wrong input format: %s. %nExpecting an equality with addition " +
                    "and/or subtraction, like this one: 'sln-nnn=lnf'", rawString);
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public String toString() {
        return rawString;
    }
}
