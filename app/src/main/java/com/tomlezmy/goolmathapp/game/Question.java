package com.tomlezmy.goolmathapp.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Question {
    private ECategory category;
    private String sign;
    private int numOne;
    private int numTwo;
    private int result;

    public Question(ECategory category, int numOne, int numTwo) {
        this.category = category;
        this.numOne = numOne;
        this.numTwo = numTwo;
        switch (category) {
            case ADDITION:
                this.result = numOne + numTwo;
                sign = "+";
                break;
            case SUBTRACTION:
                this.result = numOne - numTwo;
                sign = "-";
                break;
            case MULTIPLICATION:
                this.result = numOne * numTwo;
                sign = "*";
                break;
            case DIVISION:
                this.result = numOne / numTwo;
                sign = "/";
                break;
        }

    }

    public ECategory getCategory() {
        return category;
    }

    public int getNumOne() {
        return numOne;
    }

    public int getNumTwo() {
        return numTwo;
    }

    public int getResult() {
        return result;
    }

    public String getQuestionHiddenAnswer() {
        return numOne + sign + numTwo + "= ?";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Question questionToCompare = (Question) obj;
        return (numOne == questionToCompare.getNumOne() && numTwo == questionToCompare.getNumTwo());
    }

    @NonNull
    @Override
    public String toString() {
        return numOne + sign + numTwo + "=" + result;
    }
}
