package com.example.parkingreport.data.local.dao;

import androidx.lifecycle.LiveData;

import com.example.parkingreport.data.local.entities.ReportLog;

import java.util.List;

public interface ReportLogDao {
    void insertLog(ReportLog reportLog);
    void clearLog();
    LiveData<List<ReportLog>> getAllReportLogsLive();
}
