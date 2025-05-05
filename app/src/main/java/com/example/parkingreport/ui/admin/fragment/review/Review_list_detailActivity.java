//package com.example.parkingreport.ui.admin.fragment.review;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.example.parkingreport.R;
//import com.example.parkingreport.data.local.entities.Report;
//
//public class Review_list_detailActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//
//        Intent intent = getIntent();
//        int reportId = intent.getIntExtra("reportId", -1);   // 新增：接收 id，默认-1
//        String plate = intent.getStringExtra("plate");
//        String status = intent.getStringExtra("status");
//        String time = intent.getStringExtra("time");
//        String location = intent.getStringExtra("location");
//        String feedback = intent.getStringExtra("feedback");
//        String reporterName = intent.getStringExtra("reporterName");
//
//        // 判断启用哪一个xml
//        Log.d("Review_list", "status:" + status);
//        int layoutId = status.equals(Report.WAIT_FOR_REVIEW)?
//                R.layout.activity_unreview_list_deatil:
//                R.layout.activity_report_detail;
//        setContentView(layoutId);
//
//        TextView carPlateView = findViewById(R.id.valueCarPlate);
//        TextView locationView = findViewById(R.id.valueLocation);
//        TextView timeView = findViewById(R.id.valueTime);
//        TextView reporterNameView = findViewById(R.id.valueReporterName);
//        TextView reportIdView = findViewById(R.id.valueReportId);
//        TextView statusView = findViewById(R.id.valueStatus);
//        TextView feedbackView = findViewById(R.id.valueFeedback);
//
////        titleView.setText(title);
//        carPlateView.setText(plate);
//        locationView.setText(location);
//        timeView.setText(time);
//        reporterNameView.setText(reporterName);
//        reportIdView.setText(String.format("%d", reportId));
////        reportIdView.setText(reportId);
//        statusView.setText(status);
//        feedbackView.setText(feedback);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//
//    }
//}