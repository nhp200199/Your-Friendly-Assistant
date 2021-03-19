package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.phucnguyen.khoaluantotnghiep.R;

public class ProductItemFragment extends Fragment {

    private TabItem mTabItemPriceHistory;

    public ProductItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_item, container, false);
        //Attach the SectionsPagerAdapter to the ViewPager
        SectionsPagerAdapter pagerAdapter =
                new SectionsPagerAdapter(getChildFragmentManager());
        ViewPager pager = (ViewPager) v.findViewById(R.id.productItemPager);
        pager.setAdapter(pagerAdapter);

        //Attach the ViewPager to the TabLayout
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.productItemTabs);
        tabLayout.setupWithViewPager(pager);

        //Setup custom view for the tab items
        setTabItems(tabLayout);

        // Inflate the layout for this fragment
        return v;
    }

    private void setTabItems(TabLayout tabs) {
        // Iterate over all tabs
        for (int i = 0; i < tabs.getTabCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);

            //Inflate custom view
            initilizeTabView(tab, i);
        }
    }

    private void initilizeTabView(TabLayout.Tab tab, int i) {
        View tabCustomView = LayoutInflater.from(requireActivity())
                .inflate(R.layout.product_tablayout_item, null);
        TextView tvTabTitle = tabCustomView.findViewById(R.id.tvTabTitle);
        TextView tvTabDetail = tabCustomView.findViewById(R.id.tvTabDetail);
        ImageView imgTabIcon = tabCustomView.findViewById(R.id.imgTabIcon);

        switch (i) {
            case 0:
                tvTabTitle.setText(getResources().getString(R.string.product_seller_tab));
                break;
            case 1:
                tvTabTitle.setText(getResources().getString(R.string.product_price_history_tab));
                break;
            case 2:
                tvTabTitle.setText(getResources().getString(R.string.product_images_tab));
                break;
            case 3:
                tvTabTitle.setText(getResources().getString(R.string.product_relevant_tab));
                break;
        }
        tab.setCustomView(tabCustomView);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SellerRateFragment();
                case 1:
                    return new ProductPriceHistoryFragment();
                case 2:
                    return new ProductImagesFragment();
                case 3:
                    return new ProductRelevantFragment();
            }
            return null;
        }
    }
}