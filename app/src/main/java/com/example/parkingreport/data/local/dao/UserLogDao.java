package com.example.parkingreport.data.local.dao;

/**
 * @author @u7864325 Weimiao Sun
 * Data Access Object (DAO) interface for managing user activity logs (UserLog).
 *
 * Provides methods to insert new logs, clear all logs, and observe log changes
 * through LiveData. This interface is typically implemented using file-based
 * or database-backed storage to persist user interaction records.
 */

import androidx.lifecycle.LiveData;

import com.example.parkingreport.data.local.entities.UserLog;

import java.util.List;

public interface UserLogDao {
    void insertLog(UserLog userLog);
    void clearLog();
    LiveData<List<UserLog>> getAllUserLogsLive();
}