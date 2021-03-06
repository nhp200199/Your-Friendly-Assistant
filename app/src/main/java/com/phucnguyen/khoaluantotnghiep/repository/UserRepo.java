package com.phucnguyen.khoaluantotnghiep.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.JsonObject;
import com.phucnguyen.khoaluantotnghiep.database.AppDatabase;
import com.phucnguyen.khoaluantotnghiep.database.ProductItemDao;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.model.User;
import com.phucnguyen.khoaluantotnghiep.model.response.UserAccountResponse;
import com.phucnguyen.khoaluantotnghiep.service.RetrofitInstance;
import com.phucnguyen.khoaluantotnghiep.service.UserService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.phucnguyen.khoaluantotnghiep.utils.Contants.*;
import static com.phucnguyen.khoaluantotnghiep.utils.Contants.UserLoadingState.*;

public class UserRepo {
    private MutableLiveData<User> user = new MutableLiveData<User>();
    private LiveData<List<ProductItem>> userTrackedProducts;
    private MutableLiveData<UserLoadingState> userLoadingState = new MutableLiveData<UserLoadingState>();
    private MutableLiveData<UserActionState> userActionState = new MutableLiveData<UserActionState>();
    private MutableLiveData<String> trackedProductFilter = new MutableLiveData<String>("Thêm gần đây");
    private UserService service;
    private ProductItemDao productItemsDao;
    private Context mContext;

    public UserRepo(Context context) {
        service = RetrofitInstance.getInstance().create(UserService.class);
        productItemsDao = AppDatabase.getInstance(context)
                .getProductItemDao();
        mContext = context;
        user = new MutableLiveData<User>();
        userTrackedProducts = Transformations.switchMap(trackedProductFilter,
                orderBy -> {
                    switch (orderBy){
                        case "Thêm gần đây":
                            return productItemsDao.getAllProductsOrderedByTimeCreated();
                        case "Thời gian cập nhật giá":
                            return productItemsDao.getAllProductsOrderedByUpdateTime();
                        default: return null;
                    }
                });
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
                            tikiItem.getItem().setDesiredPrice(tikiItem.getNotifyWhenPriceLt());
                            tikiItem.getItem().setUpdate(tikiItem.getUpdate());
                            tikiItem.getItem().setCreate(tikiItem.getCreate());

                            combineTrackedProducts.add(tikiItem.getItem());
                        }
                        for (User.TrackedItem shopeeItem : user.getTrackedShopeeProducts()) {
                            shopeeItem.getItem().setPlatform("shopee");
                            shopeeItem.getItem().setDesiredPrice(shopeeItem.getNotifyWhenPriceLt());
                            shopeeItem.getItem().setUpdate(shopeeItem.getUpdate());
                            shopeeItem.getItem().setCreate(shopeeItem.getCreate());

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

    public void deleteAllProductsFromDatabase() {
        new DeleteDbAsyncTask(AppDatabase.getInstance(mContext))
                .execute();
    }

    public void trackProduct(ProductItem productItem, int desiredPrice, String authString) {
        userActionState.setValue(UserActionState.PROCESSING);

        Map<String, String> productInforMap = new HashMap<String, String>();
        productInforMap.put("itemId", productItem.getId());
        productInforMap.put("platform", productItem.getPlatform());
        productInforMap.put("notifyWhenPriceLt", String.valueOf(desiredPrice));

        service.trackProduct(productInforMap, authString)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            productItem.setCreate(Calendar.getInstance().getTimeInMillis());
                            new InsertDbAsyncTask(AppDatabase.getInstance(mContext))
                                    .execute(productItem);
                            userActionState.setValue(UserActionState.DONE);
                            userActionState.setValue(UserActionState.NONE);
                        } else {
                            userActionState.setValue(UserActionState.NOT_AUTHORIZED);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        userActionState.postValue(UserActionState.NETWORK_ERROR);
                    }
                });
    }

    public void deleteTrackedProduct(String productId, String platform, String authString) {
        userActionState.setValue(UserActionState.PROCESSING);

        service.deleteTrackedProduct(productId, platform, authString)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        //here, we just consider 2 situations:
                        //1. the request is successfully executed
                        //2. the request fail due to user's access token expired
                        //More situations should be further considered
                        if (response.isSuccessful()) {
                            new DeleteOneItemAsyncTask(AppDatabase.getInstance(mContext))
                                    .execute(productId, platform);
                            userActionState.setValue(UserActionState.DONE);
                            userActionState.setValue(UserActionState.NONE);
                        } else {
                            userActionState.setValue(UserActionState.NOT_AUTHORIZED);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        userActionState.postValue(UserActionState.NETWORK_ERROR);
                    }
                });
    }

    public MutableLiveData<UserLoadingState> getUserLoadingState() {
        return userLoadingState;
    }

    public LiveData<List<ProductItem>> getUserTrackedProducts() {
        return userTrackedProducts;
    }

    public MutableLiveData<String> getTrackedProductFilter() {
        return trackedProductFilter;
    }

    public void setTrackedProductFilter(String orderRequest) {
        trackedProductFilter.setValue(orderRequest);
    }

    public MutableLiveData<UserActionState> getUserActionState() {
        return userActionState;
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

    private static class InsertDbAsyncTask extends AsyncTask<ProductItem, Void, Void> {
        private ProductItemDao mProductItemDao;

        public InsertDbAsyncTask(AppDatabase db) {
            mProductItemDao = db.getProductItemDao();
        }

        @Override
        protected Void doInBackground(ProductItem... items) {
            mProductItemDao.insertProduct(items[0]);
            return null;
        }
    }

    private static class DeleteOneItemAsyncTask extends AsyncTask<String, Void, Void> {
        private ProductItemDao mProductItemDao;

        public DeleteOneItemAsyncTask(AppDatabase db) {
            mProductItemDao = db.getProductItemDao();
        }

        @Override
        protected Void doInBackground(String... params) {
            mProductItemDao.deleteOneTrackedProduct(params[0], params[1]);
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
