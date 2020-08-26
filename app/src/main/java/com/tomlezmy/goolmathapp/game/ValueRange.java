package com.tomlezmy.goolmathapp.game;

/**
 * This class is used to generate values between a range
 */
public class ValueRange extends ValueLimit{
    private int lower, upper;

    /**
     * Class constructor
     * @param lower The bottom of the range (inclusive)
     * @param upper The top of the range (inclusive)
     */
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

    /**
     * This method generates a random value between {@link #lower} and {@link #upper} inclusively
     * @return The generated value
     */
    @Override
    public int generateValue() {
        return rand.nextInt(upper + 1 - lower) + lower;
    }
}
