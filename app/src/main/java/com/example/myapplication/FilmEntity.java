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
public class FilmEntity {

    @NonNull
    @PrimaryKey
    private String pathPliku;
    @NonNull
    private String notatkaId;
    @NonNull
    private String nazwaFilmud;

    public FilmEntity(String pathPliku, String notatkaId, String nazwaFilmud){
        this.pathPliku = pathPliku;
        this.notatkaId = notatkaId;
        this.nazwaFilmud = nazwaFilmud;
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
    public String getNazwaFilmud() {
        return nazwaFilmud;
    }

    public void setNotatkaId(@NonNull String notatkaId) {
        this.notatkaId = notatkaId;
    }

    public void setPathPliku(@NonNull String pathPliku) {
        this.pathPliku = pathPliku;
    }

    public void setNazwaFilmud(@NonNull String nazwaFilmud) {
        this.nazwaFilmud = nazwaFilmud;
    }
}
