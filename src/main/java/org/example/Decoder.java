package org.example;

import java.util.*;

public class Decoder {
    private final Deque<Operand> operands = new ArrayDeque<>();
    private final Deque<Character> operators = new ArrayDeque<>();
    private final String expr;
    private int maxDigits;
    private List<Map<Character, Character>> maps = new ArrayList<>();

    public Decoder(String expr) {
        this.expr = expr.toLowerCase();
        checkExpression();
        parse();
    }

    public String decode() {
        List<Character> symbols = operands.stream()
                .map(operand -> operand.digits.get(operand.digits.size() - 1))
                .distinct()
                .toList();
        generateCombinations(new int[symbols.size()], 0, symbols);
        return "";
    }

    private void generateCombinations(int[] combination, int index, List<Character> symbols) {

        if (index == combination.length) {
            if (mapIsPossible(symbols, combination)) {
                System.out.println(Arrays.toString(combination));
            }
            return;
        }

        for (int digit = 0; digit <= 9; digit++) {
            boolean unique = true;
            for (int i = 0; i < index; i++) {
                if (combination[i] == digit) {
                    unique = false;
                    break;
                }
            }
            if (!unique) {
                continue;
            }
            combination[index] = digit;
            generateCombinations(combination, index + 1, symbols);
        }
    }

    private boolean mapIsPossible(List<Character> symbols, int[] values) {
        int level = 0;
        Deque<Operand> localOperands = new ArrayDeque<>(operands);
        Deque<Character> localOperators = new ArrayDeque<>(operators);
        long leftResult = 0;
        long rightResult = 0;
        while (!localOperators.isEmpty()) {
            char operator = localOperators.pop();
            if (operator == '=') {
                Operand rightOperand = localOperands.pop();
                char rightChar = rightOperand.digits.get(rightOperand.digits.size() - 1 - level);
                rightResult = values[symbols.indexOf(rightChar)];
                continue;
            }
            Operand rightOperand = localOperands.pop();
            char rightChar = rightOperand.digits.get(rightOperand.digits.size() - 1 - level);
            int rightValue = values[symbols.indexOf(rightChar)] * operator == '-' ? -1 : 1;
            Operand leftOperand = localOperands.pop();
            char leftChar = leftOperand.digits.get(leftOperand.digits.size() - 1 - level);
            int leftValue = values[symbols.indexOf(leftChar)];
            leftResult = leftResult + leftValue + rightValue;
        }
        return leftResult % 10 == rightResult;
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
            if (Character.isLetter(c)) {
                Operand operand = new Operand();
                while (i < expr.length() && Character.isLetter(expr.charAt(i))) {
                    operand.digits.add(expr.charAt(i));
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



    private void checkExpression() {
        if (!expr.matches("[a-z]+(?:\\s*[+\\-]\\s*[a-z]+)*\\s*=\\s*[a-z]+")) {
            throw new IllegalArgumentException("Expecting expression with addition and/or subtraction, " +
                    "like this one: 'sln-nnn=lnf'");
        }
    }
}
