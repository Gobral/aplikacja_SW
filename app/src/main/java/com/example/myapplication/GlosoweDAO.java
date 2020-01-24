package com.example.myapplication;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GlosoweDAO {

    @Insert
    public void insertNagranie(GlosoweEntity plik_glosowy);

    @Update
    public void updateNagranie(GlosoweEntity glosowa);

    @Query("SELECT * FROM glosoweentity WHERE notatkaId = :nazwa")
    LiveData<List<GlosoweEntity>> findPowiazane(String nazwa);

    @Query("SELECT * FROM glosoweentity WHERE nazwaNagrania = :nazwaGlosowej AND notatkaId = :nazwaNotatki")
    public GlosoweEntity loadGlosoweByNazwy(String nazwaGlosowej, String nazwaNotatki);
}
