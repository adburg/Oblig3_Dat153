package com.example.oblig3_dat153;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PhotoEntry {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name  = "name")
    public String name;

    @ColumnInfo(name  = "uri")
    public String url;

    public PhotoEntry() {}

    public PhotoEntry(String name, String url) {
        this.name = name;
        this.url = url;
    }

}
