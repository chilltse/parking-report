package com.example.parkingreport.data.local.entities;

import com.example.parkingreport.data.local.api.HasID;

import java.util.Date;

public class Report implements Comparable<Report>, HasID {
    private int ID;
    private int userId;
    private Date timestamp;
    private String carPlate;
    private String location;
    private String feedback;
    private int status;
    public static final int APPROVED = 1;
    public static final int DECLINED= 0;
    public static final int WAIT_FOR_REVIEW= -1;

    public Report(int userId, String carPlate, String location, int status) {

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
