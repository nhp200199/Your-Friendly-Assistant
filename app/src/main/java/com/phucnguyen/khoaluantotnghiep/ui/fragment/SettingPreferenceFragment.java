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
    private Preference languagePref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        notificationPref = findPreference("notification_pref");
        languagePref = findPreference("language_pref");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        switch (preference.getKey()) {
            case "language_pref":
//                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
//                        .navigate(R.id.action_setting_fragment_to_language_setting_fragment);
                return true;
            case "instruction_pref":
//                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
//                        .navigate(R.id.action_setting_fragment_to_on_boarding_fragment);
                return true;
            case "notification_pref":
                if (!notificationPref.isChecked())
                    new ConfirmActionDialog(getString(R.string.notification_dialog_text)
                            , getString(R.string.notification_dialog_positive_text)
                            , getString(R.string.notification_dialog_negative_text))
                            .show(getChildFragmentManager(), null);
                return true;
            case "comment_pref":
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_global_productItemFragment);
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

    @Override
    public void onResume() {
        super.onResume();
        languagePref.setSummary(getPreferenceManager().getSharedPreferences()
                .getString("language_pref", ""));
    }
}
