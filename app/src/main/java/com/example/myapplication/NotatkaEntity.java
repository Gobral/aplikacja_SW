package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
public class NotatkaEntity{
    @NonNull
    @PrimaryKey
    private String nazwaNotatki;
    private Date dataDodania;
    private Double lattitude;
    private Double longitude;
    private Boolean widocznosc;
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

    public Double getLattitude() {
        return lattitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Boolean getWidocznosc() {
        return widocznosc;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setWidocznosc(Boolean widocznosc) {
        this.widocznosc = widocznosc;
    }

    public NotatkaEntity(String nazwaNotatki, Date dataDodania, Double lattitude, Double longitude, Boolean widocznosc) {
        this.nazwaNotatki = nazwaNotatki;
        this.dataDodania = dataDodania;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.widocznosc = widocznosc;
    }
}
