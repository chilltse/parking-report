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

    private String lastPhotoPath = null; // Store path of selected or captured image

    private LinearLayout selectFileLayout;
    private LinearLayout takePhotoLayout;

    private ActivityResultLauncher<String> pickImageLauncher;
    private ActivityResultLauncher<Uri> takePhotoLauncher;

    private Uri cameraImageUri; // Temporary URI storage for camera photo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_page);

        // Initialize ViewModel
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);

        // Get current timestamp for report time field
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("DefaultLocale") String currentTime = String.format(
                "%04d-%02d-%02dT%02d:%02d:%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND)
        );

        selectFileLayout = findViewById(R.id.selectFileLayout);
        takePhotoLayout = findViewById(R.id.takePhotoLayout);

        EditText dateInput = findViewById(R.id.dateInput);
        dateInput.setText(currentTime);

        // Initialize image selection launcher
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        String realPath = getRealPathFromUri(uri);
                        if (realPath != null) {
                            String newPath = copyToInternalStorage(realPath, "selected_" + System.currentTimeMillis() + ".jpg");
                            if (newPath != null) {
                                lastPhotoPath = newPath;
                                Toast.makeText(this, "Image saved to: " + newPath, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "Failed to get real path!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Initialize photo capture launcher
        takePhotoLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success && cameraImageUri != null) {
                        String realPath = getRealPathFromUri(cameraImageUri);
                        if (realPath != null) {
                            String newPath = copyToInternalStorage(realPath, "captured_" + System.currentTimeMillis() + ".jpg");
                            if (newPath != null) {
                                lastPhotoPath = newPath;
                                Toast.makeText(this, "Photo saved to: " + newPath, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "Failed to get photo real path!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Click event to select image file
        selectFileLayout.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        // Click event to take photo with camera
        takePhotoLayout.setOnClickListener(v -> startCamera());

        // Retrieve user info from intent
        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", -1);
        String userName = intent.getStringExtra("userName");

        // Submit button logic
        Button button = findViewById(R.id.submitButton);
        button.setOnClickListener(v -> {
            boolean isOK = true;

            String gpsLocation = ((TextView) findViewById(R.id.locationInput)).getText().toString().trim();
            String date = ((TextView) findViewById(R.id.dateInput)).getText().toString().trim();
            String carPlate1 = ((TextView) findViewById(R.id.carPlateInput1)).getText().toString().trim();
            String carPlate2 = ((TextView) findViewById(R.id.carPlateInput2)).getText().toString().trim();

            // 1. Validate inputs are not empty
            if (carPlate1.isEmpty()) {
                ((TextView) findViewById(R.id.carPlateInput1)).setError("Please enter license plate number");
                isOK = false;
            }
            if (carPlate2.isEmpty()) {
                ((TextView) findViewById(R.id.carPlateInput2)).setError("Please re-enter license plate number");
                isOK = false;
            }

            // 2. Validate both plate inputs match
            if (isOK && !carPlate1.equals(carPlate2)) {
                ((TextView) findViewById(R.id.carPlateInput2)).setError("Inputs do not match");
                isOK = false;
            }

            // 3. Confirm checkbox validation
            CheckBox confirmCb = findViewById(R.id.confirmCheckbox);
            if (!confirmCb.isChecked()) {
                Toast.makeText(getApplicationContext(), "Please check the confirmation box", Toast.LENGTH_SHORT).show();
                isOK = false;
            }

            // 4. Validate image selected or taken
            if (lastPhotoPath == null) {
                Toast.makeText(getApplicationContext(), "Please select an image or take a photo first", Toast.LENGTH_SHORT).show();
                isOK = false;
            }

            // If all validations passed, submit report
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

        // Auto-fill current GPS location
        GPS.getCurrentLocation(this, new GPS.GpsCallback() {
            @Override
            public void onLocationReady(double lat, double lng) {
                String locationText = lat + ", " + lng;
                ((TextView) findViewById(R.id.locationInput)).setText(locationText);
            }
        });
    }

    /**
     * Start camera to take a photo and save to a temporary URI.
     */
    private void startCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_" + System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        cameraImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        if (cameraImageUri != null) {
            takePhotoLauncher.launch(cameraImageUri);
        } else {
            Toast.makeText(this, "Failed to create image file", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Convert a content Uri to a real file path.
     */
    private String getRealPathFromUri(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
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

    /**
     * Copy the selected or captured image to the app's internal storage directory.
     */
    private String copyToInternalStorage(String sourcePath, String fileName) {
        File srcFile = new File(sourcePath);

        File subDir = new File(getFilesDir(), "images");
        if (!subDir.exists() && !subDir.mkdirs()) {
            Toast.makeText(this, "Failed to create directory", Toast.LENGTH_SHORT).show();
            return null;
        }

        File destFile = new File(subDir, fileName);

        try (InputStream in = new FileInputStream(srcFile);
             OutputStream out = new FileOutputStream(destFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            return destFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Copy failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}