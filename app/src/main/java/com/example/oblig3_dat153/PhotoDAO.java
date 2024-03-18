package com.example.oblig3_dat153;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PhotoDAO {
    @Query("SELECT * from photoentry")
    List<PhotoEntry> getAll();

    @Insert
    void insert(PhotoEntry photoEntry);

    @Delete
    void delete(PhotoEntry photoEntry);
}
