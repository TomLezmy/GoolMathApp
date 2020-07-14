package com.tomlezmy.goolmathapp.game;

import java.util.Random;

public abstract class ValueLimit {
    protected Random rand;
    public abstract int generateValue();

    public ValueLimit() {
        this.rand = new Random();
    }
}
