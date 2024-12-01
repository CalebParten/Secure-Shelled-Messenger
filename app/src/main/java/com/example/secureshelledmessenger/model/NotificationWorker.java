package com.example.secureshelledmessenger.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.ui.home.ChatFragment;
import com.example.secureshelledmessenger.ui.home.MainController;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import android.os.Handler;

public class NotificationWorker extends Worker {

    private MainController mainController = MainController.getInstance();

    private ArrayList<Contact> contacts;
    private ArrayList<LocalDateTime> lastMessageTimes;
    private ArrayList<RecentMessage> recentMessages;
    private long userID;

    private Handler handler;
    private Runnable runnable;



    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try{
            contacts = mainController.getContactsList();
            userID = mainController.getCurrentUserID();

            handler = new Handler(Looper.getMainLooper());

            runnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println("notif worker checking for message every 60 seconds");
                    checkForMessages();
                    handler.postDelayed(this,60000);
                }
            };
            handler.post(runnable);

            return Result.success();
        }
        catch (Exception e){
            System.out.println("error checkign for notification");
            return Result.failure();
        }
    }

    public void buildNotification(String title, String content, int id, String username){

        Notification notification = new Notification.Builder(getApplicationContext(), "default")
                .setContentTitle(title).setContentText(content).setSmallIcon(R.drawable.ic_notifications_black_24dp).build();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id,notification);
    }

    public void checkForMessages(){
        try{
            for (Contact contact : contacts) {
                long recieverID = mainController.getContactID(contact.getUsername());
                ArrayList<Message> conversation = mainController.getConversation(userID, recieverID);
                Message lastMessage = conversation.get(conversation.size() - 1);
                if (lastMessage.getSenderId() != userID) {
                    RecentMessage lastSaved = mainController.getContactRecentMessage(contact.getUsername());
                    if (lastMessage.getTimestamp().isAfter(lastSaved.getTime())) {
                        System.out.println("notify");

                        ContextCompat.getMainExecutor(getApplicationContext()).execute(new Runnable() {
                            @Override
                            public void run() {
                                buildNotification("New Message from " + contact.getUsername(),
                                        lastMessage.getContent(), (int) recieverID,
                                        contact.getUsername());
                            }
                        });
                    } else {
                        System.out.println("dont notify");
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println("error checking messages in method");
        }
    }


}
