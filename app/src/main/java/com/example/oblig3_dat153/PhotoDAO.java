package com.example.oblig3_dat153;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PhotoDAO {

    // Data wrapper som kan observeres
    @Query("SELECT * from photoentry")
    LiveData<List<PhotoEntry>> getAll();

    @Insert
    void insert(PhotoEntry photoEntry);

    @Delete
    void delete(PhotoEntry photoEntry);
}
