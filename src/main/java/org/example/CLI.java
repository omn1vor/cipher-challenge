package org.example;

import java.util.Scanner;

public class CLI {
    private final Scanner scanner = new Scanner(System.in);

    public CLI() {
        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            System.out.println();
            System.out.println("Please select mode:");
            System.out.println("1. Encode equalities");
            System.out.println("2. Decode equalities");
            System.out.println("0. Exit");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1" -> encode();
                case "2" -> decode();
                case "0" -> System.exit(0);
                default -> System.out.println("Wrong input.");
            }
        }
    }

    private void encode() {
        Encoder encoder = new Encoder();
        System.out.println("Please enter equality to encode:");
        String input = scanner.nextLine().trim();
        try {
            System.out.println(encoder.encode(input));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void decode() {
        System.out.println("Please enter equality (or several, separated my semicolons) to decode");
        String input = scanner.nextLine();

        Decoder decoder = new Decoder();
        for (String equality : input.split(";")) {
            try {
                decoder.addEquality(equality.trim());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                break;
            }
        }
        System.out.println(decoder.decode());
    }
}
