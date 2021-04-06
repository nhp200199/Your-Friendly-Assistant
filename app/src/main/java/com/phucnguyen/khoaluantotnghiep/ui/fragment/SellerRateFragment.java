package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.model.Seller;
import com.phucnguyen.khoaluantotnghiep.viewmodel.ProductItemViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SellerRateFragment extends Fragment {
    private ProductItemViewModel mProductItemViewModel;

    private TextView tvSellerName;
    private TextView tvSellerCertification;
    private TextView tvSellerOfficialShop;
    private TextView tvSellerRating;
    private TextView tvSellerDateCreated;
    private TextView tvSellerFollwers;
    private TextView tvSellerResponseRate;
    private TextView tvTotalItems;
    private TextView tvSellerResonseStatus;
    private ProgressBar pbLoadingBar;
    private LinearLayout sellerContainer;

    public SellerRateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductItemViewModel = new ViewModelProvider(requireParentFragment())
                .get(ProductItemViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_seller_rate, container, false);
        tvSellerName = v.findViewById(R.id.tvSellerName);
        tvSellerCertification = v.findViewById(R.id.tvSellerCertification);
        tvSellerOfficialShop = v.findViewById(R.id.tvSellerOfficialShop);
        tvSellerRating = v.findViewById(R.id.tvSellerRating);
        tvSellerFollwers = v.findViewById(R.id.tvSellerFollowers);
        tvSellerDateCreated = v.findViewById(R.id.tvSellerDateCreated);
        tvSellerResponseRate = v.findViewById(R.id.tvSellerResponseRate);
        tvTotalItems = v.findViewById(R.id.tvSellerTotalItems);
        tvSellerResonseStatus = v.findViewById(R.id.tvSellerResponseStatus);
        pbLoadingBar = v.findViewById(R.id.pbLoadingBar);
        sellerContainer = v.findViewById(R.id.sellerContainer);

        mProductItemViewModel.getSeller().observe(getViewLifecycleOwner(), new Observer<Seller>() {
            @Override
            public void onChanged(Seller seller) {
                if (seller != null) {
                    pbLoadingBar.setVisibility(View.GONE);
                    sellerContainer.setVisibility(View.VISIBLE);
                    populateSellerInfoView(seller);
                }
                else{
                    pbLoadingBar.setVisibility(View.GONE);
                    tvSellerResonseStatus.setVisibility(View.VISIBLE);
                    tvSellerResonseStatus.setText("Không tìm thấy nhà bán");
                }
            }

            //long long code to show information of seller
            private void populateSellerInfoView(Seller seller) {
                BulletSpan bulletSpan = new BulletSpan(10);

                String followers = "Số người theo dõi: " + seller.getFollower();
                SpannableString follwersString = new SpannableString(followers);
                follwersString.setSpan(bulletSpan, 0, followers.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                SpannableString officialString;
                if (seller.isOfficialShop()) {
                    officialString = new SpannableString("Shop chính hãng");
                } else {
                    officialString = new SpannableString("Shop không chính hãng");
                }
                officialString.setSpan(bulletSpan, 0, officialString.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String dateFormattedString = simpleDateFormat.format(
                        new Date(seller.getCreated()));
                String joinDate = "Ngày tạo: " + dateFormattedString;
                SpannableString joinDateString = new SpannableString(joinDate);
                joinDateString.setSpan(bulletSpan, 0, joinDate.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                String shopRating = "Điểm đánh giá shop: "
                        + String.format("%.1f", seller.getRating())
                        + "/5";
                SpannableString shopRatingString = new SpannableString(shopRating);
                shopRatingString.setSpan(bulletSpan, 0, shopRating.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                tvSellerName.setText(seller.getName());
                tvSellerFollwers.setText(follwersString);
                tvSellerOfficialShop.setText(officialString);
                tvSellerDateCreated.setText(joinDateString);
                tvSellerRating.setText(shopRatingString);

                if (seller.getPlatform().equals("shopee")) {
                    String totalItems = "Số sản phẩm kinh doanh: " + seller.getTotalItem();
                    SpannableString totalItemsString = new SpannableString(totalItems);
                    totalItemsString.setSpan(bulletSpan, 0, totalItems.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    String responseRate = "Tỉ lệ phản hồi: " + seller.getResponseRate() + "%";
                    SpannableString responseRateString = new SpannableString(responseRate);
                    responseRateString.setSpan(bulletSpan, 0, responseRate.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    SpannableString verifiedString;
                    if (seller.isVerrified()) {
                        verifiedString = new SpannableString("Đã được chứng thực");
                    } else {
                        verifiedString = new SpannableString("Chưa được chứng thực");
                    }
                    verifiedString.setSpan(bulletSpan, 0, verifiedString.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    tvSellerCertification.setText(verifiedString);
                    tvSellerResponseRate.setText(responseRateString);
                    tvTotalItems.setText(totalItemsString);
                }
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}
