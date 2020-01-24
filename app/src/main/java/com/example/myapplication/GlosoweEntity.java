package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = NotatkaEntity.class,
        parentColumns = "nazwaNotatki",
        childColumns = "notatkaId",
        onDelete = CASCADE),
        primaryKeys = {"nazwaNagrania", "notatkaId"})
public class GlosoweEntity {

    @NonNull
    private String nazwaNagrania;
    @NonNull
    private String notatkaId;
    @NonNull
    private String path;

    public GlosoweEntity(String nazwaNagrania, String notatkaId, String path){
        this.nazwaNagrania = nazwaNagrania;
        this.notatkaId =notatkaId;
        this.path = path;
    }

    public void setNotatkaId(@NonNull String notatkaId) {
        this.notatkaId = notatkaId;
    }

    public void setNazwaNagrania(@NonNull String nazwaNagrania) {
        this.nazwaNagrania = nazwaNagrania;
    }

    public void setPath(@NonNull String path) {
        this.path = path;
    }

    @NonNull
    public String getNotatkaId() {
        return notatkaId;
    }

    @NonNull
    public String getNazwaNagrania() {
        return nazwaNagrania;
    }

    @NonNull
    public String getPath() {
        return path;
    }
}
