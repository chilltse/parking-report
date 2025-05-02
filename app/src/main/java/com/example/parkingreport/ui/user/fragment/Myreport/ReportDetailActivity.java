package com.example.parkingreport.ui.user.fragment.Myreport;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.parkingreport.R;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.viewModel.ReportViewModel;
import com.example.parkingreport.data.local.viewModel.UserViewModel;

import java.util.Calendar;

public class ReportDetailActivity extends AppCompatActivity {

    private UserViewModel viewModel;
    private User user;

    private ReportViewModel reportViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);





        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);   // 新增：接收 id，默认-1
        String title = intent.getStringExtra("title");
        String time = intent.getStringExtra("time");

        // 可以用于调试或者展示
        System.out.println("Report ID: " + id);

        TextView titleView = findViewById(R.id.valueCarPlate);
        TextView timeView = findViewById(R.id.valueTime);

        titleView.setText(title);
        timeView.setText(time);

    }
}


