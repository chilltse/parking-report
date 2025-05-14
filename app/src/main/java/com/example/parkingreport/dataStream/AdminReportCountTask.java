package com.example.parkingreport.dataStream;

import com.example.parkingreport.data.local.repository.ReportRepository;

/**
 * @author @u7807670 Eden Tian
 * A background task that retrieves and prints the current total number of reports.
 * Intended for admin-side monitoring or logging purposes during testing or simulation.
 * This task queries the {@link ReportRepository} and prints the result to standard output.
 * Can be scheduled using a background executor or thread in the dataStream simulation pipeline.
 */

public class AdminReportCountTask implements Runnable{
    private final ReportRepository repo;
    public AdminReportCountTask(ReportRepository repo){
        this.repo = repo;
    }

    @Override
    public void run() {
        // Get the latest total in the backend or local DB
        int count = repo.getAllReportsLive().size();
        System.out.printf("[Admin] Total reports: %d%n", count);
    }
}
