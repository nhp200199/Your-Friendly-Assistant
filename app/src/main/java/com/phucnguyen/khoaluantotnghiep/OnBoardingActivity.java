package com.phucnguyen.khoaluantotnghiep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.phucnguyen.khoaluantotnghiep.adapters.OnboardingScreensPagerAdapter;

public class OnBoardingActivity extends AppCompatActivity
        implements OnBoardingFragment.OnboardingScreenChangesListener {

    private TextView tvIgnore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        tvIgnore = findViewById(R.id.tvIgnore);

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.onboardingContainer, new WelcomeFragment())
                    .commit();
        }
    }

    @Override
    public void onPageScrolled(int position) {
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
}