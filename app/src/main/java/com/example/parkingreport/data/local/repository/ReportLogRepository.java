package com.example.parkingreport.data.local.repository;

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

    public ReportLogRepository(Context context) {
        File dir = context.getApplicationContext().getFilesDir();
        File jsonFile = new File(dir, "report_logs.json");
        this.reportLogDao = new JsonReportLogDao(jsonFile);
        this.allReportLogLive = reportLogDao.getAllReportLogsLive();
    }

    public static synchronized ReportLogRepository getInstance(Context context){
        if(Instance == null){
            Instance = new ReportLogRepository(context.getApplicationContext());
        }
        return Instance;
    }

    public LiveData<List<ReportLog>> getAllUserLogLive() {
        return allReportLogLive;
    }
    public void clearLog(){
        reportLogDao.clearLog();
    }

    /**
     *  generated id | insert log
     * @param reportLog
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
     * @param currentReportLog
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
