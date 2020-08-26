package com.tomlezmy.goolmathapp.game;

/**
 * This class holds the value limits and weight of a sub level
 */
public class LevelValueLimitProbabilities {
    private LevelValueLimits levelValueLimits;
    private int probabilityWeight;

    /**
     * Class constructor
     * @param levelValueLimits The value limits of the current sub level
     * @param probabilityWeight The weight of the sub level in the weight file
     */
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
