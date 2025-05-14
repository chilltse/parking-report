package com.example.parkingreport.ui.reportManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.parkingreport.R;
import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.viewModel.ReportViewModel;
import com.example.parkingreport.service.NotificationFactory;
import com.example.parkingreport.service.NotificationType;
import com.example.parkingreport.service.api.INotificationService;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.parkingreport.utils.FileLoader.readPlatePhone;

/**
 * @author Nanxuan Xie u8016457
 */
public class ReportDetailActivity extends AppCompatActivity {
    private ReportViewModel reportViewModel;
    private ToggleButton approveButton;
    private ToggleButton rejectButton;
    private EditText feedbackTextViewInput;
    private final String PLATE_PHONE_FILE = "plate_phone_2500.csv";

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ReportViewModel
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);

        // Get data passed from previous activity
        Intent intent = getIntent();
        int reportId = intent.getIntExtra("reportId", -1);
        String plate = intent.getStringExtra("plate");
        String status = intent.getStringExtra("status");
        String time = intent.getStringExtra("time");
        String location = intent.getStringExtra("location");
        String feedback = intent.getStringExtra("feedback");
        String reporterName = intent.getStringExtra("reporterName");
        String loginAs = intent.getStringExtra("loginAs");
        String picUrl = intent.getStringExtra("picUrl");

        // Determine which layout to use based on report status and user role
        assert status != null;
        int layoutId = status.equals(Report.WAIT_FOR_REVIEW) && Objects.equals(loginAs, User.ADMIN)
                ? R.layout.activity_unreview_list_deatil
                : R.layout.activity_report_detail;
        setContentView(layoutId);

        // Debug log for layout selection
        Log.d("ReportDetailActivity", "Selected layoutId: " + layoutId);

        // Bind basic information views
        TextView carPlateView = findViewById(R.id.valueCarPlate);
        TextView locationView = findViewById(R.id.valueLocation);
        TextView timeView = findViewById(R.id.valueTime);
        TextView reporterNameView = findViewById(R.id.valueReporterName);
        TextView reportIdView = findViewById(R.id.valueReportId);
        TextView statusView = findViewById(R.id.valueStatus);

        // Set values to views
        carPlateView.setText(plate);
        locationView.setText(location);
        timeView.setText(time);
        reporterNameView.setText(reporterName);
        reportIdView.setText(String.format("%d", reportId));
        statusView.setText(status);

        // If it's an unreviewed report and admin is logged in, setup review actions
        if (layoutId == R.layout.activity_unreview_list_deatil) {
            approveButton = findViewById(R.id.approveButton);
            rejectButton = findViewById(R.id.rejectButton);
            feedbackTextViewInput = findViewById(R.id.messageField);

            setupToggleButtonListeners();

            findViewById(R.id.submitButton).setOnClickListener(view -> {
                String feedbackInput = feedbackTextViewInput.getText().toString();

                // Update report status based on approve/reject selection
                reportViewModel.replyReport(reportId, approveButton.isChecked(), feedbackInput);

                // If approved, attempt to send an SMS to car owner
                if (approveButton.isChecked()) {
                    String phone;
                    try {
                        Map<String, String> map = readPlatePhone(getApplicationContext(), PLATE_PHONE_FILE);
                        phone = map.get(plate);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (phone == null) {
                        Toast.makeText(getApplicationContext(), "Cannot find phone number for this plate. Alarm canceled.", Toast.LENGTH_SHORT).show();
                    } else {
                        INotificationService smsService = NotificationFactory.createService(
                                "sms", getApplicationContext(), NotificationType.ALARM,
                                new HashMap<>() {{
                                    put("plate", plate);
                                }});
                        // Uncomment to actually send SMS
                        smsService.sendMsg(phone);
                        Toast.makeText(getApplicationContext(), "Alarm message sent to car owner.", Toast.LENGTH_SHORT).show();
                    }
                }

                finish();
            });
        }

        // Load and display report image
        ImageView priUrl = findViewById(R.id.imageView4);
        assert picUrl != null;
        Glide.with(this).load(new File(picUrl)).into(priUrl);

        // For normal report detail, show feedback message
        if (layoutId == R.layout.activity_report_detail) {
            TextView feedbackTextViewShow = findViewById(R.id.valueFeedback);
            feedbackTextViewShow.setText(feedback);
        }
    }

    /**
     * Setup mutual exclusion for approve and reject buttons.
     */
    private void setupToggleButtonListeners() {
        approveButton.setChecked(true);
        rejectButton.setChecked(false);

        approveButton.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                rejectButton.setChecked(false);
            }
        });

        rejectButton.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                approveButton.setChecked(false);
            }
        });
    }
}
