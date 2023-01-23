package org.example;

import java.util.*;

public class Decoder {
    private final Equality equality;
    private final List<Map<Character, Character>> maps = new ArrayList<>();
    private final Set<Character> nonZeroes = new HashSet<>();

    public Decoder(String equity) {
        this.equality = new Equality(equity);
        markFirstDigitsAsNonZeroes();
    }

    public void decode() {
        Map<Character, Integer> map = new HashMap<>();
        getWorkingCombinations(0, map);
    }

    private void markFirstDigitsAsNonZeroes() {
        for (Operand operand : equality.getOperands()) {
            nonZeroes.add(operand.digits.get(0));
        }
    }

    private void getWorkingCombinations(int offset, Map<Character, Integer> currentMap) {
        List<Character> symbols = equality.getOperands().stream()
                .map(operand -> operand.getLastDigit(offset))
                .filter(character -> character != null && !currentMap.containsKey(character))
                .distinct()
                .toList();
        List<Map<Character, Integer>> maps = generateCombinations(0, new int[symbols.size()], symbols,
                currentMap, offset);
        for (var map : maps) {
            if (offset >= equality.maxDigits - 1) {
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
            if (equality.check(map, offset)) {
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

    private String printWithMapping(Map<Character, Integer> map) {
        StringBuilder sb = new StringBuilder();
        for (char c : equality.rawString.toCharArray()) {
            if (Character.isLetter(c)) {
                sb.append(Character.forDigit(map.get(c), 10));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


}
