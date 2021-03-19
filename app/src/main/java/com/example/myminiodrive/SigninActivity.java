package com.example.myminiodrive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SigninActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Button btnSignIn=findViewById(R.id.AppSignInButton);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username=findViewById(R.id.editTextTextUserName);
                EditText password=findViewById(R.id.editTextTextPassword);

                if (username.getText().toString().equals("nisu") && password.getText().toString().equals("parola123")){
                    intent=new Intent(getApplicationContext(), DriveFirstActivity.class);
                    startActivity(intent);
                }
                else {

                }

            }
        });
    }
}