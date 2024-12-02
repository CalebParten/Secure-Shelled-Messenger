package com.example.secureshelledmessenger;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.secureshelledmessenger.ui.home.AppTheme;
import com.example.secureshelledmessenger.ui.home.MainController;

public class CreateAccountActivity extends AppCompatActivity {

    private MainController mainController;
    private EditText usernameField, passwordField, confirmPasswordField;
    private Button submitButton;
    private TextView loginTextView;

    private AppTheme currentTheme;

    private Handler handler;

    private String[] permissions = {Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.INTERNET};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check for notification permission
        if (!hasNotifPermission()) {
            requestNotificationPermssion();
        }

        mainController = MainController.getInstance(this);

        // Load saved theme
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        currentTheme = AppTheme.fromString(prefs.getString("theme", AppTheme.LIGHT.getName()));

        // Apply theme
        applyTheme(currentTheme);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        usernameField = findViewById(R.id.usernameEditText);
        passwordField = findViewById(R.id.passwordEditText);
        confirmPasswordField = findViewById(R.id.confirmPasswordEditText);
        submitButton = findViewById(R.id.createAccountButton);
        loginTextView = findViewById(R.id.loginTextView);

        // Handle account creation on submit button click
        submitButton.setOnClickListener(view -> {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            String confirmPassword = confirmPasswordField.getText().toString();

            if (username.isEmpty()) {
                usernameField.setError("Username is required");
                usernameField.requestFocus();
            } else if (password.isEmpty()) {
                passwordField.setError("Password is required");
                passwordField.requestFocus();
            } else if (confirmPassword.isEmpty()) {
                confirmPasswordField.setError("Please confirm your password");
                confirmPasswordField.requestFocus();
            } else if (!password.equals(confirmPassword)) {
                confirmPasswordField.setError("Passwords do not match");
                confirmPasswordField.requestFocus();
            } else {
                // Check if username already exists
                new Thread(() -> {
                    long userId = mainController.getContactID(username);
                    runOnUiThread(() -> {
                        if (userId > 0) {
                            usernameField.setError("Username is already taken");
                        } else {
                            // Proceed with account creation
                            mainController.createUser(username, password);
                            goToLoginActivity();
                        }
                    });
                }).start();
            }
        });

        // Add OnClickListener for loginTextView
        loginTextView.setOnClickListener(view -> {
            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);  // Navigate to LoginActivity
            startActivity(intent);
        });
    }

    // Method to transition to LoginActivity after account creation
    private void goToLoginActivity() {
        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Apply the selected theme
    private void applyTheme(AppTheme theme) {
        setTheme(theme.getStyleResId());
    }

    // Check if notification permission is granted
    private boolean hasNotifPermission() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED);
    }

    // Check if internet permission is granted
    private boolean hasInternetPermission() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED);
    }

    // Request notification permission
    private void requestNotificationPermssion() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    requestNotificationPermssion();
                }
            }
        }
    }
}
