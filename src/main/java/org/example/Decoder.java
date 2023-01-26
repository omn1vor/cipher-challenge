package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Decoder {
    private final List<Equality> equalities = new ArrayList<>();
    private final Set<Character> nonZeroes = new HashSet<>();
    private int maxDigits;
    private final List<Map<Character, Integer>> workingMaps = new ArrayList<>();

    public void addEquality(String equality) {
        equalities.add(new Equality(equality));
    }

    /**
     * The algorithm is a naive search through all possible combinations with two limiting conditions:
     * - numbers cannot start with 0
     * - algorithm takes last digits of equality members, each iteration a digit more, in hope of limiting the number
     *      of unknown digits. Only those combinations that work for the previous iteration go to the next one.
     * The worst-case scenario is having all the possible digits as the last digits of equality members.
     * @return String containing all the equalities and all possible decoded combinations of digits for them
     */
    public String decode() {
        if (equalities.isEmpty()) {
            return "";
        }
        calculateMaxDigits();
        markFirstDigitsAsNonZeroes();
        Map<Character, Integer> knownMap = new HashMap<>();
        getWorkingCombinations(0, knownMap);
        return printMappedEqualities();
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
        Map<Character, Integer> unknownMap = new LinkedHashMap<>();
        equalities.stream()
                .flatMap(equality -> equality.getOperands().stream())
                .map(operand -> operand.getLastDigit(offset))
                .filter(character -> character != null && !currentMap.containsKey(character))
                .distinct()
                .forEach(character -> unknownMap.put(character, null));

        List<Map<Character, Integer>> maps = generatePossibleMaps(unknownMap, currentMap, offset);
        for (var map : maps) {
            if (offset >= maxDigits - 1) {
                workingMaps.add(map);
            } else {
                getWorkingCombinations(offset + 1, map);
            }
        }
    }

    private List<Map<Character, Integer>> generatePossibleMaps(Map<Character, Integer> unknownMap,
                                                               Map<Character, Integer> knownMap, int offset) {

        List<Map<Character, Integer>> discoveredMaps = new ArrayList<>();

        Optional<Character> nextUnknownLetter = unknownMap.keySet().stream()
                .filter(letter -> unknownMap.get(letter) == null)
                .findFirst();

        if (nextUnknownLetter.isEmpty()) {
            Map<Character, Integer> candidateMap = new HashMap<>(unknownMap);
            candidateMap.putAll(knownMap);
            boolean combinationWorks = equalities.stream()
                    .allMatch(equality -> equality.check(candidateMap, offset));
            if (combinationWorks) {
                discoveredMaps.add(candidateMap);
            }
            return discoveredMaps;
        }

        Character currentLetter = nextUnknownLetter.get();

        for (int digit = 0; digit <= 9; digit++) {
            if (digit == 0 && nonZeroes.contains(currentLetter)) {
                continue;
            }
            if (knownMap.containsValue(digit)
                    || unknownMap.containsValue(digit)) {
                continue;
            }
            unknownMap.put(currentLetter, digit);
            discoveredMaps.addAll(generatePossibleMaps(new LinkedHashMap<>(unknownMap), knownMap, offset));
        }

        return discoveredMaps;
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
}
