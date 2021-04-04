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

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent=getIntent();
        Button btnRegister=findViewById(R.id.registerButton2);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText uName=findViewById(R.id.editTextUser);
                EditText mail=findViewById(R.id.editTextMail);
                EditText pass=findViewById(R.id.editTextTextPassword2);

                if (uName.getText().toString().isEmpty() || mail.getText().
                        toString().isEmpty() || pass.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "You must complete" +
                            " all fields", Toast.LENGTH_LONG).show();
                }
                else{
                    Users newUser = new Users(uName.getText().toString(),
                            mail.getText().toString(), pass.getText().toString());
                    UsersDB db = UsersDB.getInstance(getApplicationContext());
                    db.getUsersDao().insert(newUser);
                    Toast.makeText(getApplicationContext(), "You were " +
                            "successfully registered", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(), DriveFirstActivity.class);
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