package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.adapters.SearchHistoryRecyclerViewAdapter;
import com.phucnguyen.khoaluantotnghiep.database.RecentSearch;
import com.phucnguyen.khoaluantotnghiep.database.RecentSearchDao;
import com.phucnguyen.khoaluantotnghiep.database.SearchDatabase;

import java.util.List;

public class FindProductsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TextView tvFindIntroduction;
    private LinearLayout historySearchContainer;
    private SearchView mSearchView;

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
        inflater.inflate(R.menu.menu_search_for_real, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        // Get the SearchView
        mSearchView = (SearchView) menuItem.getActionView();

        mSearchView.setMaxWidth(Integer.MAX_VALUE);
        //update the query in search view
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setQueryHint(getString(R.string.find_guide));

        //remove search icon
        mSearchView.setIconified(false); // this is important to hide the search hint icon
        ImageView icon = (ImageView) mSearchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        icon.setVisibility(View.GONE);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //close keyboard
//                InputMethodManager imm = (InputMethodManager) SearchableActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
//                //Find the currently focused view, so we can grab the correct window token from it.
//                View view = SearchableActivity.this.getCurrentFocus();
//                //If no view currently has focus, create a new one, just so we can grab a window token from it
//                if (view == null) {
//                    view = new View(SearchableActivity.this);
//                }
//                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                //TODO: use the query to search your data somehow
                //store the search to database
//                SearchDatabase searchDatabase = SearchDatabase.getInstance(getApplicationContext());
//                mRecentSearchDao = searchDatabase.recentSearchDao();
//                RecentSearch recentSearch = new RecentSearch(query, String.valueOf(new Date().getTime()));
//                new InsertRecentSearchAsyncTask(mRecentSearchDao).execute(recentSearch);
                Bundle bundle = new Bundle();
                bundle.putString("productUrl", query);
                NavHostFragment.findNavController(FindProductsFragment.this)
                        .navigate(R.id.action_global_productItemFragment, bundle);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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