package com.tomlezmy.goolmathapp.activities;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.plattysoft.leonids.ParticleSystem;
import com.tomlezmy.goolmathapp.ButtonTouchAnimation;
import com.tomlezmy.goolmathapp.CustomAnimationDrawable;
import com.tomlezmy.goolmathapp.FileManager;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.fragments.ButtonsFragment;
import com.tomlezmy.goolmathapp.fragments.GameFinishedFragment;
import com.tomlezmy.goolmathapp.fragments.QuestionFragment;
import com.tomlezmy.goolmathapp.game.CategoryProgressData;
import com.tomlezmy.goolmathapp.game.ECategory;
import com.tomlezmy.goolmathapp.game.LevelManager;
import com.tomlezmy.goolmathapp.interfaces.IButtonFragmentAnswerListener;
import com.tomlezmy.goolmathapp.interfaces.IResultFragmentListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;
import me.toptas.fancyshowcase.listener.DismissListener;
import me.toptas.fancyshowcase.listener.OnCompleteListener;

public class GamePage extends AppCompatActivity implements IButtonFragmentAnswerListener, IResultFragmentListener {

    final String QUESTION_TAG = "QUESTION_TAG", BUTTONS_TAG = "BUTTONS_TAG", RESULT_TAG = "RESULT_TAG";
    float gameSpeed;
    int obstacleIndex = 0;
    int buttonFragmentColor, score = 0, category, level;
    QuestionFragment questionFragment;
    ButtonsFragment buttonsFragment;
    GameFinishedFragment gameFinishedFragment;
    boolean userAnswered = false;
    int[] objectImages = new int []{R.drawable.banana_peel, R.drawable.rock, R.drawable.puddle, R.drawable.wheel, R.drawable.log, R.drawable.hurdle, R.drawable.box, R.drawable.wheelbarrow, R.drawable.ladder};
    ValueAnimator valueAnimator;
    AnimationDrawable walkingAnimation, runningAnimation;
    CustomAnimationDrawable jumpAnimation, fallingAnimation;
    RelativeLayout buttonLayout, rootLayout;
    ImageView player, obstacle, backgroundOne, backgroundTwo;
    TextView scoreText, timerText;
    Button walkBtn, learnBtn, nextLevelBtn;
    float screenWidth, screenHeight, timeToCrash, linearValue, objectHeight, userAnswer;
    boolean beforeQuestion = true, gameRunning = false;
    LevelManager levelManager;
    Random rand;
    CountDownTimer countDownTimer;
    double valueDelta, framesPerMilliSec = (double)1000 / 60;
    MediaPlayer gameBackgroundRing;
    MediaPlayer clockTickingRing, correctRing, wrongRing;
    FileManager fileManager;
    List<Integer> weightsBeforeGame;
    CategoryProgressData categoryProgressData;
    SharedPreferences sharedPreferences;
    TextView tv_wood_sign;
    ImageView img_wood_sign;
    String currentLevelText = "";


    // For Tutorial Run
    boolean isTutorialRun = false, beforeTutorialQuestionPopup = true, endTutorialInitiated = false;
    ValueAnimator tutorialValueAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        fileManager = FileManager.getInstance(this);
        changeGameSpeed(1.5f);
        // Set background sound
        clockTickingRing = MediaPlayer.create(GamePage.this,R.raw.clock_ticking);
        gameBackgroundRing = MediaPlayer.create(GamePage.this,R.raw.bensound_cute);
        correctRing = MediaPlayer.create(GamePage.this,R.raw.correct_answer);
        wrongRing = MediaPlayer.create(GamePage.this,R.raw.wrong_answer);
        wrongRing.setVolume(0.4f, 0.4f);
        gameBackgroundRing.setVolume(0.5f,0.5f);
        gameBackgroundRing.setLooping(true);
        playSound(gameBackgroundRing);
        buttonFragmentColor = getResources().getColor(R.color.green, null);
        rand = new Random();
        walkBtn = findViewById(R.id.start_walk);
        learnBtn = findViewById(R.id.btn_go_to_learn);
        nextLevelBtn = findViewById(R.id.btn_next_level);
        buttonLayout = findViewById(R.id.button_fragment_layout);
        buttonLayout.setBackgroundColor(buttonFragmentColor);
        rootLayout = findViewById(R.id.root_layout);
        player = findViewById(R.id.player_image);
        player.setDrawingCacheEnabled(true);
        obstacle = findViewById(R.id.obstacle);
        backgroundOne = findViewById(R.id.background_one);
        backgroundTwo = findViewById(R.id.background_two);
        scoreText = findViewById(R.id.score_text);
        timerText = findViewById(R.id.timer_text);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        prepareAnimations("walk",gameSpeed);
        prepareAnimations("run",gameSpeed);
        prepareAnimations("fall",gameSpeed);
        // Prepare jump animation with faster game speed
        prepareAnimations("jump",2.5f);

        valueAnimator = ValueAnimator.ofFloat(0.0f, -1.0f);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration((int)(10000 / gameSpeed));
        linearValue = 0f;
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (beforeQuestion) {
                    if (obstacle.getX() > (player.getWidth() + 25)) {
                        obstacle.setX(obstacle.getX() - (3.4f * gameSpeed));
                    }
                    else {
                        // Was correct or not
                        beforeQuestion = false;
                        changeGameSpeed(2.5f);
                        if (userAnswered) {
                            animationResponse(levelManager.checkCorrectAnswer(userAnswer));
                        }
                        else {
                            removeQuestion();
                            animationResponse(false);
                        }
                        userAnswered = false;
                    }
                }
                else {
                    // Until object disappears to left screen continue moving it
                    if (obstacle.getX() > -obstacle.getWidth()) {
                        obstacle.setX(obstacle.getX() - (3.4f * gameSpeed));
                    }
                    else {
                        changeGameSpeed(1.5f);
                        nextQuestion();
                    }
                }
                //final float progress = (float) animation.getAnimatedValue();
                linearValue -= valueDelta;
                if (linearValue < -1) {
                    linearValue = 0;
                }
                final float progress = linearValue;
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;

                // Moving background
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX + width);
            }
        });



        // Get level and category
        category = getIntent().getIntExtra("category",0);
        level = getIntent().getIntExtra("level",0);

        learnBtn.setOnTouchListener(new ButtonTouchAnimation());
        nextLevelBtn.setOnTouchListener(new ButtonTouchAnimation());
        walkBtn.setOnTouchListener(new ButtonTouchAnimation());
        learnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GamePage.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("go_to","LearnSelect");
                startActivity(intent);
            }
        });

        nextLevelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score = 0;
                scoreText.setText(getString(R.string.score) + score);
                if (gameRunning) {
                    gameRunning = false;
                    fallingAnimation.stop();
                    walkingAnimation.stop();
                    player.setImageResource(R.drawable.good1);
                    valueAnimator.pause();
                    removeQuestion();
                    countDownTimer.cancel();
                    timerText.setText("");
                    // Move object out of screen
                    obstacle.setX(screenWidth);
                    walkBtn.setVisibility(View.VISIBLE);
                }

                level++;
                if (ECategory.values()[category].getNumberOfLevels() < level) {
                    level = 1;
                    category++;
                }
                // Check for last category
                if (ECategory.values().length == category) {
                    Toast.makeText(GamePage.this, getString(R.string.last_category_message), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // If user skipped, open level
                    categoryProgressData = fileManager.getUserData().getLevelsProgressData().get(ECategory.values()[category]).get(level - 1);
                    if (!categoryProgressData.isOpen()) {
                        categoryProgressData.setOpen(true);
                        fileManager.updateUserDataFile();
                    }
                    updateWoodSignOfCurrentLevel(category, level - 1);
                    tv_wood_sign.setText(currentLevelText);
                }
                tv_wood_sign.setVisibility(View.VISIBLE);
                img_wood_sign.setVisibility(View.VISIBLE);
            }
        });

        walkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameRunning = true;
                if (isTutorialRun) {
                    player.setImageDrawable(walkingAnimation);
                    walkingAnimation.start();
                    tutorialValueAnimator.start();
                    questionFragment = new QuestionFragment("1 + 1 = ?");
                    List<String> options = new ArrayList<>();
                    options.add("1");
                    options.add("2");
                    options.add("3");
                    options.add("4");
                    buttonsFragment = ButtonsFragment.newInstance(options, true);
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom).replace(R.id.button_fragment_layout, buttonsFragment, BUTTONS_TAG).commit();
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_top).replace(R.id.question_layout, questionFragment, QUESTION_TAG).commit();
                }
                else {
                    tv_wood_sign.setVisibility(View.GONE);
                    img_wood_sign.setVisibility(View.GONE);
                    if (!walkingAnimation.isRunning()) {
                        // Save Weights before game
                        weightsBeforeGame = new ArrayList<>(fileManager.getLevelWeights().get(ECategory.values()[category]).get(level - 1));
                        levelManager = new LevelManager(GamePage.this, 10, ECategory.values()[category], level);
                        obstacleIndex = rand.nextInt(objectImages.length);
                        obstacle.setImageResource(objectImages[obstacleIndex]);
                        player.setImageDrawable(walkingAnimation);
                        // Time in seconds until player reaches the obstacle, 16.665 = valueAnimator.duration / update rate
                        timeToCrash = ((player.getWidth() + 25) - obstacle.getX()) / (3.4f * gameSpeed) * -1 * 16.665f;
                        countDownTimer = new CountDownTimer((long) timeToCrash, 1000) {
                            public void onTick(long millisUntilFinished) {
                                // Set Clock ticking sound while count down timer
                                clockTickingRing.setVolume(1, 1);
                                playSound(clockTickingRing);
                                timerText.setText((millisUntilFinished / 1000) + "");
                            }

                            public void onFinish() {
                                timerText.setVisibility(View.INVISIBLE);
                                timerText.setText("");
                            }
                        };
                        timerText.setVisibility(View.VISIBLE);
                        countDownTimer.start();
                        walkingAnimation.start();
                        if (valueAnimator.isPaused()) {
                            valueAnimator.resume();
                        } else {
                            valueAnimator.start();
                        }
                        levelManager.generateQuestions();
                        showQuestion();
                    }
                }
                walkBtn.setVisibility(View.GONE);
            }
        });

        tutorialValueAnimator = ValueAnimator.ofFloat(0.0f, -1.0f);
        tutorialValueAnimator.setInterpolator(new LinearInterpolator());
        tutorialValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        tutorialValueAnimator.setDuration((int)(10000 / gameSpeed));
        linearValue = 0f;
        tutorialValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (beforeTutorialQuestionPopup) {
                    if (obstacle.getX() > (screenWidth - obstacle.getWidth())) {
                        obstacle.setX(obstacle.getX() - (3.4f * gameSpeed));
                    } else {
                        beforeTutorialQuestionPopup = false;
                        tutorialValueAnimator.pause();
                        walkingAnimation.stop();
                        FancyShowCaseView obstacleSc = new FancyShowCaseView.Builder(GamePage.this)
                                .focusOn(obstacle)
                                .title(getString(R.string.tutorial_obstacle)).titleStyle(R.style.TutorialFontLocalized, Gravity.TOP | Gravity.CENTER)
                                .build();
                        FancyShowCaseView questionSc = new FancyShowCaseView.Builder(GamePage.this)
                                .focusOn(questionFragment.getView())
                                .title(getString(R.string.tutorial_question)).titleStyle(R.style.TutorialFontLocalized, Gravity.TOP | Gravity.CENTER)
                                .build();
                        FancyShowCaseView buttonsSc = new FancyShowCaseView.Builder(GamePage.this)
                                .focusOn(buttonsFragment.getView()).focusShape(FocusShape.ROUNDED_RECTANGLE).roundRectRadius(90)
                                .title(getString(R.string.tutorial_options)).titleStyle(R.style.TutorialFontLocalized, Gravity.TOP | Gravity.CENTER)
                                .build();
                        FancyShowCaseQueue fancyShowCaseQueue = new FancyShowCaseQueue().add(obstacleSc).add(questionSc).add(buttonsSc);
                        fancyShowCaseQueue.setCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete() {
                                buttonsFragment.enableButtons();
                                timeToCrash = ((player.getWidth() + 25) - obstacle.getX()) / (3.4f*gameSpeed) * -1 * 16.665f;
                                countDownTimer =  new CountDownTimer((long)timeToCrash, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                        // Set Clock ticking sound while count down timer
                                        clockTickingRing.setVolume(1,1);
                                        playSound(clockTickingRing);
                                        timerText.setText((millisUntilFinished / 1000) + "");
                                    }

                                    public void onFinish() {
                                        timerText.setVisibility(View.INVISIBLE);
                                        timerText.setText("");
                                    }
                                };
                                timerText.setVisibility(View.VISIBLE);
                                countDownTimer.start();
                                tutorialValueAnimator.resume();
                                walkingAnimation.start();
                            }
                        });
                        fancyShowCaseQueue.show();
                    }
                }
                else {
                    if (beforeQuestion) {
                        if (obstacle.getX() > (player.getWidth() + 25)) {
                            obstacle.setX(obstacle.getX() - (3.4f * gameSpeed));
                        } else {
                            // Was correct or not
                            beforeQuestion = false;
                            changeGameSpeed(2.5f);
                            if (userAnswered) {
                                animationResponse(userAnswer == 2);
                            } else {
                                removeQuestion();
                                animationResponse(false);
                            }
                            userAnswered = false;
                        }
                    }
                    else {
                        // Until object disappears to left screen continue moving it
                        if (obstacle.getX() > -obstacle.getWidth()) {
                            obstacle.setX(obstacle.getX() - (3.4f * gameSpeed));
                        }
                        else {
                            changeGameSpeed(1.5f);
                            gameRunning = false;
                            player.setImageResource(R.drawable.good1);
                            tutorialValueAnimator.pause();
                            walkingAnimation.stop();
                            String answerResponse;
                            if (score == 1) {
                                answerResponse = getString(R.string.tutorial_correct_answer) + "\n";
                            } else {
                                answerResponse = getString(R.string.tutorial_wrong_answer) + "\n";
                            }
                            new ParticleSystem(GamePage.this, 100, R.drawable.star_pink, 3000)
                                    .setSpeedRange(0.2f, 0.5f)
                                    .oneShot(scoreText, 100);
                            FancyShowCaseView answerSc = new FancyShowCaseView.Builder(GamePage.this)
                                    .title(answerResponse + getString(R.string.tutorial_number_of_levels)).titleStyle(R.style.TutorialFontLocalized, Gravity.TOP | Gravity.CENTER).build();
                            FancyShowCaseView finishSc = new FancyShowCaseView.Builder(GamePage.this)
                                    .title(getString(R.string.tutorial_end)).titleStyle(R.style.TutorialFontLocalized, Gravity.TOP | Gravity.CENTER)
                                    .dismissListener(new DismissListener() {
                                        @Override
                                        public void onDismiss(String s) {
                                            finish();
                                        }

                                        @Override
                                        public void onSkipped(String s) {

                                        }
                                    }).build();
                            FancyShowCaseQueue fancyShowCaseQueue = new FancyShowCaseQueue().add(answerSc).add(finishSc);
                            fancyShowCaseQueue.show();
                        }
                    }
                }

                linearValue -= valueDelta;
                if (linearValue < -1) {
                    linearValue = 0;
                }
                final float progress = linearValue;
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;

                // Moving background
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX + width);
            }
        });

        if (sharedPreferences.getBoolean("is_first_run",true)) {
            sharedPreferences.edit().putBoolean("is_first_run",false).apply();
            tutorialRun();
        }
        else if (getIntent().hasExtra("tutorial")) {
            tutorialRun();
        }
        this.tv_wood_sign = findViewById(R.id.tv_wood_sign_level);
        this.img_wood_sign = findViewById(R.id.wood_sign_img);
        if (!isTutorialRun) {
            // Show sub game level before the game start
            updateWoodSignOfCurrentLevel(category, level - 1);
        }
        else {
            tv_wood_sign.setVisibility(View.GONE);
            img_wood_sign.setVisibility(View.GONE);
        }
    }

    private void tutorialRun() {
        isTutorialRun = true;
        FancyShowCaseView playerSc = new FancyShowCaseView.Builder(GamePage.this)
                .focusOn(player)
                .title(getString(R.string.tutorial_show_player)).titleStyle(R.style.TutorialFontLocalized, Gravity.TOP | Gravity.CENTER)
                .build();
        FancyShowCaseView noFocusSc = new FancyShowCaseView.Builder(GamePage.this)
                .title(getString(R.string.tutorial_game_objective)).titleStyle(R.style.TutorialFontLocalized, Gravity.TOP | Gravity.CENTER)
                .build();
        FancyShowCaseView scoreSc = new FancyShowCaseView.Builder(GamePage.this)
                .focusOn(scoreText)
                .title(getString(R.string.tutorial_score)).titleStyle(R.style.TutorialFontLocalized, Gravity.TOP | Gravity.CENTER)
                .build();
        FancyShowCaseView clockSc = new FancyShowCaseView.Builder(GamePage.this)
                .focusOn(findViewById(R.id.timer_image))
                .title(getString(R.string.tutorial_timer)).titleStyle(R.style.TutorialFontLocalized, Gravity.TOP | Gravity.CENTER)
                .build();
        FancyShowCaseView startSc = new FancyShowCaseView.Builder(GamePage.this)
                .focusOn(walkBtn)
                .title(getString(R.string.tutorial_start_button)).titleStyle(R.style.TutorialFontLocalized, Gravity.TOP | Gravity.CENTER)
                .build();
        FancyShowCaseQueue fancyShowCaseQueue = new FancyShowCaseQueue().add(playerSc).add(noFocusSc).add(scoreSc).add(clockSc).add(startSc);
        fancyShowCaseQueue.show();
    }

    private void prepareAnimations(String animation, final float speed) {
        switch (animation) {
            case "jump":
                AnimationDrawable jump = new AnimationDrawable();
                jump.addFrame(getResources().getDrawable(R.drawable.good1, null), (int)(200 / speed));
                jump.addFrame(getResources().getDrawable(R.drawable.good2, null), (int)(200 / speed));
                jump.addFrame(getResources().getDrawable(R.drawable.good3, null), (int)(200 / speed));
                jump.addFrame(getResources().getDrawable(R.drawable.good4, null), (int)(200 / speed));
                jump.addFrame(getResources().getDrawable(R.drawable.good5, null), (int)(450 / speed));
                jump.addFrame(getResources().getDrawable(R.drawable.good6, null), (int)(450 / speed));
                jump.addFrame(getResources().getDrawable(R.drawable.good7, null), (int)(200 / speed));
                jump.addFrame(getResources().getDrawable(R.drawable.good8, null), (int)(200 / speed));
                jump.addFrame(getResources().getDrawable(R.drawable.good9, null), (int)(200 / speed));

                jumpAnimation = new CustomAnimationDrawable(jump) {
                    @Override
                    public void onAnimationFinish() {
                        if (gameRunning) {
                            player.setImageDrawable(walkingAnimation);
                            walkingAnimation.start();
                        }
                    }

                    @Override
                    public void onAnimationStart() {
                        ObjectAnimator up = ObjectAnimator.ofFloat(player, "Y", objectHeight - 200);
                        up.setRepeatCount(1);
                        up.setStartDelay((int)(700 / speed));
                        up.setRepeatMode(ValueAnimator.REVERSE);
                        up.setDuration((int)(900 / speed));
                        up.start();
                    }
                };
                break;
            case "fall":
                AnimationDrawable fall = new AnimationDrawable();
                fall.addFrame(getResources().getDrawable(R.drawable.bad1, null), (int)(200 / speed));//Stand
                fall.addFrame(getResources().getDrawable(R.drawable.bad2, null), (int)(200 / speed));//Run
                fall.addFrame(getResources().getDrawable(R.drawable.bad3, null), (int)(200 / speed));//Run
                fall.addFrame(getResources().getDrawable(R.drawable.bad4, null), (int)(200 / speed));//Run
                fall.addFrame(getResources().getDrawable(R.drawable.bad5, null), (int)(200 / speed));//TouchFloor
                fall.addFrame(getResources().getDrawable(R.drawable.bad6, null), (int)(200 / speed));//SadFace
                fall.addFrame(getResources().getDrawable(R.drawable.bad7, null), (int)(200 / speed));//Trip
                fall.addFrame(getResources().getDrawable(R.drawable.bad8, null), (int)(200 / speed));//Trip
                fall.addFrame(getResources().getDrawable(R.drawable.bad9, null), (int)(400 / speed));//Ground
                fallingAnimation = new CustomAnimationDrawable(fall) {
                    @Override
                    public void onAnimationFinish() {
                        if (gameRunning) {
                            player.setImageDrawable(walkingAnimation);
                            walkingAnimation.start();
                        }
                    }

                    @Override
                    public void onAnimationStart() {
                        // Puddle splash
                        if (objectImages[obstacleIndex] == R.drawable.puddle) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AnimationDrawable splash = new AnimationDrawable();
                                    splash.addFrame(getResources().getDrawable(R.drawable.puddle_splash, null), 200);
                                    splash.addFrame(getResources().getDrawable(R.drawable.puddle_splash_2, null), 200);
                                    splash.addFrame(getResources().getDrawable(R.drawable.puddle_splash_3, null), 200);
                                    splash.addFrame(getResources().getDrawable(R.drawable.puddle_splash_4, null), 200);
                                    splash.addFrame(getResources().getDrawable(R.drawable.puddle, null), 200);
                                    splash.setOneShot(true);
                                    obstacle.setImageDrawable(splash);
                                    splash.start();
                                }
                            }, (int)(1600 / speed));
                        }
                    }
                };
                break;
            case "walk":
                walkingAnimation = new AnimationDrawable();
                walkingAnimation.addFrame(getResources().getDrawable(R.drawable.good1, null), (int)(200 / speed));
                walkingAnimation.addFrame(getResources().getDrawable(R.drawable.walk1, null), (int)(200 / speed));
                walkingAnimation.addFrame(getResources().getDrawable(R.drawable.good1, null), (int)(200 / speed));
                walkingAnimation.addFrame(getResources().getDrawable(R.drawable.walk2, null), (int)(200 / speed));
                walkingAnimation.setOneShot(false);
                break;
            case "run":
                runningAnimation = new AnimationDrawable();
                runningAnimation.addFrame(getResources().getDrawable(R.drawable.bad3, null), (int)(200 / speed));
                runningAnimation.addFrame(getResources().getDrawable(R.drawable.bad4, null), (int)(200 / speed));
                runningAnimation.addFrame(getResources().getDrawable(R.drawable.bad5, null), (int)(200 / speed));
                runningAnimation.setOneShot(false);
                break;
        }
    }

    private void nextQuestion() {
        beforeQuestion = true;
        obstacleIndex = rand.nextInt(objectImages.length);
        obstacle.setImageResource(objectImages[obstacleIndex]);
        // Move object out of screen
        obstacle.setX(screenWidth);

        // If there is a next question
        if (levelManager.nextQuestion()) {
            showQuestion();
            timeToCrash = ((player.getWidth() + 25) - obstacle.getX()) / (3.4f*gameSpeed) * -1 * 16.665f;
            timerText.setVisibility(View.VISIBLE);
            countDownTimer.start();
        }
        else {
            // Level finished
            gameRunning = false;
            fallingAnimation.stop();
            walkingAnimation.stop();
            player.setImageResource(R.drawable.good1);
            valueAnimator.pause();
            endLevelAndCheckResults();
        }
    }

    private void endLevelAndCheckResults() {
        nextLevelBtn.setVisibility(View.GONE);
        // update times played and high score
        categoryProgressData = fileManager.getUserData().getLevelsProgressData().get(ECategory.values()[category]).get(level - 1);
        categoryProgressData.setTimesPlayed(categoryProgressData.getTimesPlayed() + 1);
        if (categoryProgressData.getMaxScore() < score) {
            // New High Score
            categoryProgressData.setMaxScore(score);
            Toast.makeText(this, getString(R.string.new_highscore), Toast.LENGTH_SHORT).show();
        }
        fileManager.updateUserDataFile();

        boolean weightsAreEven = true;
        // level complete only when weights are even and correctAnswerCounter == 10
        int improvementCounter = 0, deteriorationCounter = 0;
        List<Integer> weightsAfterGame = fileManager.getLevelWeights().get(ECategory.values()[category]).get(level - 1);
        for (int i = 0; i < weightsAfterGame.size(); i++) {
            if (weightsAfterGame.get(i) != 1) {
                weightsAreEven = false;
            }
            if (weightsBeforeGame.get(i) < weightsAfterGame.get(i)) {
                deteriorationCounter++;
            }
            else if (weightsBeforeGame.get(i) > weightsAfterGame.get(i)) {
                improvementCounter++;
            }
        }
        boolean levelComplete = score == 10 && weightsAreEven;
        if (levelComplete) {
            openNextLevel();
        }
        gameFinishedFragment = new GameFinishedFragment(levelComplete, improvementCounter, deteriorationCounter, categoryProgressData);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_top).replace(R.id.result_layout, gameFinishedFragment, RESULT_TAG).commit();
        new ParticleSystem(GamePage.this, 100, R.drawable.star_pink, 3000)
                .setSpeedRange(0.2f, 0.5f)
                .oneShot(scoreText, 100);
    }

    @Override
    public void onReturn(float answer) {
        countDownTimer.cancel();
        timerText.setVisibility(View.INVISIBLE);
        timerText.setText("");
        changeGameSpeed(3.5f);
        player.setImageDrawable(runningAnimation);
        runningAnimation.start();
        removeQuestion();
        userAnswer = answer;
        userAnswered = true;
    }

    public void animationResponse(boolean wasCorrect) {
        // Uncomment|Comment this part to toggle learning mode
        // If weights weren't updated then there was no user improvement
        if (!isTutorialRun) {
            levelManager.updateWeightsFromUserAnswer(wasCorrect);
        }
        if (wasCorrect) {
            playSound(correctRing);
            score++;
            scoreText.setText(getString(R.string.score) + score);
            new ParticleSystem(this, 20, R.drawable.star_pink, 1000)
                    .setSpeedRange(0.2f, 0.5f)
                    .oneShot(scoreText, 200);
        }
        else {
            playSound(wrongRing);
        }

        if (wasCorrect) {
            // Change jump height for banana and rock
            if (objectImages[obstacleIndex] == R.drawable.ladder) {
                objectHeight = obstacle.getY();
            }
            else if (objectImages[obstacleIndex] != R.drawable.puddle) {
                objectHeight = obstacle.getY() + 200;
            }
            else {
                objectHeight = obstacle.getY() + 100;
            }
            player.setImageDrawable(jumpAnimation);
            jumpAnimation.start();
        }
        else {
            player.setImageDrawable(fallingAnimation);
            fallingAnimation.start();
        }
    }

    public void showQuestion() {
        questionFragment = new QuestionFragment(levelManager.getCurrentQuestion());

        int options = 4;
        boolean useTwoAnswers = rand.nextBoolean();
        if (useTwoAnswers) {
            options = 2;
        }
        buttonsFragment = ButtonsFragment.newInstance(levelManager.getCurrentQuestionOptions(options), false);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom).replace(R.id.button_fragment_layout, buttonsFragment, BUTTONS_TAG).commit();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_top).replace(R.id.question_layout, questionFragment, QUESTION_TAG).commit();
    }
    public void removeQuestion() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_out_top,R.anim.slide_out_top).remove(questionFragment)
                .setCustomAnimations(R.anim.slide_out_bottom, R.anim.slide_out_bottom).remove(buttonsFragment).commit();
    }

    private void playSound(MediaPlayer sound) {
        if (sharedPreferences.getBoolean("preference_enable_sound", true)) {
            sound.start();
        }
    }

    @Override
    protected void onPause() {
        if (valueAnimator.isRunning()) {
            valueAnimator.end();
            walkingAnimation.stop();
        }
        gameBackgroundRing.pause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        playSound(gameBackgroundRing);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (clockTickingRing != null) {
            clockTickingRing.release();
            clockTickingRing = null;
        }
        if (gameBackgroundRing != null) {
            gameBackgroundRing.release();
            gameBackgroundRing = null;
        }
        if (correctRing != null) {
            correctRing.release();
            gameBackgroundRing = null;
        }
        if (wrongRing != null) {
            wrongRing.release();
            wrongRing = null;
        }

        super.onDestroy();
    }

    private void changeGameSpeed(float speed) {
        gameSpeed = speed;
        valueDelta = (double)1 / (int)((int)(10000 / gameSpeed) / framesPerMilliSec);
    }

    @Override
    public void onPressBack() {
        finish();
    }

    @Override
    public void onPressContinue(boolean moveToNextLevel) {
        score = 0;
        scoreText.setText(getString(R.string.score) + score);
        if (moveToNextLevel) {
            level++;
            if (ECategory.values()[category].getNumberOfLevels() < level) {
                level = 1;
                category++;
            }
            // Check for last category
            if (ECategory.values().length == category) {
                Toast.makeText(this, getString(R.string.last_category_message), Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                // If user skipped, open level
                categoryProgressData = fileManager.getUserData().getLevelsProgressData().get(ECategory.values()[category]).get(level - 1);
                if (!categoryProgressData.isOpen()) {
                    categoryProgressData.setOpen(true);
                    fileManager.updateUserDataFile();
                }
                updateWoodSignOfCurrentLevel(category,level - 1);
                this.tv_wood_sign.setText(this.currentLevelText);
            }
        }
        walkBtn.setVisibility(View.VISIBLE);
        nextLevelBtn.setVisibility(View.VISIBLE);
        this.tv_wood_sign.setVisibility(View.VISIBLE);
        this.img_wood_sign.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_out_top,R.anim.slide_out_top).remove(gameFinishedFragment).commit();
    }

    private void openNextLevel() {
        categoryProgressData = fileManager.getUserData().getLevelsProgressData().get(ECategory.values()[category]).get(level - 1);
        if (!categoryProgressData.isFinished()) {
            categoryProgressData.setFinished(true);
            fileManager.updateUserDataFile();
        }
        int nextLevel = level + 1;
        int nextCategory = category;
        if (ECategory.values()[nextCategory].getNumberOfLevels() < nextLevel) {
            nextLevel = 1;
            nextCategory++;
        }

        // Check for last category
        if (ECategory.values().length != nextCategory) {
            // Open next level if needed
            categoryProgressData = fileManager.getUserData().getLevelsProgressData().get(ECategory.values()[nextCategory]).get(nextLevel - 1);
            if (!categoryProgressData.isOpen()) {
                categoryProgressData.setOpen(true);
                fileManager.updateUserDataFile();
            }
        }
    }

    void updateWoodSignOfCurrentLevel(int categoryId, int levelId) {
        switch (categoryId) {
            case 0:
                this.currentLevelText = getResources().getStringArray(R.array.practice_additionSubCategories)[levelId];
                break;
            case 1:
                this.currentLevelText = getResources().getStringArray(R.array.practice_subtractionSubCategories)[levelId];
                break;
            case 2:
                this.currentLevelText = getResources().getStringArray(R.array.practice_multiplicationSubCategories)[levelId];
                break;
            case 3:
                this.currentLevelText = getResources().getStringArray(R.array.practice_divisionSubCategories)[levelId];
                break;
            case 4:
                this.currentLevelText = getResources().getStringArray(R.array.practice_fractionsSubCategories)[levelId];
                break;
            case 5:
                this.currentLevelText = getResources().getStringArray(R.array.practice_percentsSubCategories)[levelId];
                break;
            case 6:
                this.currentLevelText = getResources().getStringArray(R.array.practice_decimalsSubCategories)[levelId];
                break;
        }
        this.tv_wood_sign.setText(this.currentLevelText);
    }
}

