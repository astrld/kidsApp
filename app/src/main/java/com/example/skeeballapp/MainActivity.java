package com.example.skeeballapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout backgroundLayout;
    private LinearLayout ballSpaceLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backgroundLayout = findViewById(R.id.relativeLayout);
        ballSpaceLayout = findViewById(R.id.layout2);
    }
}