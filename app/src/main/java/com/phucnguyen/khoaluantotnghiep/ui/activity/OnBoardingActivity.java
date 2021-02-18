package com.phucnguyen.khoaluantotnghiep.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.ui.fragment.WelcomeFragment;

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