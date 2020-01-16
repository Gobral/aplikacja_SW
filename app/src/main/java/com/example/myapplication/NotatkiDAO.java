package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotatkiDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertNotatki(List<NotatkaEntity> notatki);

    @Insert
    public void insertNatatka(NotatkaEntity notatka);

    @Update
    public void updateNotatki(NotatkaEntity... notatka);

    @Update
    public void updateNotatka(NotatkaEntity notatka);

    @Delete
    public void deleteNotatka(NotatkaEntity notatka);

    @Query("DELETE FROM notatkaentity")
    public void deleteAllNotatki();

    @Query("SELECT * FROM notatkaentity")
    public List<NotatkaEntity> loadAllNotatki();

    @Query("SELECT * FROM notatkaentity WHERE nazwaNotatki = :nazwaNotatki")
    public NotatkaEntity loadNotatkaByName(String nazwaNotatki);

    @Query("SELECT * FROM notatkaentity WHERE nazwaNotatki IN(:nazwyNotatkek)")
    public List<NotatkaEntity> findByNazwy(String[] nazwyNotatkek);

    @Query("SELECT * FROM notatkaentity")
    LiveData<List<NotatkaEntity>> findAll();

    @Query("UPDATE notatkaentity SET zawartosc=:nowa_zawartosc WHERE nazwaNotatki = :id")
    public void updateZawartosc(String nowa_zawartosc, String id);
}
