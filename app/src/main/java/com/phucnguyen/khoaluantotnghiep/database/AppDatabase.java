package com.phucnguyen.khoaluantotnghiep.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.phucnguyen.khoaluantotnghiep.model.ProductItem;

@Database(entities = {ProductItem.class}, version = AppDatabase.DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "app_database";

    private static RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };
    private static AppDatabase instance;

    public abstract ProductItemDao getProductItemDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProductItemDao mProductItemDao;

        public PopulateDbAsyncTask(AppDatabase db) {
            mProductItemDao = db.getProductItemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            populateDatabaseWithProductItems(mProductItemDao);
            return null;
        }

        private void populateDatabaseWithProductItems(ProductItemDao productItemDao) {
            productItemDao.insertProduct(new ProductItem(
                    "36981456",
                    "Bình sữa PPSU Pigeon",
                    "10420",
                    "36981458",
                    5,
                    "10420",
                    "https://salt.tikicdn.com/cache/280x280/ts/product/ef/4d/f3/369bcc85a81d01e36bc6c909862dcf60.jpg",
                    63,
                    10000,
                    30
            ));
            productItemDao.insertProduct(new ProductItem(
                    "52657773",
                    "Bộ 3 bình trữ sữa mẹ cổ rộng Fatzbaby 150ml + tặng 5 zipper 10x15cm",
                    "10420",
                    "69334132",
                    4.5f,
                    "10420",
                    "https://salt.tikicdn.com/cache/280x280/ts/product/82/ee/6b/3ab45620748ba7a077b9563bb7b614f7.jpg",
                    91,
                    20000,
                    51
            ));
            productItemDao.insertProduct(new ProductItem(
                    "998273",
                    "Bình Sữa Nhựa Philips Avent (260ml)",
                    "10420",
                    "998275",
                    4.5f,
                    "10420",
                    "https://salt.tikicdn.com/cache/280x280/ts/product/6b/51/ec/94a69a53b334bb71484bc6cebe025060.jpg",
                    35,
                    30000,
                    0
            ));
            productItemDao.insertProduct(new ProductItem(
                    "14933754",
                    "Bình Sữa Thủy Tinh Cổ Thường G-240Ml Chuchu Baby (Box Type)",
                    "10420",
                    "14933755",
                    4.5f,
                    "10420",
                    "https://salt.tikicdn.com/cache/280x280/ts/product/dc/a4/d8/846bf4f9cdd6b379564d166946444088.jpg",
                    49,
                    1000000,
                    90
            ));
        }
    }
}
