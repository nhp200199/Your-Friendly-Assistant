package com.phucnguyen.khoaluantotnghiep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private NavHostFragment navHostFragment;
    private NavController navController;
    private BottomNavigationView bottomNav;

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

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.on_boarding_fragment ||
                        destination.getId() == R.id.language_setting_fragment)
                    bottomNav.setVisibility(View.GONE);
                else bottomNav.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setUpActionBar() {
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    private void setUpBotNavigation() {
        bottomNav = findViewById(R.id.bttm_nav);
        NavigationUI.setupWithNavController(bottomNav,
                navHostFragment.getNavController());
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}