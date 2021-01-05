package com.phucnguyen.khoaluantotnghiep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private NavHostFragment navHostFragment;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.home_fragment,
                R.id.find_products_fragment,
                R.id.hot_products_fragment,
                R.id.setting_fragment
        ).build();

        setUpBotNavigation();
        setUpActionBar();
    }

    private void setUpActionBar() {
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    private void setUpBotNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bttm_nav);
        NavigationUI.setupWithNavController(bottomNav,
                navHostFragment.getNavController());
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}