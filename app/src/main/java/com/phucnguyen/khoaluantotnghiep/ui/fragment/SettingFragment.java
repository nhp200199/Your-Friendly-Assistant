package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.viewmodel.UserViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingFragment extends Fragment {
    private static final String LOGTAG = SettingFragment.class.getSimpleName();

    private TextView tvLogInStatus;
    private TextView tvUserStatus;
    private TextView tvUserEmail;
    private CircleImageView civLogo;

    private UserViewModel userViewModel;
    private boolean isLoggedIn;
    private String mUserName;
    private String mUserEmail;

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

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        tvLogInStatus = v.findViewById(R.id.tvLoginStatus);
        tvUserStatus = v.findViewById(R.id.tvUserStatus);
        tvUserEmail = v.findViewById(R.id.tvUserEmail);
        civLogo = v.findViewById(R.id.imgUserAvatar);

        civLogo.setEnabled(getActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
                .getString("accessToken", null) != null);

        tvLogInStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoggedIn)
                    Navigation.findNavController(requireActivity(), R.id.setting_navigation_host_fragment)
                            .navigate(R.id.action_setting_fragment_to_login_nav);
                else userViewModel.logoutUser();
            }
        });
        civLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle userInfor = new Bundle();
                userInfor.putString("userName", mUserName);
                userInfor.putString("userEmail", mUserEmail);

                Navigation.findNavController(requireActivity(), R.id.setting_navigation_host_fragment)
                        .navigate(R.id.action_setting_fragment_to_user_information_fragment, userInfor);
            }
        });

        userViewModel.getUserName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String userName) {
                if (userName != null) {
                    mUserName = userName;
                    String helloUserText = "Chào, " + userName;
                    tvUserStatus.setText(helloUserText);
                    tvUserStatus.setTypeface(null, Typeface.BOLD);
                } else {
                    tvUserStatus.setText(getString(R.string.user_status_not_log_in));
                    tvUserStatus.setTypeface(null, Typeface.NORMAL);
                }
            }
        });
        userViewModel.getUserMail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String userEmail) {
                if (userEmail != null) {
                    mUserEmail = userEmail;
                    isLoggedIn = true;
                    tvUserEmail.setVisibility(View.VISIBLE);
                    tvUserEmail.setText(userEmail);

                    //tvUserEmail.VISIBLE only when user has logged in, thus
                    //we can use this to set tvLoginStatus
                    tvLogInStatus.setText(getString(R.string.log_out));
                    tvLogInStatus.setTextColor(getResources().getColor(R.color.red_sad));
                } else {
                    isLoggedIn = false;
                    tvUserEmail.setVisibility(View.GONE);

                    tvLogInStatus.setText(getString(R.string.log_in));
                    tvLogInStatus.setTextColor(getResources().getColor(R.color.blue_500));
                }
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}