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
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class ParserTest {

    private Context context;
    private ReportRepository reportRepository;
    private UserRepository userRepository;

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
