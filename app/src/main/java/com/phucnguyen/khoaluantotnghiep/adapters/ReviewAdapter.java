package com.phucnguyen.khoaluantotnghiep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.model.Review;
import com.phucnguyen.khoaluantotnghiep.model.datasource.ReviewDataSource;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewAdapter extends PagedListAdapter<Review, RecyclerView.ViewHolder> {
    public static final int TYPE_LOAD = 1;
    public static final int TYPE_REVIEW = 2;

    private Context context;
    private ProductImagesAdapter.ImageListener mImageListener;
    private ProductVideoAdapter.VideoListener mVideoListener;
    private ReviewDataSource.LoadingState state;

    public ReviewAdapter(Context context, ProductImagesAdapter.ImageListener imageListener,
                         ProductVideoAdapter.VideoListener videoListener) {
        super(DIFF_CALLBACK);
        this.context = context;
        mImageListener = imageListener;
        mVideoListener = videoListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_REVIEW)
            return new ReviewViewHolder(LayoutInflater
                    .from(context)
                    .inflate(R.layout.review_item, parent, false));
        else return new LoadingViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.loading_progress_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Review review = getItem(position);

        if (holder instanceof ReviewViewHolder) {
            ((ReviewViewHolder) holder).tvReviewOwner.setText(review.getUser().getName());
            ((ReviewViewHolder) holder).tvReviewDate.setText(transformMilToDateString(review.getCreatedAt()));
            ((ReviewViewHolder) holder).tvReviewContent.setText(review.getContent());
            ((ReviewViewHolder) holder).tvReviewRating.setText(String.valueOf(review.getRating()));
            //setting up the nested recycler view
            ((ReviewViewHolder) holder).mediaContainer.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL, false));
            ((ReviewViewHolder) holder).mediaContainer.setHasFixedSize(true);
            ProductImagesAdapter imagesAdapter = new ProductImagesAdapter(context, mImageListener);
            imagesAdapter.setImageUrls(review.getImages());
            ProductVideoAdapter videoAdapter = null;
            //in tiki, there will be no videos, so this is the check if the review has videos
            if (review.getVideos() != null) {
                videoAdapter = new ProductVideoAdapter(context, mVideoListener);
                videoAdapter.setVideos(review.getVideos());
                //here, we combine the 2 adapters into a ConcatAdapter to display different view types
                ConcatAdapter concatenated = new ConcatAdapter(imagesAdapter, videoAdapter);
                ((ReviewViewHolder) holder).mediaContainer.setAdapter(concatenated);
            } else {
                ((ReviewViewHolder) holder).mediaContainer.setAdapter(imagesAdapter);
            }
        }
    }

    public void setState(ReviewDataSource.LoadingState state) {
        this.state = state;
    }

    private String transformMilToDateString(long createdAt) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(new Date(createdAt));
    }

    @Override
    public int getItemViewType(int position) {
        //IMPORTANT: When to show a loading spinner
        //if((we are at the last position of previous page)
        if (position == getItemCount() - 1 && state != null && state != ReviewDataSource.LoadingState.SUCCESS)
            return TYPE_LOAD;
        else return TYPE_REVIEW;
    }

    private static DiffUtil.ItemCallback<Review> DIFF_CALLBACK = new DiffUtil.ItemCallback<Review>() {
        @Override
        public boolean areItemsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    };

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView tvReviewOwner;
        private TextView tvReviewDate;
        private TextView tvReviewContent;
        private TextView tvReviewRating;
        private RecyclerView mediaContainer;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReviewOwner = itemView.findViewById(R.id.tvReviewOwner);
            tvReviewDate = itemView.findViewById(R.id.tvReviewDate);
            tvReviewContent = itemView.findViewById(R.id.tvReviewContent);
            tvReviewRating = itemView.findViewById(R.id.tvReviewRating);
            mediaContainer = itemView.findViewById(R.id.mediaContainer);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
