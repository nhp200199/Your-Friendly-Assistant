package com.phucnguyen.khoaluantotnghiep;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.phucnguyen.khoaluantotnghiep.adapters.SearchHistoryRecyclerViewAdapter;
import com.phucnguyen.khoaluantotnghiep.database.RecentSearch;
import com.phucnguyen.khoaluantotnghiep.database.RecentSearchDao;
import com.phucnguyen.khoaluantotnghiep.database.SearchDatabase;

import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;

public class FindProductsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TextView tvFindIntroduction;
    private LinearLayout historySearchContainer;

    private SearchHistoryRecyclerViewAdapter mRecyclerViewAdapter;
    private LiveData<List<RecentSearch>> mRecentSearchs;
    private RecentSearchDao mRecentSearchDao;

    public FindProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRecentSearchDao = SearchDatabase.getInstance(requireContext())
                .recentSearchDao();
        mRecentSearchs = mRecentSearchDao.getRecentSearchsForHistory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.find_products_fragment, container, false);
        connectViews(v);

        //decide which layout to show
        mRecentSearchs.observe(getViewLifecycleOwner(), new Observer<List<RecentSearch>>() {
            @Override
            public void onChanged(List<RecentSearch> recentSearches) {
                mRecyclerViewAdapter.setRecentSearches(recentSearches);

                if (mRecentSearchs == null || mRecentSearchs.getValue().size() == 0){
                    //hide the search history and show introduction
                    tvFindIntroduction.setVisibility(View.VISIBLE);
                    historySearchContainer.setVisibility(View.GONE);
                }else {
                    //otherwise show search history
                    tvFindIntroduction.setVisibility(View.GONE);
                    historySearchContainer.setVisibility(View.VISIBLE);
                }
            }
        });
        return v;
    }

    private void connectViews(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rcvSearch);
        tvFindIntroduction = (TextView) v.findViewById(R.id.tvFindIntroduction);
        historySearchContainer = (LinearLayout) v.findViewById(R.id.historySearchContainer);

        mRecyclerViewAdapter = new SearchHistoryRecyclerViewAdapter(requireContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerViewAdapter.setListener(new SearchHistoryRecyclerViewAdapter.Listener() {
            @Override
            public void onRemoveHistoryItem(RecentSearch itemToBeRemoved) {
                new DeleteRecentSearchAsyncTask(mRecentSearchDao).execute(itemToBeRemoved);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                startActivity(new Intent(requireActivity(), SearchableActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private static class DeleteRecentSearchAsyncTask extends AsyncTask<RecentSearch, Void, Void> {
        private RecentSearchDao recentSearchDao;

        private DeleteRecentSearchAsyncTask(RecentSearchDao recentSearchDao){
            this.recentSearchDao = recentSearchDao;
        }
        @Override
        protected Void doInBackground(RecentSearch... recentSearchs) {
            recentSearchDao.delete(recentSearchs[0]);
            return null;
        }
    }
}