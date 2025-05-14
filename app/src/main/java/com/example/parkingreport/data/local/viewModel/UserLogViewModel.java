package com.example.parkingreport.data.local.viewModel;

/**
 * @author @u7864325 Weimiao Sun
 * ViewModel class for managing user log data and exposing it to the UI layer.
 *
 * Responsibilities:
 * - Provides lifecycle-aware access to user logs via LiveData
 * - Delegates all data operations to the {UserLogRepository}
 * - Executes insertion and deletion operations asynchronously using an Executor
 *
 * This ViewModel decouples UI logic from data logic and ensures that user activity logs
 * can be safely accessed and modified without blocking the main thread.
 */

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.parkingreport.data.local.entities.UserLog;
import com.example.parkingreport.data.local.repository.UserLogRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserLogViewModel extends AndroidViewModel {
    private UserLogRepository userLogRepository;
    LiveData<List<UserLog>> allUserLogLive;

    // Thread Management Tools
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public UserLogViewModel(@NonNull Application application) {
        super(application);
        userLogRepository = UserLogRepository.getInstance(application.getApplicationContext());
        allUserLogLive = userLogRepository.getAllUserLogLive();
    }

    public LiveData<List<UserLog>> getAllUserLogLive() {
        return allUserLogLive;
    }

    public void clearLog(){
        executeAsync(() -> {
            userLogRepository.clearLog();
        });
    }

    /**
     * for test
     * @param userLog
     */
    public void insertLog(UserLog userLog) {
        executeAsync(() -> {
            // insert userLog
            userLogRepository.insertLog(userLog);
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

