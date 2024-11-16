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
    }

    public static ApiData getInstance(Context context){
        if(apiData == null){
            apiData = new ApiData(context);
        }
        return apiData;
    }

    public String requestLookupUser(String username){
        String reqURL = urlBase + "/lookup";
        String payload = "{\"username\":\"" + username + "\"}";
        RequestBody requestBody = RequestBody.create(payload,json);
        Request request = new Request.Builder().url(reqURL).post(requestBody)
                .addHeader("Content-Type","application/json").build();

        try(Response response = httpClient.newCall(request).execute()){
            if(response.isSuccessful() && response.body() != null){
                return response.body().string();
            }
            else{
                System.out.println("LookupUser Request Failed");
                return null;
            }
        }
        catch (IOException e){
            System.out.println(e.toString());
            return null;
        }
    }

    public String requestCreateUser(String username, String password){
        String reqURL = urlBase + "/create";
        String payload = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
        RequestBody requestBody = RequestBody.create(payload,json);
        Request request = new Request.Builder().url(reqURL).post(requestBody)
                .addHeader("Content-Type", "application/json").build();

        try(Response response = httpClient.newCall(request).execute()){
            if(response.isSuccessful() && response.body() != null){
                return response.body().string();
            }
            else{
                System.out.println("Create User Request Failed");
                return null;
            }
        }
        catch (IOException e){
            System.out.println(e.toString());
            return null;
        }
    }

    public String requestLogin(String username, String password){
        String reqURL = urlBase + "/login";
        String payload = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
        RequestBody requestBody = RequestBody.create(payload,json);
        Request request = new Request.Builder().url(reqURL).post(requestBody)
                .addHeader("Content-Type", "application/json").build();

        try(Response response = httpClient.newCall(request).execute()){
            if(response.isSuccessful() && response.body() != null){
                return response.body().string();
            }
            else{
                System.out.println("Create User Request Failed");
                return null;
            }
        }
        catch (IOException e){
            System.out.println(e.toString());
            return null;
        }
    }

}
