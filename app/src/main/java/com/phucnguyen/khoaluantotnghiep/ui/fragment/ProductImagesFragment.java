package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        tvLoadingStatus = (TextView) v.findViewById(R.id.tvLoadingStatus);
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
        reviewsContainer.setAdapter(reviewAdapter);

        mProductItemViewModel.getProductItem().observe(getViewLifecycleOwner(), new Observer<ProductItem>() {
            @Override
            public void onChanged(ProductItem productItem) {
                if (productItem != null) {
                    //enable click on filters
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

//    private List<Review> populateReviewsWithDummyData() {
//        List<Review> reviews = new ArrayList<Review>();
//        reviews.add(new Review(
//            "1",
//                "Mới nhận, chưa dùng nên chưa biết chất lượng ra sao. Tuy nhiên nhìn bắt mắt.\n Điểm trừ là : - camera trông thô và lồi quá so với độ dày của máy.\n                        - không kèm ốp lưng và miếng dán màn hình.\n Nhưng Dẫu sao cũng chấp nhận được trong tầm giá.",
//                4,
//                1615466619,
//                new String[]{},
//                new ArrayList<Review.Video>(
//                        Arrays.asList(
//                                new Review.Video(
//                                        "https://1500000774.vod2.myqcloud.com/412c22d9vodhk1500000774/3a42680c5285890813358255091/5285890813358255092.jpg",
//                                        5100,
//                                        "https://1500000774.vod2.myqcloud.com/18161081vodtranshk1500000774/3a42680c5285890813358255091/v.f132082.mp4"
//                                )
//                        )
//                ),
//                new User("Trần Hổ", "Trần Hổ")
//        ));
//        reviews.add(new Review(
//                "1",
//                "nhìn tổng quan bên ngoài thì đúng như những gì mình đặt hàng, chưa khui và sử dụng nên chưa biết thế nào, giao hành sớm hơn dự kiến 2 ngày, giao hàng lịch sự, có hẹn trước.",
//                5,
//                1615010847,
//                new String[]{
//                        "https://vcdn.tikicdn.com/ts/review/1d/a2/52/a7c5d0862200e9349bd925977d9530ac.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/20/89/d9/53d60796086cf4f52d81d21e3e8363d1.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/1a/30/77/f91f77e9bdf2621a22cdd245c8d6a1ed.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/ac/a6/8b/51f32ede5954cd06c34f464fe166f35d.jpg"
//                },
//                null,
//                new User("Lê Hoàn Vũ", "Lê Hoàn Vũ")
//        ));
//        reviews.add(new Review(
//                "1",
//                "Nhận máy như máy cũ, mép viền như bị cạy móp hết cả. Máy hoàn thiện cực kém, k nên mua. Đã gọi và gửi e mail cho tiki mà chưa thấy phản hồi.",
//                1,
//                1609812503,
//                new String[]{
//
//                },
//                null,
//                new User("Nguyen Tien Trinh", "Nguyen Tien Trinh")
//        ));
//        reviews.add(new Review(
//                "1",
//                "Người Việt ưu tiên hàng Việt. Mới trải nghiệm thấy chất lượng tốt. Máy hơi xước ở góc nhưng rất nhỏ không ảnh hưởng đến hình thức và chất lượng. Vote 5*",
//                5,
//                1615262086,
//                new String[]{
//                        "https://vcdn.tikicdn.com/ts/review/3a/d3/a0/d44e1fca4edfb1677c7c27ab992edcdd.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/72/4f/2a/7b79485eb0e3ac0da6f463f023476358.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/44/28/a8/e46e2c3df6b535509022ee2d1b1ef63f.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/77/10/b2/3556cf8f47eaba7f02c5179d7fa652cf.jpg"
//                },
//                null,
//                new User("Nguyễn Văn Huy", "Nguyễn Văn Huy")
//        ));
//        reviews.add(new Review(
//                "1",
//                "Vận chuyển nhanh tuyệt vời, chưa bao giờ thất vọng về đóng gói và vận chuyển của Tiki Now.\\nChuẩn hàng theo order.",
//                5,
//                1614322198,
//                new String[]{
//                        "https://vcdn.tikicdn.com/ts/review/60/b3/be/8915662de7e332154256879068801283.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/7e/00/19/af32babcbce0441c45eb82ee509c994b.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/6d/b2/93/0b6c3e274c672828f0bcfd2d752a65b0.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/e5/a6/9e/1158c6caad9532c2c275af352edceea9.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/2e/cd/6c/76e056a2e1eea4ba4eaa37906937dd33.jpg"
//                },
//                null,
//                new User("Nguyen VietThang", "Nguyen VietThang")
//        ));
//        reviews.add(new Review(
//                "1",
//                "Tiki giao hàng nhanh, ngày 2/3 đặt hàng sang hôm sau đã nhận được hàng. Hàng giao nguyên seal, mua để làm quà tặng nên chưa có đánh giá về chất lượng sản phẩm.",
//                5,
//                1614827457,
//                new String[]{
//                        "https://vcdn.tikicdn.com/ts/review/e9/f4/7d/5c537871b1b4806be343f29a30851df9.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/c5/8b/f9/ea3e75b3e9eb6359be0c9d045d3f7118.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/66/06/82/64120692fd9e787182a0ecc064e2ebcf.jpg"
//                },
//                null,
//                new User("Nguyễn Ngọc Bách", "Nguyễn Ngọc Bách")
//        ));
//        reviews.add(new Review(
//                "1",
//                "Sau khi dùng 1 thời gian (khoảng 2 tháng) cảm nhận điện thoại khá mượt mà ,chụp ảnh đẹp. Chơi game bình thường như pubg hoặc liên quân k bị nóng máy nhưng chơi game nặng thì nóng lên đến hơn 42độ C. Pin chơi game liên tục đc khoảng 7h. xem phim liên tục thì đc lâu hơn. Nói chung với giá khoảng 3tr5 thì điện thoại này vô địch rồi lại còn hàng VN nên mình nhiệt tình ủng hộ. sẽ mua thêm vsmart cho người thân nữa",
//                5,
//                1614773295,
//                new String[]{
//                        "https://vcdn.tikicdn.com/ts/review/ec/01/db/039c7a861ce251e35a8930edc0c55f99.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/35/b1/3f/7bbdad25dda635c51343f8cf848c46dc.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/89/3e/e4/8b248b7daf3e37ac13294630b8847353.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/93/92/cb/02d1f8781cc3e85311d1bb31c00c4d30.jpg"
//                },
//                null,
//                new User("Thiện Đức Đinh", "Thiện Đức Đinh")
//        ));
//        reviews.add(new Review(
//                "1",
//                "Tuy đóng gói rất cẩn thận. Nhưng hộp ngoài hơi củ, mép màn hình bên phía nút nguồn bị hở nhẹ mép (để ý mới thấy - Giôngd như hàng củ - đóng hộp lại)- Cảm biến Vân tay và Nhận diện khuôn mặt không xài được.\\nRất không hài lòng với TikiTrading. Chắc do điện thoại bị lổi nên giá rẻ hơn thị trường rất nhiều.",
//                1,
//                1615544951,
//                new String[]{
//                        "https://vcdn.tikicdn.com/ts/review/5b/92/3b/37a33abf8f1862c3d319487d1bc15ffc.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/be/a8/a6/e7d51ef9e4624260cc3e0860d88ce4cb.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/e1/6d/82/1369162e1a15687eb67b0cbbc8d6c712.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/b2/ea/48/6e969b92d5def8268ca5b2d60228519a.jpg"
//                },
//                null,
//                new User("Lý Thành Nhơn", "Lý Thành Nhơn")
//        ));
//        reviews.add(new Review(
//                "1",
//                "Sau khi dùng nữa tháng, mình thấy máy dùng ok. Chơi pubg ko bị quá nóng. Giao điện setup ban đầu khá xấu, nhưng chỉ cần vào cài đặt chỉnh lại là dùng ok(Như hình). Độ sáng màn hình thấp nhìn màn hình khá tối, phải 70 80% mới ổn. Còn các phần mền thì khá mượt chưa bị vấn đề gì. Những bạn quen dùng máy mỏng nhẹ thì với máy sẽ có cảm giác khá dày và nặng vì viên pin 5000. Lúc đầu chưa quen cử chỉ thì của máy thì củng có chút khó chịu khi dùng. mà quen rồi thì dùng củng thoải mái.",
//                5,
//                1600581887,
//                new String[]{
//                        "https://vcdn.tikicdn.com/ts/review/a4/b0/3c/2078d2e6067a4aa75be55994312537f7.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/ad/39/62/902d03f96a74175b104552a61bff9739.jpg"
//                },
//                null,
//                new User("Nguyen Ba Khanh", "Nguyen Ba Khanh")
//        ));
//        reviews.add(new Review(
//                "1",
//                "Mua hàng của Tiki Trading hoàn toàn yên tâm. Giao hàng nhanh, đóng gói gói chắc chắn. Hàng nguyên seal, mọi thứ đều mới tinh tươm. Điện thoại mở lên được tự kích hoạt bảo hành, mượt mà. Ấn tượng đầu tiên về sản phẩm là như vậy.",
//                5,
//                1614773845,
//                new String[]{
//                        "https://vcdn.tikicdn.com/ts/review/74/12/1c/d61f6caea6f9887d685b1aae8d110216.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/4c/10/87/08bd4786fc49ba5bb1c608bc6951ed88.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/69/d3/bd/36a872155820e1e1ca48aeb3f4a9ee48.jpg",
//                        "https://vcdn.tikicdn.com/ts/review/a0/02/a9/d90bb9512d84daa25308896ead044f7b.jpg"
//                },
//                null,
//                new User("Phạm Nhật Tiến", "Phạm Nhật Tiến")
//        ));
//        return reviews;
//    }
}