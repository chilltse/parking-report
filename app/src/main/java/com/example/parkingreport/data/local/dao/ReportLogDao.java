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
    void insertLog(ReportLog reportLog);
    void clearLog();
    LiveData<List<ReportLog>> getAllReportLogsLive();
}
