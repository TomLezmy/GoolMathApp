package com.tomlezmy.goolmathapp.game;

import java.util.List;
import java.util.Random;

public class ProbabilityGenerator {
    private List<LevelValueLimitProbabilities> valueLimitProbabilities;
    private Random rand;
    private int weightSum;

    public ProbabilityGenerator(List<LevelValueLimitProbabilities> valueLimitProbabilities) {
        rand = new Random();
        this.valueLimitProbabilities = valueLimitProbabilities;
        weightSum = 0;
        for (LevelValueLimitProbabilities limitProbabilities: valueLimitProbabilities) {
            weightSum += limitProbabilities.getProbabilityWeight();
        }
    }

    public int getLevelValueLimitIndexByProbability() {
        int index = rand.nextInt(weightSum) + 1;
        int sum = 0;
        int i = 0;
        while(sum < index) {
            sum += valueLimitProbabilities.get(i++).getProbabilityWeight();
        }
        return i - 1;
    }

    public LevelValueLimitProbabilities getLevelValueLimit(int index) {
        return valueLimitProbabilities.get(index);
    }
}
