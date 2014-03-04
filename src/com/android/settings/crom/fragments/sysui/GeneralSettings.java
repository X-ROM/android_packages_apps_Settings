/*
 * Copyright (C) 2014 The CarbonRom project
 * Copyright (C) 2014 The C-RoM project
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

package com.android.settings.crom.fragments.sysui;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManagerGlobal;
import com.android.settings.util.Helpers;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.SettingsPreferenceFragment;

public class GeneralSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "GeneralSettings";

    private static final String KEY_EXPANDED_DESKTOP = "expanded_desktop";
    private static final String KEY_EXPANDED_DESKTOP_NO_NAVBAR = "expanded_desktop_no_navbar";
    private static final String CATEGORY_NAVBAR = "navigation_bar";
    private static final String KEY_POWER_CRT_MODE = "system_power_crt_mode";
    private static final String KEY_SCREEN_GESTURE_SETTINGS = "touch_screen_gesture_settings";
    private static final String CUSTOM_RECENT_MODE = "custom_recent_mode";

    private ListPreference mCrtMode;
    private ListPreference mExpandedDesktopPref;
    private CheckBoxPreference mExpandedDesktopNoNavbarPref;
    private CheckBoxPreference mRecentsCustom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.ui_general_settings);
        PreferenceScreen prefScreen = getPreferenceScreen();

        // Expanded desktop
        mExpandedDesktopPref = (ListPreference) findPreference(KEY_EXPANDED_DESKTOP);
        mExpandedDesktopNoNavbarPref = (CheckBoxPreference) findPreference(KEY_EXPANDED_DESKTOP_NO_NAVBAR);

        boolean enableRecentsCustom = Settings.System.getBoolean(getContentResolver(),
                                      Settings.System.CUSTOM_RECENT, false);
        mRecentsCustom = (CheckBoxPreference) findPreference(CUSTOM_RECENT_MODE);
        mRecentsCustom.setChecked(enableRecentsCustom);
        mRecentsCustom.setOnPreferenceChangeListener(this);

        int expandedDesktopValue = Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_DESKTOP_STYLE, 0);

        try {
            boolean hasNavBar = WindowManagerGlobal.getWindowManagerService().hasNavigationBar();

            if (hasNavBar) {
                mExpandedDesktopPref.setOnPreferenceChangeListener(this);
                mExpandedDesktopPref.setValue(String.valueOf(expandedDesktopValue));
                updateExpandedDesktop(expandedDesktopValue);
                prefScreen.removePreference(mExpandedDesktopNoNavbarPref);
            } else {
                // Hide no-op "Status bar visible" expanded desktop mode
                mExpandedDesktopNoNavbarPref.setOnPreferenceChangeListener(this);
                mExpandedDesktopNoNavbarPref.setChecked(expandedDesktopValue > 0);
                prefScreen.removePreference(mExpandedDesktopPref);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error getting navigation bar status");
        }

        // respect device default configuration
        // true fades while false animates
        boolean electronBeamFadesConfig = getResources().getBoolean(
                com.android.internal.R.bool.config_animateScreenLights);

        mCrtMode = (ListPreference) prefScreen.findPreference(KEY_POWER_CRT_MODE);
        if (!electronBeamFadesConfig && mCrtMode != null) {
            int crtMode = Settings.System.getInt(getContentResolver(),
                    Settings.System.SYSTEM_POWER_CRT_MODE, 1);
            mCrtMode.setValue(String.valueOf(crtMode));
            mCrtMode.setSummary(mCrtMode.getEntry());
            mCrtMode.setOnPreferenceChangeListener(this);
        } else if (mCrtMode != null) {
            prefScreen.removePreference(mCrtMode);
        }

        Utils.updatePreferenceToSpecificActivityFromMetaDataOrRemove(getActivity(),
                getPreferenceScreen(), KEY_SCREEN_GESTURE_SETTINGS);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mCrtMode) {
            int value = Integer.parseInt((String) objValue);
            int index = mCrtMode.findIndexOfValue((String) objValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.SYSTEM_POWER_CRT_MODE, value);
            mCrtMode.setSummary(mCrtMode.getEntries()[index]);
            return true;
        } else if (preference == mExpandedDesktopPref) {
            int expandedDesktopValue = Integer.valueOf((String) objValue);
            updateExpandedDesktop(expandedDesktopValue);
            return true;
        } else if (preference == mExpandedDesktopNoNavbarPref) {
            boolean value = (Boolean) objValue;
            updateExpandedDesktop(value ? 2 : 0);
            return true;
        } else if (preference == mRecentsCustom) { // Enable||disbale Slim Recent
            Settings.System.putBoolean(getActivity().getContentResolver(),
                    Settings.System.CUSTOM_RECENT,
                    ((Boolean) objValue) ? true : false);
            Helpers.restartSystemUI();
            return true;
        }

        return false;
    }

    private void updateExpandedDesktop(int value) {
        ContentResolver cr = getContentResolver();
        Resources res = getResources();
        int summary = -1;

        Settings.System.putInt(cr, Settings.System.EXPANDED_DESKTOP_STYLE, value);

        if (value == 0) {
            // Expanded desktop deactivated
            Settings.System.putInt(cr, Settings.System.POWER_MENU_EXPANDED_DESKTOP_ENABLED, 0);
            Settings.System.putInt(cr, Settings.System.EXPANDED_DESKTOP_STATE, 0);
            summary = R.string.expanded_desktop_disabled;
        } else if (value == 1) {
            Settings.System.putInt(cr, Settings.System.POWER_MENU_EXPANDED_DESKTOP_ENABLED, 1);
            summary = R.string.expanded_desktop_status_bar;
        } else if (value == 2) {
            Settings.System.putInt(cr, Settings.System.POWER_MENU_EXPANDED_DESKTOP_ENABLED, 1);
            summary = R.string.expanded_desktop_no_status_bar;
        }

        if (mExpandedDesktopPref != null && summary != -1) {
            mExpandedDesktopPref.setSummary(res.getString(summary));
        }
    }
}
