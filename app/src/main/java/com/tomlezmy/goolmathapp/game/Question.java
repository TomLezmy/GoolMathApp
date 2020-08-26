package com.tomlezmy.goolmathapp.game;

import android.content.Context;

import androidx.annotation.Nullable;

import com.tomlezmy.goolmathapp.R;

/**
 * This class holds the data of a question and it's answer
 */
public class Question {
    private ECategory category;
    protected String sign;
    protected int numOne;
    protected int numTwo;
    protected float result;
    private String questionHiddenAnswer;
    protected Context context;

    /**
     * This method creates a new instance of {@link Question}, if the current question contains fractions then an instance of {@link FractionQuestion} is created instead
     * @param category The current category
     * @param valueLimits The value limits of the question
     * @param level The current level
     * @param context The current context
     * @return A new instance of {@link Question}
     */
    public static Question createQuestion(ECategory category, LevelValueLimits valueLimits, int level, Context context) {
        if (category == ECategory.FRACTIONS || (category == ECategory.DECIMALS && level == 6)) {
            return new FractionQuestion(category, valueLimits, level, context);
        }
        return new Question(category, valueLimits, level, context);
    }

    /**
     * Protected class constructor. if you want to create an instance use {@link #createQuestion(ECategory, LevelValueLimits, int, Context)}
     * @param valueLimits The value limits of the question
     */
    protected Question(LevelValueLimits valueLimits) {
        this.numOne = valueLimits.getFirstNumberLimit().generateValue();
        this.numTwo = valueLimits.getSecondNumberLimit().generateValue();
    }

    /**
     * Protected class constructor. if you want to create an instance use {@link #createQuestion(ECategory, LevelValueLimits, int, Context)}
     * @param category The current category
     * @param valueLimits The value limits of the question
     * @param level The current level
     * @param context The current context
     */
    private Question(ECategory category, LevelValueLimits valueLimits, int level, Context context) {
        this.category = category;
        this.context = context;
        this.numOne = valueLimits.getFirstNumberLimit().generateValue();
        this.numTwo = valueLimits.getSecondNumberLimit().generateValue();
        buildQuestion(valueLimits, level);
    }

    /**
     * Calculates values for the question according to the current level and category
     * @param valueLimits The value limits of the question
     * @param level The current level
     */
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
                if (level == 1) {
                    while (numOne - numTwo < 0) {
                        numOne = valueLimits.getFirstNumberLimit().generateValue();
                        numTwo = valueLimits.getSecondNumberLimit().generateValue();
                    }
                }
                questionHiddenAnswer = numOne + sign + numTwo + "= ?";
                break;
            case MULTIPLICATION:
                this.result = this.numOne * this.numTwo;
                sign = "X";
                questionHiddenAnswer = numOne + sign + numTwo + "= ?";
                break;
            case DIVISION:
                if (level == 1 || level == 2) {
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
                sign = "/";
                // If numOne is bigger than 1000 then put commas in the number
                if (level == 3 && numOne >= 1000) {
                    String bigNumOne = numOne + "";
                    StringBuilder numOneWithCommas = new StringBuilder();
                    int numCount = 0;
                    for (int i = bigNumOne.length() - 1; i >= 0; i--) {
                        numCount++;
                        numOneWithCommas.append(bigNumOne.charAt(i));
                        if (numCount == 3) {
                            numCount = 0;
                            numOneWithCommas.append(',');
                        }
                    }
                    numOneWithCommas.reverse();
                    questionHiddenAnswer = numOneWithCommas.toString() + sign + numTwo + "= ?";
                }
                else {
                    questionHiddenAnswer = numOne + sign + numTwo + "= ?";
                }
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
                questionHiddenAnswer += "% " + context.getString(R.string.percent_of) + " " + numOne + " = ?";
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
                        questionHiddenAnswer = String.format(context.getString(R.string.decimal_question), numOne + "%");
                        break;
                    case 5:
                        result = (float)numOne / (float) numTwo;
                        result = Float.parseFloat(String.format("%.3f", result).replaceAll("0*$", ""));
                        questionHiddenAnswer = String.format(context.getString(R.string.decimal_question), numOne + "/" + numTwo);
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

    /**
     * @return Current question without the answer
     */
    public String getQuestionHiddenAnswer() {
        return questionHiddenAnswer;
    }

    /**
     * @return True if question and answer of object are the same as the current question
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        Question questionToCompare = (Question) obj;
        return ((numOne == questionToCompare.getNumOne() && numTwo == questionToCompare.getNumTwo()) ||
                (numTwo == questionToCompare.getNumOne() && numOne == questionToCompare.getNumTwo()));
    }
}
