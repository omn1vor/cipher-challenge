package org.example;

import java.util.*;

public class Decoder {
    private final Deque<Operand> operands = new ArrayDeque<>();
    private final Deque<Character> operators = new ArrayDeque<>();
    private final String expr;
    private int maxDigits;
    private final List<Map<Character, Character>> maps = new ArrayList<>();
    private final Set<Character> nonZeroes = new HashSet<>();

    public Decoder(String expr) {
        this.expr = expr.toLowerCase();
        checkExpression();
        parse();
        markFirstDigitsAsNonZeroes();
    }

    public void decode() {
        Map<Character, Integer> map = new HashMap<>();
        getWorkingCombinations(0, map);
    }

    private void markFirstDigitsAsNonZeroes() {
        for (Operand operand : operands) {
            nonZeroes.add(operand.digits.get(0));
        }
    }

    private void getWorkingCombinations(int offset, Map<Character, Integer> currentMap) {
        List<Character> symbols = operands.stream()
                .map(operand -> operand.getLastDigit(offset))
                .filter(character -> character != null && !currentMap.containsKey(character))
                .distinct()
                .toList();
        List<Map<Character, Integer>> maps = generateCombinations(0, new int[symbols.size()], symbols,
                currentMap, offset);
        for (var map : maps) {
            if (offset >= maxDigits - 1) {
                System.out.println(printWithMapping(map));
            } else {
                getWorkingCombinations(offset + 1, map);
            }
        }
    }

    private List<Map<Character, Integer>> generateCombinations(int index, int[] combination, List<Character> symbols,
                                      Map<Character, Integer> knownMap, int offset) {

        List<Map<Character, Integer>> maps = new ArrayList<>();

        if (index == combination.length) {
            Map<Character, Integer> map = getMap(symbols, combination);
            map.putAll(knownMap);
            if (mapIsPossible(map, offset)) {
                maps.add(map);
            }
            return maps;
        }

        for (int digit = 0; digit <= 9; digit++) {
            if (digit == 0 && nonZeroes.contains(symbols.get(index))) {
                continue;
            }
            boolean unique = !knownMap.containsValue(digit);
            if (unique) {
                for (int i = 0; i < index; i++) {
                    if (combination[i] == digit) {
                        unique = false;
                        break;
                    }
                }
            }
            if (!unique) {
                continue;
            }
            combination[index] = digit;
            maps.addAll(generateCombinations(index + 1, combination, symbols, knownMap, offset));
        }

        return maps;
    }

    private Map<Character, Integer> getMap(List<Character> symbols, int[] values) {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < symbols.size(); i++) {
            map.put(symbols.get(i), values[i]);
        }
        return map;
    }

    private boolean mapIsPossible(Map<Character, Integer> map, int offset) {
        Deque<Operand> localOperands = new ArrayDeque<>(operands);
        Deque<Character> localOperators = new ArrayDeque<>(operators);
        long leftResult = 0;
        long rightResult = 0;
        while (!localOperators.isEmpty()) {
            char operator = localOperators.pop();
            if (operator == '=') {
                Operand rightOperand = localOperands.pop();
                rightResult = rightOperand.getValueWithOffset(offset, map);
                continue;
            }
            Operand rightOperand = localOperands.pop();
            long rightValue = rightOperand.getValueWithOffset(offset, map);
            Operand leftOperand = localOperands.pop();
            long leftValue = leftOperand.getValueWithOffset(offset, map);
            int sign = operator == '+' ? 1 : -1;
            leftResult = leftResult + leftValue + sign * rightValue;
        }
        long powerOf10 = (long) Math.pow(10, offset + 1);
        boolean fullExpression = offset + 1 == maxDigits;
        return leftResult % powerOf10 == rightResult % powerOf10
                || !fullExpression && powerOf10 + leftResult % powerOf10 == rightResult % powerOf10;
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

    private String printWithMapping(Map<Character, Integer> map) {
        StringBuilder sb = new StringBuilder();
        for (char c : expr.toCharArray()) {
            if (Character.isLetter(c)) {
                sb.append(Character.forDigit(map.get(c), 10));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private void checkExpression() {
        if (!expr.matches("[a-z]+(?:\\s*[+\\-]\\s*[a-z]+)*\\s*=\\s*[a-z]+(?:\\s*[+\\-]\\s*[a-z]+)*")) {
            throw new IllegalArgumentException("Expecting expression with addition and/or subtraction, " +
                    "like this one: 'sln-nnn=lnf'");
        }
    }
}
