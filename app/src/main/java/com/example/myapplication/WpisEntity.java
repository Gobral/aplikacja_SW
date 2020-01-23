package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = NotatkaEntity.class,
        parentColumns = "nazwaNotatki",
        childColumns = "notatkaId",
        onDelete = CASCADE),
        primaryKeys = {"nazwaWpisu", "notatkaId"})
public class WpisEntity {
    @NonNull
    private String nazwaWpisu;
    @NonNull
    private String notatkaId;
    @NonNull
    private String tresc;

    public WpisEntity(String nazwaWpisu, String notatkaId, String tresc){
        this.nazwaWpisu = nazwaWpisu;
        this.notatkaId = notatkaId;
        this.tresc = tresc;
    }

    @NonNull
    public String getNazwaWpisu() {
        return nazwaWpisu;
    }

    @NonNull
    public String getTresc() {
        return tresc;
    }

    @NonNull
    public String getNotatkaId() {
        return notatkaId;
    }

    public void setNazwaWpisu(@NonNull String nazwaWpisu) {
        this.nazwaWpisu = nazwaWpisu;
    }

    public void setNotatkaId(@NonNull String notatkaId) {
        this.notatkaId = notatkaId;
    }
    public void setTresc(@NonNull String tresc) {
        this.tresc = tresc;
    }
}
