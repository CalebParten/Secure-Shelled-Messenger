package com.example.secureshelledmessenger.model;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import com.example.secureshelledmessenger.libraries.TinyDB;
import com.example.secureshelledmessenger.ui.home.MainController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class ContactData {

    private static ContactData contactData;
    private ApiData apiData;
    private UserData userData;
    private Context context;

    private ArrayList<Contact> contacts;
    private ArrayList<RecentMessage> recentMessages;

    private TinyDB tinyDB;


    private ContactData(Context context){
        this.contacts = new ArrayList<>();
        this.recentMessages = new ArrayList<>();
        this.tinyDB = new TinyDB(context);
        apiData = ApiData.getInstance(context);
        userData = UserData.getInstance(context);
        this.context = context;
    }

    public static ContactData getInstance(Context context){
        if(contactData == null){
            contactData = new ContactData(context);
        }
        return contactData;

    }

    public void addContact(String name, String username, String key){
        Contact contact = new Contact(name,username,key);
        this.contacts.add(contact);
        saveContacts();
    }

    public void editContact(String newName, String username, String newKey){

        for(Contact contact: contacts){
            if(contact.getUsername().equals(username)){
                contact.setName(newName);
                contact.setAssignedKey(newKey);
                saveContacts();
            }
        }

    }

    public ArrayList<Contact> getContacts(){
        String contactsString = tinyDB.getString(MainController.getInstance().getCurrentUsername());
        if(contactsString.isEmpty()){
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        contacts = gson.fromJson(contactsString, new TypeToken<ArrayList<Contact>>(){}.getType());
        return contacts;
    }

    public void replaceRecentMessage(String sender, String content, LocalDateTime time){
        RecentMessage newRecentMessage = new RecentMessage(sender,content,time);
        for(int i = 0; i < recentMessages.size(); i++){
            if(recentMessages.get(i).getSender().equals(sender)){
                recentMessages.set(i,newRecentMessage);
                saveRecentMessages();
                return;
            }
        }
        recentMessages.add(newRecentMessage);
        saveRecentMessages();
    }

    public RecentMessage getContactRecentMessage(String username){
        for(RecentMessage recentMessage: recentMessages){
            if(recentMessage.getSender().equals(username)){
                return recentMessage;
            }
        }
        return null;
    }

    public void saveContacts(){
        Gson gson = new Gson();
        String contactsString = gson.toJson(contacts);
        tinyDB.putString(MainController.getInstance().getCurrentUsername(), contactsString);
    }

    public void deleteContactByIndex(int position){
        contacts.remove(position);
        saveContacts();
    }

    public void deletetContactByUsername(String username){

        for(int index = 0; index < contacts.size(); index++){

            Contact currentContact = contacts.get(index);

            if(currentContact.getUsername().equals(username)){
                contacts.remove(index);
                saveContacts();
            }
        }
    }

    public void deleteAllContacts(){
        contacts.clear();
        saveContacts();
    }

    public void initiateContacts(){
//        if(getContacts().isEmpty()){
////            loadDummyContacts();
////            saveContacts();
//        }
        getContacts();
    }

    public void saveRecentMessages(){
        Gson gson = new Gson();
        String contactsString = gson.toJson(recentMessages);
        tinyDB.putString(MainController.getInstance().getCurrentUsername() + "RecentMessages", contactsString);
    }

    public ArrayList<RecentMessage> getRecentMessages(){
        String recentMessagesString = tinyDB.getString(MainController.getInstance().getCurrentUsername() + "RecentMessages");
        if(recentMessagesString.isEmpty()){
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        recentMessages = gson.fromJson(recentMessagesString, new TypeToken<ArrayList<RecentMessage>>(){}.getType());
        return recentMessages;
    }

    public void setRecentMessages(ArrayList<RecentMessage> recentMessages){
        this.recentMessages = recentMessages;
    }

    public void loadDummyContacts(){
        contacts.add(new Contact("David","D123","d"));
        contacts.add(new Contact("Caleb","C234","c"));
        contacts.add(new Contact("Mario","M345","m"));
        contacts.add(new Contact("John","J456","j"));
        contacts.add(new Contact("Mary","Mary","m"));
    }






}
