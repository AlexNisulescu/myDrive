package com.example.myminiodrive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSignIn=findViewById(R.id.signinButton);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent=new Intent(getApplicationContext(), AddActivity.class);
                //startActivityForResult(intent, REQUEST_CODE);
                intent=new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);
            }
        });
    }
}