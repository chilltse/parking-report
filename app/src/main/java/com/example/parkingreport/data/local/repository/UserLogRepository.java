package com.example.parkingreport.data.local.repository;

/**
 * @author @u7864325 Weimiao Sun
 * Repository class that manages UserLog-related operations.
 *
 * Responsibilities:
 * - Provides a singleton interface for accessing and modifying user activity logs
 * - Delegates persistence to a UserLogDao implementation (e.g. JsonUserLogDao)
 * - Automatically assigns unique log IDs before insertion
 * - Exposes LiveData for observing user logs in real time
 *
 * This class serves as the bridge between the data layer and higher-level components
 * such as ViewModels or UI controllers.
 */

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.parkingreport.data.local.dao.JsonUserLogDao;
import com.example.parkingreport.data.local.dao.UserLogDao;
import com.example.parkingreport.data.local.entities.UserLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserLogRepository {
    private static UserLogRepository Instance;
    private UserLogDao userLogDao;
    private LiveData<List<UserLog>> allUserLogLive;

    public UserLogRepository(Context context) {
        File dir = context.getApplicationContext().getFilesDir();
        File jsonFile = new File(dir, "user_logs.json");
        this.userLogDao = new JsonUserLogDao(jsonFile);
        this.allUserLogLive = userLogDao.getAllUserLogsLive();
    }

    public static synchronized UserLogRepository getInstance(Context context) {
        if (Instance == null) {
            Instance = new UserLogRepository(context.getApplicationContext());
        }
        return Instance;
    }

    public LiveData<List<UserLog>> getAllUserLogLive() {
        return allUserLogLive;
    }
    public void clearLog(){
        userLogDao.clearLog();
    }

    /**
     *  generated id | insert log
     * @param userLog
     */
    public void insertLog(UserLog userLog) {
        //generated id
        List<UserLog> list = allUserLogLive.getValue();
        if (list == null) list = new ArrayList<>();
        int newId = generateNextAvailableID(list);
        userLog.setLogId(newId);

        //insert log
        userLogDao.insertLog(userLog);
    }

    /**
     * @param currentUserlog
     * @return find the smallest id number that has not been used, and return
     */
    private int generateNextAvailableID(List<UserLog> currentUserlog){
        Set<Integer> ids = new HashSet<>();
        for (UserLog u : currentUserlog) {
            ids.add(u.getLogId());
        }
        int id = 1;
        while (ids.contains(id)) {
            id++;
        }
        return id;
    }

}
