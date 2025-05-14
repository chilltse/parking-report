package com.example.parkingreport.dataStream;
import android.content.Context;
import com.example.parkingreport.data.local.repository.ReportRepository;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author @u7807670 Eden Tian
 * Manages periodic execution of report-related streaming tasks for both admin and user views.
 *
 * <p>This class schedules two {@link Runnable} tasks:
 * <ul>
 *   <li><b>User task</b> – polls for the status of a specific report (e.g., waiting, approved).</li>
 *   <li><b>Admin task</b> – retrieves the current count of all reports in the system.</li>
 * </ul>
 *
 * <p>Supports two usage modes:
 * <ul>
 *   <li> <b>Production mode</b>: accepts Android context to create a live instance with real repository access.</li>
 *   <li> <b>Test mode</b>: accepts injected executor and tasks for fast, headless JVM testing or mocking.</li>
 * </ul>
 *
 * Tasks are scheduled at a fixed rate using {@link ScheduledExecutorService}.
 */


public class ReportStreamManager {
    private ScheduledExecutorService executor =
            Executors.newScheduledThreadPool(2);

    private final Runnable userTask;
    private final Runnable adminTask;
    private long userPeriodMs;
    private long adminPeriodMs;


    /**Production constructor for live data streaming.
     *
     * <p>Uses the provided Android context to obtain the singleton ReportRepository,
     * then creates the user‐ and admin polling tasks for the given reportId.
     *
     * @param appContext an Android Application Context, used to initialize ReportRepository
     * @param reportId   the ID of the report to monitor for status changes
     */
    public ReportStreamManager(Context appContext, int reportId) {
        // Use appContext to get the same ReportRepository singleton
        ReportRepository repo = ReportRepository
                .getInstance(appContext.getApplicationContext());

        // Pass the repo to each task instead of asking for context in the task
        this.userTask  = new UserReportStatusTask(repo, reportId);
        this.adminTask = new AdminReportCountTask(repo);
    }

    /**
     * Injectable constructor for pure‐JVM testing and custom scheduling.
     *
     * <p>By accepting an external ScheduledExecutorService, userTask and adminTask Runnables,
     * and fine‐tuned polling intervals, this ctor allows us to:
     * <ul>
     *   <li>Run ReportStreamManager in plain JUnit (no Android Context required).</li>
     *   <li>Inject ultra‐short periods for rapid feedback in unit tests.</li>
     *   <li>Swap in mock or lambda tasks to verify periodic invocation counts.</li>
     * </ul>
     *
     * @param executor    the ScheduledExecutorService used to schedule both tasks
     * @param userTask    the Runnable to run on each “user” poll
     * @param adminTask   the Runnable to run on each “admin” poll
     * @param userPeriodMs  interval in milliseconds between userTask executions
     * @param adminPeriodMs interval in milliseconds between adminTask executions
     */
    public ReportStreamManager(
            ScheduledExecutorService executor,
            Runnable userTask,
            Runnable adminTask,
            long userPeriodMs,
            long adminPeriodMs
    ) {
        this.executor     = executor;
        this.userTask     = userTask;
        this.adminTask    = adminTask;
        this.userPeriodMs = userPeriodMs;
        this.adminPeriodMs= adminPeriodMs;
    }

    public void start() {
        executor.scheduleAtFixedRate(userTask, 0, userPeriodMs, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(adminTask, 0, adminPeriodMs, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        executor.shutdownNow();
    }

}