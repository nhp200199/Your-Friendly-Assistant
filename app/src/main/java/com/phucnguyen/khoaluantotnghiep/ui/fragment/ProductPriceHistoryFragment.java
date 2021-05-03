package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.model.Price;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.viewmodel.ProductItemViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductPriceHistoryFragment extends Fragment {
    private LineChart mChartPriceHistory;
    private ProgressBar pbLoadingBar;

    private ProductItemViewModel mProductItemViewModel;
    private LineDataSet mDataSet;
    private List<Entry> mEntries = new ArrayList<Entry>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductItemViewModel = new ViewModelProvider(requireParentFragment()).get(ProductItemViewModel.class);
    }

    public ProductPriceHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_price_history, container, false);
        mChartPriceHistory = (LineChart) v.findViewById(R.id.chartPriceHistory);
        pbLoadingBar = v.findViewById(R.id.pbLoadingBar);

        setUpChart();

        mProductItemViewModel.getProductPriceHistory().observe(getViewLifecycleOwner(), new Observer<List<Price>>() {
            @Override
            public void onChanged(List<Price> prices) {
                pbLoadingBar.setVisibility(View.GONE);
                if (prices != null) {
                    mChartPriceHistory.setVisibility(View.VISIBLE);
                    populateChartWithPrice(prices);
                }
            }

            private void populateChartWithPrice(List<Price> prices) {
                if (prices.size() == 0) {
                    mChartPriceHistory.setNoDataText("Chưa có dữ liệu cho sản phẩm này");
                    mChartPriceHistory.setNoDataTextColor(getResources().getColor(R.color.gray_300));
                    return;
                }
                if (mDataSet.getEntryCount() == 0) {
                    for (Price data : prices) {
                        // turn your data into Entry objects
                        mDataSet.addEntry(new Entry(data.getDate(), data.getPrice() / 1000, data));
                    }
                }

                mDataSet.setDrawFilled(true);
                mDataSet.setDrawValues(false);

                LineData lineData = new LineData(mDataSet);
                mChartPriceHistory.setData(lineData);
                mChartPriceHistory.invalidate(); // refresh
            }
        });
        mProductItemViewModel.getProductItem().observe(getViewLifecycleOwner(), new Observer<ProductItem>() {
            @Override
            public void onChanged(ProductItem productItem) {
                if (productItem != null) {
                    mDataSet = new LineDataSet(mEntries, "Giá sản phẩm");
                    if (productItem.getPlatform().equals("tiki")) {
                        mDataSet.setFillColor(getResources().getColor(R.color.blue_tiki));
                    } else mDataSet.setFillColor(getResources().getColor(R.color.orange_shopee));
                }
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    private void setUpChart() {
        mChartPriceHistory.setDrawGridBackground(false);
        mChartPriceHistory.getAxisRight().setEnabled(false);
        mChartPriceHistory.getDescription().setEnabled(false);
        mChartPriceHistory.setDragEnabled(true);
        mChartPriceHistory.setScaleEnabled(false);
        YAxis yAxis = mChartPriceHistory.getAxisLeft();
        yAxis.setEnabled(true);
        yAxis.setDrawLabels(false);
        XAxis xAxis = mChartPriceHistory.getXAxis();
        xAxis.setEnabled(false);

        // create marker to display box when values are selected
        MyMarkerView mv = new MyMarkerView(requireActivity(), R.layout.custom_marker_view);

        // Set the marker to the chart
        mv.setChartView(mChartPriceHistory);
        mChartPriceHistory.setMarker(mv);
    }

    private static class MyMarkerView extends MarkerView {
        private TextView tvMarkerPrice;
        private TextView tvMarkerDate;

        /**
         * Constructor. Sets up the MarkerView with a custom layout resource.
         *
         * @param context
         * @param layoutResource the layout resource to use for the MarkerView
         */
        public MyMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvMarkerPrice = findViewById(R.id.tvMarkerPrice);
            tvMarkerDate = findViewById(R.id.tvMarkerDate);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            Price price = (Price) e.getData();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String dateString = simpleDateFormat.format(new Date(price.getDate()));
            String priceString = "<u>đ</u> "
                    + Utils.formatNumber(price.getPrice(), 0, true);

            tvMarkerPrice.setText(Html.fromHtml(priceString));
            tvMarkerDate.setText(dateString);
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }
    }
}