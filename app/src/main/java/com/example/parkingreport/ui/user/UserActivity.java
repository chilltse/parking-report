package com.example.parkingreport.ui.user;

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
import com.example.parkingreport.data.local.viewModel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
/**
 * User(reporter) activity
 * @author Nanxuan Xie u8016457
 */
public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Data
        UserViewModel viewModel = new ViewModelProvider(this)
                .get(UserViewModel.class);
        Intent intent = getIntent();
        int    userId   = intent.getIntExtra("userId", -1);
        Log.e("User activity", "user id"+userId);
        User currentUser = viewModel.findUser(userId);
        viewModel.setUser(currentUser);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView3);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        // Binding BottomNavigationView and NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }
}
