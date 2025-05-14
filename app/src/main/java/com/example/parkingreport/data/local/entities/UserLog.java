package com.example.parkingreport.data.local.entities;

/**
 * @author @u7864325 Weimiao Sun
 *
 * Represents a user-related action log entry.
 *
 * Each UserLog captures:
 * - A unique log ID
 * - The timestamp of the user activity
 * - The ID of the user performing the action
 * - A description indicating the type of activity (e.g. registration, cancellation, password change)
 *
 * Used for auditing, tracking user events, and maintaining system accountability.
 */


import java.util.Date;

public class UserLog {
    private int logId;
    private Date timestamp;
    private int userId;
    private String description;
    public static final String SIGN_UP = "New user registration.";
    public static final String CANCEL_ACCOUNT = "User cancels accounts.";
    public static final String MODIFY_PASSWORD = "User modify account password.";

    public UserLog(int userId, String description) {
        this.timestamp = new Date();
        this.userId = userId;
        this.description = description;
    }
    public UserLog() {}

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public Date getTimestamp() {
        return timestamp;
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

    @Override
    public String toString() {
        return "User{" +
                "LOG_ID=" + logId +
                ", timestamp='" + timestamp + '\'' +
                ", userId='" + userId + '\'' +
                ", description=" + description +
                '}';
    }

}
