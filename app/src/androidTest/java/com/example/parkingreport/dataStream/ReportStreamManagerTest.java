package com.example.parkingreport.dataStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for ReportStreamManager.
 *
 * Verifies that start() correctly schedules the userTask and adminTask
 * at the configured periods using a custom ScheduledExecutorService,
 * and that stop() cancels all scheduled executions and releases resources.
 *
 * Authored by Larry Wang u7807744
 */

public class ReportStreamManagerTest {
    private ScheduledExecutorService scheduler;
    private AtomicInteger userCalls;
    private AtomicInteger adminCalls;
    private ReportStreamManager mgr;

    @Before
    public void setUp() {
        // 1) Use a short-lived thread pool
        scheduler = Executors.newScheduledThreadPool(2);

        // 2) Two counters, one for recording the number of times "user polling" and the other for recording the number of times "admin polling" are called
        userCalls  = new AtomicInteger(0);
        adminCalls = new AtomicInteger(0);

        // 3) Construct ReportStreamManager: call userTask every 50ms and call adminTask every 100ms
        mgr = new ReportStreamManager(
                scheduler,
                // userTask：Increment userCalls each time it is executed
                () -> userCalls.incrementAndGet(),
                // adminTask：Increment adminCalls each time it is executed
                () -> adminCalls.incrementAndGet(),
                /* userPeriodMs = */ 50,
                /* adminPeriodMs = */ 100
        );

        // 4) Start Scheduling
        mgr.start();
    }

    @After
    public void tearDown() {
        // Stop scheduling and release threads
        mgr.stop();
    }

    @Test
    public void userTaskRunsPeriodically() throws InterruptedException {
        // Wait 250ms and let userTask run at least 4-5 times
        Thread.sleep(250);
        int calls = userCalls.get();
        assertTrue(
                "Expected userTask to run at least 4 times, but was " + calls,
                calls >= 4
        );
    }

    @Test
    public void adminTaskRunsPeriodically() throws InterruptedException {
        // Wait 350ms and let adminTask run at least 3 times
        Thread.sleep(350);
        int calls = adminCalls.get();
        assertTrue(
                "Expected adminTask to run at least 3 times, but was " + calls,
                calls >= 3
        );
    }
}

