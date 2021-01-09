package com.phucnguyen.khoaluantotnghiep;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

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
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_setting_fragment_to_on_boarding_fragment);
                return true;
            case "notification_pref":
                if (!notificationPref.isChecked())
                    new ConfirmActionDialog("Đã tắt thông báo. Bạn sẽ không nhận được thông tin về giá của các sản phẩm. Bạn có muốn bật lại không?"
                            , "Nhận thông báo"
                            , "Vẫn tiếp tục")
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
