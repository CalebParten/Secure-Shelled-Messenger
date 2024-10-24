package com.example.secureshelledmessenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.secureshelledmessenger.model.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    //Dummy data container (will change)
    ArrayList<User> users;
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
        users = new ArrayList<>();
        users.add(new User((long)0,"default","default",new ArrayList<>()));
        users.add(new User((long)1,"David","D123",new ArrayList<>()));
        users.add(new User((long)2,"Caleb","C123",new ArrayList<>()));
        users.add(new User((long)3,"Mario","M123",new ArrayList<>()));

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
                    for(User user: users){
                        String currentUsername = user.getUsername();
                        String currentPassword = user.getPassword();
                        if(currentUsername.equals(username) && currentPassword.equals(password)){
                            SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putLong("userID",user.getId());
                            editor.apply();
                            goToMainActivity();
                            return;
                        }
                    }
                    usernameField.setError("Incorrect Username or Password");
                    usernameField.setText("");
                    passwordField.setText("");
                    usernameField.requestFocus();
                }
            }
        });
    }

    public void goToMainActivity(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

}