package com.example.secureshelledmessenger;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.secureshelledmessenger.model.Contact;
import com.example.secureshelledmessenger.model.User;
import com.example.secureshelledmessenger.ui.home.MainController;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.secureshelledmessenger.databinding.ActivityMainBinding;
import com.example.secureshelledmessenger.ui.home.AppTheme;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppTheme currentTheme; // Change to AppTheme type
    private Long userID;
    private User user;
    private MainController mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainController = MainController.getInstance(this);

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

    public User getGlobalUser(){
        //For now contacts is dummy data, future it will be gathered from database
        ArrayList<Contact> contacts = mainController.getContactsList();
//        ArrayList<Contact> contacts = new ArrayList<>();
//        contacts.add(new Contact("David","D123","d"));
//        contacts.add(new Contact("Caleb","C234","c"));
//        contacts.add(new Contact("Mario","M345","m"));
//        contacts.add(new Contact("John","J456","j"));
        user = new User(userID,"","",contacts);
        return user;
    }

    public void addGlobalUserContact(Contact contact){
        user.addContact(contact);
        System.out.println(user.getContacts());
    }

    public void updateGlobalUserContacts(){
        ArrayList<Contact> updatedContacts = mainController.getContactsList();
        user.setContacts(updatedContacts);
    }

    public void replaceGlobalUserContact(int position, Contact contact){
        user.getContacts().set(position,contact);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //Get instance of Navigation Controller
        NavController navController = Navigation.findNavController(findViewById(R.id.nav_host_fragment_activity_main));

        //returns True, navController returns to previous fragment
        return navController.navigateUp();
    }
}