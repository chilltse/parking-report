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

    /**
     * Constructor
     * @param context
     */
    public UserLogRepository(Context context) {
        File dir = context.getApplicationContext().getFilesDir();
        File jsonFile = new File(dir, "user_logs.json");
        this.userLogDao = new JsonUserLogDao(jsonFile);
        this.allUserLogLive = userLogDao.getAllUserLogsLive();
    }

    /**
     * For Singleton
     * @param context
     * @return Instance of Repository
     */
    public static synchronized UserLogRepository getInstance(Context context) {
        if (Instance == null) {
            Instance = new UserLogRepository(context.getApplicationContext());
        }
        return Instance;
    }

    /**
     * Returns the LiveData object containing the list of all UserLog entries.
     *
     * @return a LiveData<List<UserLog>> instance for observers to subscribe to updates
     */
    public LiveData<List<UserLog>> getAllUserLogLive() {
        return allUserLogLive;
    }

    /**
     * Clears all user logs.
     */
    public void clearLog(){
        userLogDao.clearLog();
    }

    /**
     * Generates a unique logId for the given UserLog and inserts it into the data source.
     *
     * Retrieves the current list of logs from allUserLogLive; initializes to an empty list if null.
     * Calls generateNextAvailableID to compute a new logId and assigns it to the userLog instance.
     * Invokes userLogDao.insertLog to persist the userLog with its new ID.
     *
     * @param userLog the UserLog object to insert, with its generated ID
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
     * Generates the smallest positive integer ID not already used in the provided UserLog list.
     *
     * @param currentUserlog the current list of UserLog entries
     * @return int the first available, unused logId
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
