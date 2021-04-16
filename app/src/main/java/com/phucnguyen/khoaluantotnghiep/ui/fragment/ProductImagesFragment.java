package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.adapters.ProductImagesAdapter;
import com.phucnguyen.khoaluantotnghiep.adapters.ProductVideoAdapter;
import com.phucnguyen.khoaluantotnghiep.adapters.ReviewAdapter;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.model.Review;
import com.phucnguyen.khoaluantotnghiep.model.datasource.ReviewDataSource;
import com.phucnguyen.khoaluantotnghiep.viewmodel.ProductItemViewModel;
import com.phucnguyen.khoaluantotnghiep.viewmodel.ReviewViewModel;
import com.phucnguyen.khoaluantotnghiep.viewmodel.ReviewViewModelFactory;

public class ProductImagesFragment extends Fragment implements View.OnClickListener {
    private RecyclerView reviewsContainer;
    private TextView tvFilterFiveStar;
    private TextView tvFilterFourStar;
    private TextView tvFilterThreeStar;
    private TextView tvFilterTwoStar;
    private TextView tvFilterOneStar;
    private TextView tvFilterAllStar;
    private TextView tvFilterMediaReview;
    private TextView currentFilterView;
    private TextView tvLoadingStatus;
    private ProgressBar pbLoadingBar;

    private ReviewAdapter reviewAdapter;
    private ProductItemViewModel mProductItemViewModel;
    private ReviewViewModel mReviewViewModel;
    private String currentFilterString;


    public ProductImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductItemViewModel = new ViewModelProvider(requireParentFragment())
                .get(ProductItemViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_images, container, false);
        reviewsContainer = (RecyclerView) v.findViewById(R.id.reviewsContainer);
        tvFilterOneStar = (TextView) v.findViewById(R.id.tvOneStarRatings);
        tvFilterTwoStar = (TextView) v.findViewById(R.id.tvTwoStarRatings);
        tvFilterThreeStar = (TextView) v.findViewById(R.id.tvThreeStarRatings);
        tvFilterFourStar = (TextView) v.findViewById(R.id.tvFourStarRatings);
        tvFilterFiveStar = (TextView) v.findViewById(R.id.tvFiveStarRatings);
        tvFilterAllStar = (TextView) v.findViewById(R.id.tvAllRatings);
        tvFilterMediaReview = (TextView) v.findViewById(R.id.tvRatingsWithMedia);
        tvLoadingStatus = (TextView) v.findViewById(R.id.tvLoadingResult);
        pbLoadingBar = v.findViewById(R.id.pbLoadingBar);

        tvFilterAllStar.setOnClickListener(this);
        tvFilterMediaReview.setOnClickListener(this);
        tvFilterFiveStar.setOnClickListener(this);
        tvFilterFourStar.setOnClickListener(this);
        tvFilterThreeStar.setOnClickListener(this);
        tvFilterTwoStar.setOnClickListener(this);
        tvFilterOneStar.setOnClickListener(this);

        //prevent the filter selection goes away when replacing fragment
        if (currentFilterString != null) {
            currentFilterView = (TextView) v.findViewWithTag(currentFilterString);
            currentFilterView.setSelected(true);
            currentFilterView.setTextColor(getResources().getColor(R.color.white));
        }

        reviewsContainer.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false));

        //List<Review> reviews = populateReviewsWithDummyData();
        reviewAdapter = new ReviewAdapter(requireContext(), new ProductImagesAdapter.ImageListener() {
            @Override
            public void onImageClicked(String imageUrl) {
                Bundle bundle = new Bundle();
                bundle.putString("url", imageUrl);
                NavHostFragment.findNavController(ProductImagesFragment.this)
                        .navigate(R.id.action_product_item_fragment_to_mediaPlayerFragment, bundle);
            }
        }, new ProductVideoAdapter.VideoListener() {
            @Override
            public void onVideoClicked(Review.Video video) {
                Bundle bundle = new Bundle();
                bundle.putString("url", video.getUrl());
                bundle.putLong("duration", video.getDuration());
                NavHostFragment.findNavController(ProductImagesFragment.this)
                        .navigate(R.id.action_product_item_fragment_to_mediaPlayerFragment, bundle);
            }
        });
        reviewAdapter.setListener(new ReviewAdapter.Listener() {
            @Override
            public void onViewRecycle(int position, int value) {
                mReviewViewModel.setNestedRecyclerViewPositions(position, value);
            }
        });
        reviewsContainer.setAdapter(reviewAdapter);

        mProductItemViewModel.getProductItem().observe(getViewLifecycleOwner(), new Observer<ProductItem>() {
            @Override
            public void onChanged(ProductItem productItem) {
                if (productItem != null) {
                    //enable click on filters when product item is ready
                    tvFilterAllStar.setEnabled(true);
                    tvFilterMediaReview.setEnabled(true);
                    tvFilterFiveStar.setEnabled(true);
                    tvFilterFourStar.setEnabled(true);
                    tvFilterThreeStar.setEnabled(true);
                    tvFilterTwoStar.setEnabled(true);
                    tvFilterOneStar.setEnabled(true);
                    ReviewViewModelFactory factory = new ReviewViewModelFactory(
                            productItem.getId(),
                            productItem.getPlatform(),
                            productItem.getSellerId()
                    );
                    mReviewViewModel = new ViewModelProvider(requireParentFragment(), factory).get(ReviewViewModel.class);
                    //set the nested recycler view states which are retrieved from the ViewModel
                    reviewAdapter.setNestedRecyclerViewPositions(mReviewViewModel.getNestedRecyclerViewPositions());
                    mReviewViewModel.getPagedList().observe(getViewLifecycleOwner(), new Observer<PagedList<Review>>() {
                        @Override
                        public void onChanged(PagedList<Review> reviews) {
                            reviewAdapter.submitList(reviews);
                        }
                    });
                    mReviewViewModel.getLoadingState().observe(getViewLifecycleOwner(), new Observer<ReviewDataSource.LoadingState>() {
                        @Override
                        public void onChanged(ReviewDataSource.LoadingState loadingState) {
                            if (loadingState == ReviewDataSource.LoadingState.FIRST_LOADING) {
                                pbLoadingBar.setVisibility(View.VISIBLE);
                                reviewsContainer.setVisibility(View.INVISIBLE);
                                tvLoadingStatus.setVisibility(View.INVISIBLE);
                            } else if (loadingState == ReviewDataSource.LoadingState.SUCCESS) {
                                reviewsContainer.setVisibility(View.VISIBLE);
                                pbLoadingBar.setVisibility(View.INVISIBLE);
                                tvLoadingStatus.setVisibility(View.INVISIBLE);
                            } else if (loadingState == ReviewDataSource.LoadingState.SUCCESS_WITH_NO_VALUES) {
                                tvLoadingStatus.setText("Không có bình luận nào");
                                tvLoadingStatus.setVisibility(View.VISIBLE);
                                pbLoadingBar.setVisibility(View.INVISIBLE);
                                reviewsContainer.setVisibility(View.INVISIBLE);
                            } else if (loadingState == ReviewDataSource.LoadingState.ERROR) {
                                tvLoadingStatus.setText("Đã có lỗi xảy ra");
                                tvLoadingStatus.setVisibility(View.VISIBLE);
                                pbLoadingBar.setVisibility(View.INVISIBLE);
                                reviewsContainer.setVisibility(View.INVISIBLE);
                            }
                            reviewAdapter.setState(loadingState);
                        }
                    });
                    mReviewViewModel.getFilterMutableLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String filter) {
                            if (currentFilterString == null) {
                                currentFilterView = (TextView) v.findViewWithTag(filter);
                                currentFilterView.setSelected(true);
                                currentFilterView.setTextColor(getResources().getColor(R.color.white));
                            }

                            currentFilterString = filter;
                        }
                    });
                }
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onClick(View view) {
        //un-select the previous filter
        currentFilterView.setSelected(false);
        currentFilterView.setTextColor(getResources().getColor(R.color.black));
        String selectedFilter = "has_media";
        switch (view.getId()) {
            case R.id.tvAllRatings:
                selectedFilter = "all";
                break;
            case R.id.tvRatingsWithMedia:
                selectedFilter = "has_media";
                break;
            case R.id.tvOneStarRatings:
                selectedFilter = "star:1";
                break;
            case R.id.tvTwoStarRatings:
                selectedFilter = "star:2";
                break;
            case R.id.tvThreeStarRatings:
                selectedFilter = "star:3";
                break;
            case R.id.tvFourStarRatings:
                selectedFilter = "star:4";
                break;
            case R.id.tvFiveStarRatings:
                selectedFilter = "star:5";
                break;
        }
        currentFilterView = (TextView) view;
        currentFilterView.setSelected(true);
        currentFilterView.setTextColor(getResources().getColor(R.color.white));
        //only update the filter if different one has been chosen
        if (!selectedFilter.equals(currentFilterString))
            mReviewViewModel.setFilterMutableLiveData(selectedFilter);
    }
}