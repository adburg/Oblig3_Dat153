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

    public PhotoEntry(String name, String
            url) {
        this.name = name;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
