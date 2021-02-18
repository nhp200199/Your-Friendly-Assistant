package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.phucnguyen.khoaluantotnghiep.model.OnBoardingScreenItem;
import com.phucnguyen.khoaluantotnghiep.ui.activity.MainActivity;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.adapters.OnboardingScreensPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingFragment extends Fragment {
    public static final String EXTRA_IS_REVIEWING_USAGE = "com.phucnguyen.khoaluantotnghiep.ui.fragment.OnBoardingFragment.IS_REVIEWING";

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

        List<OnBoardingScreenItem> screenItems = new ArrayList<OnBoardingScreenItem>();
        screenItems.add(new OnBoardingScreenItem("1", R.drawable.ic_happy_face_96px));
        screenItems.add(new OnBoardingScreenItem("2", R.drawable.ic_happy_face_96px));
        screenItems.add(new OnBoardingScreenItem("3", R.drawable.ic_happy_face_96px));
        screenItems.add(new OnBoardingScreenItem("4", R.drawable.ic_happy_face_96px));
        screenItems.add(new OnBoardingScreenItem("5", R.drawable.ic_happy_face_96px));
        screenItems.add(new OnBoardingScreenItem("6", R.drawable.ic_happy_face_96px));

        adapter = new OnboardingScreensPagerAdapter(
           screenItems,
           requireContext()
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