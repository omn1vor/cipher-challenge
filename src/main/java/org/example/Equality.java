package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Equality {
    final String rawString;
    Expression left;
    Expression right;
    int maxDigits;

    public Equality(String rawString) {
        this.rawString = rawString.toLowerCase();
        checkFormat();
        parse();
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

    private void parse() {
        String[] sides = rawString.split("=");
        left = new Expression(sides[0]);
        right = new Expression(sides[1]);
        maxDigits = Math.max(left.maxDigits, right.maxDigits);
    }

    private void checkFormat() {
        if (!rawString.matches("[a-z]+(?:\\s*[+\\-]\\s*[a-z]+)*\\s*=\\s*[a-z]+(?:\\s*[+\\-]\\s*[a-z]+)*")) {
            throw new IllegalArgumentException("Expecting equity with addition and/or subtraction, " +
                    "like this one: 'sln-nnn=lnf'");
        }
    }
}
