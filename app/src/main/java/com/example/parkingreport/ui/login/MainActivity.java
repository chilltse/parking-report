package com.example.parkingreport.ui.login;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.parkingreport.BuildConfig;
import com.example.parkingreport.R;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.viewModel.UserViewModel;
import com.example.parkingreport.ui.admin.AdminActivity;
import com.example.parkingreport.ui.user.UserActivity;

public class MainActivity extends AppCompatActivity {
    EditText editTextUsername, editTextPassword;
    Button buttonLogin;
    private static final String TAG = "MainActivity";

    private TextView textViewSignUp;

    private UserViewModel viewModel;

    private int loginAs;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // default value for loginAs
        loginAs = User.USER;
        viewModel =  new ViewModelProvider(this).get(UserViewModel.class);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignUp = findViewById(R.id.textViewSignUp);


// Login option -- user/admin
        ToggleButton toggleButtonUser = findViewById(R.id.toggleButtonUser);
        ToggleButton toggleButtonAdmin = findViewById(R.id.toggleButtonAdmin);

        toggleButtonUser.setOnClickListener(v -> {
            toggleButtonUser.setChecked(true);
            toggleButtonAdmin.setChecked(false);
            loginAs = User.USER;
            textViewSignUp.setVisibility(View.VISIBLE);
        });

        toggleButtonAdmin.setOnClickListener(v -> {
            toggleButtonUser.setChecked(false);
            toggleButtonAdmin.setChecked(true);
            loginAs = User.ADMIN;
            textViewSignUp.setVisibility(View.GONE);
        });


        // Sign up
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void login() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        Log.d(TAG, username + " ####login as " + loginAs);

        viewModel.validateUser(username, password, loginAs, isMatch -> {
            if (isMatch) {
                // Navigate to the next Activity based on user role
                Intent intent = loginAs == User.USER ?
                        new Intent(MainActivity.this, UserActivity.class):
                        new Intent(MainActivity.this, AdminActivity.class);
                intent.putExtra("UserName", username);
                startActivity(intent);
            } else {
                editTextPassword.setError("Invalid username or password");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

