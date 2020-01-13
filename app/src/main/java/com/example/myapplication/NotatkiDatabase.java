package com.example.myapplication;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.myapplication.NotatkaEntity;

@Database(entities = {NotatkaEntity.class}, version = 1)
@TypeConverters({MyTypeConverters.class})
public abstract class NotatkiDatabase extends RoomDatabase {
    public abstract NotatkiDAO notatkiDAO();
}
