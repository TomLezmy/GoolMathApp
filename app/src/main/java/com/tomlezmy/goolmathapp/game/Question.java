package com.tomlezmy.goolmathapp.game;

import androidx.annotation.Nullable;

public class Question {
    private ECategory category;
    protected String sign;
    protected int numOne;
    protected int numTwo;
    protected float result;
    private String questionHiddenAnswer;

    public static Question createQuestion(ECategory category, LevelValueLimits valueLimits, int level) {
        if (category == ECategory.FRACTIONS || (category == ECategory.DECIMALS && level == 6)) {
            return new FractionQuestion(category, valueLimits, level);
        }
        return new Question(category, valueLimits, level);
    }

    protected Question(LevelValueLimits valueLimits) {
        this.numOne = valueLimits.getFirstNumberLimit().generateValue();
        this.numTwo = valueLimits.getSecondNumberLimit().generateValue();
    }

    private Question(ECategory category, LevelValueLimits valueLimits, int level) {
        this.category = category;
        this.numOne = valueLimits.getFirstNumberLimit().generateValue();
        this.numTwo = valueLimits.getSecondNumberLimit().generateValue();
        buildQuestion(valueLimits, level);
    }

    private void buildQuestion(LevelValueLimits valueLimits, int level) {
        switch (category) {
            case ADDITION:
                this.result = this.numOne + this.numTwo;
                sign = "+";
                questionHiddenAnswer = numOne + sign + numTwo + "= ?";
                break;
            case SUBTRACTION:
                this.result = this.numOne - this.numTwo;
                sign = "-";
                questionHiddenAnswer = numOne + sign + numTwo + "= ?";
                break;
            case MULTIPLICATION:
                this.result = this.numOne * this.numTwo;
                sign = "*";
                questionHiddenAnswer = numOne + sign + numTwo + "= ?";
                break;
            case DIVISION:
                if (level == 1 && level == 2) {
                    int temp = numOne * numTwo;
                    result = numOne;
                    numOne = temp;
                }
                else {
                    while (numOne % numTwo != 0) {
                        this.numOne = valueLimits.getFirstNumberLimit().generateValue();
                        this.numTwo = valueLimits.getSecondNumberLimit().generateValue();
                    }
                    this.result = ((float) this.numOne / (float) this.numTwo);
                }
//                if ((int)result != result) {
//                    result = Float.parseFloat(String.format("%.3f", result).replaceAll("0*$", ""));
//                }
                sign = "/";
                questionHiddenAnswer = numOne + sign + numTwo + "= ?";
                break;
            case PERCENTS:
                int percentDenominator = 100, percentNumerator = numTwo;

                // Needs to be 33.3333, can't pass it normally because numTwo and limitValues are integers
                if (level == 4 && numTwo == 33) {
                    percentDenominator = 3;
                    percentNumerator = 1;
                    questionHiddenAnswer = "33.3333";
                }
                else {
                    questionHiddenAnswer = numTwo + "";
                }

                result = (float)(numOne * percentNumerator) / percentDenominator;
                while ((int)result != result) {
                    numOne = valueLimits.getFirstNumberLimit().generateValue();
                    result = (float)(numOne * percentNumerator) / percentDenominator;
                }

                sign = "*";
                questionHiddenAnswer += "% of " + numOne + " = ?";
                break;
            case DECIMALS:
                switch (level) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        result = (float) numOne / 100;
                        if ((int)result != result) {
                            result = Float.parseFloat(String.format("%.3f", result).replaceAll("0*$", ""));
                        }
                        questionHiddenAnswer = "What is " + numOne + "% in decimal?";
                        break;
                    case 5:
                        result = (float)numOne / (float) numTwo;
                        result = Float.parseFloat(String.format("%.3f", result).replaceAll("0*$", ""));
                        questionHiddenAnswer = "What is " + numOne + "/" + numTwo + " in decimal?";
                        break;
                }
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
        return questionHiddenAnswer;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Question questionToCompare = (Question) obj;
        return ((numOne == questionToCompare.getNumOne() && numTwo == questionToCompare.getNumTwo()) ||
                (numTwo == questionToCompare.getNumOne() && numOne == questionToCompare.getNumTwo()));
    }
}
