package com.example.parkingreport.search;

import java.util.*;

public class Tokenizer {
    public static class TokenPair {
        public List<Token> leftTokens = new ArrayList<>();
        public List<Token> rightTokens = new ArrayList<>();
    }

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

    private static String classify(String input) {
        if (input == null || input.isEmpty()) return "invalid";
        if (input.contains(" ")) return "invalid";
        if (input.matches(".*\\d.*")) return "plate";
        if (input.matches("^[A-Za-z]+$")) return "name";
        return "invalid";
    }
}
