package com.tomlezmy.goolmathapp.game;

public class ValueArray extends ValueLimit {
    private int[] values;

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

    @Override
    public int generateValue() {
        if (values.length == 1) {
            return values[0];
        }
        return values[rand.nextInt(values.length)];
    }
}
