package com.tomlezmy.goolmathapp.game;

import java.util.List;

public class ProgressResult {
    private String level;
    private int timesPlayed;
    private int highScore;
    private boolean isFinished;
    private List<Integer> weightList;

    public ProgressResult(String level, int timesPlayed, int highScore, boolean isFinished, List<Integer> weightList) {
        this.level = level;
        this.timesPlayed = timesPlayed;
        this.highScore = highScore;
        this.isFinished = isFinished;
        this.weightList = weightList;
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
}

