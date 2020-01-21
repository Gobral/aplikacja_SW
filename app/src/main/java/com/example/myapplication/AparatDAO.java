package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AparatDAO {

    @Insert
    public void insertZdjecie(AparatEntity plik_aparatu);

    @Query("SELECT * FROM aparatentity WHERE notatkaId = :nazwa")
    LiveData<List<AparatEntity>> findPowiazane(String nazwa);

    @Query("SELECT * FROM aparatentity WHERE  pathPliku = :path")
    public AparatEntity loadAparatByPath(String path);
}
