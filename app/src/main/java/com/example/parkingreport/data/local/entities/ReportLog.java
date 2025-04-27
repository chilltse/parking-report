package com.example.parkingreport.data.local.entities;

import java.util.Date;

public class ReportLog {
    private int logId;
    private Date timestamp;
    private int reportId;
    private int userId;
    private int description;
    public static final int SUBMIT = 1;
    public static final int HANDLE = 2;

    public ReportLog(int reportId, int userId, int description) {
        this.timestamp = new Date();
        this.reportId = reportId;
        this.userId = userId;
        this.description = description;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }
}
