package com.tomlezmy.goolmathapp.game;

public class LevelValueFractionLimits extends LevelValueLimits {
    private ValueLimit thirdNumberLimit, fourthNumberLimit;
    private int multiplierLimit;

    public int getMultiplierLimit() {
        return multiplierLimit;
    }

    public void setMultiplierLimit(int multiplierLimit) {
        this.multiplierLimit = multiplierLimit;
    }

    public ValueLimit getThirdNumberLimit() {
        return thirdNumberLimit;
    }

    public void setThirdNumberLimit(int lower, int upper) {
        this.thirdNumberLimit = new ValueRange(lower, upper);
    }

    public void setThirdNumberLimit(int[] array) {
        this.thirdNumberLimit = new ValueArray(array);
    }

    public ValueLimit getFourthNumberLimit() {
        return fourthNumberLimit;
    }

    public void setFourthNumberLimit(int lower, int upper) {
        this.fourthNumberLimit = new ValueRange(lower, upper);
    }

    public void setFourthNumberLimit(int[] array) {
        this.fourthNumberLimit = new ValueArray(array);
    }
}
