package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity
public class NotatkaEntity{
    @NonNull
    @PrimaryKey
    private String nazwaNotatki;
    private Date dataDodania;
    private String zawartosc;
    public String getNazwaNotatki() {
        return nazwaNotatki;
    }
    public void setNazwaNotatki(String nazwaNotatki) {
        this.nazwaNotatki = nazwaNotatki;
    }
    public Date getDataDodania() {
        return dataDodania;
    }
    public void setDataDodania(Date dataDodania) {
        this.dataDodania = dataDodania;
    }
    public String getZawartosc() {
        return zawartosc;
    }
    public void setHoardAccessible(String zawartosc) {
        this.zawartosc = zawartosc;
    }
    public NotatkaEntity(String nazwaNotatki, Date dataDodania, String zawartosc) {
        this.nazwaNotatki = nazwaNotatki;
        this.dataDodania = dataDodania;
        this.zawartosc = zawartosc;
    }
}