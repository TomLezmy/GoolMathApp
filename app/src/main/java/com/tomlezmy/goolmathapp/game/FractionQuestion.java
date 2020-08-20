package com.tomlezmy.goolmathapp.game;

import android.content.Context;

import androidx.annotation.Nullable;

import com.tomlezmy.goolmathapp.R;

import java.util.Random;

public class FractionQuestion extends Question {
    private int resultDenominator, numThree, numFour;
    private String questionHiddenAnswer;

    public FractionQuestion(ECategory category, LevelValueLimits valueLimits, int level, Context context) {
        super(valueLimits);
        this.context = context;
        calculateResult(category, level, (LevelValueFractionLimits)valueLimits);
    }

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
                    questionHiddenAnswer = numOne + "/" + numTwo + " = ?";
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
                    questionHiddenAnswer = numOne + "/" + numTwo + " = ?";
                    break;
                case 7:
                case 8:
                case 9:
                    numThree = valueLimits.getThirdNumberLimit().generateValue();
                    numFour = valueLimits.getFourthNumberLimit().generateValue();
                    int lcd = lcm(numTwo, numFour);
                    int numOneAfterLcd = numOne * (lcd / numTwo);
                    int numThreeAfterLcd = numThree * (lcd / numFour);
                    resultDenominator = lcd;
                    if (rand.nextInt(2) == 0) {
                        sign = "+";
                        result = numOneAfterLcd + numThreeAfterLcd;
                    } else {
                        sign = "-";
                        result = numOneAfterLcd - numThreeAfterLcd;
                    }
                    questionHiddenAnswer = numOne + "/" + numTwo + " " + sign + " " + numThree + "/" + numFour + " = ?";
                    break;
                case 10:
                case 11:
                    while (numTwo < numOne) {
                        numTwo = valueLimits.getSecondNumberLimit().generateValue();
                    }
                    numThree = valueLimits.getThirdNumberLimit().generateValue();
                    do {
                        numFour = valueLimits.getFourthNumberLimit().generateValue();
                    } while (numFour < numThree);
                    result = numOne * numThree;
                    resultDenominator = numTwo * numFour;
                    questionHiddenAnswer = numOne + "/" + numTwo + " X " + numThree + "/" + numFour + " = ?";
                    break;
                case 12:
                    numThree = valueLimits.getThirdNumberLimit().generateValue();
                    numFour = valueLimits.getFourthNumberLimit().generateValue();
                    result = numOne * numThree;
                    resultDenominator = numTwo * numFour;
                    questionHiddenAnswer = numOne + "/" + numTwo + " X " + numThree + "/" + numFour + " = ?";
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

    public String getFractionAnswer() {return (int)result + "/" + resultDenominator;}

    public int getAnswerDenominator() {return resultDenominator;}

    public int getAnswerNumerator() {return (int)result;}

    @Override
    public float getResult() {
        return result/(float)resultDenominator;
    }

    @Override
    public String getQuestionHiddenAnswer() {
        return questionHiddenAnswer;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        FractionQuestion questionToCompare = (FractionQuestion) obj;
        return (numOne == questionToCompare.getNumOne() && numTwo == questionToCompare.getNumTwo() &&
                result == questionToCompare.getAnswerNumerator() && resultDenominator == questionToCompare.getAnswerDenominator() );
    }

    private int gcd(int a, int b)
    {
        if (a == 0)
            return b;

        return gcd(b % a, a);
    }

    private int lcm(int a, int b)
    {
        return (a * b) / gcd(a, b);
    }
}
