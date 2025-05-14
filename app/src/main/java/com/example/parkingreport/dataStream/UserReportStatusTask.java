package com.example.parkingreport.dataStream;

import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.data.local.repository.ReportRepository;

/**
 * A background task that retrieves and prints the current status of a specific report.
 *
 * This task is designed to simulate a user-side status check in a live data stream context.
 * It queries the {@link ReportRepository} for the given report ID and prints the result to console.
 *
 * If the report is not found, it prints "UNKNOWN" as the status.
 */

public class UserReportStatusTask implements Runnable{
    private final ReportRepository repo;
    private final int reportId;
    public UserReportStatusTask(ReportRepository repo, int reportId) {
        this.repo = repo;
        this.reportId = reportId;
    }

    @Override
    public void run() {
        // Use findReport to get the Report, and then read its status
        Report r = repo.findReport(reportId, /*isWaitStatus=*/ false);
        String newStatus = (r != null ? r.getStatus() : "UNKNOWN");

        System.out.printf("[User] Report %d status: %s%n", reportId, newStatus);
    }
}

