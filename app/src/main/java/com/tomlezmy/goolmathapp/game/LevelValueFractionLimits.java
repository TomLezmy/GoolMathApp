package com.tomlezmy.goolmathapp.game;

/**
 * This class holds the value limits of the numbers in a question and additional data for fraction questions
 */
public class LevelValueFractionLimits extends LevelValueLimits {
    private int multiplierLimit;

    public int getMultiplierLimit() {
        return multiplierLimit;
    }

    public void setMultiplierLimit(int multiplierLimit) {
        this.multiplierLimit = multiplierLimit;
    }
}
