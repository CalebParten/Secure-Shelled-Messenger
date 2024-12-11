/**
 * @author Caleb Parten
 * @author David Schmith
 * @author Mario Soto
 * @date 12/10/2024
 *
 * This file defines a worker that checks for recent messages and sends a notification to the user
 * if there is a new recent message. This worker is run every 10 seconds within the main activity.
 */
package com.example.secureshelledmessenger.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.ui.home.MainController;

import java.util.ArrayList;

public class CheckMessageWorker extends Worker {

    private MainController mainController = MainController.getInstance();
    private ArrayList<Contact> contacts;
    long userID;

    //Constructor
    public CheckMessageWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    //every time worker is run
    @NonNull
    @Override
    public Result doWork() {
        try {
            contacts = mainController.getContactsList();
            userID = mainController.getCurrentUserID();

            //for all contacts check to see if saved recent message is different from API recent message
            for (Contact contact : contacts) {
                long recieverID = mainController.getContactID(contact.getUsername());

                //get conversation from the API
                ArrayList<Message> curConversation = mainController.getConversation(userID,
                        recieverID);

                //last message in conversation
                Message lastMessage = curConversation.get(curConversation.size() - 1);

                //last saved message on device
                RecentMessage lastSavedMessage = mainController.getContactRecentMessage(
                        contact.getUsername());

                /*
                 * If the message sender is not the current user and the last message is not null,
                 * then check to see if the saved message is before the last message from the API.
                 * If this is true then build a notification that notifies the user who they have
                 * received a message from and what the message contents are.
                 */
                if((lastSavedMessage != null) && (lastMessage.getSenderId() != userID)){
                    if(lastMessage.getTimestamp().isAfter(lastSavedMessage.getTime())){
                        System.out.println("notifying from check message");

                        ContextCompat.getMainExecutor(getApplicationContext()).execute(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        buildNotification("New Message from " + contact.getUsername(),
                                                mainController.decryptMessage(lastMessage, contact
                                                        .getAssignedKey()).getContent(), (int) recieverID);
                                    }
                                });
                    }
                    else{
                        System.out.println("no notification");
                    }
                }

                //save the recent messages from the api
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

    /**
     * This method builds a notification and sends it to the user's device
     * @param title
     * "New message from . . . . . "
     * @param content
     * message content
     * @param id
     */
    public void buildNotification(String title, String content, int id){
        Notification notification = new Notification.Builder(getApplicationContext(), "SSM_Channel")
                .setContentTitle(title).setContentText(content).setSmallIcon(R.drawable.ic_notifications_black_24dp).build();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id,notification);
    }
}
