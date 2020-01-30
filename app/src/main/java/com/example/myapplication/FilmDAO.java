package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FilmDAO {

    @Insert
    public void insertFilm(FilmEntity plik_aparatu);

    @Query("SELECT * FROM filmentity WHERE notatkaId = :nazwa")
    LiveData<List<FilmEntity>> findPowiazane(String nazwa);

    @Query("SELECT * FROM filmentity WHERE  pathPliku = :path")
    public FilmEntity loadFilmByPath(String path);

    @Delete
    public void deleteAparat(FilmEntity aparatEntity);
}
