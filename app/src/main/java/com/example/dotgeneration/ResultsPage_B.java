package com.example.dotgeneration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsPage_B extends AppCompatActivity {

    Button mainMenu_btn, playAgain_btn;
    TextView timeTaken_txt, accuracy_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_page_b);

        timeTaken_txt = findViewById(R.id.activity_results_page_b_timeTaken_txt);
        timeTaken_txt.setText("Time taken for part B: " + getIntent().getExtras().getInt("timeB", 0));

        accuracy_txt = findViewById(R.id.activity_results_page_b_accuracy_txt);
        accuracy_txt.setText("Error rate for part B: " + getIntent().getExtras().getDouble("errorB",0));

        mainMenu_btn = findViewById(R.id.activity_results_page_b_mainMenu_btn);
        mainMenu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ResultsPage_B.this, MainMenuPage.class);
                intent.putExtra("nric", getIntent().getExtras().getString("nric"));
                startActivity(intent);
            }
        });
        playAgain_btn = findViewById(R.id.activity_results_page_b_playAgain_btn);
        playAgain_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ResultsPage_B.this, StartGame_A.class);
                intent.putExtra("nric", getIntent().getExtras().getString("nric"));
                startActivity(intent);
            }

        });
    }
}
