package com.example.parkingreport.data.local.viewModel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.parkingreport.data.local.api.Callback;
import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.repository.ReportRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReportViewModel extends AndroidViewModel {
    private ReportRepository reportRepository;
    LiveData<List<Report>> allReportLive;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public ReportViewModel(@NonNull Application application) {
        super(application);
        reportRepository = ReportRepository.getInstance(application.getApplicationContext());
        allReportLive = reportRepository.getAllReportLive();
    }

    public LiveData<List<Report>> getAllReportLive(){return allReportLive;}

    public void insertReport(Report report){
        executeAsync(() -> {
            reportRepository.insertReport(report);
        });
    }


//    public void deleteReport(int reportId){
//        executeAsync(() -> {
//            reportRepository.deleteReport(reportId);
//        });
//    }

    public void handleReport(int reportId, int userId, int status){
        executeAsync(() -> {
            reportRepository.handleReport(reportId, userId, status);
        });
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

    public void findReport(int ID, boolean isWaitStatus, Callback<Report> callback) {
        executeAsync(() -> {
            Report report = reportRepository.findReport(ID, isWaitStatus);
            callback.onResult(report);
        });
    }


    // 通用异步执行方法
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

