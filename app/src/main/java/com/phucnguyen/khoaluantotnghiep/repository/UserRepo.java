package com.phucnguyen.khoaluantotnghiep.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.phucnguyen.khoaluantotnghiep.database.AppDatabase;
import com.phucnguyen.khoaluantotnghiep.database.ProductItemDao;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.model.User;
import com.phucnguyen.khoaluantotnghiep.model.response.UserAccountResponse;
import com.phucnguyen.khoaluantotnghiep.service.RetrofitInstance;
import com.phucnguyen.khoaluantotnghiep.service.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.phucnguyen.khoaluantotnghiep.utils.Contants.*;
import static com.phucnguyen.khoaluantotnghiep.utils.Contants.UserLoadingState.*;

public class UserRepo {
    private MutableLiveData<User> user = new MutableLiveData<User>();
    private LiveData<List<ProductItem>> userTrackedProducts;
    private MutableLiveData<UserLoadingState> userLoadingState = new MutableLiveData<UserLoadingState>();
    private UserService service;
    private ProductItemDao productItemsService;
    private Context mContext;

    public UserRepo(Context context) {
        service = RetrofitInstance.getInstance().create(UserService.class);
        productItemsService = AppDatabase.getInstance(context)
                .getProductItemDao();
        mContext = context;
        user = new MutableLiveData<User>();
        userTrackedProducts = productItemsService.getAllProducts();
    }

    public LiveData<User> getUserByTokenId(String tokenId) {
        if (tokenId == null) {
            user.postValue(null);
            return user;
        }
        userLoadingState.postValue(LOADING);
        service.getUserAccount("Bearer " + tokenId)
                .enqueue(new Callback<UserAccountResponse>() {
                    @Override
                    public void onResponse(Call<UserAccountResponse> call, Response<UserAccountResponse> response) {
                        UserAccountResponse userAccountResponse = response.body();
                        if (userAccountResponse != null) {
                            userLoadingState.postValue(SUCCESS);
                            user.postValue(response.body().getUser());

                            populateDatabaseWithTrackedItems(response.body().getUser());
                        } else {
                            userLoadingState.setValue(NOT_AUTHORIZED);
                        }
                    }

                    private void populateDatabaseWithTrackedItems(User user) {
                        ArrayList<ProductItem> combineTrackedProducts = new ArrayList<ProductItem>();
                        for (User.TrackedItem tikiItem : user.getTrackedTikiProducts()) {
                            tikiItem.getItem().setPlatform("tiki");
                            combineTrackedProducts.add(tikiItem.getItem());
                        }
                        for (User.TrackedItem shopeeItem : user.getTrackedShopeeProducts()) {
                            shopeeItem.getItem().setPlatform("shopee");
                            combineTrackedProducts.add(shopeeItem.getItem());
                        }
                        PopulateDbAsyncTask populateDbAsyncTask = new PopulateDbAsyncTask(AppDatabase.getInstance(mContext));
                        populateDbAsyncTask.execute(combineTrackedProducts);
                    }

                    @Override
                    public void onFailure(Call<UserAccountResponse> call, Throwable t) {
                        userLoadingState.postValue(NETWORK_ERROR);
                        user.postValue(null);
                    }
                });
        return user;
    }

    public void deleteAllProductsFromDatabase(){
        new DeleteDbAsyncTask(AppDatabase.getInstance(mContext))
                .execute();
    }

    public MutableLiveData<UserLoadingState> getUserLoadingState() {
        return userLoadingState;
    }

    public LiveData<List<ProductItem>> getUserTrackedProducts() {
        return userTrackedProducts;
    }

    public UserService getService() {
        return service;
    }


    private static class PopulateDbAsyncTask extends AsyncTask<List<ProductItem>, Void, Void> {
        private ProductItemDao mProductItemDao;

        public PopulateDbAsyncTask(AppDatabase db) {
            mProductItemDao = db.getProductItemDao();
        }

        @Override
        protected Void doInBackground(List<ProductItem>... lists) {
            mProductItemDao.insertUserTrackedProducts(lists[0]);
            return null;
        }
    }

    private static class DeleteDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProductItemDao mProductItemDao;

        public DeleteDbAsyncTask(AppDatabase db) {
            mProductItemDao = db.getProductItemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mProductItemDao.deleteAllProducts();
            return null;
        }
    }

}
