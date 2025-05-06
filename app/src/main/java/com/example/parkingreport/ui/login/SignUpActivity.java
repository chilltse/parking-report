package com.example.parkingreport.ui.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
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

public class SignUpActivity extends AppCompatActivity {

    private Button buttonFinish, buttonSendCode;
    private EditText editTextUsername, editTextEmail, editTextPassword, editTextCode;
    private String username, emailAdress, password, verificationCode, currentVerificationCode = "";
    private static final String TAG = "SignUpActivity";
    private UserViewModel viewModel;
    private boolean isCodeValid = false;

    private final int[] avatarResIds = {
            R.drawable.dog, R.drawable.dog2, R.drawable.chicken,
            R.drawable.cat, R.drawable.panda, R.drawable.rabbit
    };

    private int selectedAvatarResId = -1;
    private ImageView lastSelectedView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        buttonFinish = findViewById(R.id.buttonFinish);
        buttonSendCode = findViewById(R.id.buttonSendCode);
        editTextUsername = findViewById(R.id.editTextNewUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextNewPassword);
        editTextCode = findViewById(R.id.editTextNewCode);
        GridLayout avatarGrid = findViewById(R.id.avatarGrid);

        // 加载头像选择器
        for (int resId : avatarResIds) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(resId);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setOnClickListener(v -> {
                selectedAvatarResId = resId;
                if (lastSelectedView != null) lastSelectedView.setBackground(null);
                v.setBackgroundResource(R.drawable.avatar_border);
                lastSelectedView = (ImageView) v;
            });

            avatarGrid.addView(imageView);
        }

        buttonSendCode.setOnClickListener(view -> {
            emailAdress = editTextEmail.getText().toString();
            username = editTextUsername.getText().toString();
            if (isValidEmail(emailAdress)) {
                viewModel.isUserOrEmailExists(username, emailAdress, new UserViewModel.UserCheckCallback() {
                    @Override
                    public void onResult(boolean exists) {
                        if (exists) {
                            Toast.makeText(getApplicationContext(), "用户或邮箱已存在", Toast.LENGTH_SHORT).show();
                            editTextUsername.setError("已注册");
                        } else {
                            sendVerificationCode();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getApplicationContext(), "查询失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                editTextEmail.setError("请输入有效邮箱");
            }
        });

        buttonFinish.setOnClickListener(view -> {
            username = editTextUsername.getText().toString();
            emailAdress = editTextEmail.getText().toString();
            password = editTextPassword.getText().toString();
            verificationCode = editTextCode.getText().toString();

            if (selectedAvatarResId == -1) {
                Toast.makeText(this, "请先选择一个头像", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.isUserOrEmailExists(username, emailAdress, new UserViewModel.UserCheckCallback() {
                @Override
                public void onResult(boolean exists) {
                    if (exists) {
                        editTextUsername.setError("用户名或邮箱已注册");
                        Toast.makeText(getApplicationContext(), "用户名或邮箱已注册", Toast.LENGTH_SHORT).show();
                    } else {
                        if (validateForm() && verifyCode()) {
                            // 保存头像到 SharedPreferences
                            String resourceName = getResources().getResourceEntryName(selectedAvatarResId);
                            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            prefs.edit()
                                    .putInt("avatarResId", selectedAvatarResId)
                                    .putString("avatarName", resourceName)
                                    .apply();

                            String drawablePath = "android.resource://" + getPackageName() + "/drawable/" + resourceName;
                            Log.d(TAG, "头像资源路径: " + drawablePath);
                            // 注册成功，保存用户信息
                            viewModel.insertUser(new User(username, emailAdress, password,drawablePath ,User.USER, true));
                            Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "头像已保存: " + drawablePath, Toast.LENGTH_LONG).show();

                            finish();
                        } else {
                            editTextCode.setError("验证码错误或已过期");
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getApplicationContext(), "查询失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signUp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean validateForm() {
        if (username.isEmpty() || emailAdress.isEmpty() || password.isEmpty() || verificationCode.isEmpty()) {
            Toast.makeText(getApplicationContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
            return false;
        }
        return isValidEmail(emailAdress);
    }

    private void sendVerificationCode() {
        currentVerificationCode = String.format("%06d", new Random().nextInt(999999));
        isCodeValid = true;
        Log.d(TAG, "验证码: " + currentVerificationCode);

        INotificationService emailService = NotificationFactory.createService(
                "email", getApplicationContext(), NotificationType.REGISTRATION,
                new HashMap<String, String>() {{
                    put("name", username);
                    put("code", currentVerificationCode);
                }});
        emailService.sendMsg(emailAdress);

        new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                buttonSendCode.setText(String.valueOf(millisUntilFinished / 1000));
                buttonSendCode.setEnabled(false);
            }

            public void onFinish() {
                isCodeValid = false;
                buttonSendCode.setText("Send Code");
                buttonSendCode.setEnabled(true);
            }
        }.start();
    }

    private boolean verifyCode() {
        return isCodeValid && currentVerificationCode.equals(verificationCode);
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}