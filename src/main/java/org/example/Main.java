package org.example;

public class Main {
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
    }
}