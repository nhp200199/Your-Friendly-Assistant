package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phucnguyen.khoaluantotnghiep.R;

public class HotProductsFragment extends Fragment {
    private static final String LOGTAG = HotProductsFragment.class.getSimpleName();

    public HotProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hot_products, container, false);
    }
}