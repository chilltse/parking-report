package com.example.parkingreport.ui.user;

import static com.example.parkingreport.utils.FileLoader.readPlatePhone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.parkingreport.R;
import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.viewModel.ReportLogViewModel;
import com.example.parkingreport.data.local.viewModel.ReportViewModel;
import com.example.parkingreport.data.local.viewModel.UserLogViewModel;
import com.example.parkingreport.data.local.viewModel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserActivity extends AppCompatActivity {

    private UserViewModel viewModel;

    private ReportViewModel reportViewModel;

    public int USER_ID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Data
        viewModel =  new ViewModelProvider(this)
                .get(UserViewModel.class);
        reportViewModel = new ViewModelProvider(this)
                .get(ReportViewModel.class);
        Intent intent = getIntent();
        int    userId   = intent.getIntExtra("userId", -1);
        Log.e("Useractivity", "user id"+userId);
        User currentUser = viewModel.findUser(userId);
        viewModel.setUser(currentUser);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView3);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();

        // Binding BottomNavigationView and NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }

    @Override
    protected void onDestroy() {
        Log.e("Useractivity","destroied!!!");
        super.onDestroy();
//        viewModel.setUser(null);
    }
}
