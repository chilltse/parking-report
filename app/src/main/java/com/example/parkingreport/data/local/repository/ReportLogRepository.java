package com.example.parkingreport.data.local.repository;


/**
 * @author @u7864325 Weimiao Sun
 * Repository class that manages access to report log data.
 *
 * Responsibilities:
 * - Provides a singleton interface for accessing and modifying report logs
 * - Delegates storage operations to the underlying ReportLogDao (e.g., JsonReportLogDao)
 * - Automatically generates unique log IDs before insertion
 * - Exposes LiveData for observing log updates in real time
 *
 * This repository abstracts the data source and simplifies interaction for ViewModels or services.
 */


import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.parkingreport.data.local.dao.JsonReportLogDao;
import com.example.parkingreport.data.local.dao.ReportLogDao;
import com.example.parkingreport.data.local.entities.ReportLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReportLogRepository {
    private static ReportLogRepository Instance;
    private ReportLogDao reportLogDao;
    private LiveData<List<ReportLog>> allReportLogLive;

    /**
     * Constructor
     * @param context
     */
    public ReportLogRepository(Context context) {
        File dir = context.getApplicationContext().getFilesDir();
        File jsonFile = new File(dir, "report_logs.json");
        this.reportLogDao = new JsonReportLogDao(jsonFile);
        this.allReportLogLive = reportLogDao.getAllReportLogsLive();
    }

    /**
     * For Singleton
     * @param context
     * @return Instance of Repository
     */
    public static synchronized ReportLogRepository getInstance(Context context){
        if(Instance == null){
            Instance = new ReportLogRepository(context.getApplicationContext());
        }
        return Instance;
    }

    /**
     * Returns the LiveData object containing the list of all ReportLog entries.
     *
     * @return LiveData<List<ReportLog>> that observers can subscribe to for report log updates
     */
    public LiveData<List<ReportLog>> getAllUserLogLive() {
        return allReportLogLive;
    }

    /**
     * Clears all report logs.
     */
    public void clearLog(){
        reportLogDao.clearLog();
    }

    /**
     * Generates a unique logId for the provided ReportLog and inserts it into the data source.
     *
     * Retrieves the current list of logs from allReportLogLive; initializes to an empty list if null.
     * Calls generateNextAvailableID to compute a new ID and assigns it to the reportLog instance.
     * Invokes reportLogDao.insertLog to persist the reportLog with its new ID.
     *
     * @param reportLog the ReportLog object to insert
     */
    public void insertLog(ReportLog reportLog) {
        //generated id
        List<ReportLog> list = allReportLogLive.getValue();
        if (list == null) list = new ArrayList<>();
        int newId = generateNextAvailableID(list);
        reportLog.setLogId(newId);

        //insert log
        reportLogDao.insertLog(reportLog);
    }

    /**
     * @param currentReportLog List of current ReportLogs.
     * @return find the smallest id number that has not been used, and return
     */
    private int generateNextAvailableID(List<ReportLog> currentReportLog){
        Set<Integer> ids = new HashSet<>();
        for (ReportLog rl : currentReportLog) {
            ids.add(rl.getLogId());
        }
        int id = 1;
        while (ids.contains(id)) {
            id++;
        }
        return id;
    }

}
