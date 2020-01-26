package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class ParkingEntity {
    @NonNull
    @PrimaryKey
    private String nazwaParkingu;
    private Double lattitude;
    private Double longitude;

    public ParkingEntity(String nazwaParkingu, Double lattitude, Double longitude){
        this.nazwaParkingu = nazwaParkingu;
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    public void setNazwaParkingu(@NonNull String nazwaParkingu) {
        this.nazwaParkingu = nazwaParkingu;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLattitude() {
        return lattitude;
    }

    @NonNull
    public String getNazwaParkingu() {
        return nazwaParkingu;
    }
}
