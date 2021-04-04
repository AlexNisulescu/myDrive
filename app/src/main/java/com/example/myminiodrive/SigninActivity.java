package com.example.myminiodrive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class SigninActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        intent=getIntent();
        Button btnSignIn=findViewById(R.id.AppSignInButton);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username=findViewById(R.id.editTextTextUserName);
                EditText password=findViewById(R.id.editTextTextPassword);
                UsersDB db = UsersDB.getInstance(getApplicationContext());
                List<Users>usersList=db.getUsersDao().getUsersByNameAndPassword(username.getText().toString(), password.getText().toString());

                if (usersList.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Incorrect " +
                            "username or password", Toast.LENGTH_LONG).show();
                }
                else{
                    intent=new Intent(getApplicationContext(), DriveFirstActivity.class);
                    startActivity(intent);
                }

            }
        });
        Switch sw=findViewById(R.id.colorModeSwitch);
        boolean state=(boolean) intent.getSerializableExtra(MainActivity.SWITCH_STATE);
        sw.setChecked(state);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }
}