package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phucnguyen.khoaluantotnghiep.R;

public class SettingFragment extends Fragment {
    private static final String LOGTAG = SettingFragment.class.getSimpleName();

    private TextView tvLogInStatus;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.settingPreferenceContainer, new SettingPreferenceFragment())
                    .commit();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        tvLogInStatus = v.findViewById(R.id.tvLoginStatus);
        tvLogInStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
//                .navigate(R.id.action_setting_fragment_to_logInFragment);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}