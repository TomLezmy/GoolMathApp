package com.tomlezmy.goolmathapp.game;

import java.util.List;
import java.util.Random;

/**
 * This class picks a random {@link LevelValueLimitProbabilities} according to the probability of each item
 */
public class ProbabilityGenerator {
    private List<LevelValueLimitProbabilities> valueLimitProbabilities;
    private Random rand;
    private int weightSum;

    /**
     * Class constructor
     * @param valueLimitProbabilities A list of all sub level limits of a level
     */
    public ProbabilityGenerator(List<LevelValueLimitProbabilities> valueLimitProbabilities) {
        rand = new Random();
        this.valueLimitProbabilities = valueLimitProbabilities;
        weightSum = 0;
        for (LevelValueLimitProbabilities limitProbabilities: valueLimitProbabilities) {
            weightSum += limitProbabilities.getProbabilityWeight();
        }
    }

    /**
     * This method picks a index in {@link #valueLimitProbabilities} according to the probability of each item
     * @return The selected index
     */
    public int getLevelValueLimitIndexByProbability() {
        int index = rand.nextInt(weightSum) + 1;
        int sum = 0;
        int i = 0;
        while(sum < index) {
            sum += valueLimitProbabilities.get(i++).getProbabilityWeight();
        }
        return i - 1;
    }

    /**
     * This method returns the value limits of a sub level
     * @param index The index of the sub level
     */
    public LevelValueLimitProbabilities getLevelValueLimit(int index) {
        return valueLimitProbabilities.get(index);
    }
}
