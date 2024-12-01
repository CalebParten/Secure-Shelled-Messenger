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


    public CheckMessageWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }
    @NonNull
    @Override
    public Result doWork() {
        try {
            contacts = mainController.getContactsList();
            userID = mainController.getCurrentUserID();

            for (Contact contact : contacts) {
                long recieverID = mainController.getContactID(contact.getUsername());
                ArrayList<Message> curConversation = mainController.getConversation(userID, recieverID);
                Message lastMessage = curConversation.get(curConversation.size() - 1);
                RecentMessage lastSavedMessage = mainController.getContactRecentMessage(contact.getUsername());

                if((lastSavedMessage != null) && (lastMessage.getSenderId() != userID)){
                    if(lastMessage.getTimestamp().isAfter(lastSavedMessage.getTime())){
                        System.out.println("notifying from check message");

                        ContextCompat.getMainExecutor(getApplicationContext()).execute(new Runnable() {
                            @Override
                            public void run() {
                                buildNotification("New Message from " + contact.getUsername(),
                                        mainController.decryptMessage(lastMessage, contact.getAssignedKey()).getContent(), (int) recieverID);
                            }
                        });
                    }
                    else{
                        System.out.println("no notification");
                    }
                }

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

    public void buildNotification(String title, String content, int id){
        Notification notification = new Notification.Builder(getApplicationContext(), "SSM_Channel")
                .setContentTitle(title).setContentText(content).setSmallIcon(R.drawable.ic_notifications_black_24dp).build();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id,notification);
    }
}
