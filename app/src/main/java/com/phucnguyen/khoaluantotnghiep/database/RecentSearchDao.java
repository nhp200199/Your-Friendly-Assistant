package com.phucnguyen.khoaluantotnghiep.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecentSearchDao {
    @Insert
    void insert(RecentSearchs recentSearchs);

    @Delete
    void delete(RecentSearchs recentSearchs);

    @Query("SELECT * FROM recent_searchs " +
            "ORDER BY timestamps DESC " +
            "LIMIT 5")
    List<RecentSearchs> getRecentSearchsForHistory();
}
