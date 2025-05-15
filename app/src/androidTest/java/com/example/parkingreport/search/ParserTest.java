package com.example.parkingreport.search;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.parkingreport.data.local.entities.Report;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.repository.ReportRepository;
import com.example.parkingreport.data.local.repository.UserRepository;
import com.example.parkingreport.search.Token;
import com.example.parkingreport.search.Tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Instrumented tests for the Parser component in the admin search feature.
 * <p>This test class verifies that tokenized admin search queries
 * return correct results based on username, car plate, or their combination.
 * It runs on an Android device/emulator using Instrumentation and accesses
 * the real database via ReportRepository and UserRepository.
 * <p>Test cases include:
 * - Search by username only
 * - Search by car plate only
 * - Search by username and non-existent plate
 * - Search by both username and plate with strict match
 * Authored by Eden Tian u7807670
 */



@RunWith(AndroidJUnit4.class)
public class ParserTest {

    private Context context;
    private ReportRepository reportRepository;
    private UserRepository userRepository;

    /**
     * Test T1: Admin search by username only.
     * <p>Sets up one admin user "Alice" and inserts two reports under her name.
     * Tokenizes the input "U:Alice" and asserts that both reports are returned.
     */
    @Test
    public void testT1_AdminSearchOnlyUsername() {
        context = ApplicationProvider.getApplicationContext();

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            userRepository = UserRepository.getInstance(context);
            reportRepository = ReportRepository.getInstance(context);

            User alice = new User("Alice", "alice@anu.edu.au", "123", "", "admin", true);
            userRepository.insertUser(alice);
            int aliceId = userRepository.findIdByName("Alice");

            reportRepository.insertReport(new Report(aliceId, "Alice", "A1", "Loc1", "pic1", Report.WAIT_FOR_REVIEW));
            reportRepository.insertReport(new Report(aliceId, "Alice", "A2", "Loc2", "pic2", Report.WAIT_FOR_REVIEW));
        });

        List<Token> tokens = Tokenizer.tokenize("U:Alice", User.ADMIN).tokens;
        int userId = userRepository.findIdByName("Alice");

        List<Report> results = Parser.evaluateTokens(tokens, true, User.ADMIN, userId, reportRepository, userRepository);
        assertEquals(2, results.size());
    }


    /**
     * Test T2: Admin search by car plate only.
     * <p>Sets up one admin user "Alice" and inserts a report with car plate "ZZZ999".
     * Tokenizes the input "P:ZZZ999" and asserts that one matching report is returned.
     */
    @Test
    public void testT2_AdminSearchOnlyPlate() {
        context = ApplicationProvider.getApplicationContext();

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            userRepository = UserRepository.getInstance(context);
            reportRepository = ReportRepository.getInstance(context);

            User alice = new User("Alice", "alice@anu.edu.au", "123", "", "admin", true);
            userRepository.insertUser(alice);
            int aliceId = userRepository.findIdByName("Alice");

            reportRepository.insertReport(new Report(aliceId, "Alice", "ZZZ999", "Loc2", "pic2", Report.WAIT_FOR_REVIEW));
        });

        List<Token> tokens = Tokenizer.tokenize("P:ZZZ999", User.ADMIN).tokens;
        int userId = userRepository.findIdByName("Alice");

        List<Report> results = Parser.evaluateTokens(tokens, true, User.ADMIN, userId, reportRepository, userRepository);
        assertEquals(1, results.size());
    }

    /**
     * Test T3: Admin search using a username that exists and a plate that doesn't.
     * <p>Sets up one admin user "Bob", then attempts a search with "U:CAT" which
     * does not match any user or report. Asserts that zero results are returned.
     */
    @Test
    public void testT3_AdminSearchUsernameAndNonexistentPlate() {
        context = ApplicationProvider.getApplicationContext();

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            userRepository = UserRepository.getInstance(context);
            reportRepository = ReportRepository.getInstance(context);
            userRepository.clearUser();

            User bob = new User("Bob", "bob@anu.edu.au", "456", "", "admin", true);
            userRepository.insertUser(bob);
        });

        List<Token> tokens = Tokenizer.tokenize("U:CAT", User.ADMIN).tokens;
        int userId = userRepository.findIdByName("CAT");

        List<Report> results = Parser.evaluateTokens(tokens, true, User.ADMIN, userId, reportRepository, userRepository);
        assertEquals(0, results.size());
    }

    /**
     * Test T4: Admin search with both username and plate, where only one report matches both.
     * <p>Sets up two users, "Alice" and "Bob", and adds multiple reports.
     * Only one report matches both "U:Alice" and "P:ABC123".
     * Asserts that exactly one result is returned with correct car plate.
     */
    @Test
    public void testT4_EvaluateTokens_AdminSearchUAndP_MatchOnlyCorrect() {
        context = ApplicationProvider.getApplicationContext();

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            userRepository = UserRepository.getInstance(context);
            reportRepository = ReportRepository.getInstance(context);

            User alice = new User("Alice", "alice@anu.edu.au", "123", "", "admin", true);
            userRepository.insertUser(alice);
            int aliceId = userRepository.findIdByName("Alice");

            User bob = new User("Bob", "bob@anu.edu.au", "456", "", "admin", true);
            userRepository.insertUser(bob);
            int bobId = userRepository.findIdByName("Bob");

            reportRepository.insertReport(new Report(aliceId, "Alice", "ABC123", "Loc1", "pic1", Report.WAIT_FOR_REVIEW));
            reportRepository.insertReport(new Report(aliceId, "Alice", "ZZZ999", "Loc2", "pic2", Report.WAIT_FOR_REVIEW));
            reportRepository.insertReport(new Report(aliceId, "Alice", "ABC123", "Loc3", "pic3", Report.APPROVED));
            reportRepository.insertReport(new Report(bobId, "Bob", "ABC123", "Loc4", "pic4", Report.WAIT_FOR_REVIEW));
        });

        List<Token> tokens = Tokenizer.tokenize("U:Alice+P:ABC123", User.ADMIN).tokens;
        int userId = userRepository.findIdByName("Alice");

        List<Report> results = Parser.evaluateTokens(
                tokens,
                true,   // isWaitStatus
                User.ADMIN,
                userId,
                reportRepository,
                userRepository
        );

        assertEquals(1, results.size());
        assertEquals("ABC123", results.get(0).getCarPlate());
    }
}
