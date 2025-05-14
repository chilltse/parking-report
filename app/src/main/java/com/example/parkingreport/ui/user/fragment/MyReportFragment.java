package com.example.parkingreport.ui.user.fragment;

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


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Personal reports page
 * @author Yudong Qiu
 */
public class MyReportFragment extends Fragment {

    private UserViewModel viewModel;
    private ReportViewModel reportViewModel;
    private RecyclerView recyclerView;
    private EditText searchInput;

    public MyReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize ViewModels shared with Activity
        reportViewModel = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);
        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        // Inflate fragment layout
        return inflater.inflate(R.layout.fragment_my_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind UI components
        searchInput = view.findViewById(R.id.searchEditText);
        Button searchBtn = view.findViewById(R.id.searchButton);
        recyclerView = view.findViewById(R.id.recycle);

        // Setup RecyclerView with linear layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load and display all reports for current user
        loadReports();

        // Setup search button click listener
        searchBtn.setOnClickListener(v -> updateSearchResult());
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh search results when fragment becomes active again
        updateSearchResult();
    }

    /**
     * Load all reports submitted by the current user and display in RecyclerView.
     */
    private void loadReports() {
        // Get current user ID from ViewModel
        int userId = viewModel.getUser().getID();

        // Get report IDs submitted by current user
        List<Integer> reportIds = reportViewModel.getIdsByUser(userId);

        // Retrieve full report objects by ID
        List<Report> reportList = new ArrayList<>();
        for (int id : reportIds) {
            reportList.add(reportViewModel.findReport(id, false));
        }

        // Sort reports in reverse chronological order
        reportList.sort(Collections.reverseOrder());

        // Bind data to adapter and set to RecyclerView
        ReportAdapter adapter = new ReportAdapter(reportList, getContext(), viewModel.getUser().getRole());
        recyclerView.setAdapter(adapter);
    }

    /**
     * Perform search based on user input and update RecyclerView.
     * If input is invalid, show error.
     */
    private void updateSearchResult() {
        String searchContent = searchInput.getText().toString();

        // Search reports for current user matching search content
        List<Report> searchResult = reportViewModel.searchReports(
                searchContent, false,
                viewModel.getUser().getRole(),
                viewModel.getUser().getID()
        );

        // Handle invalid search input
        if (searchResult == null) {
            searchInput.setError("Invalid Input!!!");
            searchResult = new ArrayList<>();
        } else {
            // Sort search results in reverse chronological order
            searchResult.sort(Collections.reverseOrder());
        }

        // Update adapter with search results
        ReportAdapter adapter = new ReportAdapter(searchResult, getContext(), viewModel.getUser().getRole());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}