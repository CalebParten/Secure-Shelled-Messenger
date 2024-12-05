package com.example.secureshelledmessenger.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.secureshelledmessenger.LoginActivity;
import com.example.secureshelledmessenger.MainActivity;
import com.example.secureshelledmessenger.R;

public class SettingsFragment extends Fragment {

    private Spinner themes;
    private Switch notificationsSwitch;
    private Button privacySettingsButton, logoutButton, updatePasswordButton;
    private TextView accountUsernameText, accountPasswordText;
    private EditText newPasswordField;

    private static final String LIGHT_THEME = "LIGHT";
    private static final String DARK_THEME = "DARK";
    private static final String HIGH_CONTRAST_THEME = "HIGH_CONTRAST";

    private MainController mainController;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize views
        themes = view.findViewById(R.id.spinner_theme_selection);
        notificationsSwitch = view.findViewById(R.id.switch_notifications);
        privacySettingsButton = view.findViewById(R.id.btn_privacy_settings);
        logoutButton = view.findViewById(R.id.btn_logout);
        //updatePasswordButton = view.findViewById(R.id.btn_update_password);
        accountUsernameText = view.findViewById(R.id.txt_account_username);
        accountPasswordText = view.findViewById(R.id.txt_account_password);
        //newPasswordField = view.findViewById(R.id.edit_new_password);

        // Get the MainController instance
        mainController = MainController.getInstance(getContext());

        // Load and display account information
        displayAccountInfo();

        // Theme Selection
        setupThemeSelection();

        // Notifications Toggle
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        notificationsSwitch.setChecked(prefs.getBoolean("notifications_enabled", true));
        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("notifications_enabled", isChecked);
            editor.apply();
            Toast.makeText(getContext(), "Notifications " + (isChecked ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();
        });

        // Privacy Settings
        privacySettingsButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Privacy settings feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        // Logout Button
        logoutButton.setOnClickListener(v -> logout());

//        // Update Password Button
//        updatePasswordButton.setOnClickListener(v -> {
//            String newPassword = newPasswordField.getText().toString().trim();
//            if (newPassword.isEmpty()) {
//                Toast.makeText(getContext(), "Password cannot be empty.", Toast.LENGTH_SHORT).show();
//            } else if (newPassword.length() < 6) {
//                Toast.makeText(getContext(), "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
//            } else {
//                mainController.updatePassword(newPassword);
//                Toast.makeText(getContext(), "Password updated successfully.", Toast.LENGTH_SHORT).show();
//                newPasswordField.setText("");
//            }
//        });

        return view;
    }

    private void displayAccountInfo() {
        String username = mainController.getCurrentUsername();
        String password = mainController.getCurrentPassword();

        accountUsernameText.setText("Username: " + username);
        accountPasswordText.setText("Password: " + password);
    }

    private void setupThemeSelection() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.theme_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themes.setAdapter(adapter);

        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String savedTheme = prefs.getString("theme", LIGHT_THEME);
        themes.setSelection(adapter.getPosition(savedTheme));

        themes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTheme = parent.getItemAtPosition(position).toString();
                String currentTheme = prefs.getString("theme", LIGHT_THEME);

                if (!selectedTheme.equals(currentTheme)) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("theme", selectedTheme);
                    editor.apply();

                    MainActivity mainActivity = (MainActivity) getActivity();
                    if (mainActivity != null) {
                        mainActivity.updateTheme(AppTheme.fromString(selectedTheme));
                    }

                    Toast.makeText(getContext(), "Theme applied: " + selectedTheme, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    private void logout() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}
