package com.example.secureshelledmessenger.model;

import java.util.ArrayList;
import java.util.List;

public class Encryption {

    private static List<Integer> toBits(String s) {
        List<Integer> result = new ArrayList<>();
        for (char c : s.toCharArray()) {
            String bits = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
            for (char bit : bits.toCharArray()) {
                result.add(Character.getNumericValue(bit));
            }
        }
        return result;
    }

    private static String xorEncryptWithBinary(String inputStr, String key) {
        List<Integer> keyBits = toBits(key);
        List<Integer> inputBits = toBits(inputStr);

        List<Integer> fullKeyBits = new ArrayList<>(keyBits);
        while (fullKeyBits.size() < inputBits.size()) {
            fullKeyBits.addAll(keyBits);
        }
        fullKeyBits = fullKeyBits.subList(0, inputBits.size());

        List<Integer> encryptedBits = new ArrayList<>();
        for (int i = 0; i < inputBits.size(); i++) {
            encryptedBits.add(inputBits.get(i) ^ fullKeyBits.get(i));
        }

        StringBuilder binaryStr = new StringBuilder();
        for (int bit : encryptedBits) {
            binaryStr.append(bit);
        }
        return binaryStr.toString();
    }

    private static String xorDecryptWithBinary(String binaryStr, String key) {
        List<Integer> keyBits = toBits(key);
        List<Integer> inputBits = new ArrayList<>();
        for (char bit : binaryStr.toCharArray()) {
            inputBits.add(Character.getNumericValue(bit));
        }

        List<Integer> fullKeyBits = new ArrayList<>(keyBits);
        while (fullKeyBits.size() < inputBits.size()) {
            fullKeyBits.addAll(keyBits);
        }
        fullKeyBits = fullKeyBits.subList(0, inputBits.size());

        List<Integer> decryptedBits = new ArrayList<>();
        for (int i = 0; i < inputBits.size(); i++) {
            decryptedBits.add(inputBits.get(i) ^ fullKeyBits.get(i));
        }

        StringBuilder decryptedStr = new StringBuilder();
        for (int i = 0; i < decryptedBits.size(); i += 8) {
            String byteStr = "";
            for (int j = i; j < i + 8; j++) {
                byteStr += decryptedBits.get(j);
            }
            decryptedStr.append((char) Integer.parseInt(byteStr, 2));
        }
        return decryptedStr.toString();
    }

    private static String decStringFix(String decryptedString) {
        if (decryptedString == null || decryptedString.isEmpty()) {
            return "";
        }
        return decryptedString.substring(0, decryptedString.length() - 1);
    }

    private static String encStringFix(String inputString) {
        if (inputString == null) {
            return " ";
        }
        return inputString + " ";
    }

    public static String encryptMessage(String text, String key) {
        return xorEncryptWithBinary(encStringFix(text), key);
    }

    public static String decryptMessage(String binaryText, String key) {
        return decStringFix(xorDecryptWithBinary(binaryText, key));
    }
}