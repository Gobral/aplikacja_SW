package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface WpisyDAO {

    @Insert
    public void insertWpis(WpisEntity nowy_wpis);

    @Update
    public void updateWpis(WpisEntity wpis);

    @Query("SELECT * FROM wpisentity WHERE notatkaId = :nazwa")
    LiveData<List<WpisEntity>> findPowiazane(String nazwa);

    @Query("SELECT * FROM wpisentity WHERE nazwaWpisu = :nazwaWpisu AND notatkaId = :nazwaNotatki")
    public WpisEntity loadWpisByNazwy(String nazwaWpisu, String nazwaNotatki);

}
