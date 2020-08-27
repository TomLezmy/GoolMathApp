package com.tomlezmy.goolmathapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tomlezmy.goolmathapp.ButtonTouchAnimation;
import com.tomlezmy.goolmathapp.model.FileManager;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.game.ECategory;
import com.tomlezmy.goolmathapp.game.LimitFactory;
import com.tomlezmy.goolmathapp.game.ProbabilityGenerator;
import com.tomlezmy.goolmathapp.game.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * This activity Tests the user's knowledge and updates the question level weights
 */
public class FirstDiagnosisActivity extends AppCompatActivity implements Button.OnClickListener {
    String language;
    TextView questionText;
    String currentQuestion, currentAnswer;
    Random rand;
    List<String> currentOptions;
    Button[] buttons;
    int currentCategory = -1, currentQuestionIndex = -1, questionsCounter = 0, currentQuestionProgress = 0;
    Dictionary<ECategory, List<String>> questionsRepository;
    Dictionary<ECategory, List<String>> answersRepository;
    Dictionary<ECategory, List<List<String>>> optionsRepository;
    Dictionary<ECategory, List<Integer>> levelRepository;
    Dictionary<ECategory, List<Integer>> questionsProbabilityTableIndex;
    boolean stopWhenWrong = false, diagnosisDone = false;
    ProgressBar questionsProgressBar;
    FileManager fileManager;

    /**
     * For repository build
     */
    List<String> tempQuestions, tempAnswers;
    /**
     * For repository build
     */
    List<List<String>> tempOptions;
    /**
     * For repository build
     */
    List<Integer> levels, probabilityTableIndexes;

    /**
     * For user instruction
     */
    TextView tv_welcome, tv_instruction, tv_note, tv_right_answers, tv_wrong_answers, tvFinished, tv_funBegins, tv_goodLuck;
    Button btn_letsGo, btn_goToMenu, btn_skip;
    LinearLayout buttons_layout;

    int rightAnswerCount, wrongAnswerCount;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_diagnosis);

        tv_welcome = findViewById(R.id.welcome_tv);
        tv_instruction = findViewById(R.id.tv_instruction);
        tv_note = findViewById(R.id.tv_instruction_note);
        tv_right_answers = findViewById(R.id.tv_right_answers);
        tv_wrong_answers = findViewById(R.id.tv_wrong_answers);
        tvFinished = findViewById(R.id.tv_quiz_finished);
        tv_funBegins = findViewById(R.id.tv_fun_begins);
        tv_goodLuck = findViewById(R.id.tv_good_luck);
        questionText = findViewById(R.id.question_text);
        btn_letsGo = findViewById(R.id.btn_lets_go);
        btn_goToMenu = findViewById(R.id.btn_go_to_menue);
        btn_skip = findViewById(R.id.btn_skip);
        buttons_layout= findViewById(R.id.buttons_layout);
        questionsProgressBar = findViewById(R.id.question_progress);

        this.language = Locale.getDefault().getDisplayLanguage();
        if ( this.language.equalsIgnoreCase("English")) {
            questionText.setTextSize(50f);
        }

        Resources res = getResources();
        String username =  getIntent().getStringExtra("first_name");
        String text = String.format(res.getString(R.string.first_diagnostic_instructions), username);
        tv_instruction.setText(text);

        rand = new Random();
        buttons = new Button[4];

        btn_letsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_letsGo.setVisibility(View.GONE);
                tv_welcome.setVisibility(View.GONE);
                tv_goodLuck.setVisibility(View.GONE);
                tv_instruction.setVisibility(View.GONE);
                tv_note.setVisibility(View.GONE);
                questionText.setVisibility(View.VISIBLE);
                buttons_layout.setVisibility(View.VISIBLE);
            }
        });
        buttons[0] = findViewById(R.id.btn_answer_1);
        buttons[0].setOnTouchListener(new ButtonTouchAnimation());
        buttons[0].setOnClickListener(this);
        buttons[1] = findViewById(R.id.btn_answer_2);
        buttons[1].setOnTouchListener(new ButtonTouchAnimation());
        buttons[1].setOnClickListener(this);
        buttons[2] = findViewById(R.id.btn_answer_3);
        buttons[2].setOnTouchListener(new ButtonTouchAnimation());
        buttons[2].setOnClickListener(this);
        buttons[3] = findViewById(R.id.btn_answer_4);
        buttons[3].setOnTouchListener(new ButtonTouchAnimation());
        buttons[3].setOnClickListener(this);
        fileManager = FileManager.getInstance(this);
        fileManager.createNewWeightsFile();

        buildQuestionsRepository((Calendar.getInstance().get(Calendar.YEAR) - getIntent().getIntExtra("birth_year",0) > 10));
        for (ECategory category : ECategory.values()) {
            questionsCounter += questionsRepository.get(category).size();
        }
        questionsProgressBar.setMax(questionsCounter);

        rightAnswerCount = 0;
        wrongAnswerCount = 0;
        nextQuestion(true);

        btn_skip.setOnTouchListener(new ButtonTouchAnimation());
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FirstDiagnosisActivity.this);
                builder.setTitle(getString(R.string.skip_title));
                builder.setMessage(getString(R.string.skip_dialog_message));
                builder.setCancelable(false);
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fileManager.createNewUserDataFile(getIntent().getStringExtra("first_name"),getIntent().getIntExtra("birth_year",0));
                        Intent intent = new Intent(FirstDiagnosisActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    /**
     * Moves to next question and displays the question
     * @param lastQuestionWasCorrect if user answered last question correctly
     */
    private void nextQuestion(boolean lastQuestionWasCorrect) {
        // At the start of the diagnosis currentCategory and currentQuestionIndex will be -1
        if (currentCategory == -1 && currentQuestionIndex == -1) {
            currentCategory = 0;
            currentQuestionIndex = 0;
        }
        else {
            // Move to next category if user was wrong
            if ((!lastQuestionWasCorrect) && stopWhenWrong) {
                currentQuestionProgress += questionsRepository.get(ECategory.values()[currentCategory]).size() - currentQuestionIndex;
                questionsProgressBar.setProgress(currentQuestionProgress,true);
                moveCategory();
            }
            else {
                moveQuestionIndex();
                questionsProgressBar.setProgress(++currentQuestionProgress,true);
            }
        }

        if (!diagnosisDone) {
            currentQuestion = questionsRepository.get(ECategory.values()[currentCategory]).get(currentQuestionIndex);
            currentAnswer = answersRepository.get(ECategory.values()[currentCategory]).get(currentQuestionIndex);
            currentOptions = optionsRepository.get(ECategory.values()[currentCategory]).get(currentQuestionIndex);
            randomizeOptions();
            setOptions();
            questionText.startAnimation(AnimationUtils.makeInAnimation(this, true));
            questionText.setText(currentQuestion);
        }
        else {
            showDiagnosisResults();
        }
    }


    /**
     * Moves to next question in category.<br/>If last question in category then move category
     */
    private void moveQuestionIndex() {
        currentQuestionIndex++;
        if (currentCategory == ECategory.values().length) {
            diagnosisDone = true;
            return;
        }
        if (questionsRepository.get(ECategory.values()[currentCategory]).size() == currentQuestionIndex) {
            moveCategory();
        }
    }

    /**
     * Moves to next category
     */
    private void moveCategory() {
        currentQuestionIndex = 0;
        currentCategory++;
        if (currentCategory == ECategory.values().length) {
            diagnosisDone = true;
            return;
        }
        // Set stopWhenWrong to true for category's you want to move from when the user makes a mistake
        if (ECategory.values()[currentCategory] == ECategory.PERCENTS) {
            stopWhenWrong = true;
        }
    }

    /**
     * Puts current question options in option buttons
     */
    private void setOptions() {
        for (int i = 0; i < 4; i++) {
            buttons[i].setText(currentOptions.get(i));
        }
    }

    /**
     * Takes the current question options list and randomizes the order
     */
    private void randomizeOptions() {
        for (int i = currentOptions.size() - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            String temp = currentOptions.get(index);
            currentOptions.set(index, currentOptions.get(i));
            currentOptions.set(i, temp);
        }
    }


    /**
     * Used to check if user picked the correct answer to the question
     * @param v The question option button pressed
     */
    @Override
    public void onClick(View v) {
        if (((Button)v).getText().toString().equals(currentAnswer)) {
            // Correct
            rightAnswerCount++;
            nextQuestion(true);
        }
        else {
            // False
            wrongAnswerCount++;
            // Update relevant cell
            int subLevelWeight = fileManager.getSubLevelWeight(ECategory.values()[currentCategory],levelRepository.get(ECategory.values()[currentCategory]).get(currentQuestionIndex), questionsProbabilityTableIndex.get(ECategory.values()[currentCategory]).get(currentQuestionIndex));
            fileManager.updateSubLevelWeight(ECategory.values()[currentCategory], levelRepository.get(ECategory.values()[currentCategory]).get(currentQuestionIndex), questionsProbabilityTableIndex.get(ECategory.values()[currentCategory]).get(currentQuestionIndex), ++subLevelWeight);
            nextQuestion(false);
        }
    }

    /**
     * This method sets all questions, answers and answer options for each level and category. Then puts then in hash tables for future use
     * @param isOverTen If user age is above ten then single number addition and subtraction questions are not included in the repositories
     */
    private void buildQuestionsRepository(boolean isOverTen) {
        questionsRepository = new Hashtable<>();
        answersRepository = new Hashtable<>();
        optionsRepository = new Hashtable<>();
        levelRepository = new Hashtable<>();
        questionsProbabilityTableIndex = new Hashtable<>();

        // Addition
        tempQuestions = new ArrayList<>();
        tempAnswers = new ArrayList<>();
        tempOptions = new ArrayList<>();
        levels = new ArrayList<>();
        probabilityTableIndexes = new ArrayList<>();
        if (!isOverTen) {
            addToRepositories("3 + 5 = ?", "8", Arrays.asList("6","8","5","9"), 2, 0);
            addToRepositories("2 + 6 = ?", "8", Arrays.asList("6","8","4","7"), 2, 0);
        }
        addToRepositories("10 + 20 = ?", "30", Arrays.asList("30","25","40","31"), 0, 1);
        addToRepositories("30 + 15 = ?", "45", Arrays.asList("33","45","35","40"), 0, 1);
        addToRepositories("40 + 26 = ?", "66", Arrays.asList("66","64","56","60"), 4, 1);
        questionsRepository.put(ECategory.ADDITION, tempQuestions);
        optionsRepository.put(ECategory.ADDITION, tempOptions);
        answersRepository.put(ECategory.ADDITION, tempAnswers);
        questionsProbabilityTableIndex.put(ECategory.ADDITION, probabilityTableIndexes);
        levelRepository.put(ECategory.ADDITION, levels);

        // Subtraction
        tempQuestions = new ArrayList<>();
        tempAnswers = new ArrayList<>();
        tempOptions = new ArrayList<>();
        levels = new ArrayList<>();
        probabilityTableIndexes = new ArrayList<>();
        if (!isOverTen) {
            addToRepositories("7 - 4 = ?", "3", Arrays.asList("3", "4", "2", "1"), 2, 0);
            addToRepositories("9 - 8 = ?", "1", Arrays.asList("3", "0", "2", "1"), 1, 0);
        }
        addToRepositories("40 - 30 = ?", "10", Arrays.asList("9","10","11","8"), 4, 1);
        addToRepositories("86 - 50 = ?", "36", Arrays.asList("36","26","30","46"), 8, 1);
        addToRepositories("27 - 13 = ?", "14", Arrays.asList("14","15","12","16"), 0, 1);
        addToRepositories("28 - 19 = ?", "9", Arrays.asList("9","8","10","7"), 0, 1);
        addToRepositories("78 - 39 = ?", "39", Arrays.asList("39","38","28","49"), 8, 1);
        questionsRepository.put(ECategory.SUBTRACTION, tempQuestions);
        optionsRepository.put(ECategory.SUBTRACTION, tempOptions);
        answersRepository.put(ECategory.SUBTRACTION, tempAnswers);
        questionsProbabilityTableIndex.put(ECategory.SUBTRACTION, probabilityTableIndexes);
        levelRepository.put(ECategory.SUBTRACTION, levels);

        // Multiplication
        tempQuestions = new ArrayList<>();
        tempAnswers = new ArrayList<>();
        tempOptions = new ArrayList<>();
        levels = new ArrayList<>();
        probabilityTableIndexes = new ArrayList<>();
        addToRepositories("2 * 3 = ?", "6", Arrays.asList("6","3","5","4"), 1, 0);
        addToRepositories("3 * 4 = ?", "12", Arrays.asList("7","12","13","10"), 1, 0);
        addToRepositories("5 * 4 = ?", "20", Arrays.asList("20","24","25","15"), 3, 1);
        addToRepositories("6 * 5 = ?", "30", Arrays.asList("30","35","25","20"), 5, 1);
        addToRepositories("9 * 5 = ?", "45", Arrays.asList("45","35","40","50"), 5, 1);
        addToRepositories("3 * 6 = ?", "18", Arrays.asList("18","12","19","17"), 5, 1);
        addToRepositories("6 * 6 = ?", "36", Arrays.asList("39","36","33","30"), 4, 1);
        addToRepositories("7 * 6 = ?", "42", Arrays.asList("42","40","36","39"), 4, 1);
        addToRepositories("3 * 7 = ?", "21", Arrays.asList("21","22","16","19"), 5, 1);
        addToRepositories("5 * 7 = ?", "35", Arrays.asList("30","35","25","34"), 5, 1);
        addToRepositories("7 * 7 = ?", "49", Arrays.asList("49","42","47","46"), 4, 1);
        addToRepositories("20 * 8 = ?", "160", Arrays.asList("160","150","158","166"), 1, 3);
        addToRepositories("60 * 9 = ?", "540", Arrays.asList("540","500","440","550"), 3, 3);
        addToRepositories("90 * 5 = ?", "450", Arrays.asList("540","450","500","550"), 3, 3);
        addToRepositories("200 * 8 = ?", "1600", Arrays.asList("1600","1500","1580","1660"), 1, 4);
        addToRepositories("600 * 9 = ?", "5400", Arrays.asList("5400","5000","4400","5500"), 3, 4);
        addToRepositories("800 * 7 = ?", "5600", Arrays.asList("5400","5600","4400","4600"), 3, 4);
        questionsRepository.put(ECategory.MULTIPLICATION, tempQuestions);
        optionsRepository.put(ECategory.MULTIPLICATION, tempOptions);
        answersRepository.put(ECategory.MULTIPLICATION, tempAnswers);
        questionsProbabilityTableIndex.put(ECategory.MULTIPLICATION, probabilityTableIndexes);
        levelRepository.put(ECategory.MULTIPLICATION, levels);

        // Division
        tempQuestions = new ArrayList<>();
        tempAnswers = new ArrayList<>();
        tempOptions = new ArrayList<>();
        levels = new ArrayList<>();
        probabilityTableIndexes = new ArrayList<>();
        addToRepositories("25 / 5 = ?", "5", Arrays.asList("6","5","3","4"), 0, 0);
        addToRepositories("6 / 3 = ?", "2", Arrays.asList("2","1","3","4"), 0, 0);
        addToRepositories("8 / 2 = ?", "4", Arrays.asList("2","1","3","4"), 0, 0);
        addToRepositories("24 / 6 = ?", "4", Arrays.asList("6","5","3","4"), 0, 0);
        addToRepositories("16 / 8 = ?", "2", Arrays.asList("2","1","3","4"), 0, 0);
        addToRepositories("12 / 2 = ?", "6", Arrays.asList("6","2","9","8"), 1, 0);
        addToRepositories("81 / 9 = ?", "9", Arrays.asList("6","10","9","8"), 1, 0);
        addToRepositories("54 / 9 = ?", "6", Arrays.asList("6","5","9","7"), 1, 0);
        addToRepositories("42 / 6 = ?", "7", Arrays.asList("6","8","9","7"), 1, 0);
        addToRepositories("27 / 3 = ?", "9", Arrays.asList("6","8","9","7"), 1, 0);
        addToRepositories("18 / 2 = ?", "9", Arrays.asList("6","8","9","7"), 1, 0);
        addToRepositories("90 / 3 = ?", "30", Arrays.asList("30","20","33","23"), 1, 2);
        addToRepositories("40 / 2 = ?", "20", Arrays.asList("30","20","32","22"), 0, 2);
        addToRepositories("80 / 2 = ?", "40", Arrays.asList("40","20","42","22"), 1, 2);
        addToRepositories("60 / 3 = ?", "20", Arrays.asList("40","20","43","23"), 1, 2);
        addToRepositories("250 / 5 = ?", "50", Arrays.asList("50","55","25","45"), 0, 3);
        addToRepositories("240 / 6 = ?", "40", Arrays.asList("50","40","35","45"), 0, 3);
        addToRepositories("120 / 2 = ?", "60", Arrays.asList("50","40","60","80"), 0, 3);
        addToRepositories("160 / 8 = ?", "20", Arrays.asList("20","30","23","33"), 0, 3);
        addToRepositories("180 / 2 = ?", "90", Arrays.asList("90","80","70","60"), 0, 3);
        addToRepositories("270 / 3 = ?", "90", Arrays.asList("90","80","70","60"), 0, 3);
        addToRepositories("420 / 6 = ?", "70", Arrays.asList("50","80","70","60"), 0, 3);
        addToRepositories("540 / 9 = ?", "60", Arrays.asList("50","80","70","60"), 1, 3);
        addToRepositories("810 / 9 = ?", "90", Arrays.asList("90","80","70","60"), 1, 3);
        questionsRepository.put(ECategory.DIVISION, tempQuestions);
        optionsRepository.put(ECategory.DIVISION, tempOptions);
        answersRepository.put(ECategory.DIVISION, tempAnswers);
        questionsProbabilityTableIndex.put(ECategory.DIVISION, probabilityTableIndexes);
        levelRepository.put(ECategory.DIVISION, levels);
        // Fractions
        tempQuestions = new ArrayList<>();
        tempAnswers = new ArrayList<>();
        tempOptions = new ArrayList<>();
        levels = new ArrayList<>();
        probabilityTableIndexes = new ArrayList<>();
        addToRepositories("1 / 2 = ?", "3 / 6", Arrays.asList("3 / 6","2 / 6","4 / 2","3 / 4"), 0, 1);
        addToRepositories("1 / 3 = ?", "3 / 9", Arrays.asList("3 / 6","3 / 9","4 / 6","6 / 9"), 0, 1);
        addToRepositories("1 / 4 = ?", "2 / 8", Arrays.asList("2 / 8","3 / 6","4 / 8","6 / 9"), 0, 1);
        addToRepositories("1 / 5 = ?", "5 / 25", Arrays.asList("5 / 25","2 / 20","10 / 20","5 / 20"), 0, 1);
        addToRepositories("2 / 4 = ?", "1 / 2", Arrays.asList("1 / 2","1 / 4","3 / 2","1 / 3"), 0, 3);
        addToRepositories("6 / 8 = ?", "3 / 4", Arrays.asList("1 / 2","3 / 4","1 / 4","1 / 3"), 0, 4);
        addToRepositories("2 / 6 = ?", "1 / 3", Arrays.asList("1 / 2","2 / 3","1 / 4","1 / 3"), 0, 3);
        addToRepositories("3 / 9 = ?", "1 / 3", Arrays.asList("1 / 5","2 / 3","1 / 4","1 / 3"), 0, 3);
        addToRepositories("12 / 24 = ?", "1 / 2", Arrays.asList("3 / 4","2 / 3","1 / 4","1 / 2"), 0, 3);
        questionsRepository.put(ECategory.FRACTIONS, tempQuestions);
        optionsRepository.put(ECategory.FRACTIONS, tempOptions);
        answersRepository.put(ECategory.FRACTIONS, tempAnswers);
        questionsProbabilityTableIndex.put(ECategory.FRACTIONS, probabilityTableIndexes);
        levelRepository.put(ECategory.FRACTIONS, levels);

        // Percents
        tempQuestions = new ArrayList<>();
        tempAnswers = new ArrayList<>();
        tempOptions = new ArrayList<>();
        levels = new ArrayList<>();
        probabilityTableIndexes = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            ProbabilityGenerator probabilityGenerator = LimitFactory.getLevelValuesAndProbabilities(this, ECategory.PERCENTS, i);
            int index = probabilityGenerator.getLevelValueLimitIndexByProbability();
            Question question = Question.createQuestion(ECategory.PERCENTS,probabilityGenerator.getLevelValueLimit(index).getLevelValueLimits(), i, this);
            addToRepositories(question.getQuestionHiddenAnswer(), ((int)question.getResult()) + "", LimitFactory.createQuestionOptions(ECategory.PERCENTS, i - 1, 4, question), index, i - 1);
            do {
                index = probabilityGenerator.getLevelValueLimitIndexByProbability();
                question = Question.createQuestion(ECategory.PERCENTS, probabilityGenerator.getLevelValueLimit(index).getLevelValueLimits(), i, this);
            } while (tempQuestions.get(tempQuestions.size() - 1).equals(question.getQuestionHiddenAnswer()));
            addToRepositories(question.getQuestionHiddenAnswer(), ((int)question.getResult()) + "", LimitFactory.createQuestionOptions(ECategory.PERCENTS, i - 1, 4, question), index, i - 1);
        }
        questionsRepository.put(ECategory.PERCENTS, tempQuestions);
        optionsRepository.put(ECategory.PERCENTS, tempOptions);
        answersRepository.put(ECategory.PERCENTS, tempAnswers);
        questionsProbabilityTableIndex.put(ECategory.PERCENTS, probabilityTableIndexes);
        levelRepository.put(ECategory.PERCENTS, levels);

        // Decimals
        tempQuestions = new ArrayList<>();
        tempAnswers = new ArrayList<>();
        tempOptions = new ArrayList<>();
        levels = new ArrayList<>();
        probabilityTableIndexes = new ArrayList<>();
        ProbabilityGenerator probabilityGenerator = LimitFactory.getLevelValuesAndProbabilities(this, ECategory.DECIMALS, 1);
        int index = probabilityGenerator.getLevelValueLimitIndexByProbability();
        Question question = Question.createQuestion(ECategory.DECIMALS,probabilityGenerator.getLevelValueLimit(index).getLevelValueLimits(), 1, this);
        addToRepositories(question.getQuestionHiddenAnswer(), question.getResult() + "", LimitFactory.createQuestionOptions(ECategory.DECIMALS, 0, 4, question), index, 0);
        do {
            index = probabilityGenerator.getLevelValueLimitIndexByProbability();
            question = Question.createQuestion(ECategory.DECIMALS,probabilityGenerator.getLevelValueLimit(index).getLevelValueLimits(), 1, this);
        } while (tempQuestions.contains(question.getQuestionHiddenAnswer()));
        addToRepositories(question.getQuestionHiddenAnswer(), question.getResult() + "", LimitFactory.createQuestionOptions(ECategory.DECIMALS, 0, 4, question), index, 0);
        do {
            index = probabilityGenerator.getLevelValueLimitIndexByProbability();
            question = Question.createQuestion(ECategory.DECIMALS,probabilityGenerator.getLevelValueLimit(index).getLevelValueLimits(), 2, this);
        } while (tempQuestions.contains(question.getQuestionHiddenAnswer()));
        addToRepositories(question.getQuestionHiddenAnswer(), question.getResult() + "", LimitFactory.createQuestionOptions(ECategory.DECIMALS, 1, 4, question), index, 1);
        do {
            index = probabilityGenerator.getLevelValueLimitIndexByProbability();
            question = Question.createQuestion(ECategory.DECIMALS,probabilityGenerator.getLevelValueLimit(index).getLevelValueLimits(), 2, this);
        } while (tempQuestions.contains(question.getQuestionHiddenAnswer()));
        addToRepositories(question.getQuestionHiddenAnswer(), question.getResult() + "", LimitFactory.createQuestionOptions(ECategory.DECIMALS, 1, 4, question), index, 1);
        questionsRepository.put(ECategory.DECIMALS, tempQuestions);
        optionsRepository.put(ECategory.DECIMALS, tempOptions);
        answersRepository.put(ECategory.DECIMALS, tempAnswers);
        questionsProbabilityTableIndex.put(ECategory.DECIMALS, probabilityTableIndexes);
        levelRepository.put(ECategory.DECIMALS, levels);
    }


    /**
     * Adds all question information to the temp repositories
     * @param question The question displayed to the user
     * @param answer The correct answer
     * @param optionsList The answer options
     * @param probabilityIndex The weight table index of the question
     * @param level The question level
     */
    private void addToRepositories(String question, String answer, List<String> optionsList, int probabilityIndex, int level) {
        tempQuestions.add(question);
        tempAnswers.add(answer);
        tempOptions.add(optionsList);
        probabilityTableIndexes.add(probabilityIndex);
        levels.add(level);
    }

    /**
     * Shows the user his result at the end of the diagnosis
     */
    private void showDiagnosisResults() {
        Resources res = getResources();
        String rightAnswerMsg;
        String wrongAnswerMsg;
        buttons_layout.setVisibility(View.GONE);
        questionText.setVisibility(View.GONE);

        tvFinished.setVisibility(View.VISIBLE);

        rightAnswerMsg = String.format(res.getString(R.string.msg_right_answer), rightAnswerCount);
        tv_right_answers.setText(rightAnswerMsg);

        wrongAnswerMsg = String.format(res.getString(R.string.msg_wrong_answer), wrongAnswerCount);
        tv_wrong_answers.setText(wrongAnswerMsg);


        Animation slideIn_first = AnimationUtils.makeInAnimation(this, true);
        Animation slideIn_second = AnimationUtils.makeInAnimation(this, true);
        slideIn_first.setDuration(500);
        slideIn_second.setDuration(500);
        slideIn_second.setStartOffset(1000);
        tv_right_answers.startAnimation(slideIn_first);
        tv_right_answers.setVisibility(View.VISIBLE);

        tv_wrong_answers.setVisibility(View.VISIBLE);
        tv_wrong_answers.startAnimation(slideIn_second);

        tv_funBegins.setVisibility(View.GONE);
        Animation pulse = AnimationUtils.loadAnimation(this,R.anim.pulse);
        pulse.setStartOffset(1000);
        tv_funBegins.startAnimation(pulse);
        tv_funBegins.setVisibility(View.VISIBLE);

        btn_goToMenu.setVisibility(View.VISIBLE);
        btn_goToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileManager.createNewUserDataFile(getIntent().getStringExtra("first_name"),getIntent().getIntExtra("birth_year",0));
                Intent intent = new Intent(FirstDiagnosisActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}