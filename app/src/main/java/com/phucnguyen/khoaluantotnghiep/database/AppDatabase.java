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
            productItemDao.insertProduct(new ProductItem(
                    "14933755",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933756",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933757",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933758",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933759",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933760",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933761",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933762",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933763",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933764",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933765",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933766",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933767",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933768",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933769",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933770",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933771",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933772",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933773",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933774",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933775",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933776",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933777",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933778",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933779",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933780",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933781",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933782",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933783",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933784",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933785",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933786",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933787",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933788",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933789",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933790",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933791",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933792",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933793",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933794",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933795",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933796",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933797",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933798",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933799",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933800",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933801",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933802",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933803",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933804",
                    "Bình Sữa Thủy Tinh Cổ Thường G-240Ml Chuchu Baby (Box Type)",
                    "10420",
                    "14933755",
                    4.5f,
                    "10420",
                    "https://salt.tikicdn.com/cache/280x280/ts/product/dc/a4/d8/846bf4f9cdd6b379564d166946444088.jpg",
                    49,
                    1000000,
                    90
            ));productItemDao.insertProduct(new ProductItem(
                    "14933805",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933806",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933807",
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
            productItemDao.insertProduct(new ProductItem(
                    "149337808",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933809",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933810",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933811",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933812",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933813",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933814",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933815",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933816",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933817",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933818",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933819",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933820",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933821",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933822",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933823",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933824",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933825",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933826",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933827",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933828",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933829",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933830",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933831",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933832",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933833",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933834",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933835",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933836",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933837",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933838",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933839",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933840",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933841",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933842",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933843",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933844",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933845",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933846",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933847",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933848",
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

            productItemDao.insertProduct(new ProductItem(
                    "14933849",
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
            productItemDao.insertProduct(new ProductItem(
                    "14933850",
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
