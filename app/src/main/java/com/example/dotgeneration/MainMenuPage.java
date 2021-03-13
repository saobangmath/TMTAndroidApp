package com.example.dotgeneration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dotgeneration.utils.DialogBuilderUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainMenuPage extends AppCompatActivity {

    static String _endpoint = "https://murmuring-forest-94314.herokuapp.com";

    Button startGameA_btn, history_btn, back_btn;
    ProgressDialog dialog;
    String _nric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_page);

        final String nric  = getIntent().getStringExtra("nric");
        _nric = nric;

        startGameA_btn = findViewById(R.id.activity_main_menu_page_startGameA_btn);
        startGameA_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainMenuPage.this, StartGame_A.class);
                startActivity(intent);
            }

        });

        history_btn = findViewById(R.id.activity_main_menu_page_history_btn);
        history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                dialog = DialogBuilderUtils.buildProgressDialog(MainMenuPage.this);
                try {
                    URL url = new URL(_endpoint + "/records/get/" + nric + "/");
                    new getUserHistoryRecords().execute(url);
                } catch (MalformedURLException e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }

        });

        back_btn = findViewById(R.id.activity_main_menu_page_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainMenuPage.this, LoginPage.class);
                startActivity(intent);
            }

        });
    }
    class getUserHistoryRecords extends AsyncTask<URL,Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String body = "";
            for (URL url : urls) {
                try {
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK){
                        InputStreamReader isReader = new InputStreamReader(conn.getInputStream());
                        BufferedReader reader = new BufferedReader(isReader);
                        String str;
                        while((str = reader.readLine())!= null) {
                            body += str;
                        }
                    }

                } catch (IOException e) {
                }
            }
            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            Intent intent = new Intent(MainMenuPage.this, HistoryPage.class);
            Bundle bundle = new Bundle();
            bundle.putString("records", s);
            bundle.putString("nric", _nric);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}