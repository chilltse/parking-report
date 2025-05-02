package com.example.parkingreport.data.local.viewModel;

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

    // 线程管理工具
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

