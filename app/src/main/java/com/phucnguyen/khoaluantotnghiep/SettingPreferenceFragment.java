package com.phucnguyen.khoaluantotnghiep;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

public class SettingPreferenceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        switch (preference.getKey()) {
            case "instruction":
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_setting_fragment_to_on_boarding_fragment);
                return true;

            default: return super.onPreferenceTreeClick(preference);
        }

    }
}
