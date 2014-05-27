package com.android.settings.crom;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.Bundle;
import android.provider.Settings;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.internal.util.slim.DeviceUtils;

import com.android.settings.crom.ButtonSettings;
import com.android.settings.crom.InterfaceSettings;
import com.android.settings.crom.LockscreenSettings;
import com.android.settings.crom.SystemSettings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.util.ArrayList;
import java.util.List;

public class CtoolSettings extends SettingsPreferenceFragment implements ActionBar.TabListener {

    ViewPager mViewPager;
    String titleString[];
    ViewGroup mContainer;

    static Bundle mSavedState;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContainer = container;
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setIcon(R.drawable.ic_settings_system);
   
        View view = inflater.inflate(R.layout.ctool_settings, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        StatusBarAdapter StatusBarAdapter = new StatusBarAdapter(getFragmentManager());
        mViewPager.setAdapter(StatusBarAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        ActionBar.Tab interfaceTab = actionBar.newTab();
        interfaceTab.setText("Interface");
        interfaceTab.setTabListener(this);

        ActionBar.Tab buttonTab = actionBar.newTab();
        buttonTab.setText("Button");
        buttonTab.setTabListener(this);

        ActionBar.Tab lockscreenTab = actionBar.newTab();
        lockscreenTab.setText("LockScreen");
        lockscreenTab.setTabListener(this);

        ActionBar.Tab systemTab = actionBar.newTab();
        systemTab.setText("System");
        systemTab.setTabListener(this);

        actionBar.addTab(interfaceTab);
        actionBar.addTab(buttonTab);
        actionBar.addTab(lockscreenTab);
        actionBar.addTab(systemTab);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        return view;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // After confirming PreferenceScreen is available, we call super.
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle saveState) {
        super.onSaveInstanceState(saveState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!DeviceUtils.isTablet(getActivity())) {
            mContainer.setPadding(0, 0, 0, 0);
        }
    }

    class StatusBarAdapter extends FragmentPagerAdapter {
        String titles[] = getTitles();
        private Fragment frags[] = new Fragment[titles.length];

        public StatusBarAdapter(FragmentManager fm) {
            super(fm);
            frags[0] = new InterfaceSettings();
            frags[1] = new ButtonSettings();
            frags[2] = new LockscreenSettings();
            frags[3] = new SystemSettings();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }
    }

    private String[] getTitles() {
        String titleString[];
        titleString = new String[]{
                    getString(R.string.interface_category),
                    getString(R.string.button_category),
                    getString(R.string.lockscreen_category),
                    getString(R.string.system_category)};
        return titleString;
    }
}
