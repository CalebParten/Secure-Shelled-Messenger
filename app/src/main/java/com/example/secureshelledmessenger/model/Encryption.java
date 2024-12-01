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

    // Convert a list of bits back to a string
    public static String fromBits(List<Integer> bits) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < bits.size(); i += 8) {
            String byteStr = "";
            for (int j = i; j < i + 8; j++) {
                byteStr += bits.get(j);
            }
            result.append((char) Integer.parseInt(byteStr, 2));
        }
        return result.toString();
    }

    // Bit flip: Flip every second bit starting from index 1
    public static String bitFlip(String input) {
        List<Integer> bits = toBits(input);
        for (int i = 1; i < bits.size(); i += 2) {
            bits.set(i, bits.get(i) == 0 ? 1 : 0);  // Flip the bit
        }
        return fromBits(bits);
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

    // XOR decryption (same as encryption for symmetric encryption)
    public static String xorDecrypt(String encryptedStr, String key) {
        return xorEncrypt(encryptedStr, key);  // Same function for decryption as XOR is symmetric
    }

    // Save the encrypted text to a file
    public static void saveToFile(String filePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load the encrypted text from a file
    public static String loadFromFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
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


    public static void main(String[] args) {
        String key = "Howdy this is password";
        String inputStr = "hello, if you can decipher this then I would be impressed. -turtle. ";
        key = toStringBits(key);
        System.out.println(key);

        if (true) {
            System.out.println(bitFlip(inputStr));
            System.out.println(bitFlip(bitFlip(inputStr)));
        }

        if (true) {
            // Encrypt and decrypt
            String encryptedText = xorEncrypt(inputStr, key);
            saveToFile("encrypted_text.txt", encryptedText);
            String decryptedText = xorDecrypt(encryptedText, key);

            String loadedEncryptedText = loadFromFile("encrypted_text.txt");

            // Output the results
            System.out.println("Encrypted:\n" + encryptedText);
            System.out.println("\nDecrypted Text  : " + decryptedText);
            System.out.println("\nLoaded Decrypted: " + decStringFix(xorDecrypt(loadedEncryptedText, key)));
        }

        List<String> texts = new ArrayList<>();
        texts.add("Hello, world!");
        texts.add("Java is fun.");
        texts.add("I love programming.");
        texts.add("Encryption is cool.");
        texts.add("This is a test.");
        texts.add("Learning Java every day.");
        texts.add("For loops are useful.");
        texts.add("Happy coding!");
        texts.add("Let's solve problems.");
        texts.add("Keep up the good work!");

        if (true){
            for (String text : texts) {
                System.out.println(text + "\n" + xorEncrypt(text, key) + "\n" + decStringFix(xorEncrypt(encStringFix(xorEncrypt(text, key)), key)) + "\n");
            }
        }



    }
}
