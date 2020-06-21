package com.tomlezmy.goolmathapp;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.plattysoft.leonids.ParticleSystem;
import com.tomlezmy.goolmathapp.fragments.ButtonsFragment;
import com.tomlezmy.goolmathapp.fragments.QuestionFragment;
import com.tomlezmy.goolmathapp.game.ECategory;
import com.tomlezmy.goolmathapp.game.LevelManager;
import com.tomlezmy.goolmathapp.interfaces.MyDialogListener;
import com.tomlezmy.goolmathapp.interfaces.SendMessage;

public class GamePage extends AppCompatActivity implements MyDialogListener, SendMessage {

    float gameSpeed = 1.5f;
    // temp int and objectImages for test
    // 0 = rock
    // 1 = puddle
    // 2 = door
    int test = 0;
    QuestionFragment questionFragment;
    ButtonsFragment buttonsFragment;
    boolean userAnswered = false;
    int userAnswer;
    int[] objectImages = new int []{R.drawable.rock, R.drawable.puddle, R.drawable.closed_door};
    ValueAnimator valueAnimator;
    AnimationDrawable walkingAnimation;
    CustomAnimationDrawable jumpAnimation;
    CustomAnimationDrawable fallingAnimation;
    ImageView player;
    ImageView obstacle;
    ImageView backgroundOne;
    ImageView backgroundTwo;
    TextView scoreText;
    int score = 0;
    float screenWidth;
    boolean beforeQuestion = true;
    LevelManager levelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
        Button walk = findViewById(R.id.start_walk);
        final Button stand = findViewById(R.id.start_stand);
        player = findViewById(R.id.player_image);
        obstacle = findViewById(R.id.obstacle);
        backgroundOne = findViewById(R.id.background_one);
        backgroundTwo = findViewById(R.id.background_two);
        scoreText = findViewById(R.id.score_text);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        AnimationDrawable fall = new AnimationDrawable();
        fall.addFrame(getResources().getDrawable(R.drawable.bad1), (int)(200 / gameSpeed));
        fall.addFrame(getResources().getDrawable(R.drawable.bad2), (int)(200 / gameSpeed));
        fall.addFrame(getResources().getDrawable(R.drawable.bad3), (int)(200 / gameSpeed));
        fall.addFrame(getResources().getDrawable(R.drawable.bad4), (int)(200 / gameSpeed));
        fall.addFrame(getResources().getDrawable(R.drawable.bad5), (int)(200 / gameSpeed));
        fall.addFrame(getResources().getDrawable(R.drawable.bad6), (int)(200 / gameSpeed));
        fall.addFrame(getResources().getDrawable(R.drawable.bad7), (int)(200 / gameSpeed));
        fall.addFrame(getResources().getDrawable(R.drawable.bad8), (int)(200 / gameSpeed));
        fall.addFrame(getResources().getDrawable(R.drawable.bad9), (int)(400 / gameSpeed));
        fallingAnimation = new CustomAnimationDrawable(fall) {
            @Override
            public void onAnimationFinish() {
                player.setImageDrawable(walkingAnimation);
                walkingAnimation.start();
            }

            @Override
            public void onAnimationStart() {
                // Puddle splash
                if (test == 1) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            obstacle.setImageResource(R.drawable.puddle_splash);
                        }
                    }, (int)(1600 / gameSpeed));
                }
            }
        };
        AnimationDrawable jump = new AnimationDrawable();
        jump.addFrame(getResources().getDrawable(R.drawable.good1), (int)(200 / gameSpeed));
        jump.addFrame(getResources().getDrawable(R.drawable.good2), (int)(200 / gameSpeed));
        jump.addFrame(getResources().getDrawable(R.drawable.good3), (int)(200 / gameSpeed));
        jump.addFrame(getResources().getDrawable(R.drawable.good4), (int)(200 / gameSpeed));
        jump.addFrame(getResources().getDrawable(R.drawable.good5), (int)(450 / gameSpeed));
        jump.addFrame(getResources().getDrawable(R.drawable.good6), (int)(450 / gameSpeed));
        jump.addFrame(getResources().getDrawable(R.drawable.good7), (int)(200 / gameSpeed));
        jump.addFrame(getResources().getDrawable(R.drawable.good8), (int)(200 / gameSpeed));
        jump.addFrame(getResources().getDrawable(R.drawable.good9), (int)(200 / gameSpeed));
        jumpAnimation = new CustomAnimationDrawable(jump) {
            @Override
            public void onAnimationFinish() {
                player.setImageDrawable(walkingAnimation);
                walkingAnimation.start();
            }

            @Override
            public void onAnimationStart() {
                ObjectAnimator up = ObjectAnimator.ofFloat(player, "Y",obstacle.getY() - 200);
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
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                if (beforeQuestion) {
                    if (obstacle.getX() > (player.getWidth() + 25)) {
                        obstacle.setTranslationX(obstacle.getTranslationX() - (3.4f * gameSpeed));
                    }
                    else {
                        // Was correct or not
                        beforeQuestion = false;
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
                        obstacle.setTranslationX(obstacle.getTranslationX() - (3.4f * gameSpeed));
                    }
                    else {
                        beforeQuestion = true;
                        test = (test + 1) % 3;
                        obstacle.setImageResource(objectImages[test]);
                        // If there is a next question
                        if (levelManager.nextQuestion()) {
                            showQuestion();
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
                        // Move object out of screen
                        obstacle.setX(screenWidth);
                    }
                }

                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;

                // Moving background
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX + width);
            }
        });

        walkingAnimation = new AnimationDrawable();
        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.good1), (int)(200 / gameSpeed));
        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.walk1), (int)(200 / gameSpeed));
        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.good1), (int)(200 / gameSpeed));
        walkingAnimation.addFrame(getResources().getDrawable(R.drawable.walk2), (int)(200 / gameSpeed));
        walkingAnimation.setOneShot(false);

        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!walkingAnimation.isRunning()) {
                    levelManager = new LevelManager(10, ECategory.ADDITION, 90);
                    test = 0;
                    obstacle.setImageResource(objectImages[test]);
                    player.setImageDrawable(walkingAnimation);
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
    public void onReturn(int answer) {
        removeQuestion();
        userAnswer = answer;
        userAnswered = true;
    }

    public void animationResponse(boolean correct) {
        if (correct) {
            score++;
            scoreText.setText("Score : " + score);
            new ParticleSystem(this, 20, R.drawable.star_pink, 1000)
                    .setSpeedRange(0.2f, 0.5f)
                    .oneShot(scoreText, 20);
        }

        // Rock response
        if (test == 0) {
            if (correct) {
                player.setImageDrawable(jumpAnimation);
                jumpAnimation.start();
            }
            else {
                player.setImageDrawable(fallingAnimation);
                fallingAnimation.start();
            }
        }
        // Puddle response
        else if (test == 1) {
            if (correct) {
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

        // Rock question
        if (test == 0) {
            buttonsFragment = ButtonsFragment.newInstance(levelManager.getCurrentQuestionOptions(2));
        }
        // Puddle question
        else if (test == 1) {
            buttonsFragment = ButtonsFragment.newInstance(levelManager.getCurrentQuestionOptions(4));
        }
        // Door question
        else {
            buttonsFragment = ButtonsFragment.newInstance(null);
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
        }
        super.onPause();
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

