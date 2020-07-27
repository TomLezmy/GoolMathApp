package com.tomlezmy.goolmathapp.game;

import java.io.Serializable;

public class CategoryProgressData implements Serializable {
    private int timesPlayed;
    private int maxScore;
    private boolean isOpen;

    public CategoryProgressData(boolean isOpen) {
        timesPlayed = 0;
        maxScore = 0;
        this.isOpen = isOpen;
    }

    public CategoryProgressData(int timesPlayed, int maxScore, boolean isOpen) {
        this.timesPlayed = timesPlayed;
        this.maxScore = maxScore;
        this.isOpen = isOpen;
    }

    public int getTimesPlayed() {
        return timesPlayed;
    }

    public void setTimesPlayed(int timesPlayed) {
        this.timesPlayed = timesPlayed;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
