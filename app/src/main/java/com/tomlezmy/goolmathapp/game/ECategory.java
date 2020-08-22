package com.tomlezmy.goolmathapp.game;

public enum ECategory {
    ADDITION(2), SUBTRACTION(3),
    MULTIPLICATION(5), DIVISION(4),
    FRACTIONS(6), PERCENTS(8), DECIMALS(6);


    private int numberOfLevels;

    ECategory(int numberOfLevels) {
        this.numberOfLevels = numberOfLevels;
    }

    public int getNumberOfLevels() {
        return numberOfLevels;
    }
}
