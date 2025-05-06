package com.example.parkingreport.ui.admin.fragment;

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
import com.example.parkingreport.data.local.viewModel.ReportViewModel;
import com.example.parkingreport.data.local.viewModel.UserViewModel;

import com.example.parkingreport.ui.reportManager.ReportAdapter;

import java.util.List;

public class UnreFragment extends Fragment {

    private UserViewModel viewModel;
    private ReportViewModel reportViewModel;


    public UnreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reportViewModel = new ViewModelProvider(requireActivity())
                .get(ReportViewModel.class);
        viewModel =  new ViewModelProvider(requireActivity())
                .get(UserViewModel.class);
        return inflater.inflate(R.layout.fragment_unreview_list, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化 RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycle2);


         List<Report> allReports =  reportViewModel.getAllWaitingReportsLive();


        ReportAdapter adapter = new ReportAdapter(allReports, getContext(), viewModel.getUser().getRole());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
