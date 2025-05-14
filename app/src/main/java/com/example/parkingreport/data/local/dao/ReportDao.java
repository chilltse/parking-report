package com.example.parkingreport.data.local.dao;

/**
 * @author @u7864325 Weimiao Sun
 *
 * Data Access Object (DAO) interface for managing Report entities in the system.
 *
 * Defines the contract for interacting with report data, including:
 * - Insertion and update of reports
 * - Querying reports by ID, user ID, or license plate
 * - Checking report existence and status
 * - Retrieving all or pending (waiting) reports
 *
 * Implementations may vary in storage method (e.g., JSON file, database),
 * but must fulfil all method contracts defined in this interface.
 */


import androidx.lifecycle.LiveData;

import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.data.local.entities.User;

import java.util.List;

public interface ReportDao {
    void insertReport(Report report);

//    void deleteReport(int reportId);
    void updateReport(Report report);
    Report findReport(int ID, boolean isWaitStatus);
    Report copyReport(Report report);

//    void handleReport(int reportId, String status);

    List<Report> getAllReportsLive();

    int checkReportExists(int reportId);

    String checkReportStatus(int reportId);
    public List<Report> getAllWaitingReportsLive();

    List<Integer> getIdsByUser(int userId);
    List<Integer> getIdsByPlate(String plate);

}
