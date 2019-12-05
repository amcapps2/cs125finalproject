package com.example.cs125finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class newGame extends AppCompatActivity {
    private Button continueButton;
    private Button randomButton;
    private String randomName;
    private String name;
    private EditText nameInput;
    private RadioGroup colorGroup;
    private RequestQueue mQueue;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        continueButton = (Button) findViewById(R.id.continueButton);
        randomButton = (Button) findViewById(R.id.randomButton);
        nameInput = (EditText) findViewById(R.id.nameInput);
        colorGroup = (RadioGroup) findViewById(R.id.colorGroup);

        mQueue = Volley.newRequestQueue(this);

        //gets info from button listeners and textfield when continue is clicked
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameInput.getText().toString();
                String color =
                        ((RadioButton)findViewById(colorGroup.getCheckedRadioButtonId()))
                                .getText().toString();
                if (color.equals("")) {
                    color = "brown";
                }
                if (name == null || name.equals("")) {
                    name = "Your Ferret";
                }
                //runs startgame function w/ name and color
                startGame(name, color);
            }
        });

        //gets randomName from server when button is clicked
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomName();
            }
        });
    }

    public void startGame(String ferretName, String ferretColor) {
        //creates an intent w needed information & passes it to the oncreate of game.java
        Intent intent = new Intent(this, game.class);
        intent.putExtra("ferretName", ferretName);
        intent.putExtra("ferretColor", ferretColor);
        startActivity(intent);
    }

    //gets randomName from server
    private void getRandomName() {
        String url = "https://api.myjson.com/bins/1hceak";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            randomName = response.get((int) (Math.random() * response.length())).toString();
                            name = randomName;
                            nameInput.setText(randomName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}
