/**
 * @author Caleb Parten
 * @author David Schmith
 * @author Mario Soto
 * @date 12/10/2024
 *
 * This file represents the model for the contacts. This file uses the TinyDB library to save the
 * contacts of a user to shared preferences with the key being the user's username.
 */
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

    //Constructor
    private ContactData(Context context){
        this.contacts = new ArrayList<>();
        this.recentMessages = new ArrayList<>();
        this.tinyDB = new TinyDB(context);
        apiData = ApiData.getInstance(context);
        userData = UserData.getInstance(context);
        this.context = context;
    }

    //Creates new instance if it does not exist already
    public static ContactData getInstance(Context context){
        if(contactData == null){
            contactData = new ContactData(context);
        }
        return contactData;

    }

    //Add contact to the database
    public void addContact(String name, String username, String key){
        Contact contact = new Contact(name,username,key);
        this.contacts.add(contact);
        saveContacts();
    }

    //edit contact in the database
    public void editContact(String newName, String username, String newKey){

        for(Contact contact: contacts){
            if(contact.getUsername().equals(username)){
                contact.setName(newName);
                contact.setAssignedKey(newKey);
                saveContacts();
            }
        }
        contacts.clear();
        contacts.addAll(getContacts());

    }

    //returns all of the contacts in database
    public ArrayList<Contact> getContacts(){
        String contactsString = tinyDB.getString(MainController.getInstance().getCurrentUsername());
        if(contactsString.isEmpty()){
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        contacts = gson.fromJson(contactsString, new TypeToken<ArrayList<Contact>>(){}.getType());
        return contacts;
    }

    //replaces saved recent message between user and contact
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

    //gets the recent message between the user and a contact
    public RecentMessage getContactRecentMessage(String username){
        for(RecentMessage recentMessage: recentMessages){
            if(recentMessage.getSender().equals(username)){
                return recentMessage;
            }
        }
        return null;
    }

    //saves the contacts within the shared preferences in form of JSON file
    public void saveContacts(){
        Gson gson = new Gson();
        String contactsString = gson.toJson(contacts);
        tinyDB.putString(MainController.getInstance().getCurrentUsername(), contactsString);
    }

    //deletes a contact by the position in the list
    public void deleteContactByIndex(int position){
        contacts.remove(position);
        saveContacts();
    }

    //deletes a contact by the username
    public void deletetContactByUsername(String username){

        for(int index = 0; index < contacts.size(); index++){

            Contact currentContact = contacts.get(index);

            if(currentContact.getUsername().equals(username)){
                contacts.remove(index);
                saveContacts();
            }
        }
    }

    //removes all contacts from the database
    public void deleteAllContacts(){
        contacts.clear();
        saveContacts();
    }

    //initiates the contents from shared preferences
    public void initiateContacts(){
//        if(getContacts().isEmpty()){
////            loadDummyContacts();
////            saveContacts();
//        }
        getContacts();
    }

    //save the list of saved recent messages
    public void saveRecentMessages(){
        Gson gson = new Gson();
        String contactsString = gson.toJson(recentMessages);
        tinyDB.putString(MainController.getInstance().getCurrentUsername() + "RecentMessages", contactsString);
    }

    //returns the list of saved recent messages
    public ArrayList<RecentMessage> getRecentMessages(){
        String recentMessagesString = tinyDB.getString(MainController.getInstance().getCurrentUsername() + "RecentMessages");
        if(recentMessagesString.isEmpty()){
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        recentMessages = gson.fromJson(recentMessagesString, new TypeToken<ArrayList<RecentMessage>>(){}.getType());
        return recentMessages;
    }

    //replaces the list of saved recent messages with a new list
    public void setRecentMessages(ArrayList<RecentMessage> recentMessages){
        this.recentMessages = recentMessages;
    }

    //used for testing to load fake contacts
    public void loadDummyContacts(){
        contacts.add(new Contact("David","D123","d"));
        contacts.add(new Contact("Caleb","C234","c"));
        contacts.add(new Contact("Mario","M345","m"));
        contacts.add(new Contact("John","J456","j"));
        contacts.add(new Contact("Mary","Mary","m"));
    }






}
