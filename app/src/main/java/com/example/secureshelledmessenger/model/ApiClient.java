package com.example.secureshelledmessenger.model;

import com.example.secureshelledmessenger.model.Message;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiClient {

    private static final String BASE_URL = "https://ssm.cs458.enmucs.com";

    public static void main(String[] args) {
        // System.out.println("User lookup: " + lookupUser("testuser"));
        // System.out.println("Create user: " + createUser("tesuser", "password123"));
        // System.out.println("Login: " + login("testuser", "password123"));
        // System.out.println("Edit Username: " + editUsername(8, "password123", "n3ewUser"));
        // System.out.println("Edit Password: " + editPassword(8, "password123","password123"));
        // // Delete has weird bugs but it works
        // System.out.println("Delete User: " + deleteUser(22, "password123"));

        // System.out.println("Send message: " + sendMessage(1, 2, "Hello 2", "password"));
        // System.out.println("Get conversation: " + getConversation(1, 2, "password"));
        // System.out.println("Get received: " + getReceivedMessages(1, "password"));
        // System.out.println("Get sent: " + getSentMessages(1, "password"));
        // // Delete has weird bugs but it works
        // System.out.println("Delete message: " + deleteMessage(1,6, "password"));

    }


    public static String lookupUser(String username) {
        String jsonInput = String.format("{\"username\":\"%s\"}", username);
        return sendRequest("/api/users/lookup", jsonInput);
    }

    public static String createUser(String username, String password) {
        String jsonInput = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
        return sendRequest("/api/users/create", jsonInput);
    }

    public static String login(String username, String password) {
        String jsonInput = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
        return sendRequest("/api/users/login", jsonInput);
    }

    public static String editUsername(long userId, String password, String newUsername) {
        String jsonInput = String.format(
            "{\"id\":%d, \"password\":\"%s\", \"newUsername\":\"%s\"}",
            userId, password, newUsername
        );
        return sendRequest("/api/users/editUsername", jsonInput);
    }

    public static String editPassword(long userId, String password, String newPassword) {
        String jsonInput = String.format(
            "{\"id\":%d, \"password\":\"%s\", \"newPassword\":\"%s\"}",
            userId, password, newPassword
        );
        return sendRequest("/api/users/editPassword", jsonInput);
    }

    public static boolean deleteUser(String username, String password) {
        // Step 1: Check if user exists
        String lookupResponse = sendRequest("/api/users/lookup", String.format("{\"username\":\"%s\"}", username));
    
        if (lookupResponse == null || lookupResponse.isEmpty()) {
            System.out.println("User does not exist.");
            return false;
        }
    
        // Step 2: Attempt to delete user
        String deleteResponse = sendRequest("/api/users/delete", String.format("{\"id\":%s, \"password\":\"%s\"}", lookupResponse, password));
    
        if (deleteResponse == null) {
            return false;
        }
    
        // Step 3: Confirm success or log failure
        if (deleteResponse.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static Message sendMessage(long senderId, long receiverId, String content, String password) {
        String jsonInputString = "{\"senderId\":" + senderId + ", \"receiverId\":" + receiverId + ", \"content\":\"" + content + "\", \"password\":\"" + password + "\"}";
        String response = sendRequest("/api/messages/send", jsonInputString);

        if (response == null || response.isEmpty()) {
            return null;
        }
        Long messageId = extractLongValue(response, "id");
        String messageContent = extractJsonValue(response, "content");
        Long senderIdResponse = extractLongValue(response, "senderId");
        Long receiverIdResponse = extractLongValue(response, "receiverId");
        String timestampStr = extractJsonValue(response, "timestamp");
        LocalDateTime timestamp = parseTimestamp(timestampStr);
        if (messageId != null && messageContent != null && senderIdResponse != null && receiverIdResponse != null && timestamp != null) {
            return new Message(messageId, messageContent, senderIdResponse, receiverIdResponse, timestamp);
        } else {
            return null;
        }
    }

    public static List<Message> getConversation(long senderId, long receiverId, String password) {
        String requestJson = String.format(
            "{\"senderId\":%d, \"receiverId\":%d, \"password\":\"%s\"}",
            senderId, receiverId, password
        );
        String response = sendRequest("/api/messages/conversation", requestJson);
        if (response == null) {
            return Collections.emptyList();
        }
        return parseMessages(response);
    }

    public static List<Message> getReceivedMessages(long userId, String password) {
        String requestJson = String.format(
            "{\"userId\":%d, \"password\":\"%s\"}", userId, password
        );
        String response = sendRequest("/api/messages/received", requestJson);
        if (response == null || response == "[]") {
            return Collections.emptyList();
        }
        return parseMessages(response);
    }

    public static List<Message> getSentMessages(long userId, String password) {
        String requestJson = String.format(
            "{\"userId\":%d, \"password\":\"%s\"}", userId, password
        );
        String response = sendRequest("/api/messages/sent", requestJson);
        if (response == null) {
            return Collections.emptyList();
        }
        return parseMessages(response);
    }

    public static boolean deleteMessage(long userId, long messageId, String password) {
        String jsonInputString = "{\"userId\":" + userId + ", \"messageId\":" + messageId + ", \"password\":\"" + password + "\"}";
        String response = sendRequest("/api/messages/delete", jsonInputString);
        if (!response.equals("false")) {
            return true;
        } else {
            return false;
        }
    }

    private static String sendRequest(String endpoint, String jsonInputString) {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);

            // Send JSON input
            if (jsonInputString != null) {
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            }

            // Get the response code
            int statusCode = connection.getResponseCode();

            // Read the response body
            InputStream inputStream = (statusCode >= 200 && statusCode < 300)
                ? connection.getInputStream()
                : connection.getErrorStream();

            try (InputStream is = inputStream) {
                byte[] responseBytes = is.readAllBytes();
                return new String(responseBytes, StandardCharsets.UTF_8);
            }

        } catch (Exception e) {
            return "false"; // Return false if an exception occurs
        }
    }

    private static LocalDateTime parseTimestamp(String timestampStr) {
        // Parse the timestamp using LocalDateTime (assuming ISO-8601 format)
        try {
            return LocalDateTime.parse(timestampStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Long extractLongValue(String jsonResponse, String key) {
        String value = extractJsonValue(jsonResponse, key);
        if (value != null) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private static String extractJsonValue(String jsonResponse, String key) {
        int keyIndex = jsonResponse.indexOf("\"" + key + "\"");
        if (keyIndex == -1) return null; // Key not found

        // Find the value corresponding to the key
        int valueStart = jsonResponse.indexOf(":", keyIndex) + 1;
        int valueEnd = jsonResponse.indexOf(",", valueStart);
        if (valueEnd == -1) { // Handle the last element case
            valueEnd = jsonResponse.indexOf("}", valueStart);
        }

        if (valueStart == -1 || valueEnd == -1) return null;
        return jsonResponse.substring(valueStart, valueEnd).replaceAll("\"", "").trim(); // Remove surrounding quotes
    }

    private static List<Message> parseMessages(String response) {
        try{
            List<Message> messages = new ArrayList<>();
            if (response != null && !response.isEmpty()) {
                if (response.startsWith("[") && response.endsWith("]")) {
                    String[] messageJsonArray = response.substring(1, response.length() - 1).split("\\},\\{");
                    for (String messageJson : messageJsonArray) {
                        messageJson = messageJson.trim();
                        if (!messageJson.startsWith("{")) messageJson = "{" + messageJson;
                        if (!messageJson.endsWith("}")) messageJson = messageJson + "}";
                        Long id = extractLongValue(messageJson, "id");
                        String content = extractJsonValue(messageJson, "content");
                        Long senderId = extractLongValue(messageJson, "senderId");
                        Long receiverId = extractLongValue(messageJson, "receiverId");
                        String timestampStr = extractJsonValue(messageJson, "timestamp");
                        LocalDateTime timestamp = null;
                        if (timestampStr!=null){
                            timestamp = parseTimestamp(timestampStr);
                        } 
                        if (id != null && content != null && senderId != null && receiverId != null && timestamp != null) {
                            messages.add(new Message(id, content, senderId, receiverId, timestamp));
                        }
                    }
                }
            }
            return messages;
        } catch (Exception e) {
            return null;
        }
    }

}
