package com.example.secureshelledmessenger;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.secureshelledmessenger.ui.home.AppTheme;
import com.example.secureshelledmessenger.ui.home.MainController;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private Button submitButton;

    private MainController mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mainController = MainController.getInstance(this);

        usernameField = findViewById(R.id.usernameEditText);
        passwordField = findViewById(R.id.passwordEditText);
        confirmPasswordField = findViewById(R.id.confirmPasswordEditText);
        submitButton = findViewById(R.id.createAccountButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                String confirmPassword = confirmPasswordField.getText().toString();

                // Validate inputs
                if (username.isEmpty()) {
                    usernameField.setError("Username is required");
                    return;
                }

                if (password.isEmpty()) {
                    passwordField.setError("Password is required");
                    return;
                }

                if (confirmPassword.isEmpty()) {
                    confirmPasswordField.setError("Please confirm your password");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    confirmPasswordField.setError("Passwords do not match");
                    return;
                }

                // Check if the username is already taken
                long checkedID = mainController.getContactID(username);

                if (checkedID > 0) {
                    // Username is already taken
                    usernameField.setError("Username is already taken");
                } else {
                    // Username is available, proceed to create the account
                    mainController.createUser(username, password);
                    goToLoginActivity();
                }
            }
        });
    }

    private void goToLoginActivity() {
        // Transition to the login activity after account creation
        finish(); // Close CreateAccountActivity
    }
}
