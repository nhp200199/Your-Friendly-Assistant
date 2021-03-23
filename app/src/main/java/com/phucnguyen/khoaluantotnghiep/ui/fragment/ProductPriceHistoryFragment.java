package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.phucnguyen.khoaluantotnghiep.model.ProductItemResponse;
import com.phucnguyen.khoaluantotnghiep.model.Seller;
import com.phucnguyen.khoaluantotnghiep.viewmodel.ProductItemViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductPriceHistoryFragment extends Fragment {
    private LineChart mChartPriceHistory;

    private ProductItemViewModel mProductItemViewModel;

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

        setUpChart();

        mProductItemViewModel.getProductPriceHistory().observe(getViewLifecycleOwner(), new Observer<List<Price>>() {
            @Override
            public void onChanged(List<Price> prices) {
                populateChartWithPrice(prices);
            }

            private void populateChartWithPrice(List<Price> prices) {
                List<Entry> entries = new ArrayList<Entry>();
                for (Price data : prices) {
                    // turn your data into Entry objects
                    entries.add(new Entry(data.getDate(), data.getPrice(), data));
                }
                LineDataSet dataSet = new LineDataSet(entries, "Giá sản phẩm");
                dataSet.setDrawFilled(true);
                dataSet.setDrawValues(false);

                LineData lineData = new LineData(dataSet);
                mChartPriceHistory.setData(lineData);
                mChartPriceHistory.invalidate(); // refresh
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
        mChartPriceHistory.setNoDataText("Chưa có dữ liệu");
        //mChartPriceHistory.setVisibleXRange(0f, 3f);
        YAxis yAxis = mChartPriceHistory.getAxisLeft();
        yAxis.setEnabled(true);
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