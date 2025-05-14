package com.example.parkingreport.data.local.entities;

/**
 * @author @u7864325 Weimiao Sun
 * Represents a user-submitted parking violation report.
 *
 * Each Report includes key metadata such as:
 * - Associated user information (ID and name)
 * - Timestamp of submission
 * - Vehicle license plate and location of violation
 * - Optional picture URL, feedback from admin, and status
 *
 * Implements {Comparable} to support sorting by report ID.
 * Also implements {HasID} to provide a consistent ID access pattern.
 *
 * Used across both frontend UI and backend storage systems.
 */


import com.example.parkingreport.data.local.api.HasID;

import java.util.Date;

public class Report implements Comparable<Report>, HasID {
    private int ID;
    private int userId;
    private String userName;
    private Date timestamp;
    private String carPlate;
    private String location;
    private String reportPicUrl;
    private String feedback;
    private String status;
    public static final String APPROVED = "APPROVED";
    public static final String DECLINED= "DECLINED";
    public static final String WAIT_FOR_REVIEW= "WAIT_FOR_REVIEW";

    public Report(int userId, String userName, String carPlate, String location, String reportPicUrl, String status) {

        this.userId = userId;
        this.userName = userName;
        this.timestamp = new Date();
        this.carPlate = carPlate;
        this.location = location;
        this.reportPicUrl = reportPicUrl;
        this.feedback = new String();
        this.status = status;

    }

    public Report() {}


    @Override
    public int compareTo(Report other) {
        return Integer.compare(this.ID, other.ID);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getUserId() {
        return userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public String getLocation() {
        return location;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReportPicUrl() {
        return reportPicUrl;
    }
    public void setReportPicUrl(String reportPicUrl) {
        this.reportPicUrl = reportPicUrl;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Report{" +
                "report_ID=" + ID +
                ", user_ID='" + userId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", carPlate='" + carPlate + '\'' +
                ", location='" + location + '\'' +
                ", feedback='" + feedback+ '\'' +
                ", status=" + status +
                '}';
    }
}
