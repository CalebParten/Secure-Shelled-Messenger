package com.example.secureshelledmessenger.model;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.*;


public class ApiData {

    private final OkHttpClient httpClient;
    private static ApiData apiData;
    private ArrayList<Message> messages;
    private static String urlBase = "https://ssm.cs458.enmucs.com/api/users";
    private static MediaType json = MediaType.get("application/json; charset=utf-8");


    private ApiData(Context context){
        this.messages = new ArrayList<>();
        this.httpClient = new OkHttpClient();

        new Thread(new Runnable() {
            @Override
            public void run() {

                //These are users that have been created
//                requestCreateUser("Mary","m");
//                requestCreateUser("JohnSmith","js24");
//                requestCreateUser("Mark","m24");


//                Long newID = Long.parseLong(requestLookupUser("Mary"));
//                Long loggedID = Long.parseLong(requestLookupUser("David"));
//                sendMessage(newID,loggedID,"I am good", "m","test_key");
//                deleteMessagesBetweenUsers(loggedID,newID,"D123","m");
//                sendMessage(newID,loggedID,"This is a test of encrypt method","m");
//                sendMessage(loggedID,newID,"Hello Mary","D123","test_key");
//                sendMessage(loggedID,newID,"How are you?","D123","test_key");
//                sendMessage(newID,loggedID,"Good. Yourself?","m","test_key");
//                sendMessage(loggedID,newID,"good","D123","test_key");
//                sendMessage(newID,loggedID,"Glad to hear","m","test_key");
//                deleteMessage(loggedID,9,"D123");
//                deleteMessage(loggedID,10,"D123");
//                deleteMessage(loggedID,11,"D123");
//                deleteMessage(loggedID,12,"D123");
//                deleteMessage(loggedID,13,"D123");
//                deleteMessage(loggedID,14,"D123");
//                deleteMessage(loggedID,15,"D123");
            }

        }).start();


    }

    //developer test methods
    public void waitBetweenMessage(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteMessagesBetweenUsers(long senderID, long receiverID,String senderPass, String receiverPass){
        ArrayList<Message> messagesToDelete = getConversation(senderID,receiverID,senderPass);
        for(Message message: messagesToDelete){
            if(message.getSenderId() == senderID){
                deleteMessage(message.getSenderId(),message.getId(),senderPass);
            }
            if(message.getSenderId() == receiverID){
                deleteMessage(message.getSenderId(),message.getId(),receiverPass);
            }
        }
    }



    public static ApiData getInstance(Context context){
        if(apiData == null){
            apiData = new ApiData(context);
        }
        return apiData;
    }

    public String requestLookupUser(String username){
        return ApiClient.lookupUser(username);
    }

    public String requestCreateUser(String username, String password){
        return ApiClient.createUser(username,password);
    }

    public String requestLogin(String username, String password){
        return ApiClient.login(username,password);
    }

    public boolean deleteUser(String username, String password){
        return ApiClient.deleteUser(username,password);
    }

    public ArrayList<Message> getConversation(long senderID, long receiverID, String password){
        ArrayList<Message> sortedConversation = new ArrayList<>(ApiClient.getConversation(senderID,receiverID,password));
        Comparator<Message> comparator = new Comparator<Message>() {
            @Override
            public int compare(Message message, Message t1) {
                return message.getTimestamp().compareTo(t1.getTimestamp());
            }
        };
        Collections.sort(sortedConversation, comparator);
        return sortedConversation;
    }

    public ArrayList<Message> getReceivedMessages(long userID, String password){
        return new ArrayList<>(ApiClient.getReceivedMessages(userID,password));
    }

    public ArrayList<Message> getSentMessages(long userID, String password){
        return new ArrayList<>(ApiClient.getSentMessages(userID, password));
    }

    public Message sendMessage(long senderID, long receiverID, String content, String password,String key) {
        return ApiClient.sendMessage(senderID,receiverID,content,password, key);
    }

    public String editUsername(long userId, String password, String newUsername){
        return ApiClient.editUsername(userId,password,newUsername);
    }

    public String editPassword(long userId, String password, String newPassword){
        return ApiClient.editPassword(userId, password, newPassword);
    }

    public Boolean deleteMessage(long userId, long messageId, String password){
        return ApiClient.deleteMessage(userId, messageId, password);
    }

    public long getContactID(String username) {
        try {
            System.out.println("'" + ApiClient.lookupUser(username) + "'");
            return Long.parseLong(ApiClient.lookupUser(username));
        } catch (Exception e) {
            System.out.println("user doesnt exist so returning -1");
            return (long) -1;
        }
    }

    public Message decryptMessage(Message message, String key){
        String encryptedContent = message.getContent();
        String decryptedContent = ApiClient.encryptMessage(encryptedContent,key);
        return new Message(message.getId(),
                decryptedContent,
                message.getSenderId(),
                message.getReceiverId(),
                message.getTimestamp());
    }
}
