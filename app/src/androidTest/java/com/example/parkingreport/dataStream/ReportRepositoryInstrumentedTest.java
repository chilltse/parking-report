package com.example.parkingreport.dataStream;

import static org.junit.Assert.*;
import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.data.local.repository.ReportRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.File;

/**
 * Instrumented tests for ReportRepository:
 * 1. Ordinary users can see that the administrator approves their own reports
 * 2. The administrator can see that the number of reports increases as they are inserted
 *
 * Authored by Larry Wang u7807744
 */
@RunWith(AndroidJUnit4.class)
public class ReportRepositoryInstrumentedTest {

    private Context appContext;
    private ReportRepository repo;
    private File storageFile;

    @Before
    public void setUp() {
        // 1. Get the Application Context
        appContext = ApplicationProvider.getApplicationContext();
        // 2. Delete the old reports.json to ensure that each test starts from an empty repository
        storageFile = new File(appContext.getFilesDir(), "reports.json");
        if (storageFile.exists()) {
            storageFile.delete();
        }
        // 3. Initialize ReportRepository in the main thread to avoid errors when calling setValue in loadFromFile()
        InstrumentationRegistry.getInstrumentation()
                .runOnMainSync(() -> {
                    repo = ReportRepository.getInstance(appContext);
                });
    }



    @After
    public void tearDown() {
        // Clean up files after testing
        if (storageFile.exists()) {
            storageFile.delete();
        }
        //Reset the singleton (via reflection or next restart of the test process)
    }

    /**
     * Scenario 1: A normal user submits a report,
     * The default status should be WAIT_FOR_REVIEW. After the administrator calls replyReport,
     * Pull the same report again, and the status should become APPROVED.
     */
    @Test
    public void userSeesApprove() {
        // 1) User submits a new report
        Report r = new Report(/* Necessary fields can be set during construction, and id will be automatically assigned by insertReport */);
        r.setStatus(Report.WAIT_FOR_REVIEW);
        repo.insertReport(r);

        int id = r.getID();
        // 2) Verify that the initial state after insertion is WAIT_FOR_REVIEW
        Report before = repo.findReport(id, /*isWaitStatus=*/ false);
        assertNotNull(before);
        assertEquals(Report.WAIT_FOR_REVIEW, before.getStatus());

        // 3) Admin Approval
        boolean ok = repo.replyReport(id, /*isApproved=*/ true, "Looks good");
        assertTrue(ok);

        // 4) If the user pulls again, he should see APPROVED
        Report after = repo.findReport(id, /*isWaitStatus=*/ false);
        assertNotNull(after);
        assertEquals(Report.APPROVED, after.getStatus());
    }

    /**
     * Scenario 2: After inserting reports multiple times,
     * getAllReportsLive().size() should be incremented by the number of inserts.
     */
    @Test
    public void adminSeesCountIncrease() {
        // Initial quantity
        int initialCount = repo.getAllReportsLive().size();

        // Insert three
        for (int i = 0; i < 3; i++) {
            Report r = new Report();
            r.setStatus(Report.WAIT_FOR_REVIEW);
            repo.insertReport(r);
        }

        // Total verifications increased by 3
        int newCount = repo.getAllReportsLive().size();
        assertEquals(initialCount + 3, newCount);
    }
}
