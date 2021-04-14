package com.phucnguyen.khoaluantotnghiep.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phucnguyen.khoaluantotnghiep.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryNamesAdapter extends RecyclerView.Adapter<CategoryNamesAdapter.ViewHolder> {
    public interface Listener {
        void onCategoryClicked(int categoryPos);
    }

    private Context mContext;
    private List<String> categories = new ArrayList<String>();
    private Listener mListener;
    private int currentCategoryPos;
    private SparseBooleanArray categorySelectionState;

    public CategoryNamesAdapter(Context context) {
        mContext = context;
        categorySelectionState = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvCategoryName.setText(categories.get(position));
        boolean isSelected = categorySelectionState.get(position);
        holder.itemView.setSelected(isSelected);
        if (isSelected) {
            holder.tvCategoryName.setTextColor(mContext.getResources().getColor(R.color.white));
        } else
            holder.tvCategoryName.setTextColor(mContext.getResources().getColor(R.color.black));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.itemView.setSelected(true);
                holder.tvCategoryName.setTextColor(mContext.getResources().getColor(R.color.white));
                if (mListener != null)
                    mListener.onCategoryClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void setCurrentCategoryPos(int currentCategoryPos) {
        //disable selection on the previous category
        int previousSelectedCategoryPos = this.currentCategoryPos;
        categorySelectionState.put(previousSelectedCategoryPos, false);
        notifyItemChanged(previousSelectedCategoryPos);

        //notify the selected category to update its view
        this.currentCategoryPos = currentCategoryPos;
        categorySelectionState.put(currentCategoryPos, true);
        notifyItemChanged(this.currentCategoryPos);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
