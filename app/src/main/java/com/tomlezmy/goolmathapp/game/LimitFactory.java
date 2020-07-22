package com.tomlezmy.goolmathapp.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LimitFactory {
    public static ProbabilityGenerator getLevelValuesAndProbabilities(ECategory category, int level) {
        ProbabilityGenerator probabilities = null;
        List<LevelValueLimitProbabilities> valueLimitProbabilities = new ArrayList<>();
        switch (category) {
            case ADDITION:
                switch (level) {
                    case 2:
                        //                      each column in a sub level
                        int arr1[] = new int[] {10,35,60,85,10,10,10,35,35,60};
                        int arr2[] = new int[] {34,59,84,99,34,34,34,59,59,84};
                        int arr3[] = new int[] {10,35,60,85,35,60,85,60,85,85};
                        int arr4[] = new int[] {34,59,84,99,59,84,99,84,99,99};
                        LevelValueLimits levelValues;
                        for (int i = 0 ; i < 10; i++) {
                            levelValues = new LevelValueLimits();
                            levelValues.setFirstNumberLimit(arr1[i],arr2[i]);
                            levelValues.setSecondNumberLimit(arr3[i],arr4[i]);
                            valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, 1));
                        }
                        break;
                }
        }

        probabilities = new ProbabilityGenerator(valueLimitProbabilities);
        return probabilities;
    }

    public static LevelValueLimits getLevelValues(ECategory category, int level) {
        LevelValueLimits levelValues = new LevelValueLimits();

        switch (category) {
            case SUBTRACTION:
            case ADDITION:
                switch (level) {
                    case 1:
                        levelValues.setFirstNumberLimit(0,9);
                        levelValues.setSecondNumberLimit(0,9);
                        break;
                    case 2:
                        levelValues.setFirstNumberLimit(10,99);
                        levelValues.setSecondNumberLimit(10,99);
                        break;
                    case 3:
                        if (category == ECategory.SUBTRACTION) {
                            levelValues.setFirstNumberLimit(new int[]{1000});
                            levelValues.setSecondNumberLimit(0,1000);
                        }
                        break;
                }
                break;
            case MULTIPLICATION:
                switch (level) {
                    case 1:
                        levelValues.setFirstNumberLimit(0,5);
                        levelValues.setSecondNumberLimit(0,5);
                        break;
                    case 2:
                        levelValues.setFirstNumberLimit(0,10);
                        levelValues.setSecondNumberLimit(0,10);
                        break;
                    case 3:
                        levelValues.setFirstNumberLimit(new int[]{11});
                        levelValues.setSecondNumberLimit(10,99);
                        break;
                    case 4:
                        levelValues.setFirstNumberLimit(new int[]{10,20,30,40,50,60,70,80,90});
                        levelValues.setSecondNumberLimit(0,9);
                        break;
                    case 5:
                        levelValues.setFirstNumberLimit(new int[]{100,200,300,400,500,600,700,800,900});
                        levelValues.setSecondNumberLimit(0,9);
                        break;
                }
                break;
            case DIVISION:
                switch (level) {
                    case 1:
                        levelValues.setFirstNumberLimit(0,5);
                        levelValues.setSecondNumberLimit(1,5);
                        break;
                    case 2:
                        levelValues.setFirstNumberLimit(0,10);
                        levelValues.setSecondNumberLimit(1,10);
                        break;
                    case 3:
                        int[] array = new int[10000];
                        int index = 0;
                        for (int i = 10; i <= 100000; i+=10) {
                            array[index++] = i;
                        }
                        levelValues.setFirstNumberLimit(array);
                        //levelValues.setFirstNumberLimit(new int[]{10,20,30,40,50,60,70,80,90});
                        levelValues.setSecondNumberLimit(new int[]{10});
                        break;
                    case 4:
                        levelValues.setFirstNumberLimit(new int[]{10,20,30,40,50,60,70,80,90});
                        levelValues.setSecondNumberLimit(1,9);
                        break;
                    case 5:
                        levelValues.setFirstNumberLimit(new int[]{100,200,300,400,500,600,700,800,900});
                        levelValues.setSecondNumberLimit(1,9);
                        break;
                }
                break;
            case PERCENTS:
                switch (level) {
                    case 1:
                        levelValues.setFirstNumberLimit(1,10000);
                        levelValues.setSecondNumberLimit(new int[] {1});
                        break;
                    case 2:
                        levelValues.setFirstNumberLimit(1,10000);
                        levelValues.setSecondNumberLimit(new int[] {10});
                        break;
                    case 3:
                        levelValues.setFirstNumberLimit(1,10000);
                        levelValues.setSecondNumberLimit(new int[] {50});
                        break;
                    case 4:
                        levelValues.setFirstNumberLimit(1,1000);
                        levelValues.setSecondNumberLimit(new int[] {20,25,33/*33.333333*/});
                        break;
                    case 5:
                        levelValues.setFirstNumberLimit(1,1000);
                        levelValues.setSecondNumberLimit(new int[] {2,5});
                        break;
                    case 6:
                        levelValues.setFirstNumberLimit(1,1000);
                        levelValues.setSecondNumberLimit(1,100);
                        break;
                    case 7:
                        levelValues.setFirstNumberLimit(1,1000);
                        levelValues.setSecondNumberLimit(new int[] {101,110,150,99,51,49,120,19,70,21,24,26,45,125,75});
                        break;
                    case 8:
                        levelValues.setFirstNumberLimit(1,1000);
                        levelValues.setSecondNumberLimit(100,500);
                        break;
                }
                break;
            case FRACTIONS:
                levelValues = new LevelValueFractionLimits();
                switch (level) {
                    case 2:
                    case 4:
                        levelValues.setFirstNumberLimit(new int[]{1});
                        levelValues.setSecondNumberLimit(2,10);
                        ((LevelValueFractionLimits)levelValues).setMultiplierLimit(20);
                        break;
                    case 3:
                    case 5:
                        levelValues.setFirstNumberLimit(1,10);
                        levelValues.setSecondNumberLimit(2,10);
                        ((LevelValueFractionLimits)levelValues).setMultiplierLimit(20);
                        break;
                    case 6:
                    case 7:
                        levelValues.setFirstNumberLimit(1,100);
                        levelValues.setSecondNumberLimit(2,100);
                        ((LevelValueFractionLimits)levelValues).setMultiplierLimit(100);
                        break;
                    case 8:
                    case 11:
                        levelValues.setFirstNumberLimit(1,10);
                        levelValues.setSecondNumberLimit(new int[] {1,2,3,5,7,9,11,13,17,19});
                        ((LevelValueFractionLimits)levelValues).setThirdNumberLimit(1,10);
                        ((LevelValueFractionLimits)levelValues).setFourthNumberLimit(new int[] {1,2,3,5,7,9,11,13,17,19});
                        break;
                    case 9:
                        levelValues.setFirstNumberLimit(1,10);
                        levelValues.setSecondNumberLimit(2,9);
                        ((LevelValueFractionLimits)levelValues).setThirdNumberLimit(1,10);
                        ((LevelValueFractionLimits)levelValues).setFourthNumberLimit(2,9);
                        break;
                    case 10:
                        levelValues.setFirstNumberLimit(1,10);
                        levelValues.setSecondNumberLimit(10,99);
                        ((LevelValueFractionLimits)levelValues).setThirdNumberLimit(1,10);
                        ((LevelValueFractionLimits)levelValues).setFourthNumberLimit(10,99);
                        break;
                    case 12:
                    case 13:
                        levelValues.setFirstNumberLimit(1,10);
                        levelValues.setSecondNumberLimit(1,10);
                        ((LevelValueFractionLimits)levelValues).setThirdNumberLimit(1,10);
                        ((LevelValueFractionLimits)levelValues).setFourthNumberLimit(1,10);
                        break;
                }
                break;
            case DECIMALS:
                switch (level) {
                    case 1:
                        levelValues.setFirstNumberLimit(new int[] {1,10,50,20,25,2,5,30,100,90});
                        // No need for two limits, setting empty value
                        levelValues.setSecondNumberLimit(0,0);
                        break;
                    case 2:
                        levelValues.setFirstNumberLimit(1,100);
                        // No need for two limits, setting empty value
                        levelValues.setSecondNumberLimit(0,0);
                        break;
                    case 3:
                        levelValues.setFirstNumberLimit(new int[] {101,110,150,99,51,49,120,19,70,21,24,26,45,125,75});
                        // No need for two limits, setting empty value
                        levelValues.setSecondNumberLimit(0,0);
                        break;
                    case 4:
                        levelValues.setFirstNumberLimit(100,500);
                        // No need for two limits, setting empty value
                        levelValues.setSecondNumberLimit(0,0);
                        break;
                    case 5:
                        levelValues.setFirstNumberLimit(1,3);
                        levelValues.setSecondNumberLimit(new int[] {100,50,20,10,5,4,2,8,16,25,3,6,9});
                        break;
                    case 6:
                        levelValues = new LevelValueFractionLimits();
                        levelValues.setFirstNumberLimit(new int[] {1,10,50,20,25,2,5,30,100,90,80,70,60,40});
                        // No need for two limits, setting empty value
                        levelValues.setSecondNumberLimit(0,0);
                        break;
                }
                break;
        }


        return levelValues;
    }

    public static List<String> createQuestionOptions(ECategory category, int level, int numberOfOptions, Question currentQuestion) {
        List<String> options = new ArrayList<>();
        Random rand = new Random();
        String option;
        ValueLimit bounds = new ValueRange(0,15);
        switch (category) {
            case ADDITION:
                switch (level){
                    case 1:
                        ((ValueRange)bounds).setUpper(3);
                        break;
                }
            case SUBTRACTION:
                switch (level){
                    case 1:
                        ((ValueRange)bounds).setUpper(3);
                        break;
                }
            case MULTIPLICATION:
                switch (level){
                    case 4:
                    case 5:
                        bounds = new ValueArray(new int[] {10,20,30,40,50});
                        break;
                }
            case DIVISION:
            case PERCENTS:
                float answer = currentQuestion.getResult();
                options.add((int) answer + "");
                for (int i = 0; i < numberOfOptions - 1; i++) {
                    do {
                        if (rand.nextInt(2) == 1) {
                            option = ((int) answer + bounds.generateValue()) + "";
                        } else {
                            option = ((int) answer - bounds.generateValue()) + "";
                        }
                    } while (options.contains(option));
                    options.add(option);
                }
                break;
            case FRACTIONS:
            case DECIMALS:
                // Level 6 of decimals has fractions
                if (category == ECategory.FRACTIONS || (category == ECategory.DECIMALS && level == 6)){
                    String fractionAnswer = ((FractionQuestion)currentQuestion).getFractionAnswer();
                    options.add(fractionAnswer);
                    int numerator = ((FractionQuestion)currentQuestion).getAnswerNumerator();
                    int denominator = ((FractionQuestion)currentQuestion).getAnswerDenominator();
                    for (int i = 0; i < numberOfOptions - 1; i++) {
                        do {
                            if (rand.nextInt(2) == 1) {
                                option = ((int) numerator + rand.nextInt(16)) + "/" + denominator;
                            } else {
                                option = ((int) numerator - rand.nextInt(16)) + "/" + denominator;
                            }
                        } while (options.contains(option));
                        options.add(option);
                    }
                }
                else {
                    float decimalAnswer = currentQuestion.getResult();
                    if ((int) decimalAnswer == decimalAnswer) {
                        options.add((int) decimalAnswer + "");
                    } else {
                        options.add(decimalAnswer + "");
                    }
                    for (int i = 0; i < numberOfOptions - 1; i++) {
                        do {
                            float value;
                            if (rand.nextInt(2) == 1) {
                                value = decimalAnswer + (float) (rand.nextInt(16)) / 10;
                            } else {
                                value = decimalAnswer - (float) (rand.nextInt(16)) / 10;
                            }
                            if ((int) value == value) {
                                option = (int) value + "";
                            } else {
                                option = String.format("%.3f", value).replaceAll("0*$", "");
                            }
                        } while (options.contains(option));
                        options.add(option);
                    }
                }
                break;
        }

        // Randomize option positions
        for (int i = numberOfOptions - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            String temp = options.get(index);
            options.set(index, options.get(i));
            options.set(i, temp);
        }

        return options;
    }
}
