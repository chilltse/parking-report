package com.example.parkingreport.search;

import java.util.*;

public class Tokenizer {
    /**
     * A helper class representing two sets of tokens split by an operator (e.g., 'AND' or 'OR').
     * Typically used to distinguish left-hand side and right-hand side of a query expression.
     */
    public static class TokenPair {
        public List<Token> leftTokens = new ArrayList<>();
        public List<Token> rightTokens = new ArrayList<>();
    }


    /**
     * Tokenizes an input query string into a TokenPair.
     * The input is split into two parts at the first whitespace (likely representing a binary operation).
     * Each part is parsed into a list of Token objects.
     */
    public static TokenPair tokenize(String input) {
        TokenPair pair = new TokenPair();
        input = input.trim();
        String[] parts = input.split("\\+", 2);
        if (parts.length == 1) {
            pair.leftTokens = parsePart(parts[0].trim());
        } else {
            pair.leftTokens = parsePart(parts[0].trim());
            pair.rightTokens = parsePart(parts[1].trim());
        }
        return pair;
    }

    /**
     * Parses a single part of a query string into a list of Tokens.
     * Supports optional negation using a leading '!' character.
     * Throws an exception for invalid token values.
     */
    private static List<Token> parsePart(String value) {
        List<Token> tokens = new ArrayList<>();
        if (value.isEmpty()) return tokens;

        boolean isNegative = value.startsWith("!");
        if (isNegative) value = value.substring(1).trim();

        String type = classify(value);
        if (type.equals("invalid")) {
            throw new IllegalArgumentException("invalid input：" + value);
        }

        tokens.add(new Token(isNegative ? "not_" + type : type, value));
        return tokens;
    }
    /**
     * Classifies a string into a token type: "plate", "name", or "invalid".
     * Rules:
     * - contains digits → "plate"
     * - only letters → "name"
     * - otherwise → "invalid"
     */
    private static String classify(String input) {
        if (input == null || input.isEmpty()) return "invalid";
        if (input.contains(" ")) return "invalid";
        if (input.matches(".*\\d.*")) return "plate";
        if (input.matches("^[A-Za-z]+$")) return "name";
        return "invalid";
    }
}
