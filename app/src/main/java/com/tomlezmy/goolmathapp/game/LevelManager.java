package com.tomlezmy.goolmathapp.game;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private int numberOfQuestions;
    private ECategory levelCategory;
    private int level;
    private int currentQuestion;
    private List<Question> questions;
    private LevelValueLimits levelValueLimits;
    private ProbabilityGenerator levelValueLimitsAndProbability;

    public LevelManager(int numberOfQuestions, ECategory levelCategory, int level) {
        this.numberOfQuestions = numberOfQuestions;
        this.levelCategory = levelCategory;
        this.level = level;
        //levelValueLimitsAndProbability = LimitFactory.getLevelValuesAndProbabilities(levelCategory, level);
        this.levelValueLimits = LimitFactory.getLevelValues(levelCategory, level);
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
        return Question.createQuestion(levelCategory, levelValueLimits, level);
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
        return LimitFactory.createQuestionOptions(levelCategory, level, numOfOptions, questions.get(currentQuestion - 1));
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
