package com.tomlezmy.goolmathapp.model;

public class ProgressResult {
    private String level;
    private int timesPlayed;
    private int highScore;

    public ProgressResult(String level, int timesPlayed, int highScore) {
        this.level = level;
        this.timesPlayed = timesPlayed;
        this.highScore = highScore;
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

