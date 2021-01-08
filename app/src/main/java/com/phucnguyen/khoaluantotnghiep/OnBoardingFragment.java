package com.phucnguyen.khoaluantotnghiep;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.phucnguyen.khoaluantotnghiep.adapters.OnboardingScreensPagerAdapter;

public class OnBoardingFragment extends Fragment {
    public static final String EXTRA_IS_REVIEWING_USAGE = "com.phucnguyen.khoaluantotnghiep.OnBoardingFragment.IS_REVIEWING";

    private ViewPager vpOnboardingScreensContainer;
    private TabLayout tabIndicator;
    private TextView tvIgnore;

    private OnboardingScreensPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_on_boarding, container, false);
        vpOnboardingScreensContainer = v.findViewById(R.id.onboardingScreensContainer);
        tabIndicator = v.findViewById(R.id.tl_swipe);
        tvIgnore = v.findViewById(R.id.tvIgnore);

        tvIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getArguments() != null && getArguments().getBoolean(EXTRA_IS_REVIEWING_USAGE)) {
                    requireActivity().onBackPressed();
                } else startActivity(new Intent(requireActivity(), MainActivity.class));
            }
        });

        adapter = new OnboardingScreensPagerAdapter(
                getActivity().getSupportFragmentManager(),
                0
        );
        vpOnboardingScreensContainer.setAdapter(adapter);
        tabIndicator.setupWithViewPager(vpOnboardingScreensContainer);

        vpOnboardingScreensContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == OnboardingScreensPagerAdapter.NUMBER_OF_ONBOARDING_SCREENS - 1)
                    tvIgnore.setText("Đã hiểu");
                else tvIgnore.setText("Bỏ qua");
            }

            @Override
            public void onPageSelected(int position) {
                if (position == OnboardingScreensPagerAdapter.NUMBER_OF_ONBOARDING_SCREENS - 1)
                    tvIgnore.setText("Đã hiểu");
                else tvIgnore.setText("Bỏ qua");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}