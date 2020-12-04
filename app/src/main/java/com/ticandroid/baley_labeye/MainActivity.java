package com.ticandroid.baley_labeye;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("ok");
        setContentView(R.layout.activity_main);

        System.out.println("Ok2");

    }
}