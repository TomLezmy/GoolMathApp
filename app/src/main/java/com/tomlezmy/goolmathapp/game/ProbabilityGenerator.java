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

    public LevelValueLimitProbabilities getLevelValueLimitByProbability() {
        int index = rand.nextInt(weightSum) + 1;
        int sum = 0;
        int i = 0;
        while(sum < index) {
            sum = sum + valueLimitProbabilities.get(i++).getProbabilityWeight();
        }
        return valueLimitProbabilities.get(i - 1);
    }
}
