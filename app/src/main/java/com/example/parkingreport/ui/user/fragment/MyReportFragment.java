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
import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.viewModel.ReportViewModel;
import com.example.parkingreport.data.local.viewModel.UserViewModel;
import com.example.parkingreport.ui.reportManager.ReportAdapter;


import java.util.ArrayList;
import java.util.Collections;
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
        reportViewModel = new ViewModelProvider(requireActivity())
                .get(ReportViewModel.class);
        viewModel =  new ViewModelProvider(requireActivity())
                .get(UserViewModel.class);
        // 返回 fragment 对应的布局
        return inflater.inflate(R.layout.fragment_my_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadReports();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadReports();
    }

    private void loadReports() {
        // 拿到当前用户 id
        int userId = viewModel.getUser().getID();
        // 用你的原来逻辑来构造列表
        List<Integer> reportIds = reportViewModel.getIdsByUser(userId);
        List<Report> reportList = new ArrayList<>();
        for (int id : reportIds) {
            reportList.add(reportViewModel.findReport(id, false));
        }
        Collections.sort(reportList, Collections.reverseOrder());
        adapter = new ReportAdapter(reportList, getContext(), viewModel.getUser().getRole());
        recyclerView.setAdapter(adapter);
    }
}
