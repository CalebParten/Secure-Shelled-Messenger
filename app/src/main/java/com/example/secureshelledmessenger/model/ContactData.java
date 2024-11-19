package com.example.secureshelledmessenger.model;

import android.content.Context;

import com.example.secureshelledmessenger.libraries.TinyDB;
import com.example.secureshelledmessenger.ui.home.MainController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ContactData {

    private static ContactData contactData;
    private ArrayList<Contact> contacts;
    private TinyDB tinyDB;


    private ContactData(Context context){
        this.contacts = new ArrayList<>();
        this.tinyDB = new TinyDB(context);
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
        for(int i = 0; i < contacts.size(); i++){

            Contact currentContact = contacts.get(i);
            if(currentContact.getUsername().equals(username)){
                currentContact.setName(newName);
                currentContact.setAssignedKey(newKey);
                saveContacts();
                return;
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

    public void saveContacts(){
        Gson gson = new Gson();
        String contactsString = gson.toJson(contacts);
        tinyDB.putString(MainController.getInstance().getCurrentUsername(), contactsString);
    }

    public void deleteContact(int position){
        contacts.remove(position);
        saveContacts();
    }

    public void initiateContacts(){
        if(getContacts().isEmpty()){
            loadDummyContacts();
            saveContacts();
        }
        getContacts();
    }

    public void loadDummyContacts(){
        contacts.add(new Contact("David","D123","d"));
        contacts.add(new Contact("Caleb","C234","c"));
        contacts.add(new Contact("Mario","M345","m"));
        contacts.add(new Contact("John","J456","j"));
        contacts.add(new Contact("Mary","Mary","m"));
    }






}
