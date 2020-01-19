package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface AparatDAO {

    @Insert
    public void insertZdjecie(AparatEntity plik_aparatu);

}
