package com.example.oblig3_dat153;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PhotoEntry.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PhotoDAO photoDAO();
}
