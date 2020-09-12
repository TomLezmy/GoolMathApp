package com.tomlezmy.goolmathapp.model;

import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Dao;

import java.util.List;

/**
 * This interface is used to access the game records database
 */
@Dao
public interface GameRecordDao {

    @Query("SELECT * FROM gamerecord WHERE category_index=:categoryIndex AND level_index=:levelIndex")
    List<GameRecord> getAllRecordsFromLevel(int categoryIndex, int levelIndex);

    @Insert
    void insertRecord(GameRecord gameRecord);
}
