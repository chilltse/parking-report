package com.example.parkingreport.data.local.dao;

import androidx.lifecycle.LiveData;

import com.example.parkingreport.data.local.entities.Report;

import java.util.List;

public interface ReportDao {
    void insertReport(Report report);

    void clearReport();

    void deleteReport(int reportId);

    void handleReport(int reportId, int status);

    LiveData<List<Report>> getAllReportsLive();

    int checkReportExists(int reportId);

    int checkReportStatus(int reportId);

}
