package com.tomlezmy.goolmathapp.game;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelManager {
    private Random rand;
    private int numberOfQuestions;
    private ECategory levelCategory;
    private int level;
    private int currentQuestion;
    private List<Question> questions;
    private LevelValueLimits levelValueLimits;

    public LevelManager(int numberOfQuestions, ECategory levelCategory, int level) {
        this.numberOfQuestions = numberOfQuestions;
        this.levelCategory = levelCategory;
        this.level = level;
        this.levelValueLimits = LimitFactory.getLevelValues(levelCategory, level);
        rand = new Random();
    }

    public void generateQuestions() {
        questions = new ArrayList<>();
        for (int i = 0; i < numberOfQuestions; i++) {
            Question q = generateQuestion();
            while (questions.contains(q)) {
                q = generateQuestion();
            }
            questions.add(q);
        }
        currentQuestion = 1;
    }

    private Question generateQuestion() {
        if (levelCategory == ECategory.DIVISION) {
            int num1, num2;
            do {
                num1 = levelValueLimits.getFirstNumberLimit().generateValue();
                num2 = levelValueLimits.getSecondNumberLimit().generateValue();
            } while (num1 % num2 != 0);
            return new Question(levelCategory, num1, num2);
        }
        return new Question(levelCategory, levelValueLimits.getFirstNumberLimit().generateValue(), levelValueLimits.getSecondNumberLimit().generateValue());
    }

    public String getCurrentQuestion() {
        return questions.get(currentQuestion - 1).getQuestionHiddenAnswer();
    }

    public boolean nextQuestion() {
        currentQuestion++;
        return currentQuestion <= numberOfQuestions;
    }

    @Nullable
    public List<String> getCurrentQuestionOptions(int numOfOptions) {
        List<String> options = null;
        if (numOfOptions != 0) {
            options = new ArrayList<>();
            float answer = questions.get(currentQuestion - 1).getResult();
            String option;
            // For Whole numbers
            if (levelCategory != ECategory.PERCENTS) {
                options.add((int)answer + "");
                for (int i = 0; i < numOfOptions - 1; i++) {
                    do {
                        if (rand.nextInt(2) == 1) {
                            option = ((int)answer + rand.nextInt(16)) + "";
                        } else {
                            option = ((int)answer - rand.nextInt(16)) + "";
                        }
                    } while (options.contains(option));
                    options.add(option);
                }
            }
            // For Decimal numbers
            else {
                if ((int)answer == answer) {
                    options.add((int)answer + "");
                }
                else {
                    options.add(answer + "");
                }
                for (int i = 0; i < numOfOptions - 1; i++) {
                    do {
                        double value;
                        if (rand.nextInt(2) == 1) {
                            value = answer + (float)(rand.nextInt(16)) / 10;
                        } else {
                            value = answer - (float)(rand.nextInt(16)) / 10;
                        }
                        if ((int)value == value) {
                            option = (int)value + "";
                        }
                        else {
                            option = String.format("%.3f", value).replaceAll("0*$", "");
                        }
                    } while (options.contains(option));
                    options.add(option);
                }
            }
            for (int i = numOfOptions - 1; i > 0; i--)
            {
                int index = rand.nextInt(i + 1);
                String temp = options.get(index);
                options.set(index, options.get(i));
                options.set(i, temp);
            }
        }

        return options;
    }

    public Boolean checkCorrectAnswer(float guess) {
        return guess == questions.get(currentQuestion - 1).getResult();
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public ECategory getLevelCategory() {
        return levelCategory;
    }

    public int getLevel() {
        return level;
    }
}
