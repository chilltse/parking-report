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
import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.data.local.entities.ReportLog;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.entities.UserLog;
import com.example.parkingreport.data.local.viewModel.ReportLogViewModel;
import com.example.parkingreport.data.local.viewModel.ReportViewModel;
import com.example.parkingreport.data.local.viewModel.UserLogViewModel;
import com.example.parkingreport.data.local.viewModel.UserViewModel;
import com.example.parkingreport.ui.admin.AdminActivity;
import com.example.parkingreport.ui.user.UserActivity;

public class MainActivity extends AppCompatActivity {
    EditText editTextUsername, editTextPassword;
    Button buttonLogin;
    private static final String TAG = "MainActivity";

    private TextView textViewSignUp;

    private UserViewModel viewModel;
    private ReportViewModel reportViewModel; //test
    private UserLogViewModel userLogViewModel; //test
    private ReportLogViewModel reportLogViewModel; //test

    private String loginAs;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // default value for loginAs
        loginAs = User.USER;
        viewModel =  new ViewModelProvider(this)
                .get(UserViewModel.class);

        // TEST
        reportViewModel = new ViewModelProvider(this)
                .get(ReportViewModel.class);
        userLogViewModel = new ViewModelProvider(this)
                .get(UserLogViewModel.class);
        reportLogViewModel = new ViewModelProvider(this)
                .get(ReportLogViewModel.class);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignUp = findViewById(R.id.textViewSignUp);

        createDefaultUser();

        // Login option -- user/admin
        ToggleButton toggleButtonUser = findViewById(R.id.toggleButtonUser);
        ToggleButton toggleButtonAdmin = findViewById(R.id.toggleButtonAdmin);

        // —— 在这里设置默认选中 “User” ——
        toggleButtonUser.setChecked(true);
        toggleButtonAdmin.setChecked(false);
        loginAs = User.USER;
        textViewSignUp.setVisibility(View.VISIBLE);
        // ————————————————

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
        buttonLogin.setOnClickListener(v -> login());

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
        Log.d(TAG, username + " ####login as user " + viewModel.findUser(1).getName());

        viewModel.validateUser(username, password, loginAs, isMatch -> {
            if (isMatch) {
                // Navigate to the next Activity based on user role
                int userId = viewModel.findIdByName(username);
                Intent intent = loginAs == User.USER ?
                        new Intent(MainActivity.this, UserActivity.class):
                        new Intent(MainActivity.this, AdminActivity.class);
                intent.putExtra("userId", userId);
                Log.d(TAG, " ####logining as " + loginAs);
                startActivity(intent);
            } else {
                editTextPassword.setError("Invalid username or password");
            }
        });
    }

    private void createDefaultUser(){
        viewModel.insertUser(new User(BuildConfig.DEFAULT_USER_NAME_1, BuildConfig.DEFAULT_USER_EMAIL_1, BuildConfig.DEFAULT_USER_PASSWORD_1, User.USER, true));
        viewModel.insertUser(new User(BuildConfig.DEFAULT_USER_NAME_2, BuildConfig.DEFAULT_USER_EMAIL_2, BuildConfig.DEFAULT_USER_PASSWORD_2, User.USER, true));
        viewModel.insertUser(new User(BuildConfig.DEFAULT_ADMIN_NAME, BuildConfig.DEFAULT_ADMIN_EMAIL, BuildConfig.DEFAULT_ADMIN_PASSWORD, User.ADMIN, true));

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        editTextUsername.setText("");
        editTextPassword.setText("");
        // 不用再次 setOnClickListener(loginBtn...)
    }

}

