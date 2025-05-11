package com.example.parkingreport.ui.reportManager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.parkingreport.R;
import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.viewModel.ReportViewModel;
import com.example.parkingreport.data.local.viewModel.UserViewModel;
import com.example.parkingreport.utils.GPS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class ReportPageActivity extends AppCompatActivity {

    private UserViewModel viewModel;
    private ReportViewModel reportViewModel;
    private User user;
    private String lastPhotoPath = null;
    private LinearLayout selectFileLayout;
    private LinearLayout takePhotoLayout;

    private ActivityResultLauncher<String> pickImageLauncher;
    private ActivityResultLauncher<Uri> takePhotoLauncher;

    private Uri cameraImageUri; // 拍照用到的uri临时保存

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_page);

        Intent intent = getIntent();
        reportViewModel = new ViewModelProvider(this)
                .get(ReportViewModel.class);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 月份从0开始
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        @SuppressLint("DefaultLocale") String currentTime = String.format("%04d-%02d-%02dT%02d:%02d:%02d", year, month, day, hour, minute, second);
        selectFileLayout = findViewById(R.id.selectFileLayout);
        takePhotoLayout = findViewById(R.id.takePhotoLayout);

        EditText dateInput = findViewById(R.id.dateInput);
        dateInput.setText(currentTime);

        // 选择图片
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        String realPath = getRealPathFromUri(uri);
                        if (realPath != null) {
                            String newPath = copyToInternalStorage(realPath, "selected_" + System.currentTimeMillis() + ".jpg");
                            if (newPath != null) {
                                lastPhotoPath = newPath;
                                Toast.makeText(this, "图片已保存至: " + newPath, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "无法获取真实路径！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

// 拍照
        takePhotoLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success && cameraImageUri != null) {
                        String realPath = getRealPathFromUri(cameraImageUri);
                        if (realPath != null) {
                            String newPath = copyToInternalStorage(realPath, "captured_" + System.currentTimeMillis() + ".jpg");
                            if (newPath != null) {
                                lastPhotoPath = newPath;
                                Toast.makeText(this, "照片已保存至: " + newPath, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "无法获取拍照图片真实路径！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // 点击选择文件
        selectFileLayout.setOnClickListener(v -> {
            pickImageLauncher.launch("image/*");
        });

        // 点击拍照
        takePhotoLayout.setOnClickListener(v -> {
            startCamera();
        });


        // User info related
        intent = getIntent();
        int    userId   = intent.getIntExtra("userId", -1);
        String userName = intent.getStringExtra("userName");

        Button button = findViewById(R.id.submitButton);
        button.setOnClickListener(v -> {
            boolean isOK = true;

            String gpsLocation = ((TextView) findViewById(R.id.locationInput))
                    .getText().toString().trim();
            String date = ((TextView) findViewById(R.id.dateInput))
                    .getText().toString().trim();
            String carPlate1 = ((TextView) findViewById(R.id.carPlateInput1))
                    .getText().toString().trim();
            String carPlate2 = ((TextView) findViewById(R.id.carPlateInput2))
                    .getText().toString().trim();

            // 1. 非空校验
            if (carPlate1.isEmpty()) {
                ((TextView) findViewById(R.id.carPlateInput1))
                        .setError("请输入车牌号");
                isOK = false;
            }
            if (carPlate2.isEmpty()) {
                ((TextView) findViewById(R.id.carPlateInput2))
                        .setError("请再次输入车牌号");
                isOK = false;
            }

            // 2. 相等性校验（只有在都非空时才做）
            if (isOK && !carPlate1.equals(carPlate2)) {
                ((TextView) findViewById(R.id.carPlateInput2))
                        .setError("两次输入不一致");
                isOK = false;
            }

            // 3. 勾选框校验
            CheckBox confirmCb = findViewById(R.id.confirmCheckbox);
            if (!confirmCb.isChecked()) {
                Toast.makeText(getApplicationContext(),
                        "请勾选确认框", Toast.LENGTH_SHORT).show();
                isOK = false;
            }

            // 4. 图片校验
            if (lastPhotoPath == null) {
                Toast.makeText(getApplicationContext(),
                        "请先选择图片或拍照", Toast.LENGTH_SHORT).show();
                isOK = false;
            }

            // 最终 OK 时写入
            if (isOK) {
                Report report = new Report(
                        userId,
                        userName,
                        carPlate2,
                        gpsLocation,
                        lastPhotoPath,
                        Report.WAIT_FOR_REVIEW
                );
                reportViewModel.insertReport(report);
                finish();
            }
        });
        GPS.getCurrentLocation(this, new GPS.GpsCallback() {
            @Override
            public void onLocationReady(double lat, double lng) {
                String locationText = lat + ", " + lng;
                ((TextView) findViewById(R.id.locationInput)).setText(locationText);
            }
        });

    }

    private void startCamera() {
        // 创建一条空的图片uri，用来存储拍照的照片
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_" + System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        cameraImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        if (cameraImageUri != null) {
            takePhotoLauncher.launch(cameraImageUri);
        } else {
            Toast.makeText(this, "无法创建图片文件", Toast.LENGTH_SHORT).show();
        }
    }

    // 将Uri转换成真实路径
    private String getRealPathFromUri(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { MediaStore.Images.Media.DATA };
            try (android.database.Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    filePath = cursor.getString(columnIndex);
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        return filePath;
    }

    private String copyToInternalStorage(String sourcePath, String fileName) {
        File srcFile = new File(sourcePath);


        File subDir = new File(getFilesDir(), "images");
        if (!subDir.exists()) {
            if (!subDir.mkdirs()) {
                Toast.makeText(this, "创建文件夹失败", Toast.LENGTH_SHORT).show();
                return null;
            }
        }


        File destFile = new File(subDir, fileName);

        try (InputStream in = new FileInputStream(srcFile);
             OutputStream out = new FileOutputStream(destFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            return destFile.getAbsolutePath(); // 返回完整路径
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "复制失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
