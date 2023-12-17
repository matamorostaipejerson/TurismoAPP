package com.example.turismopampas_hhh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.turismopampas_hhh.CheckIn.CheckInActivity;
import com.example.turismopampas_hhh.Login.LoginActivity;

public class IntroductionActivity extends AppCompatActivity {
    Button MBlogIn,MBCheckIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        initComponents();
        initListeners();
    }

    public void initComponents(){
        MBlogIn=findViewById(R.id.MBlogIn);
        MBCheckIn=findViewById(R.id.MBCheckIn);

    }
    public void initListeners(){
        MBlogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browseToLogin();
            }
        });

        MBCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browseToCheckIn();
            }
        });
    }

    private View.OnClickListener browseToLogin() {
        Intent intent= new Intent(this, LoginActivity.class);
        startActivity(intent);
        return null;
    }
    private View.OnClickListener browseToCheckIn() {
        Intent intent= new Intent(this, CheckInActivity.class);
        startActivity(intent);
        return null;
    }
}