package com.tomlezmy.goolmathapp.activities;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.plattysoft.leonids.ParticleSystem;
import com.tomlezmy.goolmathapp.ButtonTouchAnimation;
import com.tomlezmy.goolmathapp.CustomAnimationDrawable;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.fragments.ButtonsFragment;
import com.tomlezmy.goolmathapp.fragments.QuestionFragment;
import com.tomlezmy.goolmathapp.game.ECategory;
import com.tomlezmy.goolmathapp.game.LevelManager;
import com.tomlezmy.goolmathapp.interfaces.MyDialogListener;
import com.tomlezmy.goolmathapp.interfaces.SendMessage;

import java.util.Random;

public class GamePage extends AppCompatActivity implements MyDialogListener, SendMessage {

    float gameSpeed = 1.5f;
    // temp int and objectImages for test
    // 0 = banana
    // 1 = rock
    // 2 = puddle
    // 3 = door
    int test = 0;
    int buttonFragmentColor, score = 0;
    QuestionFragment questionFragment;
    ButtonsFragment buttonsFragment;
    boolean userAnswered = false;
    int[] objectImages = new int []{R.drawable.banana_peel, R.drawable.rock, R.drawable.puddle};//, R.drawable.closed_door};
    ValueAnimator valueAnimator;
    AnimationDrawable walkingAnimation, runningAnimation;
    CustomAnimationDrawable jumpAnimation, fallingAnimation;
    RelativeLayout buttonLayout;
    ImageView player, obstacle, backgroundOne, backgroundTwo;
    TextView scoreText;
    TextView timerText;
    float screenWidth, timeToCrash, linearValue, objectHeight, userAnswer;
    boolean beforeQuestion = true;
    LevelManager levelManager;
    Random rand;
    CountDownTimer countDownTimer;
    double valueDelta, framesPerMilliSec = (double)1000 / 60;
    MediaPlayer gameBackgroundRing;
    MediaPlayer clockTickingRing, correctRing, wrongRing;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
        //  Set background sound
        clockTickingRing = MediaPlayer.create(GamePage.this,R.raw.clock_ticking);
        gameBackgroundRing = MediaPlayer.create(GamePage.this,R.raw.bensound_cute);
        correctRing = MediaPlayer.create(GamePage.this,R.raw.correct_answer);
        wrongRing = MediaPlayer.create(GamePage.this,R.raw.wrong_answer);
        wrongRing.setVolume(0.4f, 0.4f);
        gameBackgroundRing.setVolume(0.5f,0.5f);
        gameBackgroundRing.start();
        buttonFragmentColor = getResources().getColor(R.color.green, null);
        rand = new Random();
        Button walk = findViewById(R.id.start_walk);
        final Button stand = findViewById(R.id.start_stand);
        buttonLayout = findViewById(R.id.button_fragment_layout);
        buttonLayout.setBackgroundColor(buttonFragmentColor);
        player = findViewById(R.id.player_image);
        obstacle = findViewById(R.id.obstacle);
        backgroundOne = findViewById(R.id.background_one);
        backgroundTwo = findViewById(R.id.background_two);
        scoreText = findViewById(R.id.score_text);
        timerText = findViewById(R.id.timer_text);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        AnimationDrawable fall = new AnimationDrawable();
        fall.addFrame(getResources().getDrawable(R.drawable.bad1, null), (int)(200 / gameSpeed));
        fall.addFrame(getResources().getDrawable(R.drawable.bad2, null), (int)(200 / gameSpeed));
        fall.addFrame(getResources().getDrawable(R.drawable.bad3, null), (int)(200 / gameSpeed));
        fall.addFrame(getResources().getDrawable(R.drawable.bad4, null), (int)(200 / gameSpeed));
        fall.addFrame(getResources().getDrawable(R.drawable.bad5, null), (int)(200 / gameSpeed));
        fall.addFrame(getResources().getDrawable(R.drawable.bad6, null), (int)(200 / gameSpeed));
        fall.addFrame(getResources().getDrawable(R.drawable.bad7, null), (int)(200 / gameSpeed));
        fall.addFrame(getResources().getDrawable(R.drawable.bad8, null), (int)(200 / gameSpeed));
        fall.addFrame(getResources().getDrawable(R.drawable.bad9, null), (int)(400 / gameSpeed));
        fallingAnimation = new CustomAnimationDrawable(fall) {
            @Override
            public void onAnimationFinish() {
                player.setImageDrawable(walkingAnimation);
                walkingAnimation.start();
            }

            @Override
            public void onAnimationStart() {
                // Puddle splash
                if (test == 2) {
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
        AnimationDrawable jump = new AnimationDrawable();
        jump.addFrame(getResources().getDrawable(R.drawable.good1, null), (int)(200 / gameSpeed));
        jump.addFrame(getResources().getDrawable(R.drawable.good2, null), (int)(200 / gameSpeed));
        jump.addFrame(getResources().getDrawable(R.drawable.good3, null), (int)(200 / gameSpeed));
        jump.addFrame(getResources().getDrawable(R.drawable.good4, null), (int)(200 / gameSpeed));
        jump.addFrame(getResources().getDrawable(R.drawable.good5, null), (int)(450 / gameSpeed));
        jump.addFrame(getResources().getDrawable(R.drawable.good6, null), (int)(450 / gameSpeed));
        jump.addFrame(getResources().getDrawable(R.drawable.good7, null), (int)(200 / gameSpeed));
        jump.addFrame(getResources().getDrawable(R.drawable.good8, null), (int)(200 / gameSpeed));
        jump.addFrame(getResources().getDrawable(R.drawable.good9, null), (int)(200 / gameSpeed));
        jumpAnimation = new CustomAnimationDrawable(jump) {
            @Override
            public void onAnimationFinish() {
                player.setImageDrawable(walkingAnimation);
                walkingAnimation.start();
            }

            @Override
            public void onAnimationStart() {
                ObjectAnimator up = ObjectAnimator.ofFloat(player, "Y", objectHeight - 200);
                up.setRepeatCount(1);
                up.setStartDelay((int)(700 / gameSpeed));
                up.setRepeatMode(ValueAnimator.REVERSE);
                up.setDuration((int)(900 / gameSpeed));
                up.start();
            }
        };

        valueAnimator = ValueAnimator.ofFloat(0.0f, -1.0f);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration((int)(10000 / gameSpeed));
        linearValue = 0f;
        valueDelta = (double)1 / (int)((int)(10000 / gameSpeed) / framesPerMilliSec);
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
                            gameSpeed = 1.5f;
                            valueDelta = (double)1 / (int)((int)(10000 / gameSpeed) / framesPerMilliSec);
                            player.setImageDrawable(walkingAnimation);
                            walkingAnimation.start();
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
                        beforeQuestion = true;
                        test = (test + 1) % 3;
                        obstacle.setImageResource(objectImages[test]);

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
                            walkingAnimation.stop();
                            fallingAnimation.stop();
                            player.setImageResource(R.drawable.good1);
                            valueAnimator.pause();
                            new ParticleSystem(GamePage.this, 100, R.drawable.star_pink, 3000)
                                    .setSpeedRange(0.2f, 0.5f)
                                    .oneShot(scoreText, 100);
                        }
//                        Log.d("TTT", timeToCrash + "");
                    }
                }
                //final float progress = (float) animation.getAnimatedValue();
                linearValue -= valueDelta;
                if (linearValue < -1) {
                    linearValue = 0;
                }
                final float progress = linearValue;
                //Log.d("TTT", (float) animation.getAnimatedValue() + "," + linearValue);
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;

                // Moving background
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX + width);
            }
        });

        walkingAnimation = new AnimationDrawable();
        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.nwalk1, null), (int)(200 / gameSpeed));
        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.nwalk2, null), (int)(200 / gameSpeed));
        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.nwalk3, null), (int)(200 / gameSpeed));
        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.nwalk4, null), (int)(200 / gameSpeed));
        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.nwalk5, null), (int)(200 / gameSpeed));
        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.nwalk6, null), (int)(200 / gameSpeed));
//        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.good1, null), (int)(200 / gameSpeed));
//        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.walk1, null), (int)(200 / gameSpeed));
//        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.good1, null), (int)(200 / gameSpeed));
//        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.walk2, null), (int)(200 / gameSpeed));
        walkingAnimation.setOneShot(false);

        runningAnimation = new AnimationDrawable();
        runningAnimation.addFrame(getResources().getDrawable(R.drawable.nwalk7, null), (int)(200 / gameSpeed));
        runningAnimation.addFrame(getResources().getDrawable(R.drawable.nwalk8, null), (int)(200 / gameSpeed));
        runningAnimation.addFrame(getResources().getDrawable(R.drawable.nwalk9, null), (int)(200 / gameSpeed));
        runningAnimation.addFrame(getResources().getDrawable(R.drawable.nwalk10, null), (int)(200 / gameSpeed));
        runningAnimation.addFrame(getResources().getDrawable(R.drawable.nwalk11, null), (int)(200 / gameSpeed));
        runningAnimation.addFrame(getResources().getDrawable(R.drawable.nwalk12, null), (int)(200 / gameSpeed));
        runningAnimation.setOneShot(false);

        walk.setOnTouchListener(new ButtonTouchAnimation());
        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!walkingAnimation.isRunning()) {
                    levelManager = new LevelManager(10, ECategory.DIVISION, 1);
                    score = 0;
                    test = 0;
                    obstacle.setImageResource(objectImages[test]);
                    player.setImageDrawable(walkingAnimation);
                    // Time in seconds until player reaches the obstacle, 16.665 = valueAnimator.duration / update rate
                    timeToCrash = ((player.getWidth() + 25) - obstacle.getX()) / (3.4f*gameSpeed) * -1 * 16.665f;
//                    Log.d("TTT", timeToCrash + "");
                    countDownTimer =  new CountDownTimer((long)timeToCrash, 1000) {

                        public void onTick(long millisUntilFinished) {
                            //timerText.animateText((millisUntilFinished / 1000) + "");
                            // Set Clock ticking sound while count down timer
                            clockTickingRing.setVolume(1,1);
                            clockTickingRing.start();
                            timerText.setText((millisUntilFinished / 1000) + "");
                            //Log.d("TTT","seconds remaining: " + millisUntilFinished / 1000);
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
                    }
                    else {
                        valueAnimator.start();
                    }
                    levelManager.generateQuestions();
                    showQuestion();
                }
                else {
                    if (!valueAnimator.isPaused()) {
                        valueAnimator.pause();
                        if (gameSpeed == 1.5f) {
                            gameSpeed = 2f;
                        }
                        else {
                            gameSpeed = 1.5f;
                        }
                        valueAnimator.setInterpolator(new LinearInterpolator());
                        valueAnimator.setDuration((int) (10000 / gameSpeed));
                    } else {
                        valueAnimator.resume();
                    }
                }
            }
        });

        stand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walkingAnimation.isRunning()) {
                    walkingAnimation.stop();
                    valueAnimator.pause();
                }
            }
        });
    }

    @Override
    public void onReturn(float answer) {
        countDownTimer.cancel();
        timerText.setVisibility(View.INVISIBLE);
        timerText.setText("");
        gameSpeed = 3.5f;
        valueDelta = (double)1 / (int)((int)(10000 / gameSpeed) / framesPerMilliSec);
        player.setImageDrawable(runningAnimation);
        runningAnimation.start();
        removeQuestion();
        userAnswer = answer;
        userAnswered = true;
    }

    public void animationResponse(boolean correct) {
        if (correct) {
            correctRing.start();
            score++;
            scoreText.setText("Score : " + score);
            new ParticleSystem(this, 20, R.drawable.star_pink, 1000)
                    .setSpeedRange(0.2f, 0.5f)
                    .oneShot(scoreText, 200);
        }
        else {
            wrongRing.start();
        }

        // Response for every obstacle but the door
        if (test != 3) {
            if (correct) {
                // Change jump height for banana and rock
                if (test != 2) {
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
        // Door response
        else {
            if (correct) {
                //Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                obstacle.setImageResource(R.drawable.open_door);
            }
            else {
                //Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show();
            }
            walkingAnimation.start();
        }
    }

    public void showQuestion() {
        questionFragment = new QuestionFragment(levelManager.getCurrentQuestion());

        if (test != 3) {
            int options = 4;
            boolean useTwoAnswers = rand.nextBoolean();
            if (useTwoAnswers) {
                options = 2;
            }
            buttonsFragment = ButtonsFragment.newInstance(levelManager.getCurrentQuestionOptions(options), test);
        }
        // Door question
        else {
            buttonsFragment = ButtonsFragment.newInstance(null, 0);
        }

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom).replace(R.id.button_fragment_layout, buttonsFragment, "BUTTON_TAG").commit();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_top).replace(R.id.dialog_layout, questionFragment, "QUESTION_TAG").commit();

    }
    public void removeQuestion() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_out_top,R.anim.slide_out_top).remove(questionFragment)
                .setCustomAnimations(R.anim.slide_out_bottom, R.anim.slide_out_bottom).remove(buttonsFragment).commit();
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

    @Override
    public void sendUserInput(char input) {
        questionFragment.updateUserInput(input);
    }

    @Override
    public void sendResult(String result) {
        buttonsFragment.returnResult(result);
    }
}

