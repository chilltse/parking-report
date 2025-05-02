package com.example.parkingreport.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.parkingreport.R;
import com.example.parkingreport.data.local.viewModel.ReportViewModel;
import com.example.parkingreport.data.local.viewModel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {
    private UserViewModel viewModel;

    private ReportViewModel reportViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Data
        viewModel =  new ViewModelProvider(this)
                .get(UserViewModel.class);
        reportViewModel = new ViewModelProvider(this)
                .get(ReportViewModel.class);
        Intent intent = getIntent();
        int    userId   = intent.getIntExtra("userId", -1);
        viewModel.setUserId(userId);
        Log.e("Useractivity", "user id"+userId);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView5);
        NavController navController = navHostFragment.getNavController();

        // Binding BottomNavigationView and NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
