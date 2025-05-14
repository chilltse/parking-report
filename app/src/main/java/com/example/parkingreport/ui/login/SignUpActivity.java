// User registration screen logic with email verification and avatar selection
package com.example.parkingreport.ui.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.parkingreport.R;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.viewModel.UserViewModel;
import com.example.parkingreport.service.NotificationFactory;
import com.example.parkingreport.service.NotificationType;
import com.example.parkingreport.service.api.INotificationService;

import java.util.HashMap;
import java.util.Random;

/**
 * @author Nanxuan Xie
 * Activity for user(reporter) sign-up:
 * - Displays form for username, email, password, and verification code
 * - Generates and validates email verification code
 * - Allows avatar selection from predefined images
 * - Persists new user to Json
 */
public class SignUpActivity extends AppCompatActivity {

    // UI components
    private Button buttonFinish, buttonSendCode;
    private EditText editTextUsername, editTextEmail, editTextPassword, editTextCode;

    // Data variables
    private String username, emailAdress, password, verificationCode;
    private String currentVerificationCode = "";
    private boolean isCodeValid = false;

    // Avatar selection variables
    private final int[] avatarResIds = {
            R.drawable.dog, R.drawable.dog2, R.drawable.chicken,
            R.drawable.cat, R.drawable.panda, R.drawable.rabbit
    };
    private int selectedAvatarResId = -1;
    private ImageView lastSelectedView = null;

    // ViewModel for DB operations
    private UserViewModel viewModel;

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Initialize ViewModel (MVVM pattern)
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Initialize UI components
        buttonFinish = findViewById(R.id.buttonFinish);
        buttonSendCode = findViewById(R.id.buttonSendCode);
        editTextUsername = findViewById(R.id.editTextNewUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextNewPassword);
        editTextCode = findViewById(R.id.editTextNewCode);
        GridLayout avatarGrid = findViewById(R.id.avatarGrid);

        // Dynamically load avatars into GridLayout
        for (int resId : avatarResIds) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(resId);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setOnClickListener(v -> {
                // Handle avatar selection highlighting
                selectedAvatarResId = resId;
                if (lastSelectedView != null) lastSelectedView.setBackground(null);
                v.setBackgroundResource(R.drawable.avatar_border);
                lastSelectedView = (ImageView) v;
            });

            avatarGrid.addView(imageView);
        }

        // Handle sending email verification code
        buttonSendCode.setOnClickListener(view -> {
            emailAdress = editTextEmail.getText().toString();
            username = editTextUsername.getText().toString();
            if (isValidEmail(emailAdress)) {
                // Check if username or email already exists in DB
                viewModel.isUserOrEmailExists(username, emailAdress, new UserViewModel.UserCheckCallback() {
                    @Override
                    public void onResult(boolean exists) {
                        if (exists) {
                            Toast.makeText(getApplicationContext(), "Username or email already exists", Toast.LENGTH_SHORT).show();
                            editTextUsername.setError("Already registered");
                        } else {
                            sendVerificationCode(); // Send code if not exists
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getApplicationContext(), "Query failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                editTextEmail.setError("Please enter a valid email");
            }
        });

        // Handle user registration (final submit)
        buttonFinish.setOnClickListener(view -> {
            username = editTextUsername.getText().toString().trim();
            emailAdress = editTextEmail.getText().toString().trim();
            password = editTextPassword.getText().toString().trim();
            verificationCode = editTextCode.getText().toString().trim();

            // Validate avatar selection first
            if (selectedAvatarResId == -1) {
                Toast.makeText(this, "Please select an avatar", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate input fields (username, email, password, code)
            if (!validateForm()) {
                return; // Stop if invalid input
            }

            // Validate verification code separately
            if (!verifyCode()) {
                editTextCode.setError("Invalid or expired verification code");
                return;
            }

            // Check DB for existing username/email again before final registration
            viewModel.isUserOrEmailExists(username, emailAdress, new UserViewModel.UserCheckCallback() {
                @Override
                public void onResult(boolean exists) {
                    if (exists) {
                        editTextUsername.setError("Username or email already registered");
                        Toast.makeText(getApplicationContext(), "Username or email already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        // Save avatar selection in SharedPreferences
                        String resourceName = getResources().getResourceEntryName(selectedAvatarResId);
                        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        prefs.edit()
                                .putInt("avatarResId", selectedAvatarResId)
                                .putString("avatarName", resourceName)
                                .apply();

                        String drawablePath = "android.resource://" + getPackageName() + "/drawable/" + resourceName;
                        Log.d(TAG, "Avatar resource path: " + drawablePath);

                        // Insert user into database
                        viewModel.insertUser(new User(username, emailAdress, password, drawablePath, User.USER, true));
                        Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();

                        finish(); // Close activity after successful registration
                    }
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getApplicationContext(), "Query failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Adjust UI padding for system insets (safe areas, status bar etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signUp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Validates all form inputs and shows errors on invalid fields.
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateForm() {
        boolean isValid = true;

        if (username.isEmpty()) {
            editTextUsername.setError("Please enter a username");
            isValid = false;
        }

        if (emailAdress.isEmpty()) {
            editTextEmail.setError("Please enter an email");
            isValid = false;
        } else if (!isValidEmail(emailAdress)) {
            editTextEmail.setError("Please enter a valid email");
            isValid = false;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Please enter a password");
            isValid = false;
        }

        if (verificationCode.isEmpty()) {
            editTextCode.setError("Please enter the verification code");
            isValid = false;
        }

        if (!isValid) {
            Toast.makeText(getApplicationContext(), "Please correct the highlighted fields", Toast.LENGTH_SHORT).show();
        }

        return isValid;
    }

    // Generate and send a 6-digit email verification code
    private void sendVerificationCode() {
        currentVerificationCode = String.format("%06d", new Random().nextInt(999999));
        isCodeValid = true;
        Log.d(TAG, "Verification code: " + currentVerificationCode);

        // Create and send email via notification service
        INotificationService emailService = NotificationFactory.createService(
                "email", getApplicationContext(), NotificationType.REGISTRATION,
                new HashMap<String, String>() {{
                    put("name", username);
                    put("code", currentVerificationCode);
                }});
        emailService.sendMsg(emailAdress);

        // Disable resend button with 120s countdown to prevent spamming
        new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                buttonSendCode.setText(String.valueOf(millisUntilFinished / 1000));
                buttonSendCode.setEnabled(false);
            }

            public void onFinish() {
                isCodeValid = false; // Expire code after timeout
                buttonSendCode.setText("Send Code");
                buttonSendCode.setEnabled(true);
            }
        }.start();
    }

    // Check if entered verification code is correct and not expired
    private boolean verifyCode() {
        return isCodeValid && currentVerificationCode.equals(verificationCode);
    }

    // Validate email format using Android built-in pattern matcher
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
