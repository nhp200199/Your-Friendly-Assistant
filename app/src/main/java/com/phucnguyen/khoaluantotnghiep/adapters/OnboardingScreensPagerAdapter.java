package com.phucnguyen.khoaluantotnghiep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.model.OnBoardingScreenItem;

import java.util.List;

public class OnboardingScreensPagerAdapter extends PagerAdapter {
    public static final int NUMBER_OF_ONBOARDING_SCREENS = 6;
    private List<OnBoardingScreenItem> mOnBoardingScreenItems;
    private Context mContext;

    public OnboardingScreensPagerAdapter(List<OnBoardingScreenItem> onBoardingScreenItems, Context context) {
        mContext = context;
        mOnBoardingScreenItems = onBoardingScreenItems;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        View v = inflater.inflate(R.layout.onboarding_screen, null);

        ImageView imgDes = v.findViewById(R.id.imgBanner);
        TextView tvDes = v.findViewById(R.id.tvDes);

        tvDes.setText(mOnBoardingScreenItems.get(position).getDescription());
        imgDes.setImageResource(mOnBoardingScreenItems.get(position).getImageRsc());

        container.addView(v);

        return v;
    }

    @Override
    public int getCount() {
        return mOnBoardingScreenItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
