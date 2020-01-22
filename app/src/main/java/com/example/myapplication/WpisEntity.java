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
public class WpisEntity {
    @NonNull
    @PrimaryKey
    private Long timeDodania;
    @NonNull
    private String notatkaId;
    @NonNull
    private String tresc;

    public WpisEntity(Long timeDodania, String notatkaId, String tresc){
        this.timeDodania = timeDodania;
        this.notatkaId = notatkaId;
        this.tresc = tresc;
    }

    @NonNull
    public Long getTimeDodania() {
        return timeDodania;
    }

    @NonNull
    public String getTresc() {
        return tresc;
    }

    @NonNull
    public String getNotatkaId() {
        return notatkaId;
    }
    public void setTimeDodania(@NonNull Long timeDodania){
        this.timeDodania = timeDodania;
    }

    public void setNotatkaId(@NonNull String notatkaId) {
        this.notatkaId = notatkaId;
    }
    public void setTresc(@NonNull String tresc) {
        this.tresc = tresc;
    }
}
