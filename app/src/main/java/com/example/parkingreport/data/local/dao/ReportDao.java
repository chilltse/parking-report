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

    /**
     * Inserts a new Report into the list, sets its status, updates indexes, and persists to file.
     * @param report the new Report to insert.
     */
    void insertReport(Report report);

    /**
     * Updates an existing Report, synchronizes index structures, and persists changes to file.
     * @param report the report we want update.
     */
    void updateReport(Report report);

    /**
     * Finds a Report by its ID, either in the waiting-only index or in the full report index.
     * @param ID  the unique identifier of the Report to find
     * @param isWaitStatus  whether to search only in the waitingReportTree
     * @return Report  the matching Report object, or null if not found
     */
    Report findReport(int ID, boolean isWaitStatus);

    /**
     * Creates a copy of the given Report, preserving all its properties.
     * @param report  the source Report to duplicate
     * @return Report  a new Report instance with identical field values
     */
    Report copyReport(Report report);

    /**
     * Retrieves the current list of Report objects held in LiveData.
     * @return a List of all Reports, or null if LiveData has not been initialized
     */
    List<Report> getAllReportsLive();

    /**
     * Retrieves the list of all Reports currently waiting for review.
     * @return a List of Reports that are waiting for review
     */
    public List<Report> getAllWaitingReportsLive();

    /**
     * Retrieves a list of report IDs associated with the given user ID.
     * @param userId the ID of the user to query
     * @return List<Integer>  a List of report IDs for the user, or an empty list if none exist
     */
    List<Integer> getIdsByUser(int userId);

    /**
     * Retrieves a list of report IDs associated with the given car plate.
     * @param plate the car plate number to query
     * @return List<Integer>  a List of report IDs matching the plate, or an empty list if none found
     */
    List<Integer> getIdsByPlate(String plate);

}
