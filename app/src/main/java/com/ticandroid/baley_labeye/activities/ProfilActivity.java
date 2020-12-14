package com.ticandroid.baley_labeye.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.ticandroid.baley_labeye.R;

public class ProfilActivity extends AppCompatActivity {
    private transient ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        image = (ImageView) findViewById(R.id.image);
    }
}