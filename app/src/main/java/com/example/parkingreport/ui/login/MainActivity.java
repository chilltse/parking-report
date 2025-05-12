package com.example.parkingreport.ui.login;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.parkingreport.BuildConfig;
import com.example.parkingreport.R;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.viewModel.ReportLogViewModel;
import com.example.parkingreport.data.local.viewModel.ReportViewModel;
import com.example.parkingreport.data.local.viewModel.UserLogViewModel;
import com.example.parkingreport.data.local.viewModel.UserViewModel;
import com.example.parkingreport.ui.admin.AdminActivity;
import com.example.parkingreport.ui.user.UserActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

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

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        // Login option -- user/admin
        ToggleButton toggleButtonUser = findViewById(R.id.toggleButtonUser);
        ToggleButton toggleButtonAdmin = findViewById(R.id.toggleButtonAdmin);

        // Create reports
        createDefaultReport();

        // Default pic
        copyRawImageToImagesDir();

        // default value for loginAs
//        loginAs = User.USER;
        viewModel =  new ViewModelProvider(this)
                .get(UserViewModel.class);

        // TEST
        reportViewModel = new ViewModelProvider(this)
                .get(ReportViewModel.class);
        userLogViewModel = new ViewModelProvider(this)
                .get(UserLogViewModel.class);
        reportLogViewModel = new ViewModelProvider(this)
                .get(ReportLogViewModel.class);

        // Create User
        createDefaultUser();

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
                Intent intent = loginAs.equals(User.USER) ?
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
        viewModel.insertUser(new User(BuildConfig.DEFAULT_USER_NAME_1, BuildConfig.DEFAULT_USER_EMAIL_1, BuildConfig.DEFAULT_USER_PASSWORD_1,User.DEFAULT_PROFILE_PIC ,User.USER, true));
        viewModel.insertUser(new User(BuildConfig.DEFAULT_USER_NAME_2, BuildConfig.DEFAULT_USER_EMAIL_2, BuildConfig.DEFAULT_USER_PASSWORD_2,User.DEFAULT_PROFILE_PIC, User.USER, true));
        viewModel.insertUser(new User(BuildConfig.DEFAULT_ADMIN_NAME, BuildConfig.DEFAULT_ADMIN_EMAIL, BuildConfig.DEFAULT_ADMIN_PASSWORD,User.DEFAULT_PROFILE_PIC, User.ADMIN, true));

    }

    private void createDefaultReport(){
        listAssets();
        File dir = getFilesDir();
        File logFile = new File(dir, "report_logs.json");
        if (!logFile.exists()) {
            // 如果你把文件放在 assets/data/ 子文件夹，下行要改成 "data/report_logs.json"
            copyAssetToInternal("report_logs.json", logFile);
            Log.d(TAG, "report_logs.json不存在，已经复制过来");
        }else{
            Log.d(TAG, "report_logs.json已经存在");
        }
        File reportFile = new File(dir, "reports.json");
        if (!reportFile.exists()) {
            // 如果你把文件放在 assets/data/ 子文件夹，下行要改成 "data/report_logs.json"
            copyAssetToInternal("reports.json", reportFile);
            Log.d(TAG, "reports.json不存在，已经复制过来");
        }


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


    private void listAssets() {
        try {
            String[] list = getAssets().list("");
            Log.d("MainActivity", "根目录 assets 列表: " + Arrays.toString(list));
        } catch (IOException e) {
            Log.e("MainActivity", "列出 assets 失败", e);
        }
    }


    private void copyAssetToInternal(String assetName, File outFile) {
        try (InputStream is = getAssets().open(assetName);
             OutputStream os = new FileOutputStream(outFile)) {

            byte[] buffer = new byte[4096];
            int read;
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            os.flush();
        } catch (IOException e) {
            Log.e("MainActivity", "拷贝 asset 文件失败: " + assetName, e);
        }
    }

    private void copyRawImageToImagesDir() {
        // 1. 获取内部存储 files 目录下的 images 子目录
        File imagesDir = new File(getFilesDir(), "images");
        if (!imagesDir.exists()) {
            // 如果 images/ 不存在，就创建它
            boolean ok = imagesDir.mkdirs();
            if (!ok) {
                Log.d("MainActivity", "Can't create images/");
                return;
            }
        }

        // 2. 准备目标文件
        File outFile = new File(imagesDir, "default_parking.png");
        if (outFile.exists()) {
            // 已经存在，就不再重复拷贝
            return;
        }

        // 3. 打开 raw 资源并写入到目标文件
        try (InputStream is = getResources().openRawResource(R.raw.default_parking);
             OutputStream os = new FileOutputStream(outFile)) {

            byte[] buffer = new byte[8 * 1024]; // 8KB 缓冲
            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
            Log.d("MainActivity", "已拷贝 default_parking.png 到 " + outFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("MainActivity", "拷贝 default_parking.png 失败", e);
        }
    }

}

