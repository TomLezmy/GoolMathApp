package com.tomlezmy.goolmathapp.game;

import androidx.annotation.Nullable;

import java.util.Random;

public class FractionQuestion extends Question {
    private int resultDenominator;

    public FractionQuestion(ECategory category, LevelValueLimits valueLimits) {
        super(category, valueLimits);
        Random rand = new Random();
        numOne = valueLimits.getFirstNumberLimit().generateValue();
        numTwo = valueLimits.getSecondNumberLimit().generateValue();
        int mul = rand.nextInt((int)(19 / numTwo)) + 2;// numTwo * mul <= 20
        result = numOne * mul;
        resultDenominator = numTwo * mul;
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
        return numOne + "/" + numTwo + "= ?";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        FractionQuestion questionToCompare = (FractionQuestion) obj;
        return ((numOne == questionToCompare.getNumOne() && numTwo == questionToCompare.getNumTwo()) &&
                result == questionToCompare.getResult() && resultDenominator == questionToCompare.getAnswerDenominator() );
    }
}
