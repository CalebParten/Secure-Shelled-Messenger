package com.example.secureshelledmessenger;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.secureshelledmessenger.databinding.ActivityMainBinding;
import com.example.secureshelledmessenger.ui.home.AppTheme;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppTheme currentTheme; // Change to AppTheme type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load saved theme
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
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
}