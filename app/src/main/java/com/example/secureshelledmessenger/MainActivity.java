package com.example.secureshelledmessenger;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.Toast;

import com.example.secureshelledmessenger.model.CheckMessageWorker;
import com.example.secureshelledmessenger.model.Contact;
//import com.example.secureshelledmessenger.model.User;
import com.example.secureshelledmessenger.model.NotificationWorker;
import com.example.secureshelledmessenger.ui.home.MainController;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.secureshelledmessenger.databinding.ActivityMainBinding;
import com.example.secureshelledmessenger.ui.home.AppTheme;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppTheme currentTheme; // Change to AppTheme type
    private Long userID;
//    private User user;
    private MainController mainController;

    private Handler recentMessageHandler = new Handler();
    private Runnable recentMessageRunnable;
    private Handler notifyHandler = new Handler();
    private Runnable notifyRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildNotifChannel();

        mainController = MainController.getInstance(this);
        mainController.getUserContacts();

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        //Load saved User ID
        userID = prefs.getLong("userID",9999);

        // Load saved theme
        currentTheme = AppTheme.fromString(prefs.getString("theme", AppTheme.LIGHT.getName())); // Default to LIGHT if not set

        // Apply theme
        applyTheme(currentTheme);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_contacts, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        recentMessageRunnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("updating recent messages");
                initiateCheckMessageWorker();
                recentMessageHandler.postDelayed(this, 10000);
            }
        };
        recentMessageHandler.post(recentMessageRunnable);

        initiateNotifyWorker();
//        notifyRunnable = new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("checking to notify");
//                initiateNotifyWorker();
//                notifyHandler.postDelayed(this,20000);
//            }
//        };
//        notifyHandler.post(notifyRunnable);

        new Thread(new Runnable() {
            @Override
            public void run() {
//                mainController.createUser("David","D123");
                mainController.getUserString(mainController.getCurrentUsername());
                System.out.println(mainController.getCurrentPassword());
                System.out.println(mainController.getCurrentUserID());
                System.out.println(mainController.getCurrentUsername());
            }
        }).start();
    }

    private void applyTheme(AppTheme theme) {
        setTheme(theme.getStyleResId()); // Directly apply the theme
    }

    // Call this method when the theme changes in SettingsFragment
    public void updateTheme(AppTheme newTheme) {
        if (!newTheme.equals(currentTheme)) {
            currentTheme = newTheme; // Update current theme variable
            applyTheme(currentTheme);

            // Update SharedPreferences to store the new theme
            SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("theme", currentTheme.getName()); // Use getName() here
            editor.apply();

            // Show a toast message for feedback
            Toast.makeText(this, "Theme applied: " + currentTheme.getName(), Toast.LENGTH_SHORT).show();

            // Recreate the activity to apply the new theme
            recreate(); // Consider commenting this out for a smoother experience
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //Get instance of Navigation Controller
        NavController navController = Navigation.findNavController(findViewById(R.id.nav_host_fragment_activity_main));

        //returns True, navController returns to previous fragment
        return navController.navigateUp();
    }

    public void initiateCheckMessageWorker(){

//        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(CheckMessageWorker.class,
//                10, TimeUnit.SECONDS).build();

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(CheckMessageWorker.class).build();

        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest);
//        WorkManager.getInstance(this).enqueueUniquePeriodicWork("refreshRecentMessages",
//                ExistingPeriodicWorkPolicy.KEEP,periodicWorkRequest);
    }

    public void initiateNotifyWorker(){
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class,
                15,TimeUnit.MINUTES).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("checkNotifications",
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,periodicWorkRequest);
    }

    public void buildNotifChannel(){
        CharSequence channelName = "SSM";
        String description = "Notifications for Secure Shelled Messenger";
        int importanceLevel = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel = new NotificationChannel("SSM_Channel",
                channelName,importanceLevel);
        notificationChannel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);
    }
}