package org.example;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

class Expression {
    private final String rawString;
    final Deque<Operand> operands = new ArrayDeque<>();
    final Deque<Character> operators = new ArrayDeque<>();
    int maxDigits;

    Expression(String rawString) {
        this.rawString = rawString;
        parse();
    }

    long evaluate(Map<Character, Integer> map, int offset) {
        Deque<Operand> localOperands = new ArrayDeque<>(operands);
        Deque<Character> localOperators = new ArrayDeque<>(operators);
        long result = 0;

        while (!localOperators.isEmpty()) {
            char operator = localOperators.pop();

            Operand operand = localOperands.pop();
            int sign = operator == '+' ? 1 : -1;
            long value = operand.getValue(map, offset) * sign;
            result += value;
        }
        
        if (!operands.isEmpty()) {
            Operand operand = localOperands.pop();
            result += operand.getValue(map, offset);
        }

        return result;
    }

    private void parse() {
        for (int i = 0; i < rawString.length(); i++) {
            char c = rawString.charAt(i);
            if (c == ' ') {
                continue;
            }
            if (Character.isLetter(c)) {
                Operand operand = new Operand();
                while (i < rawString.length() && Character.isLetter(rawString.charAt(i))) {
                    operand.digits.add(rawString.charAt(i));
                    i++;
                }
                i--;
                if (operand.digits.size() > maxDigits) {
                    maxDigits = operand.digits.size();
                }
                operands.push(operand);
            } else {
                operators.push(c);
            }
        }
    }
}
