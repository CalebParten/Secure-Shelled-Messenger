package com.example.secureshelledmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    //Dummy data containers (will change)
    ArrayList<String> usernames;
    ArrayList<String> passwords;

    EditText usernameField;
    EditText passwordField;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Initiate Dummy Data (can Add,Delete,Edit these lines)
        usernames = new ArrayList<>();
        usernames.add("David");
        usernames.add("Caleb");
        usernames.add("Mario");
        passwords = new ArrayList<>();
        passwords.add("D123");
        passwords.add("C123");
        passwords.add("M123");

        usernameField = findViewById(R.id.username_field);
        passwordField = findViewById(R.id.password_field);
        submitButton = findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                if(username.isEmpty()){
                    usernameField.setError("Username is Required");
                    usernameField.requestFocus();
                }
                else if(password.isEmpty()){
                    passwordField.setError("Password is Required");
                    passwordField.requestFocus();
                }
                else{
                    if(usernames.contains(username) && passwords.contains(password)){
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        usernameField.setError("Incorrect Username or Password");
                        usernameField.setText("");
                        passwordField.setText("");
                        usernameField.requestFocus();
                    }
                }
            }
        });
    }
}