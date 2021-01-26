package com.phucnguyen.khoaluantotnghiep.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {RecentSearch.class}, version = SearchDatabase.DATABASE_VERSION)
public abstract class SearchDatabase extends RoomDatabase {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "recent_search_database";

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            String query = "CREATE UNIQUE INDEX index_recent_searchs_search_content " +
                    "ON recent_searchs(search_content)";
            database.execSQL(query);
        }
    };

    private static SearchDatabase instance;

    public abstract RecentSearchDao recentSearchDao();

    public static synchronized SearchDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    SearchDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return instance;
    }

}
