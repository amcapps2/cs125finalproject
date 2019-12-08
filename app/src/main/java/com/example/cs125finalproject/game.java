package com.example.cs125finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
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
    private ImageView rollBall;
    private Handler timerHandler = new Handler();
    private MediaPlayer music;
    private static int MUSIC_PLAYED = 1;
    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroListener;
    private float xmax;
    private float xAcceleration;
    private float xVelocity;
    private float xPosition;
    public float frameTime = 0.666f;
    private int delay = 0;


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

        //sets the gyroscope sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        rollBall = (ImageView) findViewById(R.id.rollBall);
        rollBall.setVisibility(ImageView.GONE);


        gyroListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[2] > 2 || sensorEvent.values[2] < -2) {
                    if (delay == 0) {
                        delay = 1;
                        if (ferretHealth != 100) {
                            ferretHealth = ferretHealth + 10;
                            updateHealth();
                        }
                        if (ferretColor.equals("brown") && b!= null) {
                            ferretImage.setImageResource(R.drawable.play_brown);
                        } else if (ferretColor.equals("red") && b!= null) {
                            ferretImage.setImageResource(R.drawable.play_red);
                        } else {
                            ferretImage.setImageResource(R.drawable.play_gray);
                        }
                        AnimationDrawable eatAnimation = (AnimationDrawable)ferretImage.getDrawable();
                        eatAnimation.start();

                        rollBall.setVisibility(ImageView.VISIBLE);
                    }

                    Display display = getWindowManager().getDefaultDisplay();
                    xmax = (float) display.getWidth() - 10;
                    xAcceleration = sensorEvent.values[2];

                    updateBall();


                    new CountDownTimer(10000, 1000) {
                        public void onFinish() {
                            // When timer is finished
                            setIdleAnimation(ferretColor, b);
                            rollBall.setVisibility(ImageView.GONE);
                            delay = 0;
                        }
                        public void onTick(long millisUntilFinished) {
                            // millisUntilFinished    The amount of time until finished.
                        }
                    }.start();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };

        //registers the gyroscope listener
        sensorManager.registerListener(gyroListener, gyroscopeSensor,
                SensorManager.SENSOR_DELAY_NORMAL);


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

    public void updateBall() {
 /**       xVelocity += (xAcceleration * frameTime);
        float xS = (xVelocity/2)*frameTime;
        xPosition -= xS; **/
        if (xAcceleration > 0) {
            rollBall.setX(rollBall.getX() + (xAcceleration * 10));
        } else if (xAcceleration < 0) {
            rollBall.setX(rollBall.getX() + (xAcceleration * 10));
        }
    }
}
