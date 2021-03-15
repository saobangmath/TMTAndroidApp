package com.example.dotgeneration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ResultsPage_A extends AppCompatActivity {
    Button partB_btn;
    double errorA;
    double accuracyRate;
    int timeA;

    EditText Timetaken;
    EditText Accuracy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_page_a);

        Bundle bundle = getIntent().getExtras();
        errorA = bundle.getDouble("errorA");
        timeA = bundle.getInt("timeA");

        setResults();

        partB_btn = findViewById(R.id.activity_results_page_a_continuePartB_btn);
        partB_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ResultsPage_A.this, StartGame_B.class);
                Bundle bundle = new Bundle();
                bundle.putString("nric", getIntent().getStringExtra("nric"));
                bundle.putDouble("errorA", errorA);
                bundle.putInt("timeA", timeA);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        });
    }

    public void setResults() {
        Timetaken = (EditText) findViewById(R.id.Timetaken);
        Accuracy = (EditText) findViewById(R.id.Accuracy);

        Timetaken.setText("Time taken: " + timeA);
        accuracyRate = 100 - Math.round(errorA / 25 * 100);
        Accuracy.setText("Accuracy: " + accuracyRate + "%");
        if (accuracyRate < 0)
            Accuracy.setText("Accuracy: 0%");
    }
}