package com.phucnguyen.khoaluantotnghiep.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecentSearchDao {
    @Insert(onConflict = REPLACE)
    void insert(RecentSearch recentSearch);

    @Delete
    void delete(RecentSearch recentSearch);

    @Query("SELECT * FROM recent_searchs " +
            "ORDER BY timestamps DESC " +
            "LIMIT 5")
    LiveData<List<RecentSearch>> getRecentSearchsForHistory();
}
