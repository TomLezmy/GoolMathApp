package com.tomlezmy.goolmathapp.game;

/**
 * This class is used to generate values from a array of values
 */
public class ValueArray extends ValueLimit {
    private int[] values;

    /**
     * Class constructor
     * @param values The values to pick from
     */
    public ValueArray(int[] values) {
        super();
        this.values = values;
    }

    public int[] getValues() {
        return values;
    }

    public void setValues(int[] values) {
        this.values = values;
    }

    /**
     * This method picks a random value from {@link #values}
     * @return The selected value
     */
    @Override
    public int generateValue() {
        if (values.length == 1) {
            return values[0];
        }
        return values[rand.nextInt(values.length)];
    }
}
