package com.phucnguyen.khoaluantotnghiep;

import android.content.Context;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnBoardingFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class OnBoardingFragment extends Fragment {

    private ViewPager vpOnboardingScreensContainer;
    private TabLayout tabIndicator;

    private OnboardingScreensPagerAdapter adapter;

    private OnboardingScreenChangesListener listener;

    interface OnboardingScreenChangesListener{
        void onPageScrolled(int position);
        void onPageSelected(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_on_boarding, container, false);
        vpOnboardingScreensContainer = v.findViewById(R.id.onboardingScreensContainer);
        tabIndicator = v.findViewById(R.id.tl_swipe);

        adapter = new OnboardingScreensPagerAdapter(
                getActivity().getSupportFragmentManager(),
                0
        );
        vpOnboardingScreensContainer.setAdapter(adapter);
        tabIndicator.setupWithViewPager(vpOnboardingScreensContainer);

        vpOnboardingScreensContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (listener != null)
                    listener.onPageScrolled(position);
            }

            @Override
            public void onPageSelected(int position) {
                if (listener != null)
                    listener.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnboardingScreenChangesListener) context;
    }
}