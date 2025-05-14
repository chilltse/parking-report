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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * @author Yudong Qiu u7937030
 * - UnreReportsFragment displays the admin's list of unreviewed reports.
 * - Allows searching through pending reports using keywords.
 * - Loads report data via ReportViewModel and UserViewModel.
 * - Displays search results in a RecyclerView using ReportAdapter.
 * - Refreshes data when fragment is resumed for up-to-date results.
 */
public class UnreReportsFragment extends Fragment {

    private UserViewModel viewModel;
    private ReportViewModel reportViewModel;

    private RecyclerView recyclerView;
    private EditText searchInput;

    public UnreReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize ViewModels shared with the hosting Activity
        reportViewModel = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);
        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unreview_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind UI components
        recyclerView = view.findViewById(R.id.recycle2);
        searchInput = view.findViewById(R.id.searchEditText);
        Button searchBtn = view.findViewById(R.id.searchButton);

        // Load all unreviewed reports initially
        updateReports();

        // Set search button click listener to update search results
        searchBtn.setOnClickListener(view1 -> updateSearchResult());
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh search results when fragment becomes active again
        updateSearchResult();
    }

    /**
     * Load and display all unreviewed reports.
     */
    private void updateReports() {
        List<Report> allReports = reportViewModel.getAllWaitingReportsLive();
        ReportAdapter adapter = new ReportAdapter(allReports, getContext(), viewModel.getUser().getRole());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    /**
     * Search unreviewed reports based on user input and update RecyclerView.
     * If no valid result is found, display an input error.
     * Sort results in descending order before displaying.
     */
    private void updateSearchResult() {
        String searchContent = searchInput.getText().toString();

        // Search for reports matching input, filter: waiting review (true)
        List<Report> searchResult = reportViewModel.searchReports(
                searchContent,
                true,  // true indicates searching for unreviewed reports
                viewModel.getUser().getRole(),
                viewModel.getUser().getID()
        );

        // Handle invalid search result
        if (searchResult == null) {
            searchInput.setError("Invalid Input!");
            searchResult = new ArrayList<>();
        } else {
            // Sort search results in reverse chronological order
            searchResult.sort(Collections.reverseOrder());
        }

        // Display search results
        ReportAdapter adapter = new ReportAdapter(searchResult, getContext(), viewModel.getUser().getRole());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
