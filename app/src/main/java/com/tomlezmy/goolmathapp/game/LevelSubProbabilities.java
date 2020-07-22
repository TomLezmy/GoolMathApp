package com.tomlezmy.goolmathapp.game;

import java.io.Serializable;
import java.util.List;

public class LevelSubProbabilities implements Serializable {
    private int level;
    private List<Integer> weights;

    public LevelSubProbabilities(int level, List<Integer> weights) {
        this.level = level;
        this.weights = weights;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Integer> getWeights() {
        return weights;
    }

    public void setWeights(List<Integer> weights) {
        this.weights = weights;
    }
}
