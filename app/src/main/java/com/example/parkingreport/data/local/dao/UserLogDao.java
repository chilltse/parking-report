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
    /**
     * Inserts a new UserLog entry into the list and persists it to the local file.
     * @param userLog the UserLog object to insert and save
     */
    void insertLog(UserLog userLog);

    /**
     * Clears all UserLog entries and updates the local file and LiveData.
     */
    void clearLog();

    /**
     * Returns the LiveData object containing the list of all UserLog entries.
     * @return LiveData<List<UserLog>> that observers can subscribe to for updates
     */
    LiveData<List<UserLog>> getAllUserLogsLive();
}