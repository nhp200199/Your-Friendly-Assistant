package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.phucnguyen.khoaluantotnghiep.R;

public class SellerRateFragment extends Fragment {

    public SellerRateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_rate, container, false);
    }
}
