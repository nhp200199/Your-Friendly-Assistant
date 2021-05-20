package com.phucnguyen.khoaluantotnghiep.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.ui.fragment.ProductItemFragment;

public class MainActivity extends AppCompatActivity implements ProductItemFragment.ProductItemListener {
    private AppBarConfiguration appBarConfiguration;
    private NavHostFragment curNavHostFragment;
    private NavController curNavController;
    private BottomNavigationView bottomNav;
    private Toolbar toolbar;
    private TextView mToolbarTitle;
    private FrameLayout frameHome, frameFind, frameHot, frameSetting;
    private NavHostFragment homeNavHostFragment;
    private NavHostFragment findNavHostFragment;
    private NavHostFragment hotNavHostFragment;
    private NavHostFragment settingNavHostFragment;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction().equals(Intent.ACTION_SEND)||
                intent.hasExtra("productUrl"))
            moveToProductItem(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("app_refs", MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(OnBoardingActivity.HAS_SEEN_ONBOARDING_REF, false)) {
            startActivity(new Intent(this, OnBoardingActivity.class));
            finish();
        }

        homeNavHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.home_navigation_host_fragment);
        findNavHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.find_navigation_host_fragment);
        hotNavHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.hot_navigation_host_fragment);
        settingNavHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.setting_navigation_host_fragment);
        frameHome = findViewById(R.id.frame_home);
        frameFind = findViewById(R.id.frame_find);
        frameHot = findViewById(R.id.frame_hot);
        frameSetting = findViewById(R.id.frame_setting);
        bottomNav = findViewById(R.id.bttm_nav);
        toolbar = findViewById(R.id.toolbar);
        mToolbarTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);

        if (savedInstanceState == null) {
            //setup botnav and nav controller at the beginning
            //if saveInstanceState != null then need to operate this thing on onRestoreInstanceState
            curNavHostFragment = homeNavHostFragment;
            curNavController = curNavHostFragment.getNavController();
            setUpBotNavigationAssociatedWithNavController();
            setUpToolbar();
            //move to product item screen only when there is a product link coming
            if (Intent.ACTION_SEND.equals(getIntent().getAction())||
                    getIntent().hasExtra("productUrl"))
                moveToProductItem(getIntent());
            handleChangesToDestination();
        }
    }

    private void handleChangesToDestination() {
        curNavController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.on_boarding_fragment ||
                        destination.getId() == R.id.product_item_fragment ||
                        destination.getId() == R.id.media_player_fragment ||
                        destination.getId() == R.id.log_in_fragment ||
                        destination.getId() == R.id.reset_password_fragment ||
                        destination.getId() == R.id.register_fragment ||
                        destination.getId() == R.id.redirect_confirmation_dialog ||
                        destination.getId() == R.id.add_to_favorite_fragment)
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

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //if this activity is restored from previous state,
        //we will have the ItemId of botnav the has been selected
        //so that we can set up nav controller accordingly
        switch (bottomNav.getSelectedItemId()) {
            case R.id.home_fragment:
                curNavHostFragment = homeNavHostFragment;
                frameHome.setVisibility(View.VISIBLE);
                frameFind.setVisibility(View.GONE);
                frameHot.setVisibility(View.GONE);
                frameSetting.setVisibility(View.GONE);
                break;
            case R.id.find_products_fragment:
                curNavHostFragment = findNavHostFragment;
                frameFind.setVisibility(View.VISIBLE);
                frameHome.setVisibility(View.GONE);
                frameHot.setVisibility(View.GONE);
                frameSetting.setVisibility(View.GONE);
                break;
            case R.id.hot_products_fragment:
                curNavHostFragment = hotNavHostFragment;
                frameHot.setVisibility(View.VISIBLE);
                frameHome.setVisibility(View.GONE);
                frameFind.setVisibility(View.GONE);
                frameSetting.setVisibility(View.GONE);
                break;
            case R.id.setting_fragment:
                curNavHostFragment = settingNavHostFragment;
                frameSetting.setVisibility(View.VISIBLE);
                frameHome.setVisibility(View.GONE);
                frameFind.setVisibility(View.GONE);
                frameHot.setVisibility(View.GONE);
                break;
        }
        curNavController = curNavHostFragment.getNavController();
        setUpBotNavigationAssociatedWithNavController();
        setUpToolbar();
        handleChangesToDestination();
    }

    private void moveToProductItem(Intent intent) {
        String productUrlString = null;
        Bundle bundle = new Bundle();
        if (intent.getAction() != null && intent.getClipData() != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                productUrlString = intent.getStringExtra(Intent.EXTRA_TEXT);
            }
        } else{
            productUrlString = intent.getStringExtra("productUrl");
        }

        bundle.putString("productUrl", productUrlString);
        curNavController.navigate(R.id.action_global_productItemFragment, bundle);
    }

    private void setUpToolbar() {
        appBarConfiguration = new AppBarConfiguration.Builder(curNavController.getGraph()).build();
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, curNavController, appBarConfiguration);

//        NavigationUI.setupWithNavController(toolbar, curNavController);
    }

    private void setUpBotNavigationAssociatedWithNavController() {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_fragment:
                    curNavHostFragment = homeNavHostFragment;
                    frameHome.setVisibility(View.VISIBLE);
                    frameFind.setVisibility(View.GONE);
                    frameHot.setVisibility(View.GONE);
                    frameSetting.setVisibility(View.GONE);
                    break;
                case R.id.find_products_fragment:
                    curNavHostFragment = findNavHostFragment;
                    frameFind.setVisibility(View.VISIBLE);
                    frameHome.setVisibility(View.GONE);
                    frameHot.setVisibility(View.GONE);
                    frameSetting.setVisibility(View.GONE);
                    break;
                case R.id.hot_products_fragment:
                    curNavHostFragment = hotNavHostFragment;
                    frameHot.setVisibility(View.VISIBLE);
                    frameHome.setVisibility(View.GONE);
                    frameFind.setVisibility(View.GONE);
                    frameSetting.setVisibility(View.GONE);
                    break;
                case R.id.setting_fragment:
                    curNavHostFragment = settingNavHostFragment;
                    frameSetting.setVisibility(View.VISIBLE);
                    frameHome.setVisibility(View.GONE);
                    frameFind.setVisibility(View.GONE);
                    frameHot.setVisibility(View.GONE);
                    break;
            }
            curNavController = curNavHostFragment.getNavController();
            //this is for setting the toolbar and destination changes listener when the back stack is change
            setUpToolbar();
            handleChangesToDestination();
            return true;
        });
    }

    @Override
    public void onProductItemReceive(String productName) {
        mToolbarTitle.setText(productName);
    }

    @Override
    public void onBackPressed() {
        //handle backpress. If there is nothing to navigateUp, destroy the activity
        if (!onSupportNavigateUp())
            super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = curNavController;
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}