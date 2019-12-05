package com.example.cs125finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button newGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newGameButton = (Button) findViewById(R.id.newGameButton);
        //starts newGame (where u fill in the info) when clicked)
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameSetup();
            }
        });
    }

    public void startGameSetup() {
        Intent intent = new Intent(this, newGame.class);
        startActivity(intent);
    }
}