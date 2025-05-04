package com.example.parkingreport.data.local.dao;

import androidx.lifecycle.LiveData;

import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.data.local.entities.User;

import java.util.List;

public interface ReportDao {
    void insertReport(Report report);

    void clearReport();

//    void deleteReport(int reportId);
    void updateReport(Report report);
    Report findReport(int ID, boolean isWaitStatus);
    Report copyReport(Report report);

    void handleReport(int reportId, String status);

    LiveData<List<Report>> getAllReportsLive();

    int checkReportExists(int reportId);

    String checkReportStatus(int reportId);

}
