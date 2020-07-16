package com.tomlezmy.goolmathapp.game;

import androidx.annotation.Nullable;

public class Question {
    private ECategory category;
    private String sign;
    protected int numOne;
    protected int numTwo;
    protected float result;

    public static Question createQuestion(ECategory category, LevelValueLimits valueLimits) {
        if (category != ECategory.FRACTIONS) {
            return new Question(category, valueLimits);
        }
        return new FractionQuestion(category,valueLimits);
    }

    public Question() {
    }

    public Question(ECategory category, LevelValueLimits valueLimits) {
        this.category = category;
        this.numOne = valueLimits.getFirstNumberLimit().generateValue();
        this.numTwo = valueLimits.getSecondNumberLimit().generateValue();
        switch (category) {
            case ADDITION:
                this.result = this.numOne + this.numTwo;
                sign = "+";
                break;
            case SUBTRACTION:
                this.result = this.numOne - this.numTwo;
                sign = "-";
                break;
            case MULTIPLICATION:
                this.result = this.numOne * this.numTwo;
                sign = "*";
                break;
            case DIVISION:
                while (numOne % numTwo != 0) {
                    this.numOne = valueLimits.getFirstNumberLimit().generateValue();
                    this.numTwo = valueLimits.getSecondNumberLimit().generateValue();
                }
                this.result = ((float)this.numOne / (float)this.numTwo);
                if ((int)result != result) {
                    result = Float.parseFloat(String.format("%.3f", result).replaceAll("0*$", ""));
                }
                sign = "/";
                break;
            case PERCENTS:
                this.result = (float)(this.numOne * this.numTwo) / 100;
                if ((int)result != result) {
                    result = Float.parseFloat(String.format("%.3f", result).replaceAll("0*$", ""));
                }
                sign = "*";
                break;
            case FRACTIONS:

                break;
        }
    }

    public int getNumOne() {
        return numOne;
    }

    public int getNumTwo() {
        return numTwo;
    }

    public float getResult()  { return result; }

    public String getQuestionHiddenAnswer() {
        if (category == ECategory.PERCENTS) {
            return numTwo + "% of " + numOne + " = ?";
        }
        return numOne + sign + numTwo + "= ?";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Question questionToCompare = (Question) obj;
        return ((numOne == questionToCompare.getNumOne() && numTwo == questionToCompare.getNumTwo()) ||
                (numTwo == questionToCompare.getNumOne() && numOne == questionToCompare.getNumTwo()));
    }
}
