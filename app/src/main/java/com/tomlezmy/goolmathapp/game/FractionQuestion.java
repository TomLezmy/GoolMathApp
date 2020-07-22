package com.tomlezmy.goolmathapp.game;

import androidx.annotation.Nullable;

import java.util.Random;

public class FractionQuestion extends Question {
    private int resultDenominator, numThree, numFour;
    private String questionHiddenAnswer;

    public FractionQuestion(ECategory category, LevelValueLimits valueLimits, int level) {
        super(valueLimits);
        calculateResult(category, level, (LevelValueFractionLimits)valueLimits);
    }

    private void calculateResult(ECategory category, int level, LevelValueFractionLimits valueLimits) {
        Random rand = new Random();
        int mul;
        if (category == ECategory.FRACTIONS) {
            switch (level) {
                case 2:
                case 3:
                case 6:
                    mul = rand.nextInt(((valueLimits.getMultiplierLimit() - 1) / numTwo)) + 2;
                    result = numOne * mul;
                    resultDenominator = numTwo * mul;
                    questionHiddenAnswer = numOne + "/" + numTwo + " = ?";
                    break;
                case 4:
                case 5:
                case 7:
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
                case 8:
                case 9:
                case 10:
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
                case 11:
                case 12:
                    while (numTwo < numOne) {
                        numTwo = valueLimits.getSecondNumberLimit().generateValue();
                    }
                    numThree = valueLimits.getThirdNumberLimit().generateValue();
                    do {
                        numFour = valueLimits.getFourthNumberLimit().generateValue();
                    } while (numFour < numThree);
                    result = numOne * numThree;
                    resultDenominator = numTwo * numFour;
                    questionHiddenAnswer = numOne + "/" + numTwo + " " + sign + " " + numThree + "/" + numFour + " = ?";
                    break;
                case 13:
                    numThree = valueLimits.getThirdNumberLimit().generateValue();
                    numFour = valueLimits.getFourthNumberLimit().generateValue();
                    result = numOne * numThree;
                    resultDenominator = numTwo * numFour;
                    questionHiddenAnswer = numOne + "/" + numTwo + " " + sign + " " + numThree + "/" + numFour + " = ?";
                    break;
            }
        }
        // Decimals
        else {
            int gcdResult = gcd(numOne,100);
            result = numOne / gcdResult;
            resultDenominator = 100 / gcdResult;
            questionHiddenAnswer = "What is " + numOne + "% in fractions?";
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
