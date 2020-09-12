package com.tomlezmy.goolmathapp.game;

import java.util.List;

/**
 * This class holds user progress data for game levels
 */
public class ProgressResult {
    private String level;
    private int timesPlayed;
    private int highScore;
    private boolean isFinished;
    private List<Integer> weightList;
    private int categoryIndex;
    private int levelIndex;

    public ProgressResult(String level, int timesPlayed, int highScore, boolean isFinished, List<Integer> weightList, int categoryIndex, int levelIndex) {
        this.level = level;
        this.timesPlayed = timesPlayed;
        this.highScore = highScore;
        this.isFinished = isFinished;
        this.weightList = weightList;
        this.categoryIndex = categoryIndex;
        this.levelIndex = levelIndex;
    }

    public List<Integer> getWeightList() {
        return weightList;
    }

    public String getLevel() {
        return level;
    }

    public int getTimesPlayed() {
        return timesPlayed;
    }

    public int getHighScore() {
        return highScore;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setTimesPlayed(int timesPlayed) {
        this.timesPlayed = timesPlayed;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
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
}

