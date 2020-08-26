package com.tomlezmy.goolmathapp.game;

import java.util.Random;

/**
 * An abstract class that holds value limits for a question in a level
 */
public abstract class ValueLimit {
    protected Random rand;
    public abstract int generateValue();

    public ValueLimit() {
        this.rand = new Random();
    }
}
