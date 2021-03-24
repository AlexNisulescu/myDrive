package com.example.myminiodrive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

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
    }
}