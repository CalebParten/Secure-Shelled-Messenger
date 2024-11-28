package com.example.secureshelledmessenger.ui.home;

import android.content.Context;

import com.example.secureshelledmessenger.model.ApiData;
import com.example.secureshelledmessenger.model.Contact;
import com.example.secureshelledmessenger.model.ContactData;
import com.example.secureshelledmessenger.model.Message;
import com.example.secureshelledmessenger.model.UserData;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainController {

    private static MainController mainController;
    private static ContactData contactDataModel;
    private static ApiData apiDataModel;
    private static UserData userData;

    private MainController(Context context){
        contactDataModel = ContactData.getInstance(context);
        apiDataModel = ApiData.getInstance(context);
        userData = UserData.getInstance(context);
    }

    public static MainController getInstance(Context context){
        if(mainController == null) {
            mainController = new MainController(context.getApplicationContext());
        }
        return mainController;
    }

    public static MainController getInstance(){
        return mainController;
    }

    public void addContact(String name, String username, String key){
        contactDataModel.addContact(name,username,key);
    }

    public void editContact(String newName, String username, String newKey){
        contactDataModel.editContact(newName,username,newKey);
    }

    public ArrayList<Contact> getContactsList(){
        for(Contact contact: contactDataModel.getContacts()){
            System.out.println(contact.getName());
        }
        return contactDataModel.getContacts();
    }

    public void deleteContactByIndex(int position){
        contactDataModel.deleteContactByIndex(position);
    }

    public void deleteContactByUsername(String username){
        contactDataModel.deletetContactByUsername(username);
    }

    public void getUserString(String username){
        System.out.println("Request Result: " + apiDataModel.requestLookupUser(username));
    }

    //Api Model interaction
    public void createUser(String username, String password){
        System.out.println("Create User Result: " + apiDataModel.requestCreateUser(username, password));
    }

    public String checkLogin(String username, String password){
        return apiDataModel.requestLogin(username,password);
    }

    public boolean deleteUser(String username, String password){
        return apiDataModel.deleteUser(username,password);
    }

    public ArrayList<Message> getConversation(long senderID, long receiverID){
        return apiDataModel.getConversation(senderID,receiverID,getCurrentPassword());
    }

    public ArrayList<Message> getReceivedMessages(long userID){
        return apiDataModel.getReceivedMessages(userID,getCurrentPassword());
    }

    public ArrayList<Message> getSentMessages(long userID){
        return apiDataModel.getSentMessages(userID,getCurrentPassword());
    }

    public Long getContactID(String username){
        return apiDataModel.getContactID(username);
    }

    public void sendMessage(long senderID, long receiverID, String content, String password, String key){
        apiDataModel.sendMessage(senderID,receiverID,content,password,key);
    }

    //User model interaction
    public void updateUsername(String username){
        userData.setUsername(username);
    }

    public void updatePassword(String password){
        userData.setPassword(password);
    }

    public void updateUserID(Long userid){
        userData.setUserid(userid);
    }

    public void updateContacts(ArrayList<Contact> contacts){
        userData.setContacts(contacts);
    }

    public String getCurrentUsername(){
        return userData.getUsername();
    }

    public String getCurrentPassword(){
        return userData.getPassword();
    }

    public Long getCurrentUserID(){
        return userData.getUserid();
    }

    public void getUserContacts(){
        contactDataModel.initiateContacts();
    }

}
