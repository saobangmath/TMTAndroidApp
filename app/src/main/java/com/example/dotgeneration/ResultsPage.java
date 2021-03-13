package com.example.dotgeneration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsPage extends AppCompatActivity {
    Button mainmenu, playagain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_page);

        mainmenu = findViewById(R.id.MainMenu);
        mainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ResultsPage.this, MainMenuPage.class);
                startActivity(intent);
            }
        });
        playagain = findViewById(R.id.PlayAgain);
        playagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ResultsPage.this, StartGame_A.class);
                startActivity(intent);
            }

        });
    }
}