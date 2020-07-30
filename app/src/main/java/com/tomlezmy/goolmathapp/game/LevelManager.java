package com.tomlezmy.goolmathapp.game;

import android.content.Context;

import androidx.annotation.Nullable;

import com.tomlezmy.goolmathapp.FileManager;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private int numberOfQuestions;
    private ECategory levelCategory;
    private int level;
    private int currentQuestion;
    private List<Question> questions;
    private List<Integer> questionsSubLevel;
    private ProbabilityGenerator levelValueLimitsAndProbability;
    private FileManager fileManager;

    public LevelManager(Context context, int numberOfQuestions, ECategory levelCategory, int level) {
        this.numberOfQuestions = numberOfQuestions;
        this.levelCategory = levelCategory;
        this.level = level;
        this.levelValueLimitsAndProbability = LimitFactory.getLevelValuesAndProbabilities(context, levelCategory, level);
        fileManager = FileManager.getInstance(context);
    }

    public void generateQuestions() {
        questions = new ArrayList<>();
        questionsSubLevel = new ArrayList<>();
        for (int i = 0; i < numberOfQuestions; i++) {
            int index = levelValueLimitsAndProbability.getLevelValueLimitIndexByProbability();
            Question q = generateQuestion(index);
            while (questions.contains(q)) {
                index = levelValueLimitsAndProbability.getLevelValueLimitIndexByProbability();
                q = generateQuestion(index);
            }
            questionsSubLevel.add(index);
            questions.add(q);
        }
        currentQuestion = 1;
    }

    public boolean updateWeightsFromUserAnswer(boolean wasCorrect) {
        boolean weightsUpdated = false;
        int subLevelWeight;
        if (wasCorrect) {
            subLevelWeight = fileManager.getSubLevelWeight(levelCategory,level - 1, questionsSubLevel.get(currentQuestion - 1));
            if (subLevelWeight != 1) {
                fileManager.updateSubLevelWeight(levelCategory,level - 1, questionsSubLevel.get(currentQuestion - 1), --subLevelWeight);
                weightsUpdated = true;
            }
        }
        else {
            subLevelWeight = fileManager.getSubLevelWeight(levelCategory,level - 1, questionsSubLevel.get(currentQuestion - 1));
            fileManager.updateSubLevelWeight(levelCategory,level - 1, questionsSubLevel.get(currentQuestion - 1), ++subLevelWeight);
            weightsUpdated = true;
        }

        return weightsUpdated;
    }

    private Question generateQuestion(int subLevelIndex) {
        return Question.createQuestion(levelCategory, levelValueLimitsAndProbability.getLevelValueLimit(subLevelIndex).getLevelValueLimits(), level);
    }

    public String getCurrentQuestion() {
        return questions.get(currentQuestion - 1).getQuestionHiddenAnswer();
    }

    public boolean nextQuestion() {
        currentQuestion++;
        return currentQuestion <= numberOfQuestions;
    }

    public boolean isLastQuestion() {
        return currentQuestion == numberOfQuestions;
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
