/**
 * @author Mario Soto
 * @date 12/10/2024
 *
 * This file is the activity that is shown to the user when they select "create account"
 */
package com.example.secureshelledmessenger;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.secureshelledmessenger.ui.home.AppTheme;
import com.example.secureshelledmessenger.ui.home.MainController;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";

    private MainController mainController;
    private EditText usernameField, passwordField, confirmPasswordField;
    private Button createAccountButton;
    private TextView loginTextView;

    private AppTheme currentTheme;
    private ProgressDialog progressDialog;

    private String[] permissions = {Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.INTERNET};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check and request permissions
        if (!hasPermission(Manifest.permission.POST_NOTIFICATIONS)) {
            requestPermission(Manifest.permission.POST_NOTIFICATIONS, 1);
        }

        // Initialize MainController
        mainController = MainController.getInstance(this);

        // Load saved theme
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        currentTheme = AppTheme.fromString(prefs.getString("theme", AppTheme.LIGHT.getName()));

        // Apply theme
        applyTheme(currentTheme);

        // Set layout
        setContentView(R.layout.activity_create_account);

        // Initialize UI components
        usernameField = findViewById(R.id.usernameEditText);
        passwordField = findViewById(R.id.passwordEditText);
        confirmPasswordField = findViewById(R.id.confirmPasswordEditText);
        createAccountButton = findViewById(R.id.createAccountButton);
        loginTextView = findViewById(R.id.loginTextView);

        // Set listeners
        createAccountButton.setOnClickListener(view -> handleCreateAccount());
        loginTextView.setOnClickListener(view -> navigateToLogin());
    }

    private void handleCreateAccount() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();

        // Validate inputs
        if (username.isEmpty()) {
            usernameField.setError("Username is required");
            usernameField.requestFocus();
            return;
        }
        if (username.length() < 3) {
            usernameField.setError("Username must be at least 3 characters long");
            usernameField.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordField.setError("Password is required");
            passwordField.requestFocus();
            return;
        }
        if (password.length() < 6) {
            passwordField.setError("Password must be at least 6 characters long");
            passwordField.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordField.setError("Passwords do not match");
            confirmPasswordField.requestFocus();
            return;
        }

        showProgressDialog("Checking username...");

        // Check if username exists
        new Thread(() -> {
            try {
                long checkedID = mainController.getContactID(username);

                runOnUiThread(() -> {
                    dismissProgressDialog();

                    if (checkedID > 0) {
                        usernameField.setError("Username is already taken");
                        usernameField.requestFocus();
                    } else {
                        // Create the user if username is available
                        createNewUser(username, password);
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    dismissProgressDialog();
                    Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_LONG).show();
                });
                Log.e(TAG, "Error checking username", e);
            }
        }).start();
    }

    private void createNewUser(String username, String password) {
        showProgressDialog("Creating Account...");
        new Thread(() -> {
            try {
                mainController.createUser(username, password);
                runOnUiThread(() -> {
                    dismissProgressDialog();
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_LONG).show();
                    navigateToLogin();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    dismissProgressDialog();
                    Toast.makeText(this, "An error occurred while creating account. Please try again.", Toast.LENGTH_LONG).show();
                });
                Log.e(TAG, "Error creating user", e);
            }
        }).start();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void applyTheme(AppTheme theme) {
        setTheme(theme.getStyleResId());
    }

    private void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission, int requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Notification permission is required for full functionality", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
