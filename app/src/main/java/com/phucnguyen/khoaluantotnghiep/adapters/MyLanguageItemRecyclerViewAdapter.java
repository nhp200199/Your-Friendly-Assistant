package com.phucnguyen.khoaluantotnghiep.adapters;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phucnguyen.khoaluantotnghiep.R;

import java.util.Arrays;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLanguageItemRecyclerViewAdapter extends RecyclerView.Adapter<MyLanguageItemRecyclerViewAdapter.ViewHolder> {
    private int selectedPos;
    private SharedPreferences languagePref;
    private Context context;
    private final List<String> mValues = Arrays.asList(
            "Vietnamese",
            "English"
    );

    public MyLanguageItemRecyclerViewAdapter(Context context){
        this.context = context;
        languagePref = PreferenceManager.getDefaultSharedPreferences(context);
        selectedPos = getSelectedLanguageIndex();
    }

    private int getSelectedLanguageIndex() {
        return mValues.indexOf(languagePref.getString("language_pref", ""));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_language_setting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(position == selectedPos) {
            holder.mView.setSelected(true);
            holder.mContentView.setTextColor(
                    context.getResources().getColor(R.color.colorPrimary)
            );
        }
        holder.mContentView.setText(mValues.get(position));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languagePref.edit()
                        .putString("language_pref", holder.mContentView.getText().toString())
                        .apply();
                if (position != selectedPos){
                    notifyItemChanged(selectedPos);
                    selectedPos = holder.getLayoutPosition();
                    notifyItemChanged(selectedPos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}