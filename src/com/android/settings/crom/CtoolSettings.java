package com.android.settings.crom;

import android.os.Bundle;
import android.preference.Preference;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class CtoolSettings extends SettingsPreferenceFragment {

    private static final String TAG = "CtoolSettings";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.ctool_settings);
    }
}
