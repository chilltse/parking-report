package com.example.parkingreport.data.local.viewModel;

/**
 * @author @u7864325 Weimiao Sun
 * ViewModel for managing and exposing ReportLog data to the UI layer.
 *
 * Responsibilities:
 * - Provides lifecycle-aware access to report logs via LiveData
 * - Delegates data operations to the ReportLogRepository
 * - Manages background threading using Executor for non-blocking insert/clear operations
 *
 * This ViewModel ensures that UI components such as Activities or Fragments
 * can observe data without worrying about application context leaks or threading concerns.
 */

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

    // Thread Management Tools
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
}
