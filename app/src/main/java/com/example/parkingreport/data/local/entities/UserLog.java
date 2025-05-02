package com.example.parkingreport.data.local.entities;

import java.util.Date;

public class UserLog {
    private int logId;
    private Date timestamp;
    private int userId;
    private int description;
    public static final int SIGN_UP = 1;
    public static final int CANCEL_ACCOUNT = 2;
    public static final int MODIFY_NAME = 3;
    public static final int MODIFY_PASSWORD = 4;

    public UserLog(int userId, int description) {
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

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
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
