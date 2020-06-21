package com.tomlezmy.goolmathapp.game;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelManager {
    private Random rand;
    private int numberOfQuestions;
    private ECategory levelCategory;
    private int levelLimit;
    private int currentQuestion;
    private List<Question> questions;

    public LevelManager(int numberOfQuestions, ECategory levelCategory, int levelLimit) {
        this.numberOfQuestions = numberOfQuestions;
        this.levelCategory = levelCategory;
        this.levelLimit = levelLimit;
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
        return new Question(levelCategory, rand.nextInt(levelLimit) + 10, rand.nextInt(levelLimit) + 10);
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
            int answer = questions.get(currentQuestion - 1).getResult();
            //int numbersToRand[] = getRandomizedNumbers(numOfOptions);
            options.add(answer + "");
            String option;
            for (int i = 0; i < numOfOptions - 1; i++) {
                do {
                    if (rand.nextInt(2) == 1) {
                        option = (answer + rand.nextInt(16)) + "";
                    }
                    else {
                        option = (answer - rand.nextInt(16)) + "";
                    }
                } while(options.contains(option));
                options.add(option);
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

    private int[] getRandomizedNumbers(int numOfOptions) {
        int numbersToRand[];
        if (numOfOptions == 2) {
            numbersToRand = new int[] {0 , 1};
        }
        else {
            numbersToRand = new int[] {0, 1, 2, 3};
        }

        for (int i = numbersToRand.length - 1; i > 0; i--)
        {
            int index = rand.nextInt(i + 1);
            int temp = numbersToRand[index];
            numbersToRand[index] = numbersToRand[i];
            numbersToRand[i] = temp;
        }

        return numbersToRand;
    }

    public Boolean checkCorrectAnswer(int guess) {
        return guess == questions.get(currentQuestion - 1).getResult();
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public ECategory getLevelCategory() {
        return levelCategory;
    }

    public int getLevelLimit() {
        return levelLimit;
    }
}
