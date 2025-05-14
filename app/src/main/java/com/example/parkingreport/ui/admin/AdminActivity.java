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
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.viewModel.ReportViewModel;
import com.example.parkingreport.data.local.viewModel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    private UserViewModel viewModel;
    private ReportViewModel reportViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("AdminActivity", "before setContentView");

        // Initialize ViewModels (scoped to this Activity)
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);

        // Get userId from the Intent that started this activity
        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", -1);
        Log.e("AdminActivity", "Received userId: " + userId);

        // Retrieve User object based on userId
        User currentUser = viewModel.findUser(userId);
        Log.d("AdminActivity", "Loaded user: " + currentUser);

        // Set the current user in the ViewModel for global access
        viewModel.setUser(currentUser);

        // Set layout for the admin activity
        setContentView(R.layout.activity_admin);

        // Initialize BottomNavigationView and NavController for navigation handling
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView5);
        NavController navController = navHostFragment.getNavController();

        // Bind BottomNavigationView with NavController to handle navigation events
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    protected void onDestroy() {
        Log.e("AdminActivity", "Destroyed!!!");
        super.onDestroy();

        // Optional cleanup if needed (currently commented out)
        // viewModel.setUser(null);
    }
}
