package com.example.parkingreport.ui.admin.fragment.unreview;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.parkingreport.R;

public class Unreview_list_detailActivity extends AppCompatActivity {

    private ToggleButton approveButton;
    private ToggleButton rejectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_unreview_list_deatil);

        // 保持EdgeToEdge处理
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 设置Title
        String title = getIntent().getStringExtra("title");
        TextView detailText = findViewById(R.id.title);
        detailText.setText("You click " + title);

        // 找到ToggleButton
        approveButton = findViewById(R.id.approveButton);
        rejectButton = findViewById(R.id.rejectButton);

        // 互斥逻辑
        setupToggleButtonListeners();
    }

    private void setupToggleButtonListeners() {
        approveButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rejectButton.setChecked(false);
            }
        });

        rejectButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                approveButton.setChecked(false);
            }
        });
    }
}
