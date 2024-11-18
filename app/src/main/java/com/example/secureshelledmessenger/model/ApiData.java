package com.example.secureshelledmessenger.model;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
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
//                requestCreateUser("Mary","m");
                Long newID = Long.parseLong(requestLookupUser("Mary"));
                Long loggedID = Long.parseLong(requestLookupUser("David"));
//                sendMessage(loggedID,newID,"Hello Mary","D123");
//                sendMessage(loggedID,newID,"How are you?","D123");
//                sendMessage(newID,loggedID,"Good, yourself?","m");
//                sendMessage(loggedID,newID,"good","D123");
//                sendMessage(newID,loggedID,"Glad to hear","m");
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
        return new ArrayList<>(ApiClient.getConversation(senderID,receiverID,password));
    }

    public ArrayList<Message> getReceivedMessages(long userID, String password){
        return new ArrayList<>(ApiClient.getReceivedMessages(userID,password));
    }

    public ArrayList<Message> getSentMessages(long userID, String password){
        return new ArrayList<>(ApiClient.getSentMessages(userID, password));
    }

    public Message sendMessage(long senderID, long receiverID, String content, String password) {
        return ApiClient.sendMessage(senderID,receiverID,content,password);
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

    public Long getContactID(String username){
        return Long.parseLong(ApiClient.lookupUser(username));
    }


}
