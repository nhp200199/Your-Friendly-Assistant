package com.phucnguyen.khoaluantotnghiep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.phucnguyen.khoaluantotnghiep.adapters.OnboardingScreensPagerAdapter;

public class OnBoardingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.onboardingContainer, new WelcomeFragment())
                    .commit();
        }
    }
}