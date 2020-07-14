package com.tomlezmy.goolmathapp.game;

public class LevelValueLimits {
    private ValueLimit firstNumberLimit, secondNumberLimit;

    public ValueLimit getFirstNumberLimit() {
        return firstNumberLimit;
    }

    public void setFirstNumberLimit(int lower, int upper) {
        this.firstNumberLimit = new ValueRange(lower, upper);
    }

    public void setFirstNumberLimit(int[] array) {
        this.firstNumberLimit = new ValueArray(array);
    }

    public ValueLimit getSecondNumberLimit() {
        return secondNumberLimit;
    }

    public void setSecondNumberLimit(int lower, int upper) {
        this.secondNumberLimit = new ValueRange(lower, upper);
    }

    public void setSecondNumberLimit(int[] array) {
        this.secondNumberLimit = new ValueArray(array);
    }
}
