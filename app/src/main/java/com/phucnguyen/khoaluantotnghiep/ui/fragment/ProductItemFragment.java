package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.utils.NumbersFormatter;
import com.phucnguyen.khoaluantotnghiep.viewmodel.ProductItemViewModel;
import com.phucnguyen.khoaluantotnghiep.viewmodel.UserViewModel;

import static com.phucnguyen.khoaluantotnghiep.utils.Contants.*;

public class ProductItemFragment extends Fragment {
    public interface ProductItemListener {
        void onProductItemReceive(String productName);
    }

    private ProductItemListener mProductItemListener;
    private String mProductUrl;
    private ProductItem mProductItem;
    private String accessToken;
    private boolean isTracked;
    private String productIdBasedOnProductUrl;
    private String productPlatformBasedOnProductUrl;
    private int mWishedPrice;

    private TabItem mTabItemPriceHistory;
    private ImageView mImgProductItem;
    private TextView mTvProductItemPrice;
    private TextView mTvProductItemRate;
    private TextView mTvProductItemReview;
    private TextView mTvPriceCurrency;
    private TextView tvUpdateWishedPriceAction;
    private Button btnAddToFavorites;
    private CardView productActionContainer;
    private Button btnGoToStore;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ProductItemViewModel mItemViewModel;
    private UserViewModel mUserViewModel;

    public ProductItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemViewModel = new ViewModelProvider(this).get(ProductItemViewModel.class);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        mProductUrl = getArguments().getString("productUrl");
        accessToken = mUserViewModel.getSharedPreferences().getString("accessToken", null);

        mItemViewModel.makeApiCallToReceiveProductItem(mProductUrl, "item,price,seller,image",
                accessToken);
    }

    private void fillProductIdAndProductPlatformFromUrl() {
        if (mProductUrl.contains("tiki.vn")) {
            productPlatformBasedOnProductUrl = "tiki";

            // find the product id from the url
            int endSubStringIndex = mProductUrl.lastIndexOf('.');
            String subString = mProductUrl.substring(0, endSubStringIndex);
            int startProductStringIndex = subString.lastIndexOf('-') + 2;
            productIdBasedOnProductUrl = subString.substring(startProductStringIndex);
        } else if (mProductUrl.contains("shopee.vn")) {
            // find the product id and category's id from the url
            // Note: shopee can have 2 url
            // eg:
            //
            //1.https://shopee.vn/product/283338743/9918567180?smtt=0.174867900-1616510545.9
            // // Shop id first, then item id
            // 2.
            //
            //https://shopee.vn/bach-tuoc-cam-xuc-2-mat-cam-xuc-do-choi-bach-tuoc-co-the-dao-nguoc-tam-trang-bach-tuoc-sang-trong-i.283338743.9918567180
            // //id shop first, then id item
            productPlatformBasedOnProductUrl = "shopee";

            if (mProductUrl.contains("?")) {
                int endSubStringIndex = mProductUrl.lastIndexOf('?');
                String subString = mProductUrl.substring(0, endSubStringIndex);
                String[] splittedStrings = subString.split("/");
                productIdBasedOnProductUrl = splittedStrings[splittedStrings.length - 1];
            } else {
                String[] splittedStrings = mProductUrl.split("\\.");
                productIdBasedOnProductUrl = splittedStrings[splittedStrings.length - 1];
            }
        }
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
        fillProductIdAndProductPlatformFromUrl();

        View v = inflater.inflate(R.layout.fragment_product_item, container, false);

        connectViews(v);

        NavController navController = NavHostFragment.findNavController(this);
        // After a configuration change or process death, the currentBackStackEntry
        // points to the dialog destination, so you must use getBackStackEntry()
        // with the specific ID of your destination to ensure we always
        // get the right NavBackStackEntry
        final NavBackStackEntry navBackStackEntry = navController.getBackStackEntry(R.id.product_item_fragment);

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        final LifecycleEventObserver observer = new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event.equals(Lifecycle.Event.ON_RESUME)) {
                    if (mProductItem != null) {
                        //because mProductItemListener.onProductItemReceive(mProductItem.getName()) method
                        //call is too early for the toolbar to update its content, provide an handle
                        //to delay this call
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mProductItemListener.onProductItemReceive(mProductItem.getName());
                            }
                        }, 100);
                    }
                    if (navBackStackEntry.getSavedStateHandle().contains("wishedPrice")) {
                        mWishedPrice = navBackStackEntry.getSavedStateHandle().get("wishedPrice");
                        mProductItem.setDesiredPrice(mWishedPrice);
                        mUserViewModel.trackProduct(mProductItem, mWishedPrice);
                        navBackStackEntry.getSavedStateHandle().remove("wishedPrice");
                    }
                }
            }
        };
        navBackStackEntry.getLifecycle().addObserver(observer);

        // As addObserver() does not automatically remove the observer, we
        // call removeObserver() manually when the view lifecycle is destroyed
        getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event.equals(Lifecycle.Event.ON_DESTROY)) {
                    navBackStackEntry.getLifecycle().removeObserver(observer);
                }
            }
        });

        mUserViewModel.getUserActionStateMLiveData().observe(getViewLifecycleOwner(), new Observer<UserActionState>() {
            @Override
            public void onChanged(UserActionState userActionStates) {
                switch (userActionStates) {
                    case PROCESSING:
                        btnAddToFavorites.setEnabled(false);
                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show();
                        break;
                    case DONE:
                        btnAddToFavorites.setEnabled(true);
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
                        break;
                    case NETWORK_ERROR:
                        btnAddToFavorites.setEnabled(true);
                        Toast.makeText(requireContext(), "Network Error", Toast.LENGTH_SHORT).show();
                        break;
                    case NOT_AUTHORIZED:
                        mUserViewModel.createNewAccessTokenFromRefreshToken();
                        Toast.makeText(requireContext(), "Not Authorized", Toast.LENGTH_SHORT).show();
                        break;
                    case REGAINED_ACCESS_TOKEN:
                        accessToken = mUserViewModel.getSharedPreferences().getString("accessToken", null);

                        if (!isTracked && mProductItem != null)
                            mUserViewModel.trackProduct(mProductItem, mWishedPrice);
                        else mUserViewModel.deleteTrackedProduct(mProductItem.getId(),
                                mProductItem.getPlatform(),
                                accessToken);
                        Toast.makeText(requireContext(), "New Access Token Received", Toast.LENGTH_SHORT).show();
                        break;
                    case EXPIRED_TOKEN:
                        btnAddToFavorites.setEnabled(true);

                        Toast.makeText(requireContext(), "Expired Token", Toast.LENGTH_SHORT).show();
                        Bundle inforBundle = new Bundle();
                        inforBundle.putString("title", "Thông báo");
                        inforBundle.putString("message", "Phiên đăng nhập của bạn đã hết hạn");
                        inforBundle.putString("posMessage", "đăng nhập");
                        inforBundle.putString("negMessage", "để sau");
                        inforBundle.putInt("actionId", R.id.action_product_item_fragment_to_login_nav);
                        RedirectionConfirmationDialogFragment dialogFragment = new RedirectionConfirmationDialogFragment();
                        dialogFragment.setArguments(inforBundle);
                        dialogFragment.setCancelable(false);
                        dialogFragment.show(getChildFragmentManager(), null);
//                        NavHostFragment.findNavController(ProductItemFragment.this)
//                                .navigate(R.id.action_product_item_fragment_to_redirect_confirmation_dialog, inforBundle);
                        break;
                }
            }
        });

        //check if user already follow this product (the product exist in the database)
        mItemViewModel.getProductItemWithIdAndPlatform(productIdBasedOnProductUrl, productPlatformBasedOnProductUrl)
                .observe(getViewLifecycleOwner(), new Observer<ProductItem>() {
                    @Override
                    public void onChanged(ProductItem cachedProductItem) {
                        if (cachedProductItem != null) {
                            btnAddToFavorites.setBackground(ResourcesCompat.getDrawable(getResources(),
                                    R.drawable.ic_favorite_violet, null));
                            isTracked = true;

                            String updateWishedPriceAction = "Thông báo khi giá thấp hơn "
                                    + Utils.formatNumber(cachedProductItem.getDesiredPrice(),
                                    0,
                                    true,
                                    '.')
                                    + ". Nhấn để thay đổi";
                            SpannableString spannableString = new SpannableString(updateWishedPriceAction);
                            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_500)),
                                    updateWishedPriceAction.lastIndexOf('.'),
                                    updateWishedPriceAction.length(),
                                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                            tvUpdateWishedPriceAction.setText(spannableString);
                            tvUpdateWishedPriceAction.setVisibility(View.VISIBLE);
                            if (!tvUpdateWishedPriceAction.hasOnClickListeners())
                                tvUpdateWishedPriceAction.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (mUserViewModel.getSharedPreferences().getString("accessToken", null) != null) {
                                            Bundle inforBundle = new Bundle();
                                            inforBundle.putInt("wishedPrice", cachedProductItem.getDesiredPrice());
                                            inforBundle.putInt("currentPrice", cachedProductItem.getProductPrice());
                                            NavHostFragment.findNavController(ProductItemFragment.this)
                                                    .navigate(R.id.action_product_item_fragment_to_add_to_favorite_fragment, inforBundle);
                                        } else {
                                            Bundle inforBundle = new Bundle();
                                            inforBundle.putString("title", "Thông báo");
                                            inforBundle.putString("message", "Để sử dụng chức năng này, bạn cần đăng nhập");
                                            inforBundle.putString("posMessage", "đăng nhập");
                                            inforBundle.putString("negMessage", "để sau");
                                            inforBundle.putInt("actionId", R.id.action_product_item_fragment_to_login_nav);
                                            RedirectionConfirmationDialogFragment dialogFragment = new RedirectionConfirmationDialogFragment();
                                            dialogFragment.setArguments(inforBundle);
                                            dialogFragment.setCancelable(false);
                                            dialogFragment.show(getChildFragmentManager(), null);
                                        }
                                    }
                                });
                        } else {
                            btnAddToFavorites.setBackground(ResourcesCompat.getDrawable(getResources(),
                                    R.drawable.ic_favorite_border_violet, null));
                            tvUpdateWishedPriceAction.setVisibility(View.GONE);
                            isTracked = false;
                        }
                    }
                });
        mItemViewModel.getProductItem()
                .observe(getViewLifecycleOwner(), new Observer<ProductItem>() {
                    @Override
                    public void onChanged(ProductItem productItem) {
                        if (productItem != null) {
                            mProductItem = productItem;
                            populateProductItemView(productItem);

                            isTracked = productItem.isTracked();
                        } else
                            mProductItemListener.onProductItemReceive("Không tìm thấy");
                    }
                });
        mItemViewModel.getLoadingState().observe(getViewLifecycleOwner(), new Observer<ItemLoadingState>() {
            @Override
            public void onChanged(ItemLoadingState itemLoadingState) {
                switch (itemLoadingState) {
                    case SUCCESS:
                        swipeRefreshLayout.setEnabled(false);
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        break;
                    case FIRST_LOAD_ERROR:
                        swipeRefreshLayout.setEnabled(true);
                        if (swipeRefreshLayout.isRefreshing())
                            swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    private void populateProductItemView(ProductItem productItem) {
        mTvProductItemPrice.setText(Utils.formatNumber(productItem.getProductPrice(),
                0,
                true,
                '.'));
        mTvProductItemRate.setText(NumbersFormatter.formatFloatToString(productItem.getRating(), 1));
        mTvProductItemReview.setText(String.valueOf(productItem.getTotalReview()));
        Glide.with(requireActivity())
                .load(productItem.getThumbnailUrl())
                .placeholder(requireActivity().getDrawable(R.drawable.logo_fade))
                .error(requireActivity().getDrawable(R.drawable.logo_fade))
                .into(mImgProductItem);
        productActionContainer.setVisibility(View.VISIBLE);
        if (productItem.getPlatform().equals("tiki")) {
            btnGoToStore.setText("đi đến tiki");
            btnGoToStore.setBackgroundColor(getResources().getColor(R.color.blue_tiki));
        } else if (productItem.getPlatform().equals("shopee")) {
            btnGoToStore.setText("đi đến shopee");
            btnGoToStore.setBackgroundColor(getResources().getColor(R.color.orange_shopee));
        }
        if (productItem.isTracked()) {
            isTracked = true;
            btnAddToFavorites.setBackground(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_favorite_violet, null));
        }

        //change the title of toolbar
        mProductItemListener.onProductItemReceive(productItem.getName());
    }

    private void connectViews(View v) {
        mImgProductItem = v.findViewById(R.id.imgProductItem);
        mTvProductItemPrice = v.findViewById(R.id.tvPrice);
        mTvProductItemRate = v.findViewById(R.id.tvProductItemRate);
        mTvProductItemReview = v.findViewById(R.id.tvProductItemReview);
        mTvPriceCurrency = v.findViewById(R.id.tvPriceCurrency);
        tvUpdateWishedPriceAction = v.findViewById(R.id.tvUpdateWishedPriceAction);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refreshLayout);
        btnGoToStore = v.findViewById(R.id.btnGoToStore);
        btnAddToFavorites = (Button) v.findViewById(R.id.btnAddToFavorite);
        productActionContainer = v.findViewById(R.id.productActionContainer);

        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mItemViewModel.retryLoadingProductItem();
            }
        });
        mTvPriceCurrency.setPaintFlags(mTvPriceCurrency.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //Attach the SectionsPagerAdapter to the ViewPager
        SectionsPagerAdapter pagerAdapter =
                new SectionsPagerAdapter(getChildFragmentManager());
        ViewPager pager = (ViewPager) v.findViewById(R.id.productItemPager);
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(3);

        //Attach the ViewPager to the TabLayout
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.productItemTabs);
        tabLayout.setupWithViewPager(pager);

        //Setup custom view for the tab items
        setTabItems(tabLayout);

        btnGoToStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(mProductUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btnAddToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUserViewModel.getSharedPreferences().getString("accessToken", null) != null) {
                    if (!isTracked) {
                        Bundle inforBundle = new Bundle();
                        inforBundle.putInt("currentPrice", mProductItem.getProductPrice());
                        NavHostFragment.findNavController(ProductItemFragment.this)
                                .navigate(R.id.action_product_item_fragment_to_add_to_favorite_fragment, inforBundle);
                    } else {
                        mUserViewModel.deleteTrackedProduct(mProductItem.getId(),
                                mProductItem.getPlatform(),
                                mUserViewModel.getSharedPreferences().getString("accessToken", null));
                    }
                } else {
                    Bundle inforBundle = new Bundle();
                    inforBundle.putString("title", "Thông báo");
                    inforBundle.putString("message", "Để sử dụng chức năng này, bạn cần đăng nhập");
                    inforBundle.putString("posMessage", "đăng nhập");
                    inforBundle.putString("negMessage", "để sau");
                    inforBundle.putInt("actionId", R.id.action_product_item_fragment_to_login_nav);
                    RedirectionConfirmationDialogFragment dialogFragment = new RedirectionConfirmationDialogFragment();
                    dialogFragment.setArguments(inforBundle);
                    dialogFragment.setCancelable(false);
                    dialogFragment.show(getChildFragmentManager(), null);
//                    NavHostFragment.findNavController(ProductItemFragment.this)
//                            .navigate(R.id.action_product_item_fragment_to_redirect_confirmation_dialog, inforBundle);
                }
            }
        });
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
                imgTabIcon.setImageResource(R.drawable.chart);
                break;
            case 1:
                tvTabTitle.setText(getResources().getString(R.string.product_seller_tab));
                imgTabIcon.setImageResource(R.drawable.home);
                break;
            case 2:
                tvTabTitle.setText(getResources().getString(R.string.product_images_tab));
                imgTabIcon.setImageResource(R.drawable.comments);
                break;
            case 3:
                tvTabTitle.setText(getResources().getString(R.string.product_relevant_tab));
                imgTabIcon.setImageResource(R.drawable.radar);
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