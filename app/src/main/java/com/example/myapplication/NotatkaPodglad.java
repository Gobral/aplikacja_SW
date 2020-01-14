package com.example.myapplication;

import java.util.Calendar;
import java.util.Date;

public class NotatkaPodglad {
    public String nazwa;
    public String data_dzien;
    public String data_godzina;
    public Date data_pelna;
    public int liczba_zdjec;
    public int liczba_filmow;
    public int liczba_nglosowychl;

    public NotatkaPodglad(String nazwa, Date data, int liczba_zdjec, int liczba_filmow, int liczba_nglosowychl){
        this.nazwa = nazwa;
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        this.data_pelna = data;
        this.data_dzien = cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.MONTH) + " " + cal.get(Calendar.YEAR);
        this.data_godzina = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
        this.liczba_filmow = liczba_filmow;
        this.liczba_zdjec = liczba_zdjec;
        this.liczba_nglosowychl = liczba_nglosowychl;
    }
}
