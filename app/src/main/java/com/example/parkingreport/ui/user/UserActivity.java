package com.example.parkingreport.ui.user;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parkingreport.R;

public class UserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
