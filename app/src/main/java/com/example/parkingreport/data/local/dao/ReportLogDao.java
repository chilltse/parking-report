package com.example.parkingreport.data.local.dao;

import androidx.lifecycle.LiveData;

import com.example.parkingreport.data.local.entities.ReportLog;

import java.util.List;

/**
 * @author @u7864325 Weimiao Sun
 * Data Access Object (DAO) interface for managing ReportLog entries.
 * This interface defines the core operations for:
 * - Inserting new report log entries
 * - Clearing all existing logs
 * - Observing logs via LiveData for real-time updates in the UI
 * Implementations are responsible for handling data persistence,
 * such as using local files or databases.
 */

public interface ReportLogDao {
    /**
     * Inserts a new ReportLog entry into the list and persists it to file.
     * @param reportLog the ReportLog object to insert and save
     */
    void insertLog(ReportLog reportLog);

    /**
     * Clears all ReportLog entries by saving an empty list and updating LiveData.
     */
    void clearLog();

    /**
     * Returns the LiveData object containing the list of all ReportLog entries.
     * @return LiveData<List<ReportLog>> that observers can subscribe to for data updates
     */
    LiveData<List<ReportLog>> getAllReportLogsLive();
}
