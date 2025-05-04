package com.example.parkingreport.data.local.entities;

import com.example.parkingreport.data.local.api.HasID;

import java.util.Date;

public class Report implements Comparable<Report>, HasID {
    private int ID;
    private int userId;
    private Date timestamp;
    private String carPlate;
    private String location;
    private String reportPicUrl;
    private String feedback;
    private String status;
    public static final String APPROVED = "APPROVED";
    public static final String DECLINED= "DECLINED";
    public static final String WAIT_FOR_REVIEW= "WAIT_FOR_REVIEW";

    public Report(int userId, String carPlate, String location, String status) {

        this.userId = userId;
        this.timestamp = new Date();
        this.carPlate = carPlate;
        this.location = location;
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
