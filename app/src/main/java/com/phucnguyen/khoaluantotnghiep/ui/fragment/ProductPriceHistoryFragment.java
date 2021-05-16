package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.model.Price;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.viewmodel.ProductItemViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductPriceHistoryFragment extends Fragment {
    private LineChart mChartPriceHistory;
    private ProgressBar pbLoadingBar;
    private TextView tvPriceChangesCounter;
    private TextView tvPriceMax;
    private TextView tvPriceMin;
    private TextView tvDailyPricesHistoryChange;
    private ListView lvDailyPricesHistoryChange;
    private LinearLayout priceHistoryContainer;
    private NestedScrollView nestedScrollView;

    private ProductItemViewModel mProductItemViewModel;
    private List<Entry> mEntries = new ArrayList<Entry>();
    private LineDataSet mDataSet = new LineDataSet(mEntries, "Giá sản phẩm");

    private Set<Integer> uniquePriceList = new HashSet<Integer>();
    private List<Price.DailyPrice> mDailyPriceList = new ArrayList<Price.DailyPrice>();
    private DailyPricesAdapter mDailyPricesAdapter;

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

        connectViews(v);

        setUpChart();

        mProductItemViewModel.getProductPriceHistory().observe(getViewLifecycleOwner(), new Observer<List<Price>>() {
            @Override
            public void onChanged(List<Price> prices) {
                pbLoadingBar.setVisibility(View.GONE);
                if (prices != null) {
                    priceHistoryContainer.setVisibility(View.VISIBLE);
                    populateChartWithPrice(prices);
                }
            }

            private void populateChartWithPrice(List<Price> prices) {
                if (prices.size() == 0) {
                    mChartPriceHistory.setNoDataText("Chưa có dữ liệu cho sản phẩm này");
                    mChartPriceHistory.setNoDataTextColor(getResources().getColor(R.color.gray_300));
                    return;
                }
                //only add entries when there is none
                if (mDataSet.getEntryCount() == 0) {
                    for (Price data : prices) {
                        // turn your data into Entry objects
                        mDataSet.addEntry(new Entry(data.getDate(), data.getPrice(), data));
                    }
                }
                //find min, max price and how many time the price has been changed
                for (Price priceItem :
                        prices) {
                    uniquePriceList.add(priceItem.getPrice());
                    if (priceItem.getPriceChangeInDay() != null) {
                        for (Price.DailyPrice dailyPrice :
                                priceItem.getPriceChangeInDay()) {
                            uniquePriceList.add(dailyPrice.getPrice());
                        }
                    }
                }
                int minPrice = Collections.min(uniquePriceList);
                int maxPrice = Collections.max(uniquePriceList);
                int numOfPriceChanges = calculateNumOfPriceChanges(prices);

                String minPriceString = "Giá thấp nhất: "
                        + Utils.formatNumber(minPrice,
                        0,
                        true,
                        '.');
                String maxPriceString = "Giá cao nhất: " +
                        Utils.formatNumber(maxPrice,
                                0,
                                true,
                                '.');

                SpannableString minSpannable = new SpannableString(minPriceString);
                minSpannable.setSpan(
                        new ForegroundColorSpan(getResources().getColor(R.color.green_happy)),
                        minPriceString.indexOf(':') + 1,
                        minPriceString.length(),
                        Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                SpannableString maxSpannable = new SpannableString(maxPriceString);
                maxSpannable.setSpan(
                        new ForegroundColorSpan(getResources().getColor(R.color.red_sad)),
                        maxPriceString.indexOf(':') + 1,
                        maxPriceString.length(),
                        Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

                tvPriceChangesCounter.setText(Html.fromHtml(
                        getString(R.string.price_changes_counter, numOfPriceChanges, uniquePriceList.size()),
                        Html.FROM_HTML_MODE_LEGACY));
                tvPriceMin.setText(minSpannable);
                tvPriceMax.setText(maxSpannable);

                mDataSet.setDrawFilled(true);
                mDataSet.setDrawValues(false);

                LineData lineData = new LineData(mDataSet);
                mChartPriceHistory.setData(lineData);
                mChartPriceHistory.moveViewToX(mChartPriceHistory.getXChartMax());
                mChartPriceHistory.highlightValue(mChartPriceHistory.getXChartMax(), 0);

                LimitLine upperLimitLine = new LimitLine(mChartPriceHistory.getYMax());
                upperLimitLine.setLineColor(getResources().getColor(R.color.red_sad));
                upperLimitLine.setLineWidth(2f);

                LimitLine lowerLimitLine = new LimitLine(mChartPriceHistory.getYMin());
                lowerLimitLine.setLineColor(getResources().getColor(R.color.green_happy));
                lowerLimitLine.setLineWidth(2f);

                mChartPriceHistory.getAxisLeft().addLimitLine(upperLimitLine);
                mChartPriceHistory.getAxisLeft().addLimitLine(lowerLimitLine);
                mChartPriceHistory.getAxisLeft().setAxisMaximum(maxPrice * 1.1f);
                mChartPriceHistory.getAxisLeft().setAxisMinimum(minPrice * 0.9f);
                mChartPriceHistory.invalidate(); // refresh
            }

            private int calculateNumOfPriceChanges(List<Price> prices) {
                int numOfPriceChanges = 1;
                if (prices.get(0).getPriceChangeInDay() != null)
                    numOfPriceChanges += prices.get(0).getPriceChangeInDay().size() - 1;
                for (int i = 1; i < prices.size(); i++) {
                    if (prices.get(i).getPrice() != prices.get(i - 1).getPrice()) {
                        numOfPriceChanges++;
                        if (prices.get(i).getPriceChangeInDay() != null)
                            numOfPriceChanges += prices.get(i).getPriceChangeInDay().size() - 1;
                    }
                }
                return numOfPriceChanges;
            }
        });
        mProductItemViewModel.getProductItem().observe(getViewLifecycleOwner(), new Observer<ProductItem>() {
            @Override
            public void onChanged(ProductItem productItem) {
                if (productItem != null) {
                    if (productItem.getPlatform().equals("tiki")) {
                        mDataSet.setFillColor(getResources().getColor(R.color.blue_tiki));
                    } else mDataSet.setFillColor(getResources().getColor(R.color.orange_shopee));
                }
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    private void connectViews(View v) {
        mChartPriceHistory = (LineChart) v.findViewById(R.id.chartPriceHistory);
        pbLoadingBar = v.findViewById(R.id.pbLoadingBar);
        tvPriceChangesCounter = v.findViewById(R.id.tvPriceChangesCounter);
        tvPriceMin = v.findViewById(R.id.tvPriceMin);
        tvPriceMax = v.findViewById(R.id.tvPriceMax);
        tvDailyPricesHistoryChange = v.findViewById(R.id.tvDailyPricesHistory);
        priceHistoryContainer = v.findViewById(R.id.priceHistoryContainer);
        lvDailyPricesHistoryChange = (ListView) v.findViewById(R.id.lvDailyPricesHistory);
        nestedScrollView = (NestedScrollView) v.findViewById(R.id.nestedScrollView);

        mDailyPricesAdapter = new DailyPricesAdapter(requireContext(), mDailyPriceList);
        lvDailyPricesHistoryChange.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //disable the scroll behavior of the nested scroll view so that the list view can scroll
                //If we instead set the behavior nestedScrollingEnable(true) in the xml, the list view can
                //scroll down but cannot scroll up (due to interference of nested scroll view)
                nestedScrollView.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        nestedScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;
                }
                return false;
            }
        });
        lvDailyPricesHistoryChange.setAdapter(mDailyPricesAdapter);
    }

    private void setUpChart() {
        mChartPriceHistory.setDrawGridBackground(false);
        mChartPriceHistory.getAxisRight().setEnabled(false);
        mChartPriceHistory.getAxisLeft().setEnabled(false);
        mChartPriceHistory.getDescription().setEnabled(false);
        mChartPriceHistory.setDragEnabled(true);
        mChartPriceHistory.setScaleXEnabled(true);
        mChartPriceHistory.setScaleYEnabled(false);
        YAxis yAxis = mChartPriceHistory.getAxisLeft();
        yAxis.setEnabled(true);
        yAxis.setDrawLimitLinesBehindData(true);
        yAxis.setDrawGridLines(false);
        XAxis xAxis = mChartPriceHistory.getXAxis();
        xAxis.setEnabled(false);
        mChartPriceHistory.setPinchZoom(true);
        mChartPriceHistory.getViewPortHandler().setMinMaxScaleX(2f, 3f);

        mChartPriceHistory.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Price price = (Price) e.getData();
                if (price.getPriceChangeInDay() != null
                        && price.getPriceChangeInDay().size() > 1) {
                    tvDailyPricesHistoryChange.setVisibility(View.VISIBLE);
                    lvDailyPricesHistoryChange.setVisibility(View.VISIBLE);

                    mDailyPricesAdapter.clear();
                    for (int i = price.getPriceChangeInDay().size() - 1; i >= 0; i--) {
                        mDailyPricesAdapter.add(price.getPriceChangeInDay().get(i));
                    }
                    mDailyPricesAdapter.notifyDataSetChanged();
                } else {
                    tvDailyPricesHistoryChange.setVisibility(View.GONE);
                    lvDailyPricesHistoryChange.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // create marker to display box when values are selected
        MyMarkerView mv = new MyMarkerView(requireActivity(), R.layout.custom_marker_view);

        // Set the marker to the chart
        mv.setChartView(mChartPriceHistory);
        mChartPriceHistory.setMarker(mv);
    }

    private class MyMarkerView extends MarkerView {
        private TextView tvMarkerPrice;
        private TextView tvMarkerDate;
        private TextView tvMarkerDailyPrice;

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
            tvMarkerDailyPrice = findViewById(R.id.tvMarkerDailyPrice);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            Price price = (Price) e.getData();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String dateString = simpleDateFormat.format(new Date(price.getDate()));
            String priceString = "<u>đ</u> "
                    + Utils.formatNumber(price.getPrice(), 0, true);

            //set how many times the product item'price changes in a day (if any)
            if (price.getPriceChangeInDay() != null && price.getPriceChangeInDay().size() > 1) {
                tvMarkerDailyPrice.setVisibility(VISIBLE);
                int numberOfDailyChanges = price.getPriceChangeInDay().size() - 1;
                String priceChangeInDayString = "Có thêm " + numberOfDailyChanges + " lần thay đổi giá\n" +
                        "trong ngày";
                tvMarkerDailyPrice.setText(priceChangeInDayString);
            } else tvMarkerDailyPrice.setVisibility(GONE);

            tvMarkerPrice.setText(Html.fromHtml(priceString));
            tvMarkerDate.setText(dateString);
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }
    }

    private class DailyPricesAdapter extends ArrayAdapter<Price.DailyPrice> {

        public DailyPricesAdapter(@NonNull Context context, List<Price.DailyPrice> priceList) {
            super(context, 0, priceList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Price.DailyPrice price = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.price_item, parent, false);
            }
            // Lookup view for data population
            TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            TextView tvPriceDate = (TextView) convertView.findViewById(R.id.tvPriceDate);
            TextView tvIsDiscountByFlashSale = (TextView) convertView.findViewById(R.id.tvIsDiscountByFlashSale);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            String dateString = simpleDateFormat.format(new Date(price.getDate()));
            // Populate the data into the template view using the data object
            tvPrice.setText(Utils.formatNumber(price.getPrice(),
                    0,
                    true,
                    '.'));
            tvPriceDate.setText(dateString);

            if (price.isFlashSale())
                tvIsDiscountByFlashSale.setVisibility(View.VISIBLE);
            else tvIsDiscountByFlashSale.setVisibility(View.GONE);
            // Return the completed view to render on screen
            return convertView;
        }
    }
}