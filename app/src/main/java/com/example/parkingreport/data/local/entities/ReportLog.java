package com.example.parkingreport.data.local.entities;

import java.util.Date;

public class ReportLog {
    private int logId;
    private Date timestamp;
    private int reportId;
    private int userId;
    private String description;
    public static final String SUBMIT = "User submits a new report.";
    public static final String APPROVE = "The administrator user passed a report.";
    public static final String DECLINE = "The administrator user declined a report.";

    public ReportLog(int reportId, int userId, String description) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
