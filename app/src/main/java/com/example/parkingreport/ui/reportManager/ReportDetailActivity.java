package com.example.parkingreport.ui.reportManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.parkingreport.R;
import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.viewModel.ReportViewModel;
import com.example.parkingreport.data.local.viewModel.UserViewModel;

import java.io.File;

public class ReportDetailActivity extends AppCompatActivity {

    private UserViewModel viewModel;
    private User user;
    private ReportViewModel reportViewModel;
    private ToggleButton approveButton;
    private ToggleButton rejectButton;
    private TextView feedbackTextViewShow;
    private EditText feedbackTextViewInput;
    private ImageView priUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reportViewModel = new ViewModelProvider(this)
                .get(ReportViewModel.class);

        Intent intent = getIntent();
        int reportId = intent.getIntExtra("reportId", -1);   // 新增：接收 id，默认-1
        String plate = intent.getStringExtra("plate");
        String status = intent.getStringExtra("status");
        String time = intent.getStringExtra("time");
        String location = intent.getStringExtra("location");
        String feedback = intent.getStringExtra("feedback");
        String reporterName = intent.getStringExtra("reporterName");
        String loginAs = intent.getStringExtra("loginAs");
        String picUrl = intent.getStringExtra("picUrl");

        // 判断启用哪一个xml
        Log.d("Review_list", "status:" + status);
        Log.d("Review_list", "R.layout.activity_unreview_list_deatil:" + R.layout.activity_unreview_list_deatil);
        Log.d("Review_list", "R.layout.activity_report_detail:" + R.layout.activity_report_detail);
        int layoutId = status.equals(Report.WAIT_FOR_REVIEW) && loginAs.equals(User.ADMIN)?
                R.layout.activity_unreview_list_deatil:
                R.layout.activity_report_detail;
        setContentView(layoutId);
        Log.d("Review_list", "layoutId:" + layoutId);

        TextView carPlateView = findViewById(R.id.valueCarPlate);
        TextView locationView = findViewById(R.id.valueLocation);
        TextView timeView = findViewById(R.id.valueTime);
        TextView reporterNameView = findViewById(R.id.valueReporterName);
        TextView reportIdView = findViewById(R.id.valueReportId);
        TextView statusView = findViewById(R.id.valueStatus);



//        titleView.setText(title);
        carPlateView.setText(plate);
        locationView.setText(location);
        timeView.setText(time);
        reporterNameView.setText(reporterName);
        reportIdView.setText(String.format("%d", reportId));
//        reportIdView.setText(reportId);
        statusView.setText(status);

        // 对于审批的report，设置监听器
        if(layoutId == R.layout.activity_unreview_list_deatil){
            approveButton = findViewById(R.id.approveButton);
            rejectButton = findViewById(R.id.rejectButton);
            feedbackTextViewInput = findViewById(R.id.messageField);
            setupToggleButtonListeners();



            findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    String isApproved = approveButton.isChecked() ? Report.APPROVED : Report.DECLINED;
                    String feedback = feedbackTextViewInput.getText().toString();

                    reportViewModel.replyReport(reportId,approveButton.isChecked(),feedback);
                    finish();
                }
            });
        }


        //设置相关的图片
        priUrl = findViewById(R.id.imageView4);
        assert picUrl != null;
        Glide.with(this).load(new File(picUrl)).into(priUrl);

        if(layoutId == R.layout.activity_report_detail) {
            feedbackTextViewShow = findViewById(R.id.valueFeedback);
            feedbackTextViewShow.setText(feedback);
        }


    }

    private void setupToggleButtonListeners() {

        approveButton.setChecked(true);
        rejectButton.setChecked(false);
        approveButton.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked) {
                rejectButton.setChecked(false);
            }
        });

        rejectButton.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked) {
                approveButton.setChecked(false);
            }
        });
    }
}


