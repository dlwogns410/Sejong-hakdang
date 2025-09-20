package com.pProject.ganada;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Voca.class}, version = 1, exportSchema = false)
public abstract class VocaDB extends RoomDatabase {

    private static VocaDB INSTANCE;

    public abstract VocaDao vocaDao();

    public static VocaDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), VocaDB.class, "voca.db")
                    .build();

        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
