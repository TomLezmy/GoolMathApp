package com.tomlezmy.goolmathapp.game;

import android.content.Context;

import androidx.annotation.Nullable;

import com.tomlezmy.goolmathapp.R;

import java.util.Random;

/**
 * This class holds additional info for questions containing fractions
 */
public class FractionQuestion extends Question {
    private int resultDenominator;
    private String questionHiddenAnswer;

    /**
     * Class constructor
     * @param category The current category
     * @param valueLimits The range of values to generate the question from
     * @param level The current level
     * @param context The current context
     */
    public FractionQuestion(ECategory category, LevelValueLimits valueLimits, int level, Context context) {
        super(valueLimits);
        this.context = context;
        calculateResult(category, level, (LevelValueFractionLimits)valueLimits);
    }

    /**
     * Calculates values for the question according to the current level and category
     * @param category - The current category
     * @param level The current level
     * @param valueLimits The range of values to generate the question from
     */
    private void calculateResult(ECategory category, int level, LevelValueFractionLimits valueLimits) {
        Random rand = new Random();
        int mul;
        if (category == ECategory.FRACTIONS) {
            switch (level) {
                case 1:
                case 2:
                case 5:
                    mul = rand.nextInt(((valueLimits.getMultiplierLimit() - 1) / numTwo)) + 2;
                    result = numOne * mul;
                    resultDenominator = numTwo * mul;
                    questionHiddenAnswer = numOne + "\n- = ?\n" + numTwo;
                    break;
                case 3:
                case 4:
                case 6:
                    mul = rand.nextInt(((valueLimits.getMultiplierLimit() - 1) / numTwo)) + 2;
                    result = numOne * mul;
                    resultDenominator = numTwo * mul;
                    int temp = (int) result;
                    result = numOne;
                    numOne = temp;
                    temp = resultDenominator;
                    resultDenominator = numTwo;
                    numTwo = temp;
                    questionHiddenAnswer = numOne + "\n- = ?\n" + numTwo;
                    break;
            }
        }
        // Decimals
        else {
            int gcdResult = gcd(numOne,100);
            result = numOne / gcdResult;
            resultDenominator = 100 / gcdResult;
            questionHiddenAnswer = String.format(context.getString(R.string.decimal_to_fraction_question), numOne + "");
        }
    }

    /**
     * @return Answer in fraction format
     */
    public String getFractionAnswer() {return (int)result + "\n-\n" + resultDenominator;}

    /**
     * @return Current answer denominator
     */
    public int getAnswerDenominator() {return resultDenominator;}

    /**
     * @return Current answer numerator
     */
    public int getAnswerNumerator() {return (int)result;}

    /**
     * @return Current answer
     */
    @Override
    public float getResult() {
        return result/(float)resultDenominator;
    }

    /**
     * @return Current question without the answer
     */
    @Override
    public String getQuestionHiddenAnswer() {
        return questionHiddenAnswer;
    }

    /**
     * @return True if question and answer of object are the same as the current question
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        FractionQuestion questionToCompare = (FractionQuestion) obj;
        return (numOne == questionToCompare.getNumOne() && numTwo == questionToCompare.getNumTwo() &&
                result == questionToCompare.getAnswerNumerator() && resultDenominator == questionToCompare.getAnswerDenominator() );
    }

    /**
     * Finds the greatest common denominator between the two integers
     * @param a First integer
     * @param b Second integer
     * @return The greatest common denominator
     */
    private int gcd(int a, int b)
    {
        if (a == 0)
            return b;

        return gcd(b % a, a);
    }
}
