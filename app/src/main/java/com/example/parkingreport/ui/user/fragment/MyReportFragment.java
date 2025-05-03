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
import com.example.parkingreport.ui.user.fragment.Myreport.ReportAdapter;
import com.example.parkingreport.ui.user.fragment.Myreport.ReportItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        reportViewModel = new ViewModelProvider(requireActivity())
                .get(ReportViewModel.class);
        viewModel =  new ViewModelProvider(requireActivity())
                .get(UserViewModel.class);
        return inflater.inflate(R.layout.fragment_my_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 模拟一些数据（plate 替代 id）
        List<Integer> reportIds = reportViewModel.getIdsByUser(viewModel.getUser().getID());

        //TODO 暂时用for循环，可换成livedata
        String reporterName =  viewModel.getUser().getName();
        List<ReportItem> reportList = new ArrayList<>();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        for(int id: reportIds){
            Report report = reportViewModel.findReport(id,false);
//            reportList.add(new ReportItem(report.getCarPlate(), String.valueOf(report.getStatus()), fmt.format(report.getTimestamp())));
            reportList.add(new ReportItem(
                    id,
                    reporterName,
                    report.getCarPlate(),
                    report.getLocation(),
                    fmt.format(report.getTimestamp()),
                    report.getFeedback(),
                    String.valueOf(report.getStatus()) )
            );
        }

//        reportList.add(new ReportItem("ABC123", "Good", "2025-04-20 09:15"));
//        reportList.add(new ReportItem("XYZ789", "Nice", "2025-04-19 18:42"));

        adapter = new ReportAdapter(reportList, getContext());
        recyclerView.setAdapter(adapter);
    }
}
