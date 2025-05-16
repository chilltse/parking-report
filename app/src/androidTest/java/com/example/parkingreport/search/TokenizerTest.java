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
 * Instrumented unit tests for the Tokenizer class.
 * <p>This test class verifies that the Tokenizer correctly parses search input
 * strings for admin users into appropriate Token types (e.g., USERNAME, CARPLATE),
 * and handles edge cases such as duplicates, invalid prefixes, and empty input.
 * <p>Test coverage includes:
 * - Tokenizing valid input strings into Token objects
 * - Preventing duplicate tokens
 * - Handling invalid prefixes and throwing exceptions
 * - Returning empty lists for empty inputs
 * Authored by Eden Tian u7807670
 */
@RunWith(AndroidJUnit4.class)
public class TokenizerTest {
    /**
     * Test T1: Valid input with both username and car plate should produce two tokens.
     * <p>Verifies that the tokenizer correctly splits a combined input
     * into a USERNAME token and a CARPLATE token.
     */
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


    /**
     * Test T2: Input with only car plate should produce a single CARPLATE token.
     */
    @Test
    public void testCarPlateTokenShouldBeParsedCorrectly() {
        String input = "P:T9988Y";
        String role = User.ADMIN;

        Tokenizer.Tokens tokens = Tokenizer.tokenize(input, role);

        assertEquals(1, tokens.tokens.size());
        Token expected = new Token(Token.CARPLATE, "T9988Y");
        assertEquals(expected, tokens.tokens.get(0));
    }

    /**
     * Test T3: Duplicate tokens should not be added more than once.
     * <p>Verifies that repeated tokens in the input result in only one token in the final list.
     */
    @Test
    public void testDuplicateTokensShouldNotBeAdded() {
        String input = "U:alice + U:alice";
        String role = User.ADMIN;

        Tokenizer.Tokens tokens = Tokenizer.tokenize(input, role);

        assertEquals(1, tokens.tokens.size());
        Token expected = new Token(Token.USERNAME, "alice");
        assertTrue(tokens.tokens.contains(expected));
    }

    /**
     * Test T4: Invalid token prefix should throw IllegalArgumentException.
     * <p>Checks the tokenizer’s validation mechanism by passing an unknown prefix (e.g., 'X:').
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPrefixShouldThrowException() {
        String input = "X:112233";
        String role = User.ADMIN;

        Tokenizer.tokenize(input, role);  // Should throw IllegalArgumentException
    }


    /**
     * Test T5: Empty input string should return an empty token list.
     */
    @Test
    public void testEmptyInputShouldReturnEmptyList() {
        String input = "   ";
        String role = User.ADMIN;

        Tokenizer.Tokens tokens = Tokenizer.tokenize(input, role);

        assertEquals(0, tokens.tokens.size());
    }


}