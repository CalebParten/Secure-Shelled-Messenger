package com.example.secureshelledmessenger.ui.home;

import com.example.secureshelledmessenger.R;

public enum AppTheme {
    LIGHT(R.style.Theme_SecureShelledMessenger_Light),
    DARK(R.style.Theme_SecureShelledMessenger_Dark),
    LIGHT_DARK(R.style.Theme_SecureShelledMessenger_LightDark),
    HIGH_CONTRAST(R.style.Theme_SecureShelledMessenger_HighContrast);
    //DARK_BORDER(R.style.Theme_SecureShelledMessenger_DarkBorder), // New dark border theme
    //DARK_PINK_BORDER(R.style.Theme_SecureShelledMessenger_DarkPinkBorder); // New dark pink border theme

    private final int styleResId;

    AppTheme(int styleResId) {
        this.styleResId = styleResId;
    }

    public int getStyleResId() {
        return styleResId;
    }

    public String getName() {
        return name(); // Returns the name of the enum constant (e.g., "LIGHT")
    }

    public static AppTheme fromString(String name) {
        for (AppTheme theme : AppTheme.values()) {
            if (theme.getName().equalsIgnoreCase(name)) {
                return theme;
            }
        }
        return LIGHT; // Default to LIGHT if no match is found
    }
}

