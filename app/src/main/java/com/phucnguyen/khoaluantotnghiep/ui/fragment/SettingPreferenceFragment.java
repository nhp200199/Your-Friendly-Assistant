package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.phucnguyen.khoaluantotnghiep.R;

public class SettingPreferenceFragment extends PreferenceFragmentCompat
        implements ConfirmActionDialog.onConfirmActionListener {
    private SwitchPreference notificationPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        notificationPref = findPreference("notification_pref");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        switch (preference.getKey()) {
            case "instruction_pref":
                NavHostFragment.findNavController(SettingPreferenceFragment.this)
                        .navigate(R.id.action_setting_fragment_to_on_boarding_fragment);
                return true;
            case "notification_pref":
                if (!notificationPref.isChecked())
                    new ConfirmActionDialog(getString(R.string.notification_dialog_text)
                            , getString(R.string.notification_dialog_positive_text)
                            , getString(R.string.notification_dialog_negative_text))
                            .show(getChildFragmentManager(), null);
                return true;
            default:
                return super.onPreferenceTreeClick(preference);
        }
    }

    @Override
    public void onPositiveConfirmed() {
        notificationPref.setChecked(true);
    }

    @Override
    public void onNegativeConfirmed() {
        notificationPref.setChecked(false);
    }
}
