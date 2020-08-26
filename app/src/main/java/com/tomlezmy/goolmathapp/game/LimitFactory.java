package com.tomlezmy.goolmathapp.game;

import android.content.Context;

import com.tomlezmy.goolmathapp.model.FileManager;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Random;

/**
 * This is a factory class for generating question value limits and question options
 */
public class LimitFactory {
    /**
     * This method creates value limits for question generation according the current category and level.<br/>
     * The question level data is hard coded.
     * @param context The current context
     * @param category The current category
     * @param level The current level
     * @return The current level value limits and probabilities
     */
    public static ProbabilityGenerator getLevelValuesAndProbabilities(Context context, ECategory category, int level) {
        FileManager fileManager = FileManager.getInstance(context);
        Dictionary<ECategory,List<List<Integer>>> levelSubProbabilities = fileManager.getLevelWeights();
        List<Integer> currentWeights = levelSubProbabilities.get(category).get(level - 1);
        ProbabilityGenerator probabilities;
        List<LevelValueLimitProbabilities> valueLimitProbabilities = new ArrayList<>();
        LevelValueLimits levelValues;
        int[] arr1,arr2,arr3,arr4;
        switch (category) {
            case ADDITION:
            case SUBTRACTION:
                switch (level) {
                    case 1:
                        //           each column is a sub level
                        arr1 = new int[]{0, 5, 5};
                        arr2 = new int[]{4, 9, 9};
                        arr3 = new int[]{0, 5, 0};
                        arr4 = new int[]{4, 9, 4};
                        for (int i = 0; i < 3; i++) {
                            levelValues = new LevelValueLimits();
                            levelValues.setFirstNumberLimit(arr1[i], arr2[i]);
                            levelValues.setSecondNumberLimit(arr3[i], arr4[i]);
                            valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(i)));
                        }
                        break;
                    case 2:
                        //          each column is a sub level
                        arr1 = new int[]{10, 35, 60, 85, 35, 60, 85, 60, 85, 85};
                        arr2 = new int[]{34, 59, 84, 99, 59, 84, 99, 84, 99, 99};
                        arr3 = new int[]{10, 35, 60, 85, 10, 10, 10, 35, 35, 60};
                        arr4 = new int[]{34, 59, 84, 99, 34, 34, 34, 59, 59, 84};
                        for (int i = 0; i < 10; i++) {
                            levelValues = new LevelValueLimits();
                            levelValues.setFirstNumberLimit(arr1[i], arr2[i]);
                            levelValues.setSecondNumberLimit(arr3[i], arr4[i]);
                            valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(i)));
                        }
                        break;

                    case 3:
                        if (category == ECategory.SUBTRACTION) {
                            levelValues = new LevelValueLimits();
                            levelValues.setFirstNumberLimit(new int[]{1000});
                            levelValues.setSecondNumberLimit(0, 9);
                            valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                            levelValues = new LevelValueLimits();
                            levelValues.setFirstNumberLimit(new int[]{1000});
                            levelValues.setSecondNumberLimit(10, 99);
                            valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                            levelValues = new LevelValueLimits();
                            levelValues.setFirstNumberLimit(new int[]{1000});
                            levelValues.setSecondNumberLimit(100, 1000);
                            valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(2)));
                        }
                        break;
                }
                break;
            case MULTIPLICATION:
                switch (level) {
                    case 1:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(2, 5);
                        levelValues.setSecondNumberLimit(2, 5);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        break;
                    case 2:
                        //           each column is a sub level
                        arr1 = new int[]{2, 6, 2};
                        arr2 = new int[]{5, 9, 5};
                        arr3 = new int[]{2, 6, 6};
                        arr4 = new int[]{5, 9, 9};
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[]{10});
                        levelValues.setSecondNumberLimit(new int[]{10});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[]{10});
                        levelValues.setSecondNumberLimit(2, 5);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[]{10});
                        levelValues.setSecondNumberLimit(6, 9);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(2)));
                        for (int i = 0; i < 3; i++) {
                            levelValues = new LevelValueLimits();
                            levelValues.setFirstNumberLimit(arr1[i], arr2[i]);
                            levelValues.setSecondNumberLimit(arr3[i], arr4[i]);
                            valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(i + 3)));
                        }
                        break;

                    case 3:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[]{11});
                        levelValues.setSecondNumberLimit(new int[] {10,20,30,40,50,60,70,80,90});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        arr1 = new int[]{11,21,31,41,51,61,71,81,91};
                        arr2 = new int[]{19,29,39,49,59,69,79,89,99};
                        for (int i = 0; i < 9; i++) {
                            levelValues = new LevelValueLimits();
                            levelValues.setFirstNumberLimit(new int[]{11});
                            levelValues.setSecondNumberLimit(arr1[i], arr2[i]);
                            valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(i + 1)));
                        }
                        break;
                    case 4:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[]{10,20,30,40});
                        levelValues.setSecondNumberLimit(1,4);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[]{10,20,30,40});
                        levelValues.setSecondNumberLimit(5,9);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[]{50,60,70,80,90});
                        levelValues.setSecondNumberLimit(1,4);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(2)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[]{50,60,70,80,90});
                        levelValues.setSecondNumberLimit(5,9);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(3)));
                        break;
                    case 5:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[]{100,200,300,400});
                        levelValues.setSecondNumberLimit(1,4);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[]{100,200,300,400});
                        levelValues.setSecondNumberLimit(5,9);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[]{500,600,700,800,900});
                        levelValues.setSecondNumberLimit(1,4);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(2)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[]{500,600,700,800,900});
                        levelValues.setSecondNumberLimit(5,9);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(3)));
                        break;
                }
                break;
            case DIVISION:
                switch (level) {
                    case 1:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(1, 5);
                        levelValues.setSecondNumberLimit(2, 10);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(6, 10);
                        levelValues.setSecondNumberLimit(2, 10);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        break;
                    case 2:
                        levelValues = new LevelValueLimits();
                        int[] array = new int[10000];
                        int index = 0;
                        for (int i = 10; i <= 100000; i += 10) {
                            array[index++] = i;
                        }
                        levelValues.setFirstNumberLimit(array);
                        levelValues.setSecondNumberLimit(new int[]{10});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        break;
                    case 3:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[]{10,20,30,40});
                        levelValues.setSecondNumberLimit(1,9);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[]{50,60,70,80,90});
                        levelValues.setSecondNumberLimit(1,9);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        break;
                    case 4:
                        int[] valueArray = new int[31];
                        int valueIndex = 0;
                        levelValues = new LevelValueLimits();
                        for (int i = 100; i <= 400; i += 10) {
                            valueArray[valueIndex++] = i;
                        }
                        levelValues.setFirstNumberLimit(valueArray);
                        levelValues.setSecondNumberLimit(1,9);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        valueArray = new int[41];
                        valueIndex = 0;
                        for (int i = 500; i <= 900; i += 10) {
                            valueArray[valueIndex++] = i;
                        }
                        levelValues.setFirstNumberLimit(valueArray);
                        levelValues.setSecondNumberLimit(1,9);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        break;
                }
                break;
            case FRACTIONS:
                switch (level) {
                    case 1:
                    case 3:
                        levelValues = new LevelValueFractionLimits();
                            levelValues.setFirstNumberLimit(new int[]{1});
                            levelValues.setSecondNumberLimit(2,5);
                            ((LevelValueFractionLimits)levelValues).setMultiplierLimit(20);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueFractionLimits();
                        levelValues.setFirstNumberLimit(new int[]{1});
                        levelValues.setSecondNumberLimit(6,10);
                        ((LevelValueFractionLimits)levelValues).setMultiplierLimit(20);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        break;
                    case 2:
                    case 4:
                        levelValues = new LevelValueFractionLimits();
                        levelValues.setFirstNumberLimit(1,10);
                        levelValues.setSecondNumberLimit(2,5);
                        ((LevelValueFractionLimits)levelValues).setMultiplierLimit(20);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueFractionLimits();
                        levelValues.setFirstNumberLimit(1,10);
                        levelValues.setSecondNumberLimit(6,10);
                        ((LevelValueFractionLimits)levelValues).setMultiplierLimit(20);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        break;
                    case 5:
                    case 6:
                        levelValues = new LevelValueFractionLimits();
                        levelValues.setFirstNumberLimit(1,49);
                        levelValues.setSecondNumberLimit(2,49);
                        ((LevelValueFractionLimits)levelValues).setMultiplierLimit(100);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueFractionLimits();
                        levelValues.setFirstNumberLimit(1,49);
                        levelValues.setSecondNumberLimit(50,100);
                        ((LevelValueFractionLimits)levelValues).setMultiplierLimit(100);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        levelValues = new LevelValueFractionLimits();
                        levelValues.setFirstNumberLimit(50,100);
                        levelValues.setSecondNumberLimit(2,49);
                        ((LevelValueFractionLimits)levelValues).setMultiplierLimit(100);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(2)));
                        levelValues = new LevelValueFractionLimits();
                        levelValues.setFirstNumberLimit(50,100);
                        levelValues.setSecondNumberLimit(50,100);
                        ((LevelValueFractionLimits)levelValues).setMultiplierLimit(100);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(3)));
                        break;
                }
                break;
            case PERCENTS:
                int[] array;
                int index;
                switch (level) {
                    case 1:
                        levelValues = new LevelValueLimits();
                        array = new int[100];
                        index = 0;
                        for (int i = 100; i <= 10000; i += 100) {
                            array[index++] = i;
                        }
                        levelValues.setFirstNumberLimit(array);
                        levelValues.setSecondNumberLimit(new int[]{1});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        break;
                    case 2:
                        levelValues = new LevelValueLimits();
                        array = new int[1000];
                        index = 0;
                        for (int i = 10; i <= 10000; i += 10) {
                            array[index++] = i;
                        }
                        levelValues.setFirstNumberLimit(array);
                        levelValues.setSecondNumberLimit(new int[]{10});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        break;
                    case 3:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(2,1000);
                        levelValues.setSecondNumberLimit(new int[]{50});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(1001,10000);
                        levelValues.setSecondNumberLimit(new int[]{50});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        break;
                    case 4:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(1,99);
                        levelValues.setSecondNumberLimit(new int[]{20});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(100,1000);
                        levelValues.setSecondNumberLimit(new int[]{20});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(1,99);
                        levelValues.setSecondNumberLimit(new int[]{33});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(2)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(100,1000);
                        levelValues.setSecondNumberLimit(new int[]{33});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(3)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(1,99);
                        levelValues.setSecondNumberLimit(new int[]{25});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(4)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(100,1000);
                        levelValues.setSecondNumberLimit(new int[]{25});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(5)));
                        break;
                    case 5:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(1,99);
                        levelValues.setSecondNumberLimit(new int[]{2});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(100,1000);
                        levelValues.setSecondNumberLimit(new int[]{2});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(1,99);
                        levelValues.setSecondNumberLimit(new int[]{5});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(2)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(100,1000);
                        levelValues.setSecondNumberLimit(new int[]{5});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(3)));
                        break;
                    case 6:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(1,1000);
                        levelValues.setSecondNumberLimit(1,49);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(1,1000);
                        levelValues.setSecondNumberLimit(50,100);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        break;
                    case 7:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(1,100);
                        levelValues.setSecondNumberLimit(new int[] {101,110,150,99,120,125});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(1,100);
                        levelValues.setSecondNumberLimit(new int[] {49,19,24});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(1,100);
                        levelValues.setSecondNumberLimit(new int[] {51,70,21,26,45,75});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(2)));
                        break;
                    case 8:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(1,100);
                        levelValues.setSecondNumberLimit(100,299);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(1,100);
                        levelValues.setSecondNumberLimit(300,500);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        break;
                }
                break;
            case DECIMALS:
                switch (level) {
                    case 1:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[] {10,20,30,40,50,60,70,80,90,100});
                        // No need for two limits, setting empty value
                        levelValues.setSecondNumberLimit(0,0);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[] {1,25,2,5});
                        // No need for two limits, setting empty value
                        levelValues.setSecondNumberLimit(0,0);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        break;
                    case 2:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[] {10,20,30,40,50,60,70,80,90,100});
                        // No need for two limits, setting empty value
                        levelValues.setSecondNumberLimit(0,0);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        int[] arr = new int[90];
                        int arrIndex = 0;
                        for (int i = 1; i < 100; i++) {
                            if (i % 10 != 0) {
                                arr[arrIndex++] = i;
                            }
                        }
                        levelValues.setFirstNumberLimit(arr);
                        // No need for two limits, setting empty value
                        levelValues.setSecondNumberLimit(0,0);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        break;
                    case 3:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[] {101,110,150,120,125});
                        // No need for two limits, setting empty value
                        levelValues.setSecondNumberLimit(0,0);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(new int[] {99,51,49,19,70,21,24,26,45,75});
                        // No need for two limits, setting empty value
                        levelValues.setSecondNumberLimit(0,0);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        break;
                    case 4:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(100,299);
                        // No need for two limits, setting empty value
                        levelValues.setSecondNumberLimit(0,0);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(300,500);
                        // No need for two limits, setting empty value
                        levelValues.setSecondNumberLimit(0,0);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        break;
                    case 5:
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(1,3);
                        levelValues.setSecondNumberLimit(new int[] {100,50,20,10,4,2,3,6,9});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueLimits();
                        levelValues.setFirstNumberLimit(1,3);
                        levelValues.setSecondNumberLimit(new int[] {25,16,8,5});
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        break;
                    case 6:
                        levelValues = new LevelValueFractionLimits();
                        levelValues.setFirstNumberLimit(new int[] {10,20,30,40,50,60,70,80,90,100});
                        // No need for two limits, setting empty value
                        levelValues.setSecondNumberLimit(0,0);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(0)));
                        levelValues = new LevelValueFractionLimits();
                        levelValues.setFirstNumberLimit(new int[] {1,2,5,25});
                        // No need for two limits, setting empty value
                        levelValues.setSecondNumberLimit(0,0);
                        valueLimitProbabilities.add(new LevelValueLimitProbabilities(levelValues, currentWeights.get(1)));
                        break;
                }
                break;
        }

        probabilities = new ProbabilityGenerator(valueLimitProbabilities);
        return probabilities;
    }

    /**
     * This method creates different answer options for a question
     * @param category The current category
     * @param level The current level
     * @param numberOfOptions The number of options to create
     * @param currentQuestion The current question
     * @return A list of answer options
     */
    public static List<String> createQuestionOptions(ECategory category, int level, int numberOfOptions, Question currentQuestion) {
        List<String> options = new ArrayList<>();
        Random rand = new Random();
        String option;
        ValueLimit bounds = new ValueRange(0,15);
        switch (category) {
            case ADDITION:
            case SUBTRACTION:
                if (level == 1) {
                    ((ValueRange)bounds).setUpper(3);
                }
            case MULTIPLICATION:
                switch (level) {
                    case 3:
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
                    } while (options.contains(option) || Integer.parseInt(option) < 0);
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
                                option = (numerator + rand.nextInt(16)) + "\n-\n" + denominator;
                            } else {
                                option = (numerator - rand.nextInt(16)) + "\n-\n" + denominator;
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
