package com.example.dotgeneration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsPage_End extends AppCompatActivity {
    Button mainmenu, history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_page_end);

        mainmenu = findViewById(R.id.MainMenu);
        mainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ResultsPage_End.this, MainMenuPage.class);
                startActivity(intent);
            }
        });
        history = findViewById(R.id.History);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ResultsPage_End.this, HistoryPage.class);
                startActivity(intent);
            }

        });
    }
}
