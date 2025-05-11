package com.example.parkingreport.ui.admin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

import java.util.Collections;
import java.util.List;

public class UnreFragment extends Fragment {

    private UserViewModel viewModel;
    private ReportViewModel reportViewModel;
    private RecyclerView recyclerView;

    private EditText searchInput;
    private Button searchBtn;


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
        recyclerView = view.findViewById(R.id.recycle2);

        searchInput = view.findViewById(R.id.searchEditText);
        searchBtn = view.findViewById(R.id.searchButton);

        updateReports();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSearchResult();
            }
        });

//         List<Report> allReports =  reportViewModel.getAllWaitingReportsLive();
//
//
//        ReportAdapter adapter = new ReportAdapter(allReports, getContext(), viewModel.getUser().getRole());
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateReports();
    }

    private void updateReports() {
        // 初始化 RecyclerView
        List<Report> allReports =  reportViewModel.getAllWaitingReportsLive();
        ReportAdapter adapter = new ReportAdapter(allReports, getContext(), viewModel.getUser().getRole());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void updateSearchResult() {
        String searchContent = searchInput.getText().toString();

        List<Report> searchResult =  reportViewModel.searchReports(searchContent,false);
        Collections.sort(searchResult, Collections.reverseOrder());
        ReportAdapter adapter = new ReportAdapter(searchResult, getContext(), viewModel.getUser().getRole());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
