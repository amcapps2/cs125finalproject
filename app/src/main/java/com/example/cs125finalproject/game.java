package com.example.cs125finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis();
            int seconds = (int) (millis / 1000);
            if (seconds % 15 == 0 && ferretHealth >= 10) {
                ferretHealth = ferretHealth - 10;
            }
            updateHealth();
            timerHandler.postDelayed(this, 500);
        }
    };

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //timer
        timerHandler.postDelayed(timerRunnable, 0);
        //get info from instance
        nameTagText = (TextView) findViewById(R.id.nameTagText);
        Intent intent = getIntent();
        final Bundle b = intent.getExtras();
        if(b!=null) {
            nameTagText.setText(b.get("ferretName").toString());
        }
        final String ferretColor = b.get("ferretColor").toString();
        final ImageView ferretImage = (ImageView) findViewById(R.id.ferretImage);

        //set animation based on color
        setIdleAnimation(ferretColor, b);

        //button listeners
        foodButton = (ImageButton) findViewById(R.id.foodButton);
        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ferretHealth != 100) {
                    ferretHealth = ferretHealth + 10;
                    updateHealth();
                }
                if (ferretColor.equals("brown") && b!= null) {
                    ferretImage.setImageResource(R.drawable.eat_brown);
                } else if (ferretColor.equals("red") && b!= null) {
                    ferretImage.setImageResource(R.drawable.eat_red);
                } else {
                    ferretImage.setImageResource(R.drawable.eat_gray);
                }
                AnimationDrawable eatAnimation = (AnimationDrawable)ferretImage.getDrawable();
                eatAnimation.start();
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
                if (ferretHealth != 100) {
                    ferretHealth = ferretHealth + 10;
                    updateHealth();
                }
                if (ferretColor.equals("brown") && b!= null) {
                    ferretImage.setImageResource(R.drawable.sleep_brown);
                } else if (ferretColor.equals("red") && b!= null) {
                    ferretImage.setImageResource(R.drawable.sleep_red);
                } else {
                    ferretImage.setImageResource(R.drawable.sleep_gray);
                }
                AnimationDrawable sleepAnimation = (AnimationDrawable)ferretImage.getDrawable();
                sleepAnimation.start();
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
