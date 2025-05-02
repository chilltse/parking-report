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

    private int loginAs;

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

        //for debug
        tempInitUserInfo();

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

    private void tempInitUserInfo(){
//        usertest
//        viewModel.clearUser();
        viewModel.insertUser(new User(BuildConfig.DEFAULT_USER_NAME_1, BuildConfig.DEFAULT_USER_EMAIL_1, BuildConfig.DEFAULT_USER_PASSWORD_1, User.USER));
        viewModel.insertUser(new User(BuildConfig.DEFAULT_USER_NAME_2, BuildConfig.DEFAULT_USER_EMAIL_2, BuildConfig.DEFAULT_USER_PASSWORD_2, User.USER));
        viewModel.insertUser(new User(BuildConfig.DEFAULT_ADMIN_NAME, BuildConfig.DEFAULT_ADMIN_EMAIL, BuildConfig.DEFAULT_ADMIN_PASSWORD, User.ADMIN));
        viewModel.insertUser(new User("swm", "swm_2019@163.com", "swm", User.USER));
//        viewModel.clearUser();
//        viewModel.deleteUser(1);
//        viewModel.modifyUserName(1, "newComp2100");
//        viewModel.modifyUserPassword(1, "newPassword");
//        viewModel.getAllUserLive().observe(this, users -> {
//            // 当数据真正准备好后，这里一定不为 null
//            for (User u : users) {
//                Log.d("LiveUser", u.toString());
//            }
//        });

//        report test
//        reportViewModel.clearReport();
//        reportViewModel.insertReport(new Report(1,"车牌1","地点1",Report.WAIT_FOR_REVIEW));
//        reportViewModel.insertReport(new Report(2,"车牌2","地点2", Report.WAIT_FOR_REVIEW));
//        reportViewModel.insertReport(new Report(1,"车牌3","地点1", Report.WAIT_FOR_REVIEW));
//        reportViewModel.deleteReport(1);
//        reportViewModel.handleReport(1, 1, Report.APPROVED);
//        reportViewModel.handleReport(1, 1, Report.DECLINED);

//        reportViewModel.getAllReportLive().observe(this, reports -> {
//            // 当数据真正准备好后，这里一定不为 null
//            for (Report r : reports) {
//                Log.d("LiveReports", r.toString());
//            }
//        });

//        userLog test
//        userLogViewModel.clearLog();
//        userLogViewModel.insertLog(new UserLog(5, UserLog.SIGN_UP));
//        userLogViewModel.insertLog(new UserLog(6, UserLog.CANCEL_ACCOUNT));
//        userLogViewModel.insertLog(new UserLog(7, UserLog.MODIFY_NAME));
//        userLogViewModel.insertLog(new UserLog(8, UserLog.MODIFY_PASSWORD));
//        viewModel.insertUser(new User("testLog", "1@163.com", "test", User.USER));
//        viewModel.modifyUserName(5, "NewTestLog");
//        viewModel.modifyUserPassword(5, "NewTest");
//        viewModel.deleteUser(5);
//        viewModel.getAllUserLive().observe(this, users -> {
//            // 当数据真正准备好后，这里一定不为 null
//            for (User u : users) {
//                Log.d("LiveUser", u.toString());
//            }
//        });
//        userLogViewModel.getAllUserLogLive().observe(this, userLogs -> {
//            // 当数据真正准备好后，这里一定不为 null
//            for (UserLog ul : userLogs) {
//                Log.d("LiveUserLogs", ul.toString());
//            }
//        });


//        reportLog test
//        reportLogViewModel.clearLog();
//        reportLogViewModel.insertLog(new ReportLog(1,1,ReportLog.SUBMIT));
//        reportLogViewModel.insertLog(new ReportLog(1,1,ReportLog.HANDLE));
//        reportLogViewModel.insertLog(new ReportLog(2,2,ReportLog.SUBMIT));
//        reportLogViewModel.insertLog(new ReportLog(2,2,ReportLog.HANDLE));
//        reportViewModel.insertReport(new Report(1,"车牌1","地点1",Report.WAIT_FOR_REVIEW));
//        reportViewModel.insertReport(new Report(2,"车牌2","地点2", Report.WAIT_FOR_REVIEW));
//        reportViewModel.insertReport(new Report(1,"车牌3","地点1", Report.WAIT_FOR_REVIEW));
//        reportViewModel.handleReport(1, 1, Report.APPROVED);
//        reportViewModel.handleReport(2, 2, Report.DECLINED);
//        reportViewModel.handleReport(3, 1, Report.DECLINED);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

