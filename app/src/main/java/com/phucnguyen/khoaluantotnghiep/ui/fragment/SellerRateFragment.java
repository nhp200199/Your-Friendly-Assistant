package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.model.Seller;
import com.phucnguyen.khoaluantotnghiep.utils.NumbersFormatter;
import com.phucnguyen.khoaluantotnghiep.viewmodel.ProductItemViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SellerRateFragment extends Fragment {
    private static final  float SHOP_RATING_POINT = 6f;
    private static final  float SHOP_IS_OFFICIAL_POINT = 2f;
    private static final  float SHOP_FOLLOWERS_POINT = 1f;
    private static final  float SHOP_CREATED_DATE_POINT = 1f;
    private static final  float SHOP_IS_CERTIFICATED_POINT = 0.5f;
    private static final  float SHOP_RESPONSE_RATE_POINT = 0.25f;
    private static final  float SHOP_TOTAL_AMOUNT_POINT = 0.25f;
    private static final long ONE_YEAR = (long) 3600 * 24 * 365 * 1000;
    private static final long TWO_YEARS = ONE_YEAR * 2;
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
    private ImageView iconSellerRate;

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
        iconSellerRate = v.findViewById(R.id.iconSellerRate);

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
                /*Point rate to evaluate seller*/
//                Shop's rating: 4
//                Is seller official?: 3
//                Seller has a lot of follwers?: 2
//                This's seller has been for a long time: 1
                /*Bonus points for meeting following conditions*/
//                Shop has certification: 0.25 point
//                Shop has more than 10 items which are selling: 0.25
//                Shop's response rate is more than 80%: 0.5
                float totalPoint = 0f;
                float rating = seller.getRating();
                int followersNum = seller.getFollower();
                boolean official = seller.isOfficialShop();
                boolean verified = seller.isVerrified();
                long existingTime = seller.getCreated();
                float response = seller.getResponseRate();
                int amounts = seller.getTotalItem();

                BulletSpan greenBulletSpan = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    greenBulletSpan = new BulletSpan(10,
                            getResources().getColor(R.color.green_happy),
                            18);
                } else {
                    greenBulletSpan = new BulletSpan(10,
                            getResources().getColor(R.color.green_happy));
                }
                BulletSpan redBulletSpan = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    redBulletSpan = new BulletSpan(10,
                            getResources().getColor(R.color.red_sad),
                            18);
                } else {
                    redBulletSpan = new BulletSpan(10,
                            getResources().getColor(R.color.red_sad));
                }
                BulletSpan orangeBulletSpan = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    orangeBulletSpan = new BulletSpan(10,
                            getResources().getColor(R.color.orange_medium),
                            18);
                } else {
                    orangeBulletSpan = new BulletSpan(10,
                            getResources().getColor(R.color.orange_medium));
                }

                //rating
                String shopRating = "Điểm đánh giá shop: "
                        + NumbersFormatter.formatFloatToString(rating, 1)
                        + "/5";
                SpannableString shopRatingString = new SpannableString(shopRating);
                if (rating >= 4){
                    totalPoint += SHOP_RATING_POINT;
                    shopRatingString.setSpan(greenBulletSpan, 0, shopRating.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (rating >= 2){
                    totalPoint += 0.5 * SHOP_RATING_POINT;
                    shopRatingString.setSpan(orangeBulletSpan, 0, shopRating.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (rating >= 1){
                    totalPoint += 0.25 * SHOP_RESPONSE_RATE_POINT;
                    shopRatingString.setSpan(redBulletSpan, 0, shopRating.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    shopRatingString.setSpan(redBulletSpan, 0, shopRating.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                //followers
                String followers = "Số người theo dõi: " + seller.getFollower();
                SpannableString follwersString = new SpannableString(followers);
                if (followersNum >= 100){
                    totalPoint += SHOP_FOLLOWERS_POINT;
                    follwersString.setSpan(greenBulletSpan, 0, followers.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else if (followersNum >= 50){
                    totalPoint += 0.5 * SHOP_FOLLOWERS_POINT;
                    follwersString.setSpan(orangeBulletSpan, 0, followers.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (followersNum >= 10){
                    totalPoint += 0.25 * SHOP_FOLLOWERS_POINT;
                    follwersString.setSpan(redBulletSpan, 0, followers.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    follwersString.setSpan(redBulletSpan, 0, followers.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                //official
                SpannableString officialString;
                if (official) {
                    totalPoint += SHOP_IS_OFFICIAL_POINT;
                    officialString = new SpannableString("Shop chính hãng");
                    officialString.setSpan(greenBulletSpan, 0, officialString.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    officialString = new SpannableString("Shop không chính hãng");
                    officialString.setSpan(redBulletSpan, 0, officialString.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                //existingTime
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String dateFormattedString = simpleDateFormat.format(
                        new Date(seller.getCreated()));
                String joinDate = "Ngày tạo: " + dateFormattedString;
                SpannableString joinDateString = new SpannableString(joinDate);
                long now = Calendar.getInstance().getTimeInMillis();
                if (existingTime <= now - TWO_YEARS){
                    totalPoint += SHOP_CREATED_DATE_POINT;
                    joinDateString.setSpan(greenBulletSpan, 0, joinDate.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (existingTime <= now - ONE_YEAR){
                    totalPoint += 0.5 * SHOP_CREATED_DATE_POINT;
                    joinDateString.setSpan(orangeBulletSpan, 0, joinDate.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    totalPoint += 0.25 * SHOP_CREATED_DATE_POINT;
                    joinDateString.setSpan(redBulletSpan, 0, joinDate.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                tvSellerName.setText(seller.getName());
                tvSellerFollwers.setText(follwersString);
                tvSellerOfficialShop.setText(officialString);
                tvSellerDateCreated.setText(joinDateString);
                tvSellerRating.setText(shopRatingString);

                if (seller.getPlatform().equals("shopee")) {
                    //totalItems
                    String totalItems = "Số sản phẩm kinh doanh: " + seller.getTotalItem();
                    SpannableString totalItemsString = new SpannableString(totalItems);
                    if (amounts > 500){
                        totalPoint += SHOP_TOTAL_AMOUNT_POINT;
                        totalItemsString.setSpan(greenBulletSpan, 0, totalItems.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (amounts > 250){
                        totalPoint += 0.5 * SHOP_TOTAL_AMOUNT_POINT;
                        totalItemsString.setSpan(orangeBulletSpan, 0, totalItems.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        totalPoint += 0.25 * SHOP_TOTAL_AMOUNT_POINT;
                        totalItemsString.setSpan(redBulletSpan, 0, totalItems.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    //responseRate
                    String responseRate = "Tỉ lệ phản hồi: " + seller.getResponseRate() + "%";
                    SpannableString responseRateString = new SpannableString(responseRate);
                    if (response >= 75){
                        totalPoint += SHOP_RESPONSE_RATE_POINT;
                        responseRateString.setSpan(greenBulletSpan, 0, responseRate.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (response >= 50){
                        totalPoint += 0.5 * SHOP_RESPONSE_RATE_POINT;
                        responseRateString.setSpan(orangeBulletSpan, 0, responseRate.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (response >= 0.25){
                        totalPoint += 0.25 * SHOP_RESPONSE_RATE_POINT;
                        responseRateString.setSpan(redBulletSpan, 0, responseRate.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        responseRateString.setSpan(redBulletSpan, 0, responseRate.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    SpannableString verifiedString;
                    if (verified) {
                        totalPoint += SHOP_IS_CERTIFICATED_POINT;
                        verifiedString = new SpannableString("Đã được chứng thực");
                        verifiedString.setSpan(greenBulletSpan, 0, verifiedString.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        verifiedString = new SpannableString("Chưa được chứng thực");
                        verifiedString.setSpan(redBulletSpan, 0, verifiedString.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    tvSellerCertification.setVisibility(View.VISIBLE);
                    tvSellerCertification.setText(verifiedString);
                    tvSellerResponseRate.setText(responseRateString);
                    tvTotalItems.setText(totalItemsString);
                }

                if (totalPoint >= 8){
                    iconSellerRate.setImageResource(R.drawable.ic_happy_face_96px);
                } else if (totalPoint >= 5){
                    iconSellerRate.setImageResource(R.drawable.ic_medium_happy_96px);
                } else {
                    iconSellerRate.setImageResource(R.drawable.ic_sad_face_96px);
                }
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}
