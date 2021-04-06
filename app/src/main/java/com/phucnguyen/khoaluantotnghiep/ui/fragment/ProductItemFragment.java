package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.viewmodel.ProductItemViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductItemFragment extends Fragment {

    private String mProductUrl;

    public interface ProductItemListener {
        void onProductItemReceive(String productName);
    }

    private ProductItemListener mProductItemListener;

    private TabItem mTabItemPriceHistory;
    private ImageView mImgProductItem;
    private TextView mTvProductItemPrice;
    private TextView mTvProductItemRate;
    private TextView mTvProductItemReview;
    private TextView mTvPriceCurrency;
    private CircleImageView mImgActionGoToStore;
    private FloatingActionButton mFabFav;

    private ProductItemViewModel mItemViewModel;

    public ProductItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemViewModel = new ViewModelProvider(this).get(ProductItemViewModel.class);
        mProductUrl = getArguments().getString("productUrl");
        mItemViewModel.makeApiCallToReceiveProductItem(mProductUrl, "item,price,seller,image");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mProductItemListener = (ProductItemListener) context;
        } catch (ClassCastException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_item, container, false);
        mImgProductItem = v.findViewById(R.id.imgProductItem);
        mTvProductItemPrice = v.findViewById(R.id.tvPrice);
        mTvProductItemRate = v.findViewById(R.id.tvProductItemRate);
        mTvProductItemReview = v.findViewById(R.id.tvProductItemReview);
        mTvPriceCurrency = v.findViewById(R.id.tvPriceCurrency);
        mImgActionGoToStore = (CircleImageView) v.findViewById(R.id.imgActionGoToStore);

        mTvPriceCurrency.setPaintFlags(mTvPriceCurrency.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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

        mImgActionGoToStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(mProductUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        mItemViewModel.getProductItem()
                .observe(getViewLifecycleOwner(), new Observer<ProductItem>() {
                    @Override
                    public void onChanged(ProductItem productItem) {
                        if (productItem != null)
                            populateProductItemView(productItem);
                        else
                            mProductItemListener.onProductItemReceive("Không tìm thấy");
                    }

                    private void populateProductItemView(ProductItem productItem) {
                        mTvProductItemPrice.setText(Utils.formatNumber(productItem.getProductPrice(),
                                0,
                                true,
                                '.'));
                        mTvProductItemRate.setText(String.format("%.1f", productItem.getRating()));
                        mTvProductItemReview.setText(String.valueOf(productItem.getTotalReview()));
                        Glide.with(requireActivity())
                                .load(productItem.getThumbnailUrl())
                                .into(mImgProductItem);
                        if (productItem.getPlatform().equals("tiki")) {
                            mImgActionGoToStore.setImageResource(R.drawable.tiki);
                        } else if (productItem.getPlatform().equals("shopee")) {
                            //mFabGoToStore.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_shopee)));
                            mImgActionGoToStore.setImageResource(R.drawable.shopee);
                        }

                        //change the title of toolbar
                        mProductItemListener.onProductItemReceive(productItem.getName());
                    }
                });
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
        ImageView imgTabIcon = tabCustomView.findViewById(R.id.imgTabIcon);

        switch (i) {
            case 0:
                tvTabTitle.setText(getResources().getString(R.string.product_price_history_tab));
                break;
            case 1:
                tvTabTitle.setText(getResources().getString(R.string.product_seller_tab));
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
                    return new ProductPriceHistoryFragment();
                case 1:
                    return new SellerRateFragment();
                case 2:
                    return new ProductImagesFragment();
                case 3:
                    return new ProductRelevantFragment();
            }
            return null;
        }
    }
}