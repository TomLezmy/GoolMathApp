package com.tomlezmy.goolmathapp.model;

import android.text.format.DateFormat;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * This class holds the data for entities in the game records database
 */
@Entity
public class GameRecord {
    @PrimaryKey
    private long timeStamp;

    @ColumnInfo(name = "category_index")
    private int categoryIndex;

    @ColumnInfo(name = "level_index")
    private int levelIndex;

    @ColumnInfo(name = "correct_answers")
    private int correctAnswers;

    public GameRecord(long timeStamp, int categoryIndex, int levelIndex, int correctAnswers) {
        this.timeStamp = timeStamp;
        this.categoryIndex = categoryIndex;
        this.levelIndex = levelIndex;
        this.correctAnswers = correctAnswers;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getCategoryIndex() {
        return categoryIndex;
    }

    public void setCategoryIndex(int categoryIndex) {
        this.categoryIndex = categoryIndex;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    @Override
    public String toString() {
        return "GameRecord{" +
                "date=" + DateFormat.format("dd-MM-yyyy HH:mm:ss",timeStamp) +
                ", correctAnswers='" + correctAnswers + '\'' +
                '}';
    }
}
