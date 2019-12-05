package com.example.cs125finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;

public class game extends AppCompatActivity {
    private TextView nameTagText;
    private ImageButton foodButton;
    private ImageButton playButton;
    private ImageButton sleepButton;
    private int ferretHealth = 100;
    private ImageView happinessMeter;
    private Handler timerHandler = new Handler();
    private MediaPlayer music;
    private static int MUSIC_PLAYED = 1;


    //timer that decays the health of the ferret by 10 every 15 seconds
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis();
            int seconds = (int) (millis / 1000);
            if (seconds % 15 == 0 && ferretHealth >= 10) {
                ferretHealth = ferretHealth - 10;
            }

            //calls the updateHealth function, which updates the health icon
            updateHealth();

            //re-runs the timer
            timerHandler.postDelayed(this, 500);
        }
    };

    //re-sets the idle animation (after an action) depending on the chosen color
    public void setIdleAnimation(String color, Bundle b) {
        final ImageView ferretImage = (ImageView) findViewById(R.id.ferretImage);
        if (color.equals("brown") && b!= null) {
            ferretImage.setImageResource(R.drawable.idle_animation_brown);
        } else if (color.equals("red") && b!= null) {
            ferretImage.setImageResource(R.drawable.idle_animation_red);
        } else {
            ferretImage.setImageResource(R.drawable.idle_animation_gray);
        }
        final AnimationDrawable idleAnimation = (AnimationDrawable)ferretImage.getDrawable();
        idleAnimation.start();
    }

    //updates the smiley face to match the current health level
    public void updateHealth() {
        happinessMeter = (ImageView) findViewById(R.id.happinessMeter);
        if (ferretHealth >= 80) {
            happinessMeter.setImageResource(R.drawable.lvl4);
        } else if (ferretHealth >= 60) {
            happinessMeter.setImageResource(R.drawable.lvl3);
        } else if (ferretHealth >= 40) {
            happinessMeter.setImageResource(R.drawable.lvl2);
        } else {
            happinessMeter.setImageResource(R.drawable.lvl1);
        }
    }

    //oncreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //begins running timer
        timerHandler.postDelayed(timerRunnable, 0);

        //begins playing music
        music = MediaPlayer.create(game.this,R.raw.milkworld);
        if (MUSIC_PLAYED == 1) {
            music.start();
            music.setLooping(true);
            MUSIC_PLAYED = 0;
        }

        //get info from instance
        nameTagText = (TextView) findViewById(R.id.nameTagText);
        Intent intent = getIntent();
        final Bundle b = intent.getExtras();
        if(b!=null) {
            //sets textview to the chosen name
            nameTagText.setText(b.get("ferretName").toString());
        }
        //gets ferret's color
        final String ferretColor = b.get("ferretColor").toString();
        final ImageView ferretImage = (ImageView) findViewById(R.id.ferretImage);

        //set animation based on color
        setIdleAnimation(ferretColor, b);

        //button listeners
        foodButton = (ImageButton) findViewById(R.id.foodButton);
        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clicking food adds 10 to health
                if (ferretHealth != 100) {
                    ferretHealth = ferretHealth + 10;
                    updateHealth();
                }
                //sets food animation based on color
                if (ferretColor.equals("brown") && b!= null) {
                    ferretImage.setImageResource(R.drawable.eat_brown);
                } else if (ferretColor.equals("red") && b!= null) {
                    ferretImage.setImageResource(R.drawable.eat_red);
                } else {
                    ferretImage.setImageResource(R.drawable.eat_gray);
                }
                AnimationDrawable eatAnimation = (AnimationDrawable)ferretImage.getDrawable();
                eatAnimation.start();

                //sets a timer that ends the animation in 5 seconds
                new CountDownTimer(5000, 1000) {
                    public void onFinish() {
                        // When timer is finished
                        setIdleAnimation(ferretColor, b);
                    }
                    public void onTick(long millisUntilFinished) {
                        // millisUntilFinished    The amount of time until finished.
                    }
                }.start();
            }
        });
        playButton = (ImageButton) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ferretHealth != 100) {
                    ferretHealth = ferretHealth + 10;
                    updateHealth();
                }
            }
        });
        sleepButton = (ImageButton) findViewById(R.id.sleepButton);
        sleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when sleep button clicked adds 10 to health
                if (ferretHealth != 100) {
                    ferretHealth = ferretHealth + 10;
                    updateHealth();
                }
                //sets animation based on color
                if (ferretColor.equals("brown") && b!= null) {
                    ferretImage.setImageResource(R.drawable.sleep_brown);
                } else if (ferretColor.equals("red") && b!= null) {
                    ferretImage.setImageResource(R.drawable.sleep_red);
                } else {
                    ferretImage.setImageResource(R.drawable.sleep_gray);
                }
                AnimationDrawable sleepAnimation = (AnimationDrawable)ferretImage.getDrawable();
                sleepAnimation.start();
                //sets timer so animation runs for 5 seconds
                new CountDownTimer(5000, 1000) {
                    public void onFinish() {
                        // When timer is finished
                        setIdleAnimation(ferretColor, b);
                    }
                    public void onTick(long millisUntilFinished) {
                        // millisUntilFinished    The amount of time until finished.
                    }
                }.start();
            }
        });
    }
}
