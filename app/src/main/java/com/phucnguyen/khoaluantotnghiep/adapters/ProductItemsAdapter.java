package com.phucnguyen.khoaluantotnghiep.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;

import java.util.ArrayList;
import java.util.List;

public class ProductItemsAdapter extends RecyclerView.Adapter<ProductItemsAdapter.ProductItemViewHolder> {
    private Context mContext;
    private List<ProductItem> mProductItems = new ArrayList<>();

    public ProductItemsAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.product_item, parent, false);
        return new ProductItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, int position) {
        ProductItem item = mProductItems.get(position);
        holder.tvProductRate.setText(String.valueOf(item.getRating()));
        holder.tvProductTitle.setText(item.getName());
        holder.tvPrice.setText(String.valueOf(item.getProductPrice()));
        holder.tvSellerRate.setText(String.valueOf(item.getSellerRate()));
        //Đây chỉ là placeholder cho thằng số lượng sản phẩm bán ra, cần chỉnh sửa lại khi
        //dữ liệu bên Hùng chỉnh lại
        holder.tvProductItemSoldQuantities.setText(String.valueOf(item.getTotalReview()));
        Glide.with(mContext)
                .load(item.getThumbnailUrl())
                .into(holder.imgProduct);
        //Decide which shop rate icon to show depends on the rating of the seller
        Drawable shopRateIconRes;
        if(item.getSellerRate() == 0)
            shopRateIconRes = ResourcesCompat.getDrawable(mContext.getResources(),
                    R.drawable.ic_question_face_12px,
                    null);
        else if (item.getSellerRate() < 50)
            shopRateIconRes = ResourcesCompat.getDrawable(mContext.getResources(),
                    R.drawable.ic_sad_face_12px,
                    null);
        else if (item.getSellerRate() < 80)
            shopRateIconRes = ResourcesCompat.getDrawable(mContext.getResources(),
                    R.drawable.ic_medium_happy_12px,
                    null);
        else shopRateIconRes = ResourcesCompat.getDrawable(mContext.getResources(),
                    R.drawable.ic_face_happy_12px,
                    null);
        Glide.with(mContext)
                .load(shopRateIconRes)
                .into(holder.imgSellerRateIcon);
    }

    @Override
    public int getItemCount() {
        return mProductItems.size();
    }

    public void setProductItems(List<ProductItem> productItems) {
        mProductItems = productItems;
        notifyDataSetChanged();
    }

    public static class ProductItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView tvPrice;
        private TextView tvProductTitle;
        private TextView tvSellerRate;
        private ImageView imgSellerRateIcon;
        private TextView tvProductRate;
        private TextView tvProductItemSoldQuantities;
        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = (ImageView) itemView.findViewById(R.id.imgProduct);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvProductTitle = (TextView) itemView.findViewById(R.id.tvProductTitle);
            tvSellerRate = (TextView) itemView.findViewById(R.id.tvSellerRate);
            imgSellerRateIcon = (ImageView) itemView.findViewById(R.id.imgSellerRateIcon);
            tvProductRate = (TextView) itemView.findViewById(R.id.tvProductRate);
            tvProductItemSoldQuantities = (TextView)itemView.findViewById(R.id.tvProductItemSoldQuantities);
        }
    }
}
