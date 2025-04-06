package com.example.parkingreport.ui.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SignUpActivity extends AppCompatActivity {
    private Button buttonFinish;
    private Button buttonSendCode;
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextCode;
    private String username;
    private String emailAdress;
    private String password;
    private String verificationCode;
    private String TAG = "SignUpActivity";
    UserViewModel viewModel;
    private boolean isCodeValid = false;

    private String currentVerificationCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);


        viewModel =  new ViewModelProvider(this).get(UserViewModel.class);

        buttonFinish = findViewById(R.id.buttonFinish);
        buttonSendCode = findViewById(R.id.buttonSendCode);
        editTextUsername = findViewById(R.id.editTextNewUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextNewPassword);
        editTextCode = findViewById(R.id.editTextNewCode);

//        sendVerificationCode
        buttonSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailAdress = editTextEmail.getText().toString();
                username = editTextUsername.getText().toString();
                if(isValidEmail(emailAdress)){
                    Log.d(TAG, "validateEmail succeed!");
                    viewModel.isUserOrEmailExists(username, emailAdress, new UserViewModel.UserCheckCallback() {
                        @Override
                        public void onResult(boolean exists) {
                            if (exists) {
                                Toast.makeText(getApplicationContext(), "用户或邮箱已存在", Toast.LENGTH_SHORT).show();
                                editTextUsername.setError("Username or email has been registered!");
                                Toast.makeText(getApplicationContext(), "Username or email has been registered!", Toast.LENGTH_LONG).show();
                                return;
                            }else{
                                sendVerificationCode();
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(getApplicationContext(), "查询失败 " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    editTextEmail.setError("Please input valid Email!");
                }
            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = editTextUsername.getText().toString();
                emailAdress = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                verificationCode = editTextCode.getText().toString();

                viewModel.isUserOrEmailExists(username, emailAdress, new UserViewModel.UserCheckCallback() {
                    @Override
                    public void onResult(boolean exists) {
                        if (exists) {
                            Toast.makeText(getApplicationContext(), "用户或邮箱已存在", Toast.LENGTH_SHORT).show();
                            editTextUsername.setError("Username or email has been registered!");
                            Toast.makeText(getApplicationContext(), "Username or email has been registered!", Toast.LENGTH_LONG).show();
                        }else{
                            if (validateForm()) {
                                if(verifyCode()){
                                    // Save new user to DB
                                    Log.d(TAG, "Succeed save user data!");
                                    viewModel.insertUser(new User(emailAdress, username, password, User.USER));
                                    Toast.makeText(getApplicationContext(), "Succeed save user data!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Log.d(TAG, "Wrong verification code!");
                                    editTextCode.setError("Wrong verification code!");
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getApplicationContext(), "用户没被注册 " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signUp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private boolean validateForm() {
        if (username.isEmpty()
                || emailAdress.isEmpty()
                || password.isEmpty()
                || verificationCode.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill all fields correctly!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return isValidEmail(emailAdress);
    }


    public void sendVerificationCode() {
        // Generate the code.
        currentVerificationCode = String.format("%06d", new Random().nextInt(999999));
        isCodeValid = true;
        Log.d(TAG, "Generated Code: " + currentVerificationCode);

        // Send sms msg.
        INotificationService emailService = NotificationFactory.createService("email", getApplicationContext(), NotificationType.REGISTRATION, new HashMap<String, String>() {{
            put("name", username);
            put("code", currentVerificationCode);
        }});
        emailService.sendMsg(emailAdress);

        // Verification code expire in 2 mins.
        new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                // 每秒调用一次
                String remainingSec = String.valueOf(millisUntilFinished / 1000);
                buttonSendCode.setText(remainingSec);
                buttonSendCode.setEnabled(false);
                Log.d(TAG, "seconds remaining: " + remainingSec );
            }

            public void onFinish() {
                // When counter finished
                isCodeValid = false;
                buttonSendCode.setText("Send Code");
                buttonSendCode.setEnabled(true);
                Log.d(TAG, "Verification code has expired");
            }
        }.start();
    }

    public boolean verifyCode() {
        // Check if the code is expire or match
        if (isCodeValid && currentVerificationCode.equals(verificationCode)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}