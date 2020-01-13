package com.example.myapplication;

import android.content.Context;

import androidx.room.Room;

public class NotatkaDatabaseAccessor {
    private static NotatkiDatabase NotatkaDatabaseInstance;
    private static final String NOTATKI_DB_NAME = "notatki_db";
    private NotatkaDatabaseAccessor() {}
    public static NotatkiDatabase getInstance(Context context) {
        if (NotatkaDatabaseInstance == null) {
            NotatkaDatabaseInstance = Room.databaseBuilder(context,
                    NotatkiDatabase.class, NOTATKI_DB_NAME).build();
        }
        return NotatkaDatabaseInstance;
    }
}
