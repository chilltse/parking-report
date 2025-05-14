package com.example.parkingreport.ui.login;

import android.content.Intent;
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
import com.example.parkingreport.data.local.viewModel.UserViewModel;
import com.example.parkingreport.ui.admin.AdminActivity;
import com.example.parkingreport.ui.user.UserActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * @author Nanxuan Xie u8016457
 * - MainActivity handles user/admin login and entry point of the app.
 * - Supports user registration, account validation, and role switching.
 * - Initializes default data (users, reports, images) on first load.
 */
public class MainActivity extends AppCompatActivity {
    EditText editTextUsername, editTextPassword;
    Button buttonLogin;
    private static final String TAG = "MainActivity";
    private TextView textViewSignUp;
    private UserViewModel viewModel;
    private String loginAs;

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
        viewModel = new ViewModelProvider(this)
                .get(UserViewModel.class);

        // Create User
        createDefaultUser();

        toggleButtonUser.setChecked(true);
        toggleButtonAdmin.setChecked(false);
        loginAs = User.USER;
        textViewSignUp.setVisibility(View.VISIBLE);

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
        textViewSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
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
                        new Intent(MainActivity.this, UserActivity.class) :
                        new Intent(MainActivity.this, AdminActivity.class);
                intent.putExtra("userId", userId);
                Log.d(TAG, "Login as:" + loginAs);
                startActivity(intent);
            } else {
                editTextPassword.setError("Invalid username or password");
            }
        });
    }

    private void createDefaultUser() {
        viewModel.insertUser(new User(BuildConfig.DEFAULT_USER_NAME_1, BuildConfig.DEFAULT_USER_EMAIL_1, BuildConfig.DEFAULT_USER_PASSWORD_1, User.DEFAULT_PROFILE_PIC, User.USER, true));
        viewModel.insertUser(new User(BuildConfig.DEFAULT_USER_NAME_2, BuildConfig.DEFAULT_USER_EMAIL_2, BuildConfig.DEFAULT_USER_PASSWORD_2, User.DEFAULT_PROFILE_PIC, User.USER, true));
        viewModel.insertUser(new User(BuildConfig.DEFAULT_ADMIN_NAME, BuildConfig.DEFAULT_ADMIN_EMAIL, BuildConfig.DEFAULT_ADMIN_PASSWORD, User.DEFAULT_PROFILE_PIC, User.ADMIN, true));

    }

    private void createDefaultReport() {
        listAssets();
        File dir = getFilesDir();

        File logFile = new File(dir, "report_logs.json");
        if (!logFile.exists()) {
            // Copy default report_logs.json to internal storage if not exists
            copyAssetToInternal("report_logs.json", logFile);
            Log.d(TAG, "report_logs.json does not exist, copied successfully");
        } else {
            Log.d(TAG, "report_logs.json already exists");
        }

        File reportFile = new File(dir, "reports.json");
        if (!reportFile.exists()) {
            // Copy default reports.json to internal storage if not exists
            copyAssetToInternal("reports.json", reportFile);
            Log.d(TAG, "reports.json does not exist, copied successfully");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Clear input fields when activity restarts
        editTextUsername.setText("");
        editTextPassword.setText("");
    }

    /**
     * List all files and folders in the assets root directory.
     */
    private void listAssets() {
        try {
            String[] list = getAssets().list("");
            Log.d("MainActivity", "Assets root directory list: " + Arrays.toString(list));
        } catch (IOException e) {
            Log.e("MainActivity", "Failed to list assets", e);
        }
    }

    /**
     * Copy a file from assets to internal storage.
     *
     * @param assetName Name of the asset file
     * @param outFile   Destination file in internal storage
     */
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
            Log.e("MainActivity", "Failed to copy asset file: " + assetName, e);
        }
    }

    /**
     * Copy a raw resource image to internal storage under the 'images' directory.
     */
    private void copyRawImageToImagesDir() {
        // 1. Get the 'images' directory under internal storage files directory
        File imagesDir = new File(getFilesDir(), "images");
        if (!imagesDir.exists()) {
            // If 'images/' does not exist, create it
            boolean ok = imagesDir.mkdirs();
            if (!ok) {
                Log.d("MainActivity", "Failed to create 'images/' directory");
                return;
            }
        }

        // 2. Prepare the target file
        File outFile = new File(imagesDir, "default_parking.png");
        if (outFile.exists()) {
            // If file already exists, do not copy again
            return;
        }

        // 3. Open raw resource and write it to the target file
        try (InputStream is = getResources().openRawResource(R.raw.default_parking);
             OutputStream os = new FileOutputStream(outFile)) {

            byte[] buffer = new byte[8 * 1024]; // 8KB buffer
            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
            Log.d("MainActivity", "Copied default_parking.png to " + outFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("MainActivity", "Failed to copy default_parking.png", e);
        }
    }
}


