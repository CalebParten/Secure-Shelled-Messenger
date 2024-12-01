package com.example.secureshelledmessenger.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.secureshelledmessenger.ui.home.MainController;

import java.util.ArrayList;

public class CheckMessageWorker extends Worker {

    private MainController mainController = MainController.getInstance();
    private ArrayList<Contact> contacts;


    public CheckMessageWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }
    @NonNull
    @Override
    public Result doWork() {
        contacts = mainController.getContactsList();

        try {
            for (Contact contact : contacts) {
                long recieverID = mainController.getContactID(contact.getUsername());
                long userID = mainController.getCurrentUserID();
                ArrayList<Message> curConversation = mainController.getConversation(userID, recieverID);
                for (int i = curConversation.size() - 1; i >= 0; i--) {
                    System.out.println("checking");
                    if (curConversation.get(i).getSenderId() == recieverID) {
                        mainController.replaceRecentMessage(contact.getUsername(),
                                curConversation.get(i).getContent(),
                                curConversation.get(i).getTimestamp());
                        break;
                    }
                }
            }
            return Result.success();
        }
        catch (Exception e){
            System.out.println("worker failed to get recent messages");
            return Result.failure();
        }
    }
}
