package com.tomlezmy.goolmathapp.activities;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
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
import com.tomlezmy.goolmathapp.interfaces.SendMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;
import me.toptas.fancyshowcase.listener.DismissListener;
import me.toptas.fancyshowcase.listener.OnCompleteListener;

public class GamePage extends AppCompatActivity implements IButtonFragmentAnswerListener, SendMessage, IResultFragmentListener {

    final String QUESTION_TAG = "QUESTION_TAG", BUTTONS_TAG = "BUTTONS_TAG", RESULT_TAG = "RESULT_TAG";
    float gameSpeed;
    int obstacleIndex = 0; // 0 = banana 1 = rock 2 = puddle
    int buttonFragmentColor, score = 0, correctAnswerCounter = 0, category, level, collectablesAmount;
    QuestionFragment questionFragment;
    ButtonsFragment buttonsFragment;
    GameFinishedFragment gameFinishedFragment;
    boolean userAnswered = false;
    int[] collectableImages = new int[] {R.drawable.orange, R.drawable.apple1, R.drawable.apple2, R.drawable.pineapple, R.drawable.persimmon, R.drawable.peach, R.drawable.plum, R.drawable.cherry, R.drawable.strawberry, R.drawable.pomegranate};
    int[] objectImages = new int []{R.drawable.banana_peel, R.drawable.rock, R.drawable.puddle};
    ValueAnimator valueAnimator;
    AnimationDrawable walkingAnimation, runningAnimation;
    CustomAnimationDrawable jumpAnimation, fallingAnimation;
    RelativeLayout buttonLayout, rootLayout;
    ImageView player, obstacle, backgroundOne, backgroundTwo, collectable;
    TextView scoreText, timerText;
    Button jumpBtn, walkBtn;
    float screenWidth, screenHeight, timeToCrash, linearValue, objectHeight, userAnswer;
    boolean beforeQuestion = true, isBonus = false;
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
        //  Set background sound
        clockTickingRing = MediaPlayer.create(GamePage.this,R.raw.clock_ticking);
        gameBackgroundRing = MediaPlayer.create(GamePage.this,R.raw.bensound_cute);
        correctRing = MediaPlayer.create(GamePage.this,R.raw.correct_answer);
        wrongRing = MediaPlayer.create(GamePage.this,R.raw.wrong_answer);
        wrongRing.setVolume(0.4f, 0.4f);
        gameBackgroundRing.setVolume(0.5f,0.5f);
        gameBackgroundRing.setLooping(true);
        gameBackgroundRing.start();
        buttonFragmentColor = getResources().getColor(R.color.green, null);
        rand = new Random();
        walkBtn = findViewById(R.id.start_walk);
        jumpBtn = findViewById(R.id.start_stand);
        jumpBtn.setOnTouchListener(new ButtonTouchAnimation());
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

        prepareAnimations("jump");
        prepareAnimations("fall");
        prepareAnimations("walk");
        prepareAnimations("run");

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
                        if (userAnswered) {
                            animationResponse(levelManager.checkCorrectAnswer(userAnswer));
                            changeGameSpeed(1.5f);
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
                        if (!isBonus) {
                            // Bonus round has a  1 to 5 chance of occurring and a 0 chance after the last question
                            if(rand.nextInt(5) == 0 && !levelManager.isLastQuestion()) {
                                isBonus = true;
//                                changeGameSpeed(2.5f);
                                prepareAnimations("jump");
                                Animation slideIn = AnimationUtils.loadAnimation(GamePage.this, R.anim.slide_in_bottom);
                                jumpBtn.startAnimation(slideIn);
                                jumpBtn.setVisibility(View.VISIBLE);
                                // Change jump height
                                objectHeight = obstacle.getY() + 100;
                                collectable = new ImageView(GamePage.this);
                                collectable.setImageResource(collectableImages[rand.nextInt(10)]);
                                collectable.setScaleType(ImageView.ScaleType.FIT_XY);
                                ViewGroup.LayoutParams coinParams = new ViewGroup.LayoutParams(60,80);
                                collectable.setLayoutParams(coinParams);
                                rootLayout.addView(collectable);
                                collectable.setX(screenWidth);
                                collectable.setY(objectHeight - rand.nextInt(191));
                                collectable.setDrawingCacheEnabled(true);
                                collectablesAmount = 0;
                            }
                            else {
                                nextQuestion();
                            }
                        }
                        else {
                            if (collectable.getX() > -collectable.getWidth()) {
                                if (checkCollision()) {
                                    score++;
                                    scoreText.setText(getString(R.string.score) + score);
                                    playSound(correctRing);
                                    new ParticleSystem(GamePage.this, 100, R.drawable.star_pink, 3000)
                                            .setSpeedRange(0.2f, 0.5f)
                                            .oneShot(scoreText, 100);
                                    collectable.setX(-collectable.getWidth());
                                } else {
                                    collectable.setX(collectable.getX() - (3.4f * gameSpeed));
                                }
                            } else {
                                collectablesAmount++;
                                if (collectablesAmount < 3) {
                                    collectable.setY(objectHeight - rand.nextInt(191));
                                    collectable.setImageResource(collectableImages[rand.nextInt(10)]);
                                    collectable.setX(screenWidth);
                                }
                                else {
                                    isBonus = false;
//                                    changeGameSpeed(1.5f);
                                    prepareAnimations("jump");
                                    Animation slideOut = AnimationUtils.loadAnimation(GamePage.this, R.anim.slide_out_bottom);
                                    if (jumpBtn.getVisibility() != View.GONE) {
                                        jumpBtn.startAnimation(slideOut);
                                        jumpBtn.setVisibility(View.GONE);
                                    }
                                    nextQuestion();
                                }
                            }
                        }
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

        walkBtn.setOnTouchListener(new ButtonTouchAnimation());
        walkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    buttonsFragment = ButtonsFragment.newInstance(options);
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom).replace(R.id.button_fragment_layout, buttonsFragment, BUTTONS_TAG).commit();
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_top).replace(R.id.question_layout, questionFragment, QUESTION_TAG).commit();
                    //buttonsFragment.disableButtons();
                }
                else {
                    if (!walkingAnimation.isRunning()) {
                        // Get level and category
                        category = getIntent().getIntExtra("category",0);
                        level = getIntent().getIntExtra("level",0);
                        // Save Weights before game
                        weightsBeforeGame = new ArrayList<>(fileManager.getLevelWeights().get(ECategory.values()[category]).get(level - 1));
                        levelManager = new LevelManager(GamePage.this, 10, ECategory.values()[category], level);
                        obstacleIndex = 0;
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
                        walkBtn.setVisibility(View.GONE);
                    }
                }
            }
        });

        jumpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpBtn.setVisibility(View.GONE);
                player.setImageDrawable(jumpAnimation);
                jumpAnimation.start();
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
                                //buttonsFragment.enableButtons();
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
                            if (userAnswered) {
                                animationResponse(userAnswer == 2);
                                changeGameSpeed(1.5f);
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
                            if (!isBonus) {
                                tutorialValueAnimator.pause();
                                walkingAnimation.stop();
                                String answerResponse;
                                if (score == 1) {
                                    answerResponse = getString(R.string.tutorial_correct_answer) + "\n";
                                } else {
                                    answerResponse = getString(R.string.tutorial_wrong_answer) + "\n";
                                }
                                FancyShowCaseView answerSc = new FancyShowCaseView.Builder(GamePage.this)
                                        .title(answerResponse + getString(R.string.tutorial_number_of_levels)).titleStyle(R.style.TutorialFontLocalized, Gravity.TOP | Gravity.CENTER)
                                        .dismissListener(new DismissListener() {
                                            @Override
                                            public void onDismiss(String s) {
                                                Animation slideIn = AnimationUtils.loadAnimation(GamePage.this, R.anim.slide_in_bottom);
                                                jumpBtn.startAnimation(slideIn);
                                                jumpBtn.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onSkipped(String s) {

                                            }
                                        }).build();
                                FancyShowCaseView jumpSc = new FancyShowCaseView.Builder(GamePage.this)
                                        .focusOn(jumpBtn)
                                        .title(getString(R.string.tutorial_jump_button) + "\n" + getString(R.string.tutorial_bonus)).titleStyle(R.style.TutorialFontLocalized, Gravity.TOP | Gravity.CENTER)
                                        .build();

                                FancyShowCaseQueue fancyShowCaseQueue = new FancyShowCaseQueue().add(answerSc).add(jumpSc);
                                fancyShowCaseQueue.setCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete() {
                                        tutorialValueAnimator.resume();
                                        walkingAnimation.start();
                                        // Set up bonus
                                        objectHeight = obstacle.getY() + 100;
                                        isBonus = true;
                                        collectable = new ImageView(GamePage.this);
                                        collectable.setImageResource(collectableImages[rand.nextInt(10)]);
                                        collectable.setScaleType(ImageView.ScaleType.FIT_XY);
                                        ViewGroup.LayoutParams coinParams = new ViewGroup.LayoutParams(60, 80);
                                        collectable.setLayoutParams(coinParams);
                                        rootLayout.addView(collectable);
                                        collectable.setX(screenWidth);
                                        collectable.setY(objectHeight - rand.nextInt(191));
                                        collectable.setDrawingCacheEnabled(true);
                                        collectablesAmount = 0;
                                    }
                                });
                                fancyShowCaseQueue.show();
                            }
                            else {
                                if (collectable.getX() > -collectable.getWidth()) {
                                    if (checkCollision()) {
                                        score++;
                                        scoreText.setText(getString(R.string.score) + score);
                                        playSound(correctRing);
                                        new ParticleSystem(GamePage.this, 100, R.drawable.star_pink, 3000)
                                                .setSpeedRange(0.2f, 0.5f)
                                                .oneShot(scoreText, 100);
                                        collectable.setX(-collectable.getWidth());
                                    } else {
                                        collectable.setX(collectable.getX() - (3.4f * gameSpeed));
                                    }
                                } else {
                                    collectablesAmount++;
                                    if (collectablesAmount < 3) {
                                        collectable.setY(objectHeight - rand.nextInt(191));
                                        collectable.setImageResource(collectableImages[rand.nextInt(10)]);
                                        collectable.setX(screenWidth);
                                    }
                                    else {
                                        if (!endTutorialInitiated) {
                                            endTutorialInitiated = true;
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    isBonus = false;
                                                    Animation slideOut = AnimationUtils.loadAnimation(GamePage.this, R.anim.slide_out_bottom);
                                                    if (jumpBtn.getVisibility() != View.GONE) {
                                                        jumpBtn.startAnimation(slideOut);
                                                        jumpBtn.setVisibility(View.GONE);
                                                    }
                                                    tutorialValueAnimator.pause();
                                                    walkingAnimation.stop();
                                                    new ParticleSystem(GamePage.this, 100, R.drawable.star_pink, 3000)
                                                            .setSpeedRange(0.2f, 0.5f)
                                                            .oneShot(scoreText, 100);
                                                    new FancyShowCaseView.Builder(GamePage.this)
                                                            .title(getString(R.string.tutorial_end)).titleStyle(R.style.TutorialFontLocalized, Gravity.TOP | Gravity.CENTER)
                                                            .dismissListener(new DismissListener() {
                                                                @Override
                                                                public void onDismiss(String s) {
                                                                    finish();
                                                                }

                                                                @Override
                                                                public void onSkipped(String s) {

                                                                }
                                                            }).build().show();
                                                }
                                            }, 2000);
                                        }
                                    }
                                }
                            }
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

        if (getIntent().hasExtra("tutorial")) {
            tutorialRun();
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

    private void prepareAnimations(String animation) {
        switch (animation) {
            case "jump":
                AnimationDrawable jump = new AnimationDrawable();
                if (isBonus) {
                    jump.addFrame(getResources().getDrawable(R.drawable.good4, null), (int)(200 / gameSpeed));
                    jump.addFrame(getResources().getDrawable(R.drawable.good5, null), (int)(450 / gameSpeed));
                    jump.addFrame(getResources().getDrawable(R.drawable.good6, null), (int)(450 / gameSpeed));
                    jump.addFrame(getResources().getDrawable(R.drawable.good7, null), (int)(200 / gameSpeed));
                    jump.addFrame(getResources().getDrawable(R.drawable.good8, null), (int)(200 / gameSpeed));
                    jump.addFrame(getResources().getDrawable(R.drawable.good9, null), (int)(200 / gameSpeed));
                }
                else {
                    jump.addFrame(getResources().getDrawable(R.drawable.good1, null), (int)(200 / gameSpeed));
                    jump.addFrame(getResources().getDrawable(R.drawable.good2, null), (int)(200 / gameSpeed));
                    jump.addFrame(getResources().getDrawable(R.drawable.good3, null), (int)(200 / gameSpeed));
                    jump.addFrame(getResources().getDrawable(R.drawable.good4, null), (int)(200 / gameSpeed));
                    jump.addFrame(getResources().getDrawable(R.drawable.good5, null), (int)(450 / gameSpeed));
                    jump.addFrame(getResources().getDrawable(R.drawable.good6, null), (int)(450 / gameSpeed));
                    jump.addFrame(getResources().getDrawable(R.drawable.good7, null), (int)(200 / gameSpeed));
                    jump.addFrame(getResources().getDrawable(R.drawable.good8, null), (int)(200 / gameSpeed));
                    jump.addFrame(getResources().getDrawable(R.drawable.good9, null), (int)(200 / gameSpeed));

                }
                jumpAnimation = new CustomAnimationDrawable(jump) {
                    @Override
                    public void onAnimationFinish() {
                        player.setImageDrawable(walkingAnimation);
                        walkingAnimation.start();
                        if (isBonus) {
                            jumpBtn.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationStart() {
                        ObjectAnimator up = ObjectAnimator.ofFloat(player, "Y", objectHeight - 200);
                        up.setRepeatCount(1);
                        if (!isBonus) {
                            up.setStartDelay((int)(700 / gameSpeed));
                        }
                        up.setRepeatMode(ValueAnimator.REVERSE);
                        up.setDuration((int)(900 / gameSpeed));
                        up.start();
                    }
                };
                break;
            case "fall":
                AnimationDrawable fall = new AnimationDrawable();
                fall.addFrame(getResources().getDrawable(R.drawable.bad1, null), (int)(200 / gameSpeed));//Stand
                fall.addFrame(getResources().getDrawable(R.drawable.bad2, null), (int)(200 / gameSpeed));//Run
                fall.addFrame(getResources().getDrawable(R.drawable.bad3, null), (int)(200 / gameSpeed));//Run
                fall.addFrame(getResources().getDrawable(R.drawable.bad4, null), (int)(200 / gameSpeed));//Run
                fall.addFrame(getResources().getDrawable(R.drawable.bad5, null), (int)(200 / gameSpeed));//TouchFloor
                fall.addFrame(getResources().getDrawable(R.drawable.bad6, null), (int)(200 / gameSpeed));//SadFace
                fall.addFrame(getResources().getDrawable(R.drawable.bad7, null), (int)(200 / gameSpeed));//Trip
                fall.addFrame(getResources().getDrawable(R.drawable.bad8, null), (int)(200 / gameSpeed));//Trip
                fall.addFrame(getResources().getDrawable(R.drawable.bad9, null), (int)(400 / gameSpeed));//Ground
                fallingAnimation = new CustomAnimationDrawable(fall) {
                    @Override
                    public void onAnimationFinish() {
                        player.setImageDrawable(walkingAnimation);
                        walkingAnimation.start();
                    }

                    @Override
                    public void onAnimationStart() {
                        // Puddle splash
                        if (obstacleIndex == 2) {
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
                            }, (int)(1600 / gameSpeed));
                        }
                    }
                };
                break;
            case "walk":
                walkingAnimation = new AnimationDrawable();
                walkingAnimation.addFrame(getResources().getDrawable(R.drawable.good1, null), (int)(200 / gameSpeed));
                walkingAnimation.addFrame(getResources().getDrawable(R.drawable.walk1, null), (int)(200 / gameSpeed));
                walkingAnimation.addFrame(getResources().getDrawable(R.drawable.good1, null), (int)(200 / gameSpeed));
                walkingAnimation.addFrame(getResources().getDrawable(R.drawable.walk2, null), (int)(200 / gameSpeed));
                walkingAnimation.setOneShot(false);
                break;
            case "run":
                runningAnimation = new AnimationDrawable();
                runningAnimation.addFrame(getResources().getDrawable(R.drawable.bad3, null), (int)(200 / gameSpeed));
                runningAnimation.addFrame(getResources().getDrawable(R.drawable.bad4, null), (int)(200 / gameSpeed));
                runningAnimation.addFrame(getResources().getDrawable(R.drawable.bad5, null), (int)(200 / gameSpeed));
                runningAnimation.setOneShot(false);
                break;
        }
    }

    private void nextQuestion() {
        beforeQuestion = true;
        obstacleIndex = (obstacleIndex + 1) % 3;
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
            walkingAnimation.stop();
            fallingAnimation.stop();
            player.setImageResource(R.drawable.good1);
            valueAnimator.pause();
            endLevelAndCheckResults();
        }
    }

    private void endLevelAndCheckResults() {
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
        boolean levelComplete = correctAnswerCounter == 10 && weightsAreEven;
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
            correctAnswerCounter++;
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
            if (obstacleIndex != 2) {
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
        buttonsFragment = ButtonsFragment.newInstance(levelManager.getCurrentQuestionOptions(options));

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom).replace(R.id.button_fragment_layout, buttonsFragment, BUTTONS_TAG).commit();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_top).replace(R.id.question_layout, questionFragment, QUESTION_TAG).commit();
    }
    public void removeQuestion() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_out_top,R.anim.slide_out_top).remove(questionFragment)
                .setCustomAnimations(R.anim.slide_out_bottom, R.anim.slide_out_bottom).remove(buttonsFragment).commit();
    }

    public boolean checkCollision() {
        if ((collectable.getBottom() + collectable.getTranslationY()) < (player.getTop() + player.getTranslationY())) {
            return false;
        }
        if ((collectable.getTop() + collectable.getTranslationY()) > (player.getBottom() + player.getTranslationY())){
            return false;
        }
        if ((collectable.getRight() + collectable.getTranslationX()) < (player.getLeft() + player.getTranslationX())){
            return false;
        }
        if ((collectable.getLeft() + collectable.getTranslationX()) > (player.getRight() + player.getTranslationX())){
            return false;
        }
        // Bounding box overlap
        Bitmap coinBitmap = collectable.getDrawingCache();
        Bitmap playerBitmap = player.getDrawingCache();
        //Bitmap playerBitmap = ((BitmapDrawable)player.getDrawable()).getBitmap();
        int[] coinLocation = new int[2];
        collectable.getLocationOnScreen(coinLocation);
        Rect coinRect = new Rect(coinLocation[0], coinLocation[1], coinLocation[0] + collectable.getWidth(), coinLocation[1] + collectable.getHeight());
        int[] playerLocation = new int[2];
        player.getLocationOnScreen(playerLocation);
        Rect playerRect = new Rect(playerLocation[0], playerLocation[1], playerLocation[0] + player.getWidth(), playerLocation[1] + player.getHeight());
        // After intersect playerRect is changed to intersection rect
        Rect playerRectCopy = new Rect(playerRect);
        playerRect.intersect(coinRect);
        for (int i = playerRect.left; i < playerRect.right; i++) {
            for (int j = playerRect.top; j < playerRect.bottom; j++) {
                if (playerBitmap.getPixel(i - playerRectCopy.left, j - playerRectCopy.top)!= Color.TRANSPARENT) {
                    //Log.d("TTT", i +" - " + coinRect.left + "," + j + " - " + coinRect.top);
                    if ((i - coinRect.left) >= 0 && (i - coinRect.left) < coinBitmap.getWidth() && (j - coinRect.top) >= 0 && (j - coinRect.top) < coinBitmap.getHeight()) {
                        if (coinBitmap.getPixel(i - coinRect.left, j - coinRect.top) != Color.TRANSPARENT) {
                            // Collision
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
        gameBackgroundRing.start();
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
    public void sendUserInput(char input) {
        questionFragment.updateUserInput(input);
    }

    @Override
    public void sendResult(String result) {
        buttonsFragment.returnResult(result);
    }

    @Override
    public void onPressBack() {
        finish();
    }

    @Override
    public void onPressContinue(boolean moveToNextLevel) {
        score = 0;
        correctAnswerCounter = 0;
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
                // Save Weights before game
                weightsBeforeGame = new ArrayList<>(fileManager.getLevelWeights().get(ECategory.values()[category]).get(level - 1));
                levelManager = new LevelManager(GamePage.this, 10, ECategory.values()[category], level);
                obstacleIndex = 0;
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
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_out_top,R.anim.slide_out_top).remove(gameFinishedFragment).commit();
    }

    private void openNextLevel() {
        int nextLevel = level + 1;
        int nextCategory = category;
        if (ECategory.values()[nextCategory].getNumberOfLevels() < nextLevel) {
            nextLevel = 0;
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
}

