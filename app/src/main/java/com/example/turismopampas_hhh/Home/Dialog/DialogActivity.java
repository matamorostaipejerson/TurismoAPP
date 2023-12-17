package com.example.turismopampas_hhh.Home.Dialog;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.turismopampas_hhh.R;

import java.util.List;

public class DialogActivity extends AppCompatActivity {

    String hotelName;
    Intent intent;
    String hotelDescription;
    List<Integer> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        intent = getIntent();
        hotelName = intent.getStringExtra("hotelName");
        hotelDescription = intent.getStringExtra("hotelDescription");
        images = intent.getIntegerArrayListExtra("hotelImages");
    }
}