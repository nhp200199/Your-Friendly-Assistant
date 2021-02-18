package com.phucnguyen.khoaluantotnghiep.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.phucnguyen.khoaluantotnghiep.R;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private NavHostFragment navHostFragment;
    private NavController navController;
    private BottomNavigationView bottomNav;
    private Toolbar toolbar;
    private TextView mToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.home_fragment,
                R.id.find_products_fragment,
                R.id.hot_products_fragment,
                R.id.setting_fragment
        ).build();

        mToolbarTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);

        setUpBotNavigation();
        setUpToolbar();

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.on_boarding_fragment ||
                        destination.getId() == R.id.language_setting_fragment)
                    bottomNav.setVisibility(View.GONE);
                else bottomNav.setVisibility(View.VISIBLE);
                if (destination.getId() == R.id.on_boarding_fragment)
                    toolbar.setVisibility(View.GONE);
                else toolbar.setVisibility(View.VISIBLE);

                //change the toolbar title according to the fragment
                mToolbarTitle.setText(toolbar.getTitle());
                getSupportActionBar().setTitle("");
            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    private void setUpBotNavigation() {
        bottomNav = findViewById(R.id.bttm_nav);
        NavigationUI.setupWithNavController(bottomNav,
                navHostFragment.getNavController());
    }
}