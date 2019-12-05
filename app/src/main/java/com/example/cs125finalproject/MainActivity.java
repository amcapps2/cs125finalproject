package com.example.cs125finalproject;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button newGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newGameButton = (Button) findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameSetup();
            }
        });
        final ImageView ferretIcon = (ImageView) findViewById(R.id.ferretIcon);
        ferretIcon.setImageResource(R.drawable.iconanimation);
        final AnimationDrawable iconanimation = (AnimationDrawable)ferretIcon.getDrawable();
        iconanimation.start();

    }

    public void startGameSetup() {
        Intent intent = new Intent(this, newGame.class);
        startActivity(intent);
    }
}