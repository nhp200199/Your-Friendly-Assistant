package com.phucnguyen.khoaluantotnghiep.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "recent_searchs", indices = {@Index(value = "search_content", unique = true)})
public class RecentSearch {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "search_content")
    public String searchContent;
    @ColumnInfo(name = "timestamps")
    public String timestamps;

    public RecentSearch(String searchContent, String timestamps) {
        this.searchContent = searchContent;
        this.timestamps = timestamps;
    }
}
