package com.phucnguyen.khoaluantotnghiep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.database.RecentSearch;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryRecyclerViewAdapter
        extends RecyclerView.Adapter<SearchHistoryRecyclerViewAdapter.ViewHolder> {
    public interface Listener{
        void onRemoveHistoryItem(RecentSearch itemToBeRemoved);
    }

    private List<RecentSearch> mRecentSearches = new ArrayList<RecentSearch>();
    private Context mContext;
    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public SearchHistoryRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_search_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryRecyclerViewAdapter.ViewHolder holder, int position) {
        RecentSearch recentSearchItem = mRecentSearches.get(position);
        holder.tvHistorySearchContent
                .setText(recentSearchItem.searchContent);
        holder.imgRemoveHistoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null)
                    mListener.onRemoveHistoryItem(recentSearchItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecentSearches.size();
    }

    public void setRecentSearches(List<RecentSearch> recentSearches) {
        mRecentSearches = recentSearches;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHistorySearchContent;
        ImageView imgRemoveHistoryItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHistorySearchContent = itemView.findViewById(R.id.tvHistorySearchContent);
            imgRemoveHistoryItem = itemView.findViewById(R.id.imgRemoveHistoryItem);
        }
    }
}
