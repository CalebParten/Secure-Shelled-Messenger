package com.example.secureshelledmessenger.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Encryption {

    // Convert a string to a list of bits
    public static List<Integer> toBits(String s) {
        List<Integer> result = new ArrayList<>();
        for (char c : s.toCharArray()) {
            String bits = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
            for (char bit : bits.toCharArray()) {
                result.add(Character.getNumericValue(bit));
            }
        }
        return result;
    }

    // Convert a string to a list of bits
    public static String toStringBits(String s) {
        String result = "";
        for (char c : s.toCharArray()) {
            String bits = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
            for (char bit : bits.toCharArray()) {
                result = result + String.valueOf(Character.getNumericValue(bit));
            }
        }
        return result;
    }

    // XOR encryption
    public static String xorEncrypt(String inputStr, String key) {
        List<Integer> keyBits = toBits(key);
        List<Integer> inputBits = new ArrayList<>();
        for (char c : inputStr.toCharArray()) {
            String bitString = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
            for (char bit : bitString.toCharArray()) {
                inputBits.add(Character.getNumericValue(bit));
            }
        }

        // Ensure key is long enough for the input
        List<Integer> fullKeyBits = new ArrayList<>(keyBits);
        while (fullKeyBits.size() < inputBits.size()) {
            fullKeyBits.addAll(keyBits);
        }
        fullKeyBits = fullKeyBits.subList(0, inputBits.size());

        List<Integer> encryptedBits = new ArrayList<>();
        for (int i = 0; i < inputBits.size(); i++) {
            encryptedBits.add(inputBits.get(i) ^ fullKeyBits.get(i));  // XOR operation
        }

        // Convert encrypted bits back to string
        StringBuilder encryptedStr = new StringBuilder();
        for (int i = 0; i < encryptedBits.size(); i += 8) {
            String byteStr = "";
            for (int j = i; j < i + 8; j++) {
                byteStr += encryptedBits.get(j);
            }
            encryptedStr.append((char) Integer.parseInt(byteStr, 2));
        }
        return encryptedStr.toString();
    }

    public static String decStringFix(String decryptedString) {
        if (decryptedString == null || decryptedString.isEmpty()) {
            return "";
        }
        String fixedString = decryptedString.substring(0, decryptedString.length() - 1);
        return fixedString;
    }

    public static String encStringFix(String inputString) {
        if (inputString == null) {
            return " ";
        }
        return inputString + " ";
    }

    public static String encryptMessage(String text, String key){
        return encStringFix(xorEncrypt(text, toStringBits(key)));
    }

    public static String decryptMessage(String text, String key){
        return decStringFix(xorEncrypt(text, toStringBits(key)));
    }
}