package com.example.parkingreport.data.local.viewModel;

/**
 * @author @u7864325 Weimiao Sun
 * ViewModel class for managing Report-related data and business logic.
 *
 * Responsibilities:
 * - Acts as an intermediary between the UI and the ReportRepository/UserRepository
 * - Handles insertion, update, filtering, and search of reports
 * - Manages asynchronous background execution using a single-threaded Executor
 * - Exposes LiveData for report selection and UI data-binding
 * - Integrates token-based search via Tokenizer and Parser for advanced filtering
 *
 * This ViewModel ensures thread safety and UI lifecycle awareness while handling
 * complex report interaction and filtering logic.
 */

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.parkingreport.data.local.api.Callback;
import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.repository.ReportRepository;
import com.example.parkingreport.data.local.repository.UserRepository;
import com.example.parkingreport.search.Parser;
import com.example.parkingreport.search.Token;
import com.example.parkingreport.search.Tokenizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReportViewModel extends AndroidViewModel {
    private ReportRepository reportRepository;
    private UserRepository userRepository;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     * Constructor
     * @param application
     */
    public ReportViewModel(@NonNull Application application) {
        super(application);
        reportRepository = ReportRepository.getInstance(application.getApplicationContext());
        userRepository = UserRepository.getInstance(application.getApplicationContext());
    }

    /**
     * Inserts a new Report, assigns a unique ID, and logs the submission.
     * @param report the Report object to insert
     */
    public void insertReport(Report report){
        executeAsync(() -> {
            reportRepository.insertReport(report);
        });
    }

    /**
     * Retrieves the list of all Report objects.
     * @return a List containing all Reports (in reverse order)
     */
    public List<Report> getAllReportsLive() {
        List<Report> originals =  reportRepository.getAllReportsLive();
        List<Report> sorted = new ArrayList<>(originals);
        Collections.sort(sorted, Collections.reverseOrder());
        return sorted;
    }

    /**
     * Retrieves a list of Reports that are currently waiting for review.
     * @return a List of Report objects in waiting-for-review status(in reverse order)
     */
    public List<Report> getAllWaitingReportsLive() {
        List<Report> originals =  reportRepository.getAllWaitingReportsLive();
        List<Report> sorted = new ArrayList<>(originals);
        Collections.sort(sorted, Collections.reverseOrder());
        return sorted;
    }

    /**
     * Retrieves a list of report IDs associated with the specified user ID.
     * @param userId  the unique identifier of the user
     * @return List<Integer>  a List of report IDs for the user, or an empty list if none are found
     */
    public List<Integer> getIdsByUser(int userId) {
        return reportRepository.getIdsByUser(userId);
    }

    /**
     * Retrieves a list of report IDs associated with the given car plate.
     * @param plate  the car plate number to query
     * @return List<Integer>  a List of report IDs matching the plate, or an empty list if none are found
     */
    public List<Integer> getIdsByPlate(String plate) {
        return reportRepository.getIdsByPlate(plate);
    }

    /**
     * Reviews a report by updating its status and feedback, and logs the action.
     *
     * @param ID  the unique identifier of the Report to be reviewed
     * @param isApproved  the review decision; true for approval, false for rejection
     * @param feedBack  the feedback message for the review
     * @return Boolean  true if the review and update succeed; false otherwise
     */
    public boolean replyReport(int ID, boolean isApproved, String feedBack){
        return reportRepository.replyReport(ID, isApproved, feedBack);
    }

    /**
     * Finds a Report by its ID, optionally limiting the search to waiting-for-review reports.
     *
     * @param ID  the unique identifier of the report
     * @param isWaitStatus  if true, searches only in the waiting-for-review collection; otherwise searches all reports
     * @return Report  the matching Report, or null if not found
     */
    public Report findReport(int ID, boolean isWaitStatus) {
        Report report = reportRepository.findReport(ID, isWaitStatus);
        return report;
    }

    /**
     * Searches for reports based on input keywords, review status, user role, and user ID.
     *
     * Tokenizes the input string; returns null if tokenization throws IllegalArgumentException to prevent crashes.
     * Logs all generated tokens for debugging purposes.
     * Calls Parser.evaluateTokens with the tokens and filter parameters to perform the actual report lookup.
     *
     * @author u7807744 LarryWang
     * @param input  the search input string
     * @param isWaitStatus  whether to search only waiting-for-review reports
     * @param role  the user role for tokenization and permission filtering
     * @param userID  the user ID for filtering report ownership
     * @return List<Report>  a List of matching Report objects, or null if tokenization fails
     */
    public List<Report> searchReports(String input, boolean isWaitStatus, String role, int userID){
        List<Report> result = new ArrayList<>();
        Tokenizer.Tokens allToken = null;
        try {
            allToken = Tokenizer.tokenize(input, role);
        } catch (IllegalArgumentException e) {
            //Temporarily set it to return null if there is an error, so that background exceptions will not interrupt the program running
            Log.d("IllegalArgs", "IllegalArgs");
            return null;
        }

        if(allToken.tokens == null || allToken.tokens.size() == 0){
            Log.d("Token_Null", "No tokens");
        }
        for (Token token : allToken.tokens) {
            Log.d("Tokens", token.toString());
        }

        result = Parser.evaluateTokens(allToken.tokens, isWaitStatus, role, userID, this.reportRepository, this.userRepository);
        return result;
    }

    /**
     * Executes the given task asynchronously on a background thread,
     * and posts any exception to the main thread.
     *
     * @param task the Runnable task to execute asynchronously
     */
    private void executeAsync(Runnable task) {
        executor.execute(() -> {
            try {
                task.run();
            } catch (Exception e) {
                mainHandler.post(() -> new Exception("Exception: " + e)
                );
            }
        });
    }
}

