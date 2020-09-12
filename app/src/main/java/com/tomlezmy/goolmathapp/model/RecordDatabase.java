package com.tomlezmy.goolmathapp.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * The database class, this class holds the game records database
 */
@Database(entities = {GameRecord.class}, version = 1)
public abstract class RecordDatabase extends RoomDatabase {
    public abstract GameRecordDao gameRecordDao();
    private static RecordDatabase instance;

    public static RecordDatabase getInstance(Context context) {
        if (instance == null) {
            instance =  Room.databaseBuilder(context, RecordDatabase.class, "game_records").build();
        }
        return instance;
    }
}
