package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.phucnguyen.khoaluantotnghiep.R;

public class FavoriteActionDialogFragment extends BottomSheetDialogFragment {
    private static final int DELAY = 50;
    private Handler handler = new Handler();
    private int mWishedPrice;
    private int currentPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_to_favorite_action_dialog, container, false);

        currentPrice = getArguments().getInt("currentPrice");
        if (getArguments().getInt("wishedPrice") != 0) {
            mWishedPrice = getArguments().getInt("wishedPrice");
        } else {
            mWishedPrice = currentPrice;
        }

        Button btnIncrease = v.findViewById(R.id.btnIncrease);
        Button btnDecrease = v.findViewById(R.id.btnDecrease);
        Button btnAddToFavorite = v.findViewById(R.id.btnAddToFavorite);
        TextView tvPrice = v.findViewById(R.id.tvPrice);

        tvPrice.setText(String.valueOf(Utils.formatNumber(mWishedPrice,
                0,
                true,
                '.')));

        btnAddToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FavoriteActionDialogFragment.this)
                        .getPreviousBackStackEntry()
                        .getSavedStateHandle()
                        .set("wishedPrice", mWishedPrice);
                NavHostFragment.findNavController(FavoriteActionDialogFragment.this).popBackStack();
            }
        });
        btnIncrease.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (btnIncrease.isPressed() && mWishedPrice <= 1.2 * currentPrice) {
                            mWishedPrice = (int) (mWishedPrice * 1.1);
                            tvPrice.setText(Utils.formatNumber(mWishedPrice,
                                    0,
                                    true,
                                    '.'));
                            handler.postDelayed(this, DELAY);
                        }
                    }
                }, DELAY);
                return true;
            }
        });
        btnDecrease.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (btnDecrease.isPressed() && mWishedPrice > 1000) {
                            mWishedPrice = (int) (mWishedPrice * 0.9);
                            tvPrice.setText(String.valueOf(Utils.formatNumber(mWishedPrice,
                                    0,
                                    true,
                                    '.')));
                            handler.postDelayed(this, DELAY);
                        }
                    }
                }, DELAY);
                return true;
            }
        });
        return v;
    }
}
