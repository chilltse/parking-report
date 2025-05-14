package com.example.parkingreport.search;

import static com.ctc.wstx.shaded.msv_core.driver.textui.Debug.debug;

import android.util.Log;

import com.example.parkingreport.data.local.entities.User;

import java.util.*;

public class Tokenizer {
    /**
     * @author @u7807744 Larry Wang
     * A helper class representing one sets of tokens split by an operator ("+").
     */
    public static class Tokens {
        public List<Token> tokens = new ArrayList<>();
    }

    /**
     * Splits the input string on "+" and produces a unique list of Tokens for query processing.
     *
     * Trims leading and trailing whitespace from the input.
     * Splits the input by "+" into segments and processes each non-empty segment.
     * Uses prefixHandle(part, role) to generate a Token; throws IllegalArgumentException for invalid prefixes.
     * Checks for duplicates before adding, ensuring each Token in the returned list has a unique type and value.
     *
     * @param input  the raw input string to tokenize
     * @param role  the user role used for prefix handling and authorization
     * @return Tokens  a Tokens object containing all unique Tokens
     * @throws IllegalArgumentException if any segment has an invalid prefix
     */
    public static Tokens tokenize(String input, String role) {
        Tokens tokens = new Tokens();
        input = input.trim();

        String[] parts = input.split("\\+");
        for (String part : parts) {
            part = part.trim();
            Token token = null;
            if (part.isEmpty()) continue;
            // handle the prefix
            try{
                token = prefixHandle(part, role);
                Log.d("prefixHandle_result", token.toString());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("invalid input：" + part);
            }
            // Check if it's repeated, as long as type and value are the same, token is the same. And contain will be compare by equal.
            if(!tokens.tokens.contains(token)){
                // Only add to tokens if it's not repeated.
                tokens.tokens.add(token);
            }


        }
        return tokens;
    }

    /**
     * Generates a Token based on the input string prefix and user role.
     *
     * Returns null if the input value is empty.
     * Calls classify(value, role) to determine the token type and logs it for debugging.
     * Throws IllegalArgumentException if the classification result is "invalid", indicating an illegal prefix.
     * For Token.USERNAME or Token.CARPLATE types, strips the first two characters (the prefix), trims whitespace, and constructs the Token.
     *
     * @param value the raw input string with prefix (e.g., "u:alice" or "p:ABC123")
     * @param role  the user role used for classification and permission logic
     * @return Token  the constructed Token, or null if the input is empty
     * @throws IllegalArgumentException if classification yields "invalid"
     */
    private static Token prefixHandle(String value, String role) {
        Token token = null;
        if (value.isEmpty()) return token;

        String type = classify(value, role);

         //debug
        if(type != null){
            Log.d("classify_type",type);
        }

        switch (type){
            case "invalid":
                throw new IllegalArgumentException("invalid input：" + value);
            case Token.USERNAME:
                token = new Token(Token.USERNAME, value.substring(2).trim());
                break;
            case Token.CARPLATE:
                token = new Token(Token.CARPLATE, value.substring(2).trim());
                break;
        }

        return token;

    }

    /**
     * Determines and returns the token type based on the input string’s prefix and the user’s role.
     *
     * Converts the input to uppercase and checks if it starts with "U:"; only valid for User.ADMIN roles.
     * If not a valid username prefix, checks for a "P:" prefix to identify car plate tokens.
     * Returns the corresponding Token type (Token.USERNAME or Token.CARPLATE) when matched, or "invalid" otherwise.
     *
     * @param value  the raw input string with prefix (e.g., "u:alice" or "p:ABC123")
     * @param role  the user’s role for permission validation on username prefix
     * @return String  Token.USERNAME, Token.CARPLATE, or "invalid" if the prefix is not recognized
     */
    private static String classify(String value, String role) {
        String prefix = null;
        String upper = value.toUpperCase();
        Boolean checkFlag = false; //Check if the prefix is valid

        boolean isUser = upper.startsWith("U:");
        if (isUser && role.equals(User.ADMIN)){
            prefix = upper.substring(0, 1); // only get first char.
            checkFlag = true;
        }
        // Only compare if the prefix is not matched.
        if(!checkFlag){
            boolean isCarplate = upper.startsWith("P:");
            if (isCarplate) {
                prefix = upper.substring(0, 1); // only get first char.
                checkFlag = true;
            }
        }
        if(prefix != null){
            Log.d("Prefix",prefix);
        }
        if (prefix == null || checkFlag == false) return "invalid";
        if (prefix.equals("U")) return Token.USERNAME;
        if (prefix.equals("P")) return Token.CARPLATE;
        return "invalid";
    }
}

