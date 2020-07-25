package com.tomlezmy.goolmathapp.activities;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.tomlezmy.goolmathapp.fragments.GameFinishedFragment;
import com.tomlezmy.goolmathapp.fragments.QuestionFragment;
import com.tomlezmy.goolmathapp.game.ECategory;
import com.tomlezmy.goolmathapp.game.LevelManager;
import com.tomlezmy.goolmathapp.interfaces.IButtonFragmentAnswerListener;
import com.tomlezmy.goolmathapp.interfaces.IResultFragmentListener;
import com.tomlezmy.goolmathapp.interfaces.SendMessage;

import java.util.Random;

public class GamePage extends AppCompatActivity implements IButtonFragmentAnswerListener, SendMessage, IResultFragmentListener {

    final String QUESTION_TAG = "QUESTION_TAG", BUTTONS_TAG = "BUTTONS_TAG", RESULT_TAG = "RESULT_TAG";
    float gameSpeed;
    // temp int and objectImages for test
    // 0 = banana
    // 1 = rock
    // 2 = puddle
    // 3 = door
    int test = 0;
    int buttonFragmentColor, score = 0, category, level, coinAmount;
    QuestionFragment questionFragment;
    ButtonsFragment buttonsFragment;
    GameFinishedFragment gameFinishedFragment;
    boolean userAnswered = false;
    int[] objectImages = new int []{R.drawable.banana_peel, R.drawable.rock, R.drawable.puddle};//, R.drawable.closed_door};
    ValueAnimator valueAnimator;
    AnimationDrawable walkingAnimation, runningAnimation;
    CustomAnimationDrawable jumpAnimation, fallingAnimation;
    RelativeLayout buttonLayout, rootLayout;
    ImageView player, obstacle, backgroundOne, backgroundTwo, coin;
    TextView scoreText;
    TextView timerText;
    float screenWidth, screenHeight, timeToCrash, linearValue, objectHeight, userAnswer;
    boolean beforeQuestion = true, isBonus = false;
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

        // Get level and category
        category = getIntent().getIntExtra("category",0);
        level = getIntent().getIntExtra("level",0);
        changeGameSpeed(1.5f);
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
        final Button jumpBtn = findViewById(R.id.start_stand);
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
        final AnimationDrawable jump = new AnimationDrawable();
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
                if (isBonus) {
                    jumpBtn.setVisibility(View.VISIBLE);
                }
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
                            if(rand.nextInt(5) == 0) {
                                Animation slideIn = AnimationUtils.loadAnimation(GamePage.this, R.anim.slide_in_bottom);
                                jumpBtn.startAnimation(slideIn);
                                jumpBtn.setVisibility(View.VISIBLE);
                                // Change jump height
                                objectHeight = obstacle.getY() + 100;
                                isBonus = true;
                                coin = new ImageView(GamePage.this);
                                coin.setImageResource(R.drawable.coin);
                                coin.setScaleType(ImageView.ScaleType.FIT_XY);
                                ViewGroup.LayoutParams coinParams = new ViewGroup.LayoutParams(50,50);
                                coin.setLayoutParams(coinParams);
                                rootLayout.addView(coin);
                                coin.setX(screenWidth);
                                //coin.setY(objectHeight - 1);
                                coin.setY(objectHeight - rand.nextInt(191));
                                coin.setDrawingCacheEnabled(true);
                                coinAmount = 0;
                            }
                            else {
                                nextQuestion();
                            }
                        }
                        else {
                            if (coin.getX() > -coin.getWidth()) {
                                if (checkCollision()) {
                                    score++;
                                    scoreText.setText("Score : " + score);
                                    new ParticleSystem(GamePage.this, 100, R.drawable.star_pink, 3000)
                                            .setSpeedRange(0.2f, 0.5f)
                                            .oneShot(scoreText, 100);
                                    coin.setX(-coin.getWidth());
                                } else {
                                    coin.setX(coin.getX() - (3.4f * gameSpeed));
                                }
                            } else {
                                coinAmount++;
                                if (coinAmount < 3) {
                                    coin.setY(objectHeight - rand.nextInt(191));
                                    coin.setX(screenWidth);
                                }
                                else {
                                    isBonus = false;
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
                //Log.d("TTT", (float) animation.getAnimatedValue() + "," + linearValue);
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;

                // Moving background
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX + width);
            }
        });

        walkingAnimation = new AnimationDrawable();
        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.good1, null), (int)(200 / gameSpeed));
        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.walk1, null), (int)(200 / gameSpeed));
        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.good1, null), (int)(200 / gameSpeed));
        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.walk2, null), (int)(200 / gameSpeed));
        walkingAnimation.setOneShot(false);

        runningAnimation = new AnimationDrawable();
        runningAnimation.addFrame(getResources().getDrawable(R.drawable.bad3, null), (int)(200 / gameSpeed));
        runningAnimation.addFrame(getResources().getDrawable(R.drawable.bad4, null), (int)(200 / gameSpeed));
        runningAnimation.addFrame(getResources().getDrawable(R.drawable.bad5, null), (int)(200 / gameSpeed));
        runningAnimation.setOneShot(false);

        walk.setOnTouchListener(new ButtonTouchAnimation());
        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!walkingAnimation.isRunning()) {
                    levelManager = new LevelManager(GamePage.this,10, ECategory.values()[category], level);
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
    }

    private void nextQuestion() {
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
            // Level finished
            walkingAnimation.stop();
            fallingAnimation.stop();
            player.setImageResource(R.drawable.good1);
            valueAnimator.pause();
            gameFinishedFragment = new GameFinishedFragment(score == 10);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_top).replace(R.id.result_layout, gameFinishedFragment, RESULT_TAG).commit();
            new ParticleSystem(GamePage.this, 100, R.drawable.star_pink, 3000)
                    .setSpeedRange(0.2f, 0.5f)
                    .oneShot(scoreText, 100);
        }
        //Log.d("TTT", timeToCrash + "");
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
        // Uncomment|Comment this line to toggle learning mode
        //levelManager.updateDataFromUserAnswer(wasCorrect);
        if (wasCorrect) {
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
            if (wasCorrect) {
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
            if (wasCorrect) {
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
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom).replace(R.id.button_fragment_layout, buttonsFragment, BUTTONS_TAG).commit();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_top).replace(R.id.question_layout, questionFragment, QUESTION_TAG).commit();

    }
    public void removeQuestion() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_out_top,R.anim.slide_out_top).remove(questionFragment)
                .setCustomAnimations(R.anim.slide_out_bottom, R.anim.slide_out_bottom).remove(buttonsFragment).commit();
    }

    public boolean checkCollision() {
        if ((coin.getBottom() + coin.getTranslationY()) < (player.getTop() + player.getTranslationY())) {
            return false;
        }
        if ((coin.getTop() + coin.getTranslationY()) > (player.getBottom() + player.getTranslationY())){
            return false;
        }
        if ((coin.getRight() + coin.getTranslationX()) < (player.getLeft() + player.getTranslationX())){
            return false;
        }
        if ((coin.getLeft() + coin.getTranslationX()) > (player.getRight() + player.getTranslationX())){
            return false;
        }
        // Bounding box overlap
        Bitmap coinBitmap = coin.getDrawingCache();
        Bitmap playerBitmap = player.getDrawingCache();
        //Bitmap playerBitmap = ((BitmapDrawable)player.getDrawable()).getBitmap();
        int[] coinLocation = new int[2];
        coin.getLocationOnScreen(coinLocation);
        Rect coinRect = new Rect(coinLocation[0], coinLocation[1], coinLocation[0] + coin.getWidth(), coinLocation[1] + coin.getHeight());
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
        scoreText.setText("Score : " + score);
        if (moveToNextLevel) {
            level++;
            if (ECategory.values()[category].getNumberOfLevels() < level) {
                level = 1;
                category++;
                //TODO check for last category
            }
        }
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_out_top,R.anim.slide_out_top).remove(gameFinishedFragment).commit();
    }
}

