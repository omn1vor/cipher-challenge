package org.example;

public class Main {
    public static void main(String[] args) {
        Cipher cipher = new Cipher("321-111=210");
        System.out.println(cipher.encode());
    }
}