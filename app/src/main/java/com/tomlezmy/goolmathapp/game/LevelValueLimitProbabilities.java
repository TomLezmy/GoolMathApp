package com.tomlezmy.goolmathapp.game;

public class LevelValueLimitProbabilities {
    private LevelValueLimits levelValueLimits;
    private int probabilityWeight;

    public LevelValueLimitProbabilities(LevelValueLimits levelValueLimits, int probabilityWeight) {
        this.levelValueLimits = levelValueLimits;
        this.probabilityWeight = probabilityWeight;
    }

    public LevelValueLimits getLevelValueLimits() {
        return levelValueLimits;
    }

    public void setLevelValueLimits(LevelValueLimits levelValueLimits) {
        this.levelValueLimits = levelValueLimits;
    }

    public int getProbabilityWeight() {
        return probabilityWeight;
    }

    public void setProbabilityWeight(int probabilityWeight) {
        this.probabilityWeight = probabilityWeight;
    }
}
