package com.phucnguyen.khoaluantotnghiep.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.database.RecentSearchDao;
import com.phucnguyen.khoaluantotnghiep.database.RecentSearch;
import com.phucnguyen.khoaluantotnghiep.database.SearchDatabase;

import java.util.Date;

public class SearchableActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SearchView mSearchView;
    private RecentSearchDao mRecentSearchDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search_for_real, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        // Get the SearchView
        mSearchView = (SearchView) menuItem.getActionView();

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
                InputMethodManager imm = (InputMethodManager) SearchableActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Find the currently focused view, so we can grab the correct window token from it.
                View view = SearchableActivity.this.getCurrentFocus();
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = new View(SearchableActivity.this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                //TODO: use the query to search your data somehow
                //store the search to database
                SearchDatabase searchDatabase = SearchDatabase.getInstance(getApplicationContext());
                mRecentSearchDao = searchDatabase.recentSearchDao();
                RecentSearch recentSearch = new RecentSearch(query, String.valueOf(new Date().getTime()));
                new InsertRecentSearchAsyncTask(mRecentSearchDao).execute(recentSearch);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
    private static class InsertRecentSearchAsyncTask extends AsyncTask<RecentSearch, Void, Void>{
        private RecentSearchDao recentSearchDao;

        private InsertRecentSearchAsyncTask(RecentSearchDao recentSearchDao){
            this.recentSearchDao = recentSearchDao;
        }
        @Override
        protected Void doInBackground(RecentSearch... recentSearchs) {
            recentSearchDao.insert(recentSearchs[0]);
            return null;
        }
    }
}