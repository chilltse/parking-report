package com.example.parkingreport.data.local.dao;


import androidx.lifecycle.LiveData;

import com.example.parkingreport.data.local.entities.UserLog;

import java.util.List;

public interface UserLogDao {
    void insertLog(UserLog userLog);
    void clearLog();
    LiveData<List<UserLog>> getAllUserLogsLive();
}