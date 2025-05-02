package com.example.parkingreport.ui.user.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingreport.R;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.viewModel.ReportViewModel;
import com.example.parkingreport.data.local.viewModel.UserViewModel;
import com.example.parkingreport.ui.user.fragment.Myreport.ReportAdapter;
import com.example.parkingreport.ui.user.fragment.Myreport.ReportItem;

import java.util.ArrayList;
import java.util.List;

public class MyReportFragment extends Fragment {

    private UserViewModel viewModel;
    private ReportViewModel reportViewModel;
    private User user;

    private RecyclerView recyclerView;
    private ReportAdapter adapter;

    public MyReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 返回 fragment 对应的布局
        return inflater.inflate(R.layout.fragment_my_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<ReportItem> reportList = new ArrayList<>();
        reportList.add(new ReportItem(1, "Good", "2025-04-20 09:15"));  // 加上 id
        reportList.add(new ReportItem(2, "Nice", "2025-04-19 18:42"));


        // 设置适配器
        adapter = new ReportAdapter(reportList, getContext());
        recyclerView.setAdapter(adapter);
    }
}
