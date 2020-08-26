package com.tomlezmy.goolmathapp.game;

import android.content.Context;

import androidx.annotation.Nullable;

import com.tomlezmy.goolmathapp.model.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the run of each game, and generates questions for each round
 */
public class LevelManager {
    private int numberOfQuestions;
    private ECategory levelCategory;
    private int level;
    private int currentQuestion;
    /**
     * A list of all the questions in the round
     */
    private List<Question> questions;
    /**
     * A list containing each question's sub index used to access correct index in the weight file
     */
    private List<Integer> questionsSubLevel;
    private ProbabilityGenerator levelValueLimitsAndProbability;
    private FileManager fileManager;
    private Context context;

    /**
     * Class constructor
     * @param context The current context
     * @param numberOfQuestions The number of questions to generate
     * @param levelCategory The current level category
     * @param level The current level
     */
    public LevelManager(Context context, int numberOfQuestions, ECategory levelCategory, int level) {
        this.numberOfQuestions = numberOfQuestions;
        this.levelCategory = levelCategory;
        this.level = level;
        this.levelValueLimitsAndProbability = LimitFactory.getLevelValuesAndProbabilities(context, levelCategory, level);
        this.context = context;
        fileManager = FileManager.getInstance(context);
    }

    /**
     * Generates questions using {@link #levelValueLimitsAndProbability} without duplicates, the amount of questions is set by {@link #numberOfQuestions}
     */
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

    /**
     * Called after user answers a question and increases or decreases the relevant weight depending on the users answer
     * @param wasCorrect Whether the user answered correctly or not
     */
    public void updateWeightsFromUserAnswer(boolean wasCorrect) {
        int subLevelWeight;
        if (wasCorrect) {
            subLevelWeight = fileManager.getSubLevelWeight(levelCategory,level - 1, questionsSubLevel.get(currentQuestion - 1));
            if (subLevelWeight != 1) {
                fileManager.updateSubLevelWeight(levelCategory,level - 1, questionsSubLevel.get(currentQuestion - 1), --subLevelWeight);
            }
        }
        else {
            subLevelWeight = fileManager.getSubLevelWeight(levelCategory,level - 1, questionsSubLevel.get(currentQuestion - 1));
            fileManager.updateSubLevelWeight(levelCategory,level - 1, questionsSubLevel.get(currentQuestion - 1), ++subLevelWeight);
        }
    }

    /**
     * This method uses {@link #levelValueLimitsAndProbability} to generate a question in a specific range of values
     * @param subLevelIndex The sub level index used to get the value ranges
     * @return A new instance of question
     */
    private Question generateQuestion(int subLevelIndex) {
        return Question.createQuestion(levelCategory, levelValueLimitsAndProbability.getLevelValueLimit(subLevelIndex).getLevelValueLimits(), level, context);
    }

    public String getCurrentQuestion() {
        return questions.get(currentQuestion - 1).getQuestionHiddenAnswer();
    }

    /**
     * Moves to the next question in the list
     * @return True if there is a next question
     */
    public boolean nextQuestion() {
        currentQuestion++;
        return currentQuestion <= numberOfQuestions;
    }

    /**
     * This method creates options to the current question
     * @param numOfOptions The number of options to create
     * @return A list of answer options
     */
    @Nullable
    public List<String> getCurrentQuestionOptions(int numOfOptions) {
        return LimitFactory.createQuestionOptions(levelCategory, level, numOfOptions, questions.get(currentQuestion - 1));
    }

    public Boolean checkCorrectAnswer(float guess) {
        return guess == questions.get(currentQuestion - 1).getResult();
    }

    public int getLevel() {
        return level;
    }
}
