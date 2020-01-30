package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = NotatkaEntity.class,
        parentColumns = "nazwaNotatki",
        childColumns = "notatkaId",
        onDelete = CASCADE))
public class AparatEntity{
    @NonNull
    @PrimaryKey
    private String pathPliku;
    @NonNull
    private String notatkaId;
    @NonNull
    private String nazwaZdjecia;

    public AparatEntity(String pathPliku, String notatkaId, String nazwaZdjecia){
        this.pathPliku = pathPliku;
        this.notatkaId = notatkaId;
        this.nazwaZdjecia = nazwaZdjecia;
    }

    @NonNull
    public String getNotatkaId() {
        return notatkaId;
    }

    @NonNull
    public String getPathPliku() {
        return pathPliku;
    }

    @NonNull
    public String getNazwaZdjecia() {
        return nazwaZdjecia;
    }

    public void setNotatkaId(@NonNull String notatkaId) {
        this.notatkaId = notatkaId;
    }

    public void setPathPliku(@NonNull String pathPliku) {
        this.pathPliku = pathPliku;
    }

    public void setNazwaZdjecia(@NonNull String nazwaZdjecia) {
        this.nazwaZdjecia = nazwaZdjecia;
    }
}
