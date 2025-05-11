package com.example.parkingreport.data.local.viewModel;

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
    LiveData<List<Report>> allReportLive;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public ReportViewModel(@NonNull Application application) {
        super(application);
        reportRepository = ReportRepository.getInstance(application.getApplicationContext());
        userRepository = UserRepository.getInstance(application.getApplicationContext());
        allReportLive = reportRepository.getAllReportLive();
    }

    public List<Report> getAllReportLive(){return allReportLive;}

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
    public List<Report> getAllWaitingReportsLive() { return reportRepository.getAllWaitingReportsLive(); }


//    public void deleteReport(int reportId){
//        executeAsync(() -> {
//            reportRepository.deleteReport(reportId);
//        });
//    }

//    public void handleReport(int reportId, int userId, String status){
//        executeAsync(() -> {
//            reportRepository.handleReport(reportId, userId, status);
//        });
//    }

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

    // 新增search功能，在这里调用token和parser
    public List<Report> searchReports(String input, boolean isWaitStatus){
        List<Report> result = new ArrayList<>();
        Tokenizer.Tokens allToken = null;
        try {
            allToken = Tokenizer.tokenize(input);
        } catch (IllegalArgumentException e) {
            // 暂时设置为有错误返回null来提示， 这样后台exception不会打断程序运行
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

        result = Parser.evaluateTokens(allToken.tokens, isWaitStatus, this.reportRepository, this.userRepository);
        return result;
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

    // User
    private final MutableLiveData<Report> reportLive = new MutableLiveData<>();
    // 主线程
    public void setReport(Report report) {
        reportLive.setValue(report);
    }
    // 任意线程
    public void postReport(Report report) {
        reportLive.postValue(report);
    }
    public LiveData<Report> getReportLive() { return reportLive; }
    public Report getReport() { return reportLive.getValue(); }

}

