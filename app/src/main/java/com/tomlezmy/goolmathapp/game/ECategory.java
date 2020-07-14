package com.tomlezmy.goolmathapp.game;

public enum ECategory {
    ADDITION(2), SUBTRACTION(3),
    MULTIPLICATION(5), DIVISION(5),
    FRACTIONS(10), PERCENTS(8), DECIMALS(3);


    private int numberOfLevels;

    ECategory(int numberOfLevels) {
        this.numberOfLevels = numberOfLevels;
    }

    public int getNumberOfLevels() {
        return numberOfLevels;
    }
}
