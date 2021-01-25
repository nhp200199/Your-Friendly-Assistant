package com.phucnguyen.khoaluantotnghiep.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RecentSearchs.class}, version = SearchDatabase.DATABASE_VERSION)
public abstract class SearchDatabase extends RoomDatabase {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "recent_search_database";
    private static SearchDatabase instance;

    public abstract RecentSearchDao recentSearchDao();

    public static synchronized SearchDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    SearchDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
