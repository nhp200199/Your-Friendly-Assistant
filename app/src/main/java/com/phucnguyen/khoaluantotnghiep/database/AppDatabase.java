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

    private static AppDatabase instance;

    public abstract ProductItemDao getProductItemDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
