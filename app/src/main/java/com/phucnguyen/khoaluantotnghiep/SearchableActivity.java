package com.phucnguyen.khoaluantotnghiep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import com.phucnguyen.khoaluantotnghiep.database.RecentSearchDao;
import com.phucnguyen.khoaluantotnghiep.database.RecentSearch;
import com.phucnguyen.khoaluantotnghiep.database.SearchDatabase;

import java.util.Date;

public class SearchableActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SearchView mSearchView;
    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.expandActionView();
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS|MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        //update the query in search view
        mSearchView.setQuery(mQuery, false);
        mSearchView.clearFocus();
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mQuery = intent.getStringExtra(SearchManager.QUERY).toLowerCase();
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySearchSuggestionProvider.AUTHORITY, MySearchSuggestionProvider.MODE);
            suggestions.saveRecentQuery(mQuery, null);
            //TODO: use the query to search your data somehow
            //store the search to database
            SearchDatabase searchDatabase = SearchDatabase.getInstance(getApplicationContext());
            RecentSearchDao recentSearchDao = searchDatabase.recentSearchDao();
            RecentSearch recentSearch = new RecentSearch(mQuery, String.valueOf(new Date().getTime()));
            new InsertRecentSearchAsyncTask(recentSearchDao).execute(recentSearch);
        }
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