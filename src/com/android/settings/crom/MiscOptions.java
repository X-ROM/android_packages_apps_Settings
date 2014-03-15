/*
 * Copyright (C) 2014 The Dirty Unicorns Project
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

import android.app.ActivityManager;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SeekBarPreference;
import android.provider.Settings;

import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.hfm.HfmHelpers;

public class MiscOptions extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String DISABLE_FC_NOTIFICATIONS = "disable_fc_notifications";
    private static final String HFM_DISABLE_ADS = "hfm_disable_ads";
    private static final String POWER_MENU_ONTHEGO_ENABLED = "power_menu_onthego_enabled";

    private CheckBoxPreference mDisableFC;
    private CheckBoxPreference mHfmDisableAds;
    private CheckBoxPreference mOnTheGoPowerMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.misc_options);

        final ContentResolver resolver = getActivity().getContentResolver();

        mDisableFC = (CheckBoxPreference) findPreference(DISABLE_FC_NOTIFICATIONS);
        mDisableFC.setChecked((Settings.System.getInt(resolver,
                Settings.System.DISABLE_FC_NOTIFICATIONS, 0) == 1));

        mHfmDisableAds = (CheckBoxPreference) findPreference(HFM_DISABLE_ADS);
        mHfmDisableAds.setChecked((Settings.System.getInt(resolver,
                Settings.System.HFM_DISABLE_ADS, 0) == 1));

        mOnTheGoPowerMenu = (CheckBoxPreference) findPreference(POWER_MENU_ONTHEGO_ENABLED);
        mOnTheGoPowerMenu.setChecked(Settings.System.getInt(resolver,
                Settings.System.POWER_MENU_ONTHEGO_ENABLED, 0) == 1);
        mOnTheGoPowerMenu.setOnPreferenceChangeListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if  (preference == mDisableFC) {
            boolean checked = ((CheckBoxPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.DISABLE_FC_NOTIFICATIONS, checked ? 1:0);
            return true;
        } else if  (preference == mHfmDisableAds) {
            boolean checked = ((CheckBoxPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.HFM_DISABLE_ADS, checked ? 1:0);
            HfmHelpers.checkStatus(getActivity());
            return true;
        } else if (preference == mOnTheGoPowerMenu) {
            boolean checked = ((CheckBoxPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWER_MENU_ONTHEGO_ENABLED, checked ? 1:0);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object value) {
         return true;
    }

    public static class DeviceAdminLockscreenReceiver extends DeviceAdminReceiver {}

}
