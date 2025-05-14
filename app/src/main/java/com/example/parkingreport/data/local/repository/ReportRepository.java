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

    /**
     * Constructor
     * @param context
     */
    public ReportRepository(Context context) {
        File dir = context.getApplicationContext().getFilesDir();
        File jsonFile = new File(dir, "reports.json");
        this.reportDao= new JsonReportDao(jsonFile);
        this.allReportLive = reportDao.getAllReportsLive();
        // get the instance of userLog, when creating userRepository
        this.reportLogRepository= ReportLogRepository.getInstance(context);
    }

    /**
     * For Singleton
     * @param context
     * @return Instance of Repository
     */
    public static synchronized  ReportRepository getInstance(Context context) {
        if (Instance == null) {
            Instance = new ReportRepository(context.getApplicationContext());
        }
        return Instance;
    }

    /**
     * Retrieves the list of all Report objects.
     * @return a List containing all Reports
     */
    public List<Report> getAllReportsLive() { return reportDao.getAllReportsLive(); }

    /**
     * Retrieves a list of Reports that are currently waiting for review.
     * @return a List of Report objects in waiting-for-review status
     */
    public List<Report> getAllWaitingReportsLive() { return reportDao.getAllWaitingReportsLive(); }

    /**
     * Retrieves a list of report IDs associated with the specified user ID.
     * @param userId  the unique identifier of the user
     * @return List<Integer>  a List of report IDs for the user, or an empty list if none are found
     */
    public List<Integer> getIdsByUser(int userId) {
        return reportDao.getIdsByUser(userId);
    }

    /**
     * Retrieves a list of report IDs associated with the given car plate.
     * @param plate  the car plate number to query
     * @return List<Integer>  a List of report IDs matching the plate, or an empty list if none are found
     */
    public List<Integer> getIdsByPlate(String plate) {
        return reportDao.getIdsByPlate(plate);
    }

    /**
     * Inserts a new Report, assigns a unique ID, and logs the submission.
     *
     * Retrieves the current list of reports from allReportLive;
     * initializes to an empty list if null.
     * Generates a new ID as (list size + 1) and sets it on the report.
     * Calls reportDao.insertReport to persist the report.
     * Creates a ReportLog with status SUBMIT and invokes reportLogRepository.
     * insertLog to record the submission.
     *
     * @param report the Report object to insert
     */
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
     * Reviews a report by updating its status and feedback, and logs the action.
     *
     * Finds the Report with the given ID in the waiting-for-review status; returns false if not found or status mismatch.
     * Creates a copy of the Report, sets the new status (APPROVED or DECLINED) and feedback, and updates it via reportDao.
     * Inserts a corresponding ReportLog (APPROVE or DECLINE) based on the review outcome.
     *
     * @param ID  the unique identifier of the Report to be reviewed
     * @param isApproved  the review decision; true for approval, false for rejection
     * @param feedBack  the feedback message for the review
     * @return Boolean  true if the review and update succeed; false otherwise
     */
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

    /**
     * Finds a Report by its ID, optionally limiting the search to waiting-for-review reports.
     *
     * @param ID  the unique identifier of the report
     * @param isWaitStatus  if true, searches only in the waiting-for-review collection; otherwise searches all reports
     * @return Report  the matching Report, or null if not found
     */
    public Report findReport(int ID, boolean isWaitStatus) {
        return reportDao.findReport(ID,isWaitStatus);
    }


}
