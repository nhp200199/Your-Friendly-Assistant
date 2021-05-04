package com.phucnguyen.khoaluantotnghiep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.utils.Utils;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;
import com.phucnguyen.khoaluantotnghiep.utils.NumbersFormatter;

import static com.phucnguyen.khoaluantotnghiep.utils.Contants.LOAD_VIEW_TYPE;
import static com.phucnguyen.khoaluantotnghiep.utils.Contants.PRODUCT_VIEW_TYPE;

public class ProductItemsPagingAdapter extends PagedListAdapter<ProductItem, RecyclerView.ViewHolder> {

    public interface btnListener{
        void onRetry();
    }
    private Context mContext;
    private Contants.ItemLoadingState mItemLoadingState;
    private ProductItemsAdapter.Listener listener;
    private btnListener mBtnListener;

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
                    .placeholder(mContext.getDrawable(R.drawable.logo_fade))
                    .error(mContext.getDrawable(R.drawable.logo_fade))
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
                viewHolder.tvProductRate.setText(NumbersFormatter.formatFloatToString(item.getRating(), 1));
                viewHolder.tvProductRate.setTextColor(mContext.getResources().getColor(R.color.black));
            }
            viewHolder.tvProductReviewQuantities.setText(mContext.getString(R.string.review_quantities,
                    item.getTotalReview()));
            int priceDifference = item.getPriceDifference();
            viewHolder.priceChangeContainer.setVisibility(View.VISIBLE);
            if (priceDifference < 0){
                viewHolder.tvPriceDifference.setText(Utils.formatNumber(Math.abs(priceDifference),
                        0,
                        true,
                        '.'));
                viewHolder.tvPriceDifference.setTextColor(mContext.getResources().getColor(R.color.green_happy));
                //set the arrow to indicate price change
                viewHolder.iconPriceChange.setRotation(90);
                viewHolder.iconPriceChange.setColorFilter(mContext.getResources().getColor(R.color.green_happy));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onItemClicked(item.getProductUrl());
                }
            });
        } else {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
            if (mItemLoadingState == Contants.ItemLoadingState.SUB_LOAD_ERROR){
                viewHolder.pbLoadingBar.setVisibility(View.GONE);
                viewHolder.btnRetry.setVisibility(View.VISIBLE);
            }
            viewHolder.btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.pbLoadingBar.setVisibility(View.VISIBLE);
                    viewHolder.btnRetry.setVisibility(View.GONE);
                    if (mBtnListener != null)
                        mBtnListener.onRetry();
                }
            });
        }
    }

    public void setListener(ProductItemsAdapter.Listener listener) {
        this.listener = listener;
    }

    public void setItemLoadingState(Contants.ItemLoadingState itemLoadingState) {
        this.mItemLoadingState = itemLoadingState;
        //update the last view holder for handle reload
        notifyItemChanged(getItemCount() - 1);
    }

    public void setBtnListener(btnListener btnListener) {
        mBtnListener = btnListener;
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
        if (position == getItemCount() - 1 && mItemLoadingState != null && mItemLoadingState != Contants.ItemLoadingState.SUCCESS)
            return LOAD_VIEW_TYPE;
        else return PRODUCT_VIEW_TYPE;
    }

    public static class ProductItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private ImageView iconRating;
        private ImageView iconPriceChange;
        private TextView tvPrice;
        private TextView tvProductTitle;
        private TextView tvProductRate;
        private TextView tvProductReviewQuantities;
        private TextView tvPlatform;
        private TextView tvPriceDifference;
        private LinearLayout priceChangeContainer;

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
            tvPriceDifference = (TextView) itemView.findViewById(R.id.tvPriceDifferrence);
            iconPriceChange = (ImageView) itemView.findViewById(R.id.iconPriceChange);
            priceChangeContainer = (LinearLayout) itemView.findViewById(R.id.priceChangesContainer);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar pbLoadingBar;
        private Button btnRetry;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            pbLoadingBar = itemView.findViewById(R.id.pbLoadingBar);
            btnRetry = itemView.findViewById(R.id.btnRetry);
        }
    }
}
