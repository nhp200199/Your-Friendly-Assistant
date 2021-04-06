package com.phucnguyen.khoaluantotnghiep.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phucnguyen.khoaluantotnghiep.R;

import java.util.ArrayList;
import java.util.List;

public class ProductImagesAdapter extends RecyclerView.Adapter<ProductImagesAdapter.ImageViewHolder> {
    public interface ImageListener {
        void onImageClicked(String productUrl);
    }

    private ImageListener mImageListener;
    private Context mContext;
    private String[] mImageUrls;

    public ProductImagesAdapter(Context context, ImageListener listener) {
        mContext = context;
        mImageListener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (FrameLayout) LayoutInflater.from(mContext).inflate(R.layout.image_item,
                parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Glide.with(mContext)
                .load(mImageUrls[position])
                .into(holder.imgRealProduct);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageListener.onImageClicked(mImageUrls[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrls.length;
    }

    public void setImageUrls(String[] imageUrls) {
        mImageUrls = imageUrls;
        notifyDataSetChanged();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgRealProduct;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRealProduct = (ImageView) itemView.findViewById(R.id.imgRealProduct);

        }
    }
}
