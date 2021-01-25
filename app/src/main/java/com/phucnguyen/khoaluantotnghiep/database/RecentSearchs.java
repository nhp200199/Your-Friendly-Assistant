package com.phucnguyen.khoaluantotnghiep.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recent_searchs")
public class RecentSearchs {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "search_content")
    public String searchContent;
    @ColumnInfo(name = "timestamps")
    public String timestamps;

    public RecentSearchs(String searchContent, String timestamps) {
        this.searchContent = searchContent;
        this.timestamps = timestamps;
    }
}
