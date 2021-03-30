package com.example.myminiodrive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DriveFirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_first);

        Runnable rnb = new ServerConnection();

        Thread th = new Thread(rnb);
        th.start();
    }
}