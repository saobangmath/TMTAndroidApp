package com.example.dotgeneration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dotgeneration.utils.DialogBuilderUtils;

public class LoginPage extends AppCompatActivity {
    Button enter_btn;
    EditText nric_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        nric_input = findViewById(R.id.activity_login_page_nric_input);
        enter_btn = findViewById(R.id.activity_login_page_enter_btn);

        enter_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String nric = nric_input.getText().toString();
                nric_input.setText("");
                if (nric.equals("")){
                    DialogBuilderUtils.getEmptyInputDialogBuilder(LoginPage.this).show();
                    return;
                }
                if (!checkNRICValidity(nric)){
                    DialogBuilderUtils.getInvalidInputDialogBuilder(LoginPage.this).show();
                    return;
                }
                Intent intent = new Intent(LoginPage.this, MainMenuPage.class);
                intent.putExtra("nric", nric);
                startActivity(intent);
            }
        });
    }

    public boolean checkNRICValidity(String nric){
        boolean valid = true;
        if (nric.length() != 4){
            valid = false;
        }
        else {
            valid = (nric.charAt(0) >= '0' && nric.charAt(0) <= '9') &&
                    (nric.charAt(1) >= '0' && nric.charAt(1) <= '9') &&
                    (nric.charAt(2) >= '0' && nric.charAt(2) <= '9') &&
                    (nric.charAt(3) >= 'A' && nric.charAt(3) <= 'Z');
        }
        return valid;
    }
}