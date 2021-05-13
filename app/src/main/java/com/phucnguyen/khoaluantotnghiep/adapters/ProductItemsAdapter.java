package com.phucnguyen.khoaluantotnghiep.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.res.ResourcesCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.utils.Utils;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.utils.NumbersFormatter;

import java.util.ArrayList;
import java.util.List;

public class ProductItemsAdapter extends RecyclerView.Adapter<ProductItemsAdapter.ProductItemViewHolder> {
    public static final int ACTION_UPDATE_PRICE = 0;
    public static final int ACTION_DETETE_ITEM = 1;

    public interface Listener {
        void onItemClicked(String url);
    }

    public interface ActionListener {
        void onActionClicked(int which, ProductItem itemToBeActedOn);
    }

    private Context mContext;
    private int mResId;
    private List<ProductItem> mProductItems = new ArrayList<>();
    private Listener mListener;
    private ActionListener actionListener;

    public ProductItemsAdapter(Context context, int resId) {
        mContext = context;
        mResId = resId;
    }

    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(mResId, parent, false);
        return new ProductItemViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mProductItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, int position) {
        ProductItem item = mProductItems.get(position);
        holder.tvProductTitle.setText(item.getName());
        holder.tvPrice.setText(Utils.formatNumber(item.getProductPrice(),
                0,
                true,
                '.'));
        Glide.with(mContext)
                .load(item.getThumbnailUrl())
                .placeholder(mContext.getDrawable(R.drawable.logo_fade))
                .error(mContext.getDrawable(R.drawable.logo_fade))
                .into(holder.imgProduct);
        holder.tvPlatform.setText(item.getPlatform());
        if (item.getPlatform().equals("tiki"))
            holder.tvPlatform.setTextColor(mContext.getResources().getColor(R.color.blue_tiki));
        else
            holder.tvPlatform.setTextColor(mContext.getResources().getColor(R.color.orange_shopee));

        //bind views for product_item layout
        if (mResId == R.layout.product_item) {
            if (item.getRating() == 0) {
                holder.iconRating.setImageResource(R.drawable.ic_question_face_12px);
                holder.tvProductRate.setText("?");
                holder.tvProductRate.setTextColor(mContext.getResources().getColor(R.color.purple_question));
            } else {
                holder.iconRating.setImageResource(R.drawable.ic_star);
                holder.tvProductRate.setText(NumbersFormatter.formatFloatToString(item.getRating(), 1));
                holder.tvProductRate.setTextColor(mContext.getResources().getColor(R.color.black));
            }
            holder.tvProductReviewQuantities.setText(mContext.getString(R.string.review_quantities,
                    item.getTotalReview()));
        } else if (mResId == R.layout.tracked_product_item) {
            holder.tvWishedPrice.setText("Bạn sẽ nhận được thông báo khi giá thấp hơn " + item.getDesiredPrice());
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mContext, holder.icActionMore);
                    popupMenu.inflate(R.menu.tracked_product);
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (actionListener != null) {
                                switch (menuItem.getItemId()) {
                                    case R.id.actionUpdateWishedPrice:
                                        actionListener.onActionClicked(ACTION_UPDATE_PRICE, item);
                                        break;
                                    case R.id.actionDeleteTrackedProduct:
                                        actionListener.onActionClicked(ACTION_DETETE_ITEM, item);
                                        break;
                                }
                                return true;
                            }
                            return false;

                        }
                    });
                    return true;
                }
            });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onItemClicked(item.getProductUrl());
            }
        });
        //bind views for ...
//        else{
//            holder.tvProductRate.setText(String.valueOf(item.getRating()));
//            holder.tvSellerRate.setText(String.valueOf(item.getSellerRate()));
//            //Đây chỉ là placeholder cho thằng số lượng sản phẩm bán ra, cần chỉnh sửa lại khi
//            //dữ liệu bên Hùng chỉnh lại
//            holder.tvProductItemSoldQuantities.setText(String.valueOf(item.getTotalReview()));
//            //Decide which shop rate icon to show depends on the rating of the seller
//            Drawable shopRateIconRes;
//            if(item.getSellerRate() == 0)
//                shopRateIconRes = ResourcesCompat.getDrawable(mContext.getResources(),
//                        R.drawable.ic_question_face_12px,
//                        null);
//            else if (item.getSellerRate() < 50)
//                shopRateIconRes = ResourcesCompat.getDrawable(mContext.getResources(),
//                        R.drawable.ic_sad_face_12px,
//                        null);
//            else if (item.getSellerRate() < 80)
//                shopRateIconRes = ResourcesCompat.getDrawable(mContext.getResources(),
//                        R.drawable.ic_medium_happy_12px,
//                        null);
//            else shopRateIconRes = ResourcesCompat.getDrawable(mContext.getResources(),
//                        R.drawable.ic_face_happy_12px,
//                        null);
//            Glide.with(mContext)
//                    .load(shopRateIconRes)
//                    .into(holder.imgSellerRateIcon);
//        }
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void setProductItems(List<ProductItem> productItems) {
        mProductItems = productItems;
        notifyDataSetChanged();
    }

    public static class ProductItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private ImageView iconRating;
        private ImageView icActionMore;
        private TextView tvPrice;
        private TextView tvProductTitle;
        private TextView tvProductRate;
        private TextView tvProductReviewQuantities;
        private TextView tvPlatform;
        private TextView tvWishedPrice;

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

            //additional mapping views for tracked_product_item.xml
            icActionMore = itemView.findViewById(R.id.imgActionMore);
            tvWishedPrice = itemView.findViewById(R.id.tvWishedPrice);
        }
    }
}
