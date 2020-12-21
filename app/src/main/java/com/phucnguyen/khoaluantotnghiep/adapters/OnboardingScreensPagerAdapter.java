package com.phucnguyen.khoaluantotnghiep.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.phucnguyen.khoaluantotnghiep.ScreenFour;
import com.phucnguyen.khoaluantotnghiep.ScreenFive;
import com.phucnguyen.khoaluantotnghiep.ScreenOne;
import com.phucnguyen.khoaluantotnghiep.ScreenSix;
import com.phucnguyen.khoaluantotnghiep.ScreenThree;
import com.phucnguyen.khoaluantotnghiep.ScreenTwo;

public class OnboardingScreensPagerAdapter extends FragmentPagerAdapter {
    public static final int NUMBER_OF_ONBOARDING_SCREENS = 6;
    public OnboardingScreensPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ScreenOne();
            case 1:
                return new ScreenTwo();
            case 2:
                return new ScreenThree();
            case 3:
                return new ScreenFour();
            case 4:
                return new ScreenFive();
            case 5:
                return new ScreenSix();
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUMBER_OF_ONBOARDING_SCREENS;
    }
}
