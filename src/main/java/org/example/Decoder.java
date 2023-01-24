package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Decoder {
    private final List<Equality> equalities = new ArrayList<>();
    private final Set<Character> nonZeroes = new HashSet<>();
    private int maxDigits;
    private final List<Map<Character, Integer>> workingMaps = new ArrayList<>();

    public Decoder() {

    }

    public Decoder(String equality) {
        addEquality(equality);
    }

    public void addEquality(String equality) {
        equalities.add(new Equality(equality));
    }

    public String decode() {
        calculateMaxDigits();
        markFirstDigitsAsNonZeroes();
        Map<Character, Integer> knownMap = new HashMap<>();
        getWorkingCombinations(0, knownMap);
        return printMappedEqualities();
    }

    private String printMappedEqualities() {
        StringBuilder sb = new StringBuilder();
        for (Equality equality : equalities) {
            sb.append(String.format("%s:", equality));
            sb.append(System.lineSeparator());
            String mappings = workingMaps.stream()
                    .map(equality::printWithMapping)
                    .collect(Collectors.joining("; "));
            sb.append(mappings);
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private void markFirstDigitsAsNonZeroes() {
        for (Equality equality : equalities) {
            for (Operand operand : equality.getOperands()) {
                nonZeroes.add(operand.digits.get(0));
            }
        }
    }

    private void calculateMaxDigits() {
        maxDigits = equalities.stream()
                .mapToInt(Equality::getMaxDigits)
                .max()
                .orElse(0);
    }

    private void getWorkingCombinations(int offset, Map<Character, Integer> currentMap) {
        List<Character> symbols = equalities.stream()
                .flatMap(equality -> equality.getOperands().stream())
                .map(operand -> operand.getLastDigit(offset))
                .filter(character -> character != null && !currentMap.containsKey(character))
                .distinct()
                .toList();
        List<Map<Character, Integer>> maps = generateCombinations(0, new int[symbols.size()], symbols,
                currentMap, offset);
        for (var map : maps) {
            if (offset >= maxDigits - 1) {
                workingMaps.add(map);
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
            boolean combinationWorks = equalities.stream()
                    .allMatch(equality -> equality.check(map, offset));
            if (combinationWorks) {
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
}
