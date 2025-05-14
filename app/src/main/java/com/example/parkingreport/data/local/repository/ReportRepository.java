package com.example.parkingreport.data.local.repository;

/**
 * @author @u7864325 Weimiao Sun
 * Repository class that manages Report-related operations.
 *
 * Responsibilities:
 * - Acts as a unified interface between the data layer (DAO) and higher-level components (e.g. ViewModel)
 * - Handles insertion, lookup, and update of reports
 * - Automatically generates unique report IDs
 * - Delegates report logging to {ReportLogRepository} when reports are submitted or reviewed
 * - Supports filtering reports by user ID or license plate
 *
 * This class uses a singleton pattern to ensure a single shared data access point across the app.
 */

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.parkingreport.data.local.dao.JsonReportDao;
import com.example.parkingreport.data.local.dao.ReportDao;
import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.data.local.entities.ReportLog;
import com.example.parkingreport.data.local.entities.User;


import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReportRepository {
    private static ReportRepository Instance;
    private ReportDao reportDao;
    private List<Report> allReportLive;

    private ReportLogRepository reportLogRepository;

    public ReportRepository(Context context) {
        File dir = context.getApplicationContext().getFilesDir();
        File jsonFile = new File(dir, "reports.json");
        this.reportDao= new JsonReportDao(jsonFile);
        this.allReportLive = reportDao.getAllReportsLive();
        // get the instance of userLog, when creating userRepository
        this.reportLogRepository= ReportLogRepository.getInstance(context);
    }
    public List<Report> getAllReportsLive() { return reportDao.getAllReportsLive(); }
    public List<Report> getAllWaitingReportsLive() { return reportDao.getAllWaitingReportsLive(); }
    public static synchronized  ReportRepository getInstance(Context context) {
        if (Instance == null) {
            Instance = new ReportRepository(context.getApplicationContext());
        }
        return Instance;
    }


    public List<Integer> getIdsByUser(int userId) {
        return reportDao.getIdsByUser(userId);
    }

    public List<Integer> getIdsByPlate(String plate) {
        return reportDao.getIdsByPlate(plate);
    }

    public void insertReport(Report report){
        // generated id
        List<Report> list = allReportLive;
        if (list == null) list = new ArrayList<>();

        int newId = list.size() + 1;
        report.setID(newId);

        // insert report
        reportDao.insertReport(report);

        // insert reportLog
        reportLogRepository.insertLog(new ReportLog(report.getID(), ReportLog.SUBMIT));
    }

    /**
     * @param currentReports
     * @return find the smallest id number that has not been used, and return
     */
    private int generateNextAvailableID(List<Report> currentReports) {
        Set<Integer> ids = new HashSet<>();
        for (Report r : currentReports) {
            ids.add(r.getID());
        }
        int id = 1;
        while (ids.contains(id)) {
            id++;
        }
        return id;
    }



    public boolean replyReport(int ID, boolean isApproved, String feedBack){
        Log.d("ReportRepository", "feedBack:"+feedBack);
        Report report = reportDao.findReport(ID, true);
        // Only when the report exists and the status is waiting for review
        if(report!=null && report.getStatus().equals(Report.WAIT_FOR_REVIEW)){
            Report newReport = reportDao.copyReport(report);
            newReport.setStatus(isApproved ? Report.APPROVED : Report.DECLINED);
            newReport.setFeedback(feedBack);
            reportDao.updateReport(newReport);
            // insert reportLog
            if(isApproved){
                reportLogRepository.insertLog(new ReportLog(ID, ReportLog.APPROVE));
            }else{
                reportLogRepository.insertLog(new ReportLog(ID, ReportLog.DECLINE));
            }
            return true;
        }else{
            return false;
        }
    }

    public Report findReport(int ID, boolean isWaitStatus) {
        return reportDao.findReport(ID,isWaitStatus);
    }


}
