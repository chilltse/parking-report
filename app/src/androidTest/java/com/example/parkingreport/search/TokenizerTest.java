package com.example.parkingreport.search;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.search.Token;
import com.example.parkingreport.search.Tokenizer;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TokenizerTest {
    @Test
    public void testAdminValidInput_UAndP() {
        String input = "U:comp2100@anu.edu.au + P:T9988Y";
        String role = User.ADMIN;

        Tokenizer.Tokens tokens = Tokenizer.tokenize(input, role);
        List<Token> list = tokens.tokens;

        assertEquals(2, list.size());
        assertEquals(Token.USERNAME, list.get(0).getType());
        assertEquals("comp2100@anu.edu.au", list.get(0).getValue());
        assertEquals(Token.CARPLATE, list.get(1).getType());
        assertEquals("T9988Y", list.get(1).getValue());
    }

    @Test
    public void testCarPlateTokenShouldBeParsedCorrectly() {
        String input = "P:T9988Y";
        String role = User.ADMIN;

        Tokenizer.Tokens tokens = Tokenizer.tokenize(input, role);

        assertEquals(1, tokens.tokens.size());
        Token expected = new Token(Token.CARPLATE, "T9988Y");
        assertEquals(expected, tokens.tokens.get(0));
    }

    @Test
    public void testDuplicateTokensShouldNotBeAdded() {
        String input = "U:alice + U:alice";
        String role = User.ADMIN;

        Tokenizer.Tokens tokens = Tokenizer.tokenize(input, role);

        assertEquals(1, tokens.tokens.size());
        Token expected = new Token(Token.USERNAME, "alice");
        assertTrue(tokens.tokens.contains(expected));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPrefixShouldThrowException() {
        String input = "X:112233";
        String role = User.ADMIN;

        Tokenizer.tokenize(input, role);  // Should throw IllegalArgumentException
    }

    @Test
    public void testEmptyInputShouldReturnEmptyList() {
        String input = "   ";
        String role = User.ADMIN;

        Tokenizer.Tokens tokens = Tokenizer.tokenize(input, role);

        assertEquals(0, tokens.tokens.size());
    }


}