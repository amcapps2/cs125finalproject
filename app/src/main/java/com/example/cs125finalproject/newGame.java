package com.example.cs125finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.w3c.dom.Text;

public class newGame extends AppCompatActivity {
    private Button continueButton;
    private EditText nameInput;
    private RadioGroup colorGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        continueButton = (Button) findViewById(R.id.continueButton);
        nameInput = (EditText) findViewById(R.id.nameInput);

        colorGroup = (RadioGroup) findViewById(R.id.colorGroup);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String color =
                        ((RadioButton)findViewById(colorGroup.getCheckedRadioButtonId()))
                                .getText().toString();
                if (color.equals("")) {
                    color = "brown";
                }
                startGame(name, color);
            }
        });
    }

    public void startGame(String ferretName, String ferretColor) {
        Intent intent = new Intent(this, game.class);
        intent.putExtra("ferretName", ferretName);
        intent.putExtra("ferretColor", ferretColor);
        startActivity(intent);
    }
}