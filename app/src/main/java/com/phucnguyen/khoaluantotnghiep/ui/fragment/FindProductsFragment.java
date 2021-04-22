package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.adapters.ProductItemsAdapter;
import com.phucnguyen.khoaluantotnghiep.adapters.SearchHistoryRecyclerViewAdapter;
import com.phucnguyen.khoaluantotnghiep.database.RecentSearch;
import com.phucnguyen.khoaluantotnghiep.database.RecentSearchDao;
import com.phucnguyen.khoaluantotnghiep.database.SearchDatabase;
import com.phucnguyen.khoaluantotnghiep.model.Category;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;
import com.phucnguyen.khoaluantotnghiep.viewmodel.FindProductViewModel;

import java.util.Date;
import java.util.List;

public class FindProductsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView productsContainer;
    private TextView tvFindIntroduction;
    private TextView tvNoProductFound;
    private LinearLayout historySearchContainer;
    private SearchView mSearchView;
    private EditText edtProductSearch;
    private ProgressBar pbLoadingProgress;
    private ImageView icClearSearch;
    private RadioGroup radioPlatformGroup;
    private ImageView icFilter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private SearchHistoryRecyclerViewAdapter mHistorySearchAdapter;
    private ProductItemsAdapter mProductItemsAdapter;
    private LiveData<List<RecentSearch>> mRecentSearchs;
    private RecentSearchDao mRecentSearchDao;
    private FindProductViewModel mFindProductViewModel;

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
        Log.i("FindFragment", "onCreate called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFindProductViewModel = new ViewModelProvider(this).get(FindProductViewModel.class);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.find_products_fragment, container, false);
        connectViews(v);
        Log.i("FindFragment", "onCreate called");

        mFindProductViewModel.getRecentSearchs().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> recentSearchs) {
                mHistorySearchAdapter.setRecentSearches(recentSearchs);

                //show the introduction if no recent searchs found
                if (recentSearchs.size() == 0) {
                    tvFindIntroduction.setVisibility(View.VISIBLE);
                } else {
                    tvFindIntroduction.setVisibility(View.GONE);
                }

            }
        });
        mFindProductViewModel.getSuggestedProducts().observe(getViewLifecycleOwner(), new Observer<List<ProductItem>>() {
            @Override
            public void onChanged(List<ProductItem> items) {
                if (items != null) {
                    if (items.size() > 0) {
                        productsContainer.setVisibility(View.VISIBLE);
                        tvNoProductFound.setVisibility(View.INVISIBLE);
                        //set the items for suggested products adapter
                        mProductItemsAdapter.setProductItems(items);
                    } else {
                        productsContainer.setVisibility(View.INVISIBLE);
                        tvNoProductFound.setVisibility(View.VISIBLE);
                    }
                } else {
                    productsContainer.setVisibility(View.INVISIBLE);
                    tvNoProductFound.setVisibility(View.VISIBLE);
                }
            }
        });
        mFindProductViewModel.getCategories().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                //dont add more categories to the sub menu if there are.
                //Though this is a workaround (because when there are new categories coming in, this
                // menu section needs to be updated)
                if (navigationView.getMenu().getItem(0).getSubMenu().size() < 2)
                    for (Category category : categories)
                        navigationView.getMenu().getItem(0).getSubMenu().add(R.id.categoriesGroup,
                                category.getId(),
                                Menu.NONE,
                                category.getName());
                //mark the check for selected category
                if (mFindProductViewModel.getCurrentCategory() == 0) {
                    mFindProductViewModel.setCurrentCategory(R.id.action_query_all_category);
                    navigationView.getMenu().findItem(R.id.action_query_all_category).setChecked(true);
                } else
                    navigationView.getMenu().findItem(mFindProductViewModel.getCurrentCategory()).setChecked(true);
            }
        });
        mFindProductViewModel.getLoadingState().observe(getViewLifecycleOwner(), new Observer<Contants.LoadingState>() {
            @Override
            public void onChanged(Contants.LoadingState loadingState) {
                if (loadingState == Contants.LoadingState.LOADING) {
                    pbLoadingProgress.setVisibility(View.VISIBLE);
                } else if (loadingState == Contants.LoadingState.SUCCESS) {
                    pbLoadingProgress.setVisibility(View.INVISIBLE);
                } else if (loadingState == Contants.LoadingState.FIRST_LOAD_ERROR)
                    pbLoadingProgress.setVisibility(View.INVISIBLE);
            }
        });
        return v;
    }

    private void connectViews(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rcvSearch);
        productsContainer = (RecyclerView) v.findViewById(R.id.productsContainer);
        tvNoProductFound = (TextView) v.findViewById(R.id.tvNoProductsFound);
        pbLoadingProgress = (ProgressBar) v.findViewById(R.id.pbLoadingBar);
        tvFindIntroduction = (TextView) v.findViewById(R.id.tvFindIntroduction);
        edtProductSearch = (EditText) v.findViewById(R.id.edtSearchProduct);
        historySearchContainer = (LinearLayout) v.findViewById(R.id.historySearchContainer);
        icClearSearch = (ImageView) v.findViewById(R.id.iconClearSearch);
        radioPlatformGroup = (RadioGroup) v.findViewById(R.id.radioPlatformGroup);
        icFilter = (ImageView) v.findViewById(R.id.icFilter);
        navigationView = (NavigationView) v.findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout) v.findViewById(R.id.drawerLayout);

        mHistorySearchAdapter = new SearchHistoryRecyclerViewAdapter(requireContext());
        mProductItemsAdapter = new ProductItemsAdapter(requireContext(), R.layout.product_item);

        edtProductSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (edtProductSearch.getText().toString().equals("")) {
                    historySearchContainer.setVisibility(View.VISIBLE);
                    icClearSearch.setVisibility(View.INVISIBLE);
                    radioPlatformGroup.setVisibility(View.GONE);
                    icFilter.setVisibility(View.GONE);
                    if (mFindProductViewModel.getRecentSearchs().getValue().size() == 0)
                        tvFindIntroduction.setVisibility(View.INVISIBLE);
                } else {
                    if (!edtProductSearch.getText().toString().startsWith("https://tiki.vn/") &&
                            !edtProductSearch.getText().toString().startsWith("https://shopee.vn/")) {
                        radioPlatformGroup.setVisibility(View.VISIBLE);
                        icFilter.setVisibility(View.VISIBLE);
                    }
                    historySearchContainer.setVisibility(View.INVISIBLE);
                    icClearSearch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtProductSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH &&
                        edtProductSearch.getText().toString().trim().length() != 0) {
                    handleQueryText(edtProductSearch.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
        //hide keyboard when edt loses focus and show when focus
        edtProductSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view.getId() == R.id.edtSearchProduct && !hasFocus) {
                    InputMethodManager imm = (InputMethodManager) requireActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                } else if (view.getId() == R.id.edtSearchProduct && hasFocus) {
                    InputMethodManager imm = (InputMethodManager)
                            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        icClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtProductSearch.setText("");
                edtProductSearch.requestFocus();
            }
        });

        //we dont want the recycler view to be scroll
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRecyclerView.setAdapter(mHistorySearchAdapter);
        mRecyclerView.setHasFixedSize(true);
        mHistorySearchAdapter.setListener(new SearchHistoryRecyclerViewAdapter.Listener() {
            @Override
            public void onRemoveHistoryItem(String searchToBeRemoved) {
                new DeleteRecentSearchAsyncTask(mRecentSearchDao).execute(searchToBeRemoved);
            }

            @Override
            public void onSearchItemClicked(String searchItem) {
                edtProductSearch.setText(searchItem);
                handleQueryText(searchItem);
            }
        });
        productsContainer.setLayoutManager(new LinearLayoutManager(requireActivity()));
        mProductItemsAdapter.setListener(new ProductItemsAdapter.Listener() {
            @Override
            public void onItemClicked(String url) {
                Bundle bundle = new Bundle();
                bundle.putString("productUrl", url);
                NavHostFragment.findNavController(FindProductsFragment.this)
                        .navigate(R.id.action_global_productItemFragment, bundle);
            }
        });
        productsContainer.setAdapter(mProductItemsAdapter);

        radioPlatformGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                String checkedPlatform = null;
                switch (checkedId) {
                    case R.id.radioAll:
                        checkedPlatform = "all";
                        break;
                    case R.id.radioTiki:
                        checkedPlatform = "tiki";
                        break;
                    case R.id.radioShopee:
                        checkedPlatform = "shopee";
                        break;
                }
                mFindProductViewModel.setPlatform(checkedPlatform);
            }
        });
        icFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int previouslySelectedCategoryId = mFindProductViewModel.getCurrentCategory();
                if (item.getItemId() != previouslySelectedCategoryId) {
                    //calling setCheckedItem to set checked item on the group does not work,
                    //maybe due to adding menu items dynamically
//                    navigationView.setCheckedItem(item);
                    navigationView.getMenu().findItem(previouslySelectedCategoryId).setChecked(false);
                    item.setChecked(true);
                    drawerLayout.closeDrawer(GravityCompat.END);
                    mFindProductViewModel.setCurrentCategory(item.getItemId());
                    mFindProductViewModel.setCategoryLiveData(item.getTitle().toString());
                }
                return true;
            }
        });
    }


    private void handleQueryText(String query) {
        edtProductSearch.clearFocus();
        if (query.startsWith("https://tiki.vn/") || query.startsWith("https://shopee.vn/")) {
            Bundle bundle = new Bundle();
            bundle.putString("productUrl", query);
            NavHostFragment.findNavController(FindProductsFragment.this)
                    .navigate(R.id.action_global_productItemFragment, bundle);
        } else {
            //call api with the query string to receive product.
            mFindProductViewModel.setQuery(query);
            //save the query to database
            RecentSearch recentSearch = new RecentSearch(query, String.valueOf(new Date().getTime()));
            new InsertRecentSearchAsyncTask(mRecentSearchDao).execute(recentSearch);
        }
    }

    //    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_search_for_real, menu);
//        MenuItem menuItem = menu.findItem(R.id.action_search);
//        // Get the SearchView
//        mSearchView = (SearchView) menuItem.getActionView();
//
//        mSearchView.setMaxWidth(Integer.MAX_VALUE);
//        //update the query in search view
//        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
//        mSearchView.setIconifiedByDefault(true);
//        mSearchView.setQueryHint(getString(R.string.find_guide));
//
//        //remove search icon
////        mSearchView.setIconified(false); // this is important to hide the search hint icon
////        ImageView icon = (ImageView) mSearchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
////        icon.setVisibility(View.GONE);
//
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                //close keyboard
////                InputMethodManager imm = (InputMethodManager) SearchableActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
////                //Find the currently focused view, so we can grab the correct window token from it.
////                View view = SearchableActivity.this.getCurrentFocus();
////                //If no view currently has focus, create a new one, just so we can grab a window token from it
////                if (view == null) {
////                    view = new View(SearchableActivity.this);
////                }
////                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//
//                //TODO: use the query to search your data somehow
//                //store the search to database
////                SearchDatabase searchDatabase = SearchDatabase.getInstance(getApplicationContext());
////                mRecentSearchDao = searchDatabase.recentSearchDao();
////                RecentSearch recentSearch = new RecentSearch(query, String.valueOf(new Date().getTime()));
////                new InsertRecentSearchAsyncTask(mRecentSearchDao).execute(recentSearch);
//                Bundle bundle = new Bundle();
//                bundle.putString("productUrl", query);
//                NavHostFragment.findNavController(FindProductsFragment.this)
//                        .navigate(R.id.action_global_productItemFragment, bundle);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//    }
    private static class DeleteRecentSearchAsyncTask extends AsyncTask<String, Void, Void> {
        private RecentSearchDao recentSearchDao;

        private DeleteRecentSearchAsyncTask(RecentSearchDao recentSearchDao) {
            this.recentSearchDao = recentSearchDao;
        }

        @Override
        protected Void doInBackground(String... recentSearchs) {
            recentSearchDao.deleteBySearchContent(recentSearchs[0]);
            return null;
        }
    }

    private static class InsertRecentSearchAsyncTask extends AsyncTask<RecentSearch, Void, Void> {
        private RecentSearchDao recentSearchDao;

        private InsertRecentSearchAsyncTask(RecentSearchDao recentSearchDao) {
            this.recentSearchDao = recentSearchDao;
        }

        @Override
        protected Void doInBackground(RecentSearch... recentSearchs) {
            recentSearchDao.insert(recentSearchs[0]);
            return null;
        }
    }
}