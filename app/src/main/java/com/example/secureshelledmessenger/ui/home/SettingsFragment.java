package com.example.secureshelledmessenger.ui.home;

import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.MainActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.SharedPreferences;
import android.widget.AdapterView;
import android.widget.Toast;
import android.util.Log;

public class SettingsFragment extends Fragment {

    private Spinner themes;
    private static final String LIGHT_THEME = "Light";
    private static final String DARK_THEME = "Dark";

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        themes = view.findViewById(R.id.spinner_theme_selection);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.theme_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themes.setAdapter(adapter);

        SharedPreferences prefs = getActivity().getSharedPreferences("AppPrefs", getContext().MODE_PRIVATE);
        String savedTheme = prefs.getString("theme", LIGHT_THEME);
        themes.setSelection(adapter.getPosition(savedTheme));

        themes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedThemeString = (String) parent.getItemAtPosition(position);
                
                AppTheme selectedTheme;
                try {
                    selectedTheme = AppTheme.valueOf(selectedThemeString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    Log.e("SettingsFragment", "Invalid theme selected: " + selectedThemeString, e);
                    Toast.makeText(getContext(), "Invalid theme selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                String currentTheme = prefs.getString("theme", LIGHT_THEME);
                if (!selectedThemeString.equals(currentTheme)) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("theme", selectedThemeString);
                    editor.apply();

                    MainActivity mainActivity = (MainActivity) getActivity();
                    if (mainActivity != null) {
                        mainActivity.updateTheme(selectedTheme);
                    }

                    Toast.makeText(getContext(), "Theme applied: " + selectedThemeString, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

        return view;
    }
}
