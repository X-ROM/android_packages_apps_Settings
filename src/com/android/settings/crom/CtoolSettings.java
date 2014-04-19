/*
 * Copyright (C) 2014 The C-RoM Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.crom;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class CtoolSettings extends SettingsPreferenceFragment
        implements OnSharedPreferenceChangeListener {

    private static final String TAG = "CtoolSettings";

    private static final String KERNELTWEAKER_START = "kerneltweaker_start";
    private static final String CROMOTA_START = "crom_ota_start";

    // Package name of the kernel tweaker app
    public static final String KERNELTWEAKER_PACKAGE_NAME = "com.dsht.kerneltweaker";
    // Intent for launching the kernel tweaker main actvity
    public static Intent INTENT_KERNELTWEAKER = new Intent(Intent.ACTION_MAIN)
            .setClassName(KERNELTWEAKER_PACKAGE_NAME, KERNELTWEAKER_PACKAGE_NAME + ".MainActivity");

    // Package name of the C-RoM Ota app
    public static final String CROMOTA_PACKAGE_NAME = "com.crom.cromota";
    // Intent for launching the C-RoM ota main actvity
    public static Intent INTENT_CROMOTA = new Intent(Intent.ACTION_MAIN)
            .setClassName(CROMOTA_PACKAGE_NAME, CROMOTA_PACKAGE_NAME + ".MainActivity");

    private Preference mKernelTweaker;
    private Preference mCromOta;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.ctool_settings);

        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        mKernelTweaker = (Preference)
                prefSet.findPreference(KERNELTWEAKER_START);

        mCromOta = (Preference)
                prefSet.findPreference(CROMOTA_START);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mKernelTweaker) {
            startActivity(INTENT_KERNELTWEAKER);
            return true;
        } else if (preference == mCromOta) {
            startActivity(INTENT_CROMOTA);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}

