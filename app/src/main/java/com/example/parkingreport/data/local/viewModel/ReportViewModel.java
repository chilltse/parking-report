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
//    List<Report> allReportLive;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public ReportViewModel(@NonNull Application application) {
        super(application);
        reportRepository = ReportRepository.getInstance(application.getApplicationContext());
        userRepository = UserRepository.getInstance(application.getApplicationContext());
//        allReportLive = reportRepository.getAllReportLive();
    }

//    public List<Report> getAllReportLive(){return allReportLive;}

    public void insertReport(Report report){
        executeAsync(() -> {
            reportRepository.insertReport(report);
        });
    }

    public List<Report> getAllReportsLive() {
        List<Report> originals =  reportRepository.getAllReportsLive();
        List<Report> sorted = new ArrayList<>(originals);
        Collections.sort(sorted, Collections.reverseOrder());
        return sorted;
    }
    public List<Report> getAllWaitingReportsLive() {
        List<Report> originals =  reportRepository.getAllWaitingReportsLive();
        List<Report> sorted = new ArrayList<>(originals);
        Collections.sort(sorted, Collections.reverseOrder());
        return sorted;
    }



    public List<Integer> getIdsByUser(int userId) {
        return reportRepository.getIdsByUser(userId);
    }

    public List<Integer> getIdsByPlate(String plate) {
        return reportRepository.getIdsByPlate(plate);
    }

    public boolean replyReport(int ID, boolean isApproved, String feedBack){
        return reportRepository.replyReport(ID, isApproved, feedBack);
    }

//    public void findReport(int ID, boolean isWaitStatus, Callback<Report> callback) {
    public Report findReport(int ID, boolean isWaitStatus) {
//        executeAsync(() -> {
            Report report = reportRepository.findReport(ID, isWaitStatus);
//            callback.onResult(report);
//        });
        return report;
    }

    // Add new search function, use token and parser here
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

        // debug
        if(allToken.tokens == null || allToken.tokens.size() == 0){
            Log.d("Token_Null", "No tokens");
        }
        for (Token token : allToken.tokens) {
            Log.d("Tokens", token.toString());
        }

        result = Parser.evaluateTokens(allToken.tokens, isWaitStatus, role, userID, this.reportRepository, this.userRepository);
        return result;
    }


    // General asynchronous execution method
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

    // User
    private final MutableLiveData<Report> reportLive = new MutableLiveData<>();
    // Main thread
    public void setReport(Report report) {
        reportLive.setValue(report);
    }
    // Any thread
    public void postReport(Report report) {
        reportLive.postValue(report);
    }
    public LiveData<Report> getReportLive() { return reportLive; }
    public Report getReport() { return reportLive.getValue(); }

}

