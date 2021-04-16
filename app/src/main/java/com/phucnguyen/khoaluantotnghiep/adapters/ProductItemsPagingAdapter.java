package com.phucnguyen.khoaluantotnghiep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.utils.Utils;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.model.Review;
import com.phucnguyen.khoaluantotnghiep.model.datasource.ReviewDataSource;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;

import static com.phucnguyen.khoaluantotnghiep.utils.Contants.LOAD_VIEW_TYPE;
import static com.phucnguyen.khoaluantotnghiep.utils.Contants.PRODUCT_VIEW_TYPE;

public class ProductItemsPagingAdapter extends PagedListAdapter<ProductItem, RecyclerView.ViewHolder> {

    private Context mContext;
    private Contants.LoadingState loadingState;
    private ProductItemsAdapter.Listener listener;

    public ProductItemsPagingAdapter(Context context) {
        super(DIFF_CALLBACK);
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == PRODUCT_VIEW_TYPE){
            View v = LayoutInflater.from(mContext)
                    .inflate(R.layout.product_item, parent, false);
            return new ProductItemViewHolder(v);
        } else {
            View v = LayoutInflater.from(mContext)
                    .inflate(R.layout.loading_progress_item, parent, false);
            return new LoadingViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProductItemViewHolder){
            ProductItemViewHolder viewHolder = (ProductItemViewHolder) holder;
            ProductItem item = getItem(position);
            viewHolder.tvProductTitle.setText(item.getName());
            viewHolder.tvPrice.setText(Utils.formatNumber(item.getProductPrice(),
                    0,
                    true,
                    '.'));
            Glide.with(mContext)
                    .load(item.getThumbnailUrl())
                    .into(viewHolder.imgProduct);
            viewHolder.tvPlatform.setText(item.getPlatform());
            if (item.getPlatform().equals("tiki"))
                viewHolder.tvPlatform.setTextColor(mContext.getResources().getColor(R.color.blue_tiki));
            else
                viewHolder.tvPlatform.setTextColor(mContext.getResources().getColor(R.color.orange_shopee));

            if (item.getRating() == 0) {
                viewHolder.iconRating.setImageResource(R.drawable.ic_question_face_12px);
                viewHolder.tvProductRate.setText("?");
                viewHolder.tvProductRate.setTextColor(mContext.getResources().getColor(R.color.purple_question));
            } else {
                viewHolder.iconRating.setImageResource(R.drawable.ic_star);
                viewHolder.tvProductRate.setText(String.format("%.1f", item.getRating()));
                viewHolder.tvProductRate.setTextColor(mContext.getResources().getColor(R.color.black));
            }
            viewHolder.tvProductReviewQuantities.setText(mContext.getString(R.string.review_quantities,
                    item.getTotalReview()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onItemClicked(item.getProductUrl());
                }
            });
        }
    }

    public void setListener(ProductItemsAdapter.Listener listener) {
        this.listener = listener;
    }

    public void setLoadingState(Contants.LoadingState loadingState) {
        this.loadingState = loadingState;
    }

    private static DiffUtil.ItemCallback<ProductItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<ProductItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull ProductItem oldItem, @NonNull ProductItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ProductItem oldItem, @NonNull ProductItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    };

    @Override
    public int getItemViewType(int position) {
        //IMPORTANT: When to show a loading spinner
        //if((we are at the last position of previous page)
        if (position == getItemCount() - 1 && loadingState != null && loadingState != Contants.LoadingState.SUCCESS)
            return LOAD_VIEW_TYPE;
        else return PRODUCT_VIEW_TYPE;
    }

    public static class ProductItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private ImageView iconRating;
        private TextView tvPrice;
        private TextView tvProductTitle;
        private TextView tvProductRate;
        private TextView tvProductReviewQuantities;
        private TextView tvPlatform;

        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductTitle = (TextView) itemView.findViewById(R.id.tvProductTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            imgProduct = (ImageView) itemView.findViewById(R.id.imgProduct);
            tvPlatform = (TextView) itemView.findViewById(R.id.tvPlatform);
            iconRating = (ImageView) itemView.findViewById(R.id.iconRating);

            //additional mapping views for product_item.xml
            tvProductRate = (TextView) itemView.findViewById(R.id.tvProductRate);
            tvProductReviewQuantities = (TextView) itemView.findViewById(R.id.tvProductItemReview);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
