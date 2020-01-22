package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface WpisyDAO {

    @Insert
    public void insertWpis(WpisEntity nowy_wpis);

    @Query("SELECT * FROM wpisentity WHERE notatkaId = :nazwa")
    LiveData<List<WpisEntity>> findPowiazane(String nazwa);

    @Query("SELECT * FROM wpisentity WHERE timeDodania = :data")
    public WpisEntity loadWpisByData(Long data);

}
