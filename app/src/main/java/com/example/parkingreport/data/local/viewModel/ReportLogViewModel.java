package com.example.parkingreport.data.local.viewModel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.parkingreport.data.local.entities.ReportLog;
import com.example.parkingreport.data.local.repository.ReportLogRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReportLogViewModel extends AndroidViewModel {
    private ReportLogRepository reportLogRepository;
    LiveData<List<ReportLog>> allReportLogLive;

    // 线程管理工具
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public ReportLogViewModel(@NonNull Application application) {
        super(application);
        reportLogRepository = ReportLogRepository.getInstance(application.getApplicationContext());
        allReportLogLive = reportLogRepository.getAllUserLogLive();
    }

    public LiveData<List<ReportLog>> getAllReportLogLive(){
        return allReportLogLive;
    }

    public void clearLog(){
        executeAsync(() -> {
            reportLogRepository.clearLog();
        });
    }

    /**
     * for test
     * @param reportLog
     */
    public void insertLog(ReportLog reportLog) {
        executeAsync(() -> {
            // insert userLog
            reportLogRepository.insertLog(reportLog);
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
