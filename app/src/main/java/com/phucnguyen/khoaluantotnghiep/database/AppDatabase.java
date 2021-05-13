package com.phucnguyen.khoaluantotnghiep.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.phucnguyen.khoaluantotnghiep.model.ProductItem;

import org.jetbrains.annotations.NotNull;

@Database(entities = {ProductItem.class}, version = AppDatabase.DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "app_database";

    private static AppDatabase instance;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE products " +
                    "ADD COLUMN priceDifference INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE products " +
                    "ADD COLUMN desiredPrice INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE products " +
                    "ADD COLUMN updateTime INTEGER NOT NULL DEFAULT 0");
        }
    };

    public abstract ProductItemDao getProductItemDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
