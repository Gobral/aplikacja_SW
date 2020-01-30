package com.example.myapplication;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.myapplication.NotatkaEntity;

@Database(entities = {NotatkaEntity.class, AparatEntity.class, FilmEntity.class, WpisEntity.class, GlosoweEntity.class, ParkingEntity.class}, version = 1, exportSchema = false)
@TypeConverters({MyTypeConverters.class})
public abstract class NotatkiDatabase extends RoomDatabase {
    public abstract NotatkiDAO notatkiDAO();
    public abstract AparatDAO aparatDAO();
    public abstract FilmDAO filmDAO();
    public abstract WpisyDAO wpisyDAO();
    public abstract GlosoweDAO glosoweDAO();
    public abstract ParkingDAO parkingDAO();
}
