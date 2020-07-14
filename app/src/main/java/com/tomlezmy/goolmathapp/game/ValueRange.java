package com.tomlezmy.goolmathapp.game;

public class ValueRange extends ValueLimit{
    private int lower, upper;

    public ValueRange(int lower, int upper) {
        super();
        this.lower = lower;
        this.upper = upper;
    }

    public int getLower() {
        return lower;
    }

    public void setLower(int lower) {
        this.lower = lower;
    }

    public int getUpper() {
        return upper;
    }

    public void setUpper(int upper) {
        this.upper = upper;
    }


    @Override
    public int generateValue() {
        return rand.nextInt(upper + 1 - lower) + lower;
    }
}
