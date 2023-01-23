package org.example;

import java.util.Arrays;

public class Main {
    static long num;

    public static void main(String[] args) {
        Encoder encoder = new Encoder();
        System.out.println("Simple expression, '12+21=33':");
        System.out.println(encoder.encode("12+21=33"));
        System.out.println();

        System.out.println("Same expression, '321-111=210', with reshuffling between calls:");
        System.out.println(encoder.encode("321-111=210"));
        encoder.shuffle();
        System.out.println(encoder.encode("321-111=210"));
        System.out.println();

        System.out.println("Malformed expression, '12/21=33':");
        try {
            System.out.println(encoder.encode("12/21=33"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println();


        System.out.println("Expression: 'xwc-ccc=wcm'");
        Decoder decoder = new Decoder("xwc-ccc=wcm");
        decoder.decode();

        System.out.println("Expression: 'xwc+ccc=wcm'");
        decoder = new Decoder("xwc+ccc=wcm");
        decoder.decode();

        System.out.println("Expression: 'hff+hff=pf+pf+pf+hpf'");
        decoder = new Decoder("hff+hff=pf+pf+pf+hpf");
        decoder.decode();

    }

    public static void generateCombinations(int[] elements, int[] combination, int index) {
        if (index == elements.length) {
            System.out.println(Arrays.toString(combination));
            num++;
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
            generateCombinations(elements, combination, index + 1);
        }
    }
}