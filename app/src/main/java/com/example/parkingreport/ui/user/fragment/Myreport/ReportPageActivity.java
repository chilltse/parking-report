package com.example.parkingreport.ui.user.fragment.Myreport;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Insets;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import java.util.Calendar;

public class ReportPageActivity extends AppCompatActivity {

    private UserViewModel viewModel;
    private ReportViewModel reportViewModel;
    private User user;
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

        // 选择照片
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        String realPath = getRealPathFromUri(uri);
                        if (realPath != null) {
                            Toast.makeText(this, realPath, Toast.LENGTH_LONG).show();
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
                            Toast.makeText(this, realPath, Toast.LENGTH_LONG).show();
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

        Button button = findViewById(R.id.submitButton);
        button.setOnClickListener(v -> {
            boolean isOK = true;
            String gpsLocation = ((TextView)findViewById(R.id.locationInput)).getText().toString();
            String date = ((TextView)findViewById(R.id.dateInput)).getText().toString();
            String carPlate1 = ((TextView)findViewById(R.id.carPlateInput1)).getText().toString();
            String carPlate2 = ((TextView)findViewById(R.id.carPlateInput2)).getText().toString();
            if(!carPlate1.equals(carPlate2)){
                ((TextView)findViewById(R.id.carPlateInput2)).setError("Not matching!");
                isOK = false;
            }
            if(!((CheckBox )findViewById(R.id.confirmCheckbox)).isChecked()){
                isOK = false;
                Toast.makeText(getApplicationContext(), "Please click the confirm Checkbox",Toast.LENGTH_SHORT).show();
            }

            // Create report
            if(isOK){
                Report report = new Report(
                        userId,
                carPlate2,
                gpsLocation,
                Report.WAIT_FOR_REVIEW);
                reportViewModel.insertReport(report);
                finish();
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
}
