package com.example.parkingreport.data.local.repository;

import android.content.Context;

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
    private LiveData<List<Report>> allReportLive;

    private ReportLogRepository reportLogRepository;

    public ReportRepository(Context context) {
        File dir = context.getApplicationContext().getFilesDir();
        File jsonFile = new File(dir, "reports.json");
        this.reportDao= new JsonReportDao(jsonFile);
        this.allReportLive = reportDao.getAllReportsLive();
        // get the instance of userLog, when creating userRepository
        this.reportLogRepository= ReportLogRepository.getInstance(context);
    }
    public LiveData<List<Report>> getAllReportsLive() { return reportDao.getAllReportsLive(); }
    public List<Report> getAllWaitingReportsLive() { return reportDao.getAllWaitingReportsLive(); }
    public static synchronized  ReportRepository getInstance(Context context) {
        if (Instance == null) {
            Instance = new ReportRepository(context.getApplicationContext());
        }
        return Instance;
    }

    public LiveData<List<Report>> getAllReportLive() {
        return allReportLive;
    }

    public List<Integer> getIdsByUser(int userId) {
        return reportDao.getIdsByUser(userId);
    }

    public List<Integer> getIdsByPlate(String plate) {
        return reportDao.getIdsByPlate(plate);
    }

    public void insertReport(Report report){
        // generated id
        List<Report> list = allReportLive.getValue();
        if (list == null) list = new ArrayList<>();

        int newId = list.size();
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


//    public void deleteReport(int reportId){
//        if(reportDao.checkReportExists(reportId) == 0)
//            return;
//        reportDao.deleteReport(reportId);
//    }

//    /**
//     *  Handle report,
//     * @param reportId the Id of report that we want to handle.
//     * @param status  the status(Approve or decline) that we want to set for this report.
//     */
//    public void handleReport(int reportId, int userId, String status){
//        //check report exists
//        if(reportDao.checkReportExists(reportId) == 0)
//            return;
//        //check if status are legal (approve or decline)
//        if(!status.equals(Report.APPROVED) && !status.equals(Report.DECLINED))
//            return;
//        //check if current status is wait for review
//        if(!reportDao.checkReportStatus(reportId).equals(Report.WAIT_FOR_REVIEW))
//            return;
//        //only current status is wait for review can be review
//        reportDao.handleReport(reportId, status);
//
//        // insert reportLog
//        if(status == Report.APPROVED){
//            reportLogRepository.insertLog(new ReportLog(reportId, userId, ReportLog.APPROVE));
//        }else{
//            reportLogRepository.insertLog(new ReportLog(reportId, userId, ReportLog.DECLINE));
//        }
//    }


    public boolean replyReport(int ID, boolean isApproved, String feedBack){
        Report report = reportDao.findReport(ID, true);
        // 只有当报告存在且状态是待处理的才OK
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
