package com.phucnguyen.khoaluantotnghiep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.model.Review;

import java.util.List;

public class ProductVideoAdapter extends RecyclerView.Adapter<ProductImagesAdapter.ImageViewHolder> {
    public interface VideoListener {
        void onVideoClicked(Review.Video video);
    }

    private Context mContext;
    private List<Review.Video> mVideos;
    private VideoListener mVideoListener;

    public ProductVideoAdapter(Context context, VideoListener videoListener) {
        mContext = context;
        mVideoListener = videoListener;
    }

    @NonNull
    @Override
    public ProductImagesAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (FrameLayout) LayoutInflater.from(mContext).inflate(R.layout.image_item,
                parent, false);
        v.findViewById(R.id.imgPlayback).setVisibility(View.VISIBLE);
        return new ProductImagesAdapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductImagesAdapter.ImageViewHolder holder, int position) {
        Glide.with(mContext)
                .load(mVideos.get(position).getCover())
                .into(holder.imgRealProduct);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoListener.onVideoClicked(mVideos.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public void setVideos(List<Review.Video> videos) {
        mVideos = videos;
        notifyDataSetChanged();
    }
}
