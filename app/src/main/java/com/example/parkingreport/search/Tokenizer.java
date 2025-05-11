package com.example.parkingreport.search;

import android.util.Log;

import com.example.parkingreport.data.local.entities.User;

import java.util.*;

public class Tokenizer {
    /**
     * A helper class representing two sets of tokens split by an operator (e.g., 'NOT' or 'OR').
     * Typically used to distinguish left-hand side and right-hand side of a query expression.
     */
    public static class Tokens {
        public List<Token> tokens = new ArrayList<>();
//        public List<Token> userNameTokens = new ArrayList<>();
//        public List<Token> carPlateTokens = new ArrayList<>();
    }


    /**
     * Tokenizes an input query string into a TokenPair.
     * The input is split into two parts at the first whitespace (likely representing a binary operation).
     * Each part is parsed into a list of Token objects.
     */
    public static Tokens tokenize(String input, String role) {
        Tokens tokens = new Tokens();
        input = input.trim();

        String[] parts = input.split("\\+");
        for (String part : parts) {
            part = part.trim();
            Token token = null;
            if (part.isEmpty()) continue;
            // 处理前缀
            try{
                token = prefixHandle(part, role);
                Log.d("prefixHandle_result", token.toString());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("invalid input：" + part);
            }
            // 检查是否重复，我重写了token的equal，只要type和value值一样，token就一样，而contain会用equal来比较
            if(!tokens.tokens.contains(token)){
                // 只有不重复时才添加到token中，这样不会重复查找
                tokens.tokens.add(token);
            }
//            switch (token.type){
//                case Token.USERNAME:
//                    if(!tokens.userNameTokens.contains(token)){
//                        // 只有不重复时才添加到token中，这样不会重复查找
//                        tokens.userNameTokens.add(token);
//                    }
//                    break;
//                case Token.CARPLATE:
//                    if(!tokens.carPlateTokens.contains(token)){
//                        // 只有不重复时才添加到token中，这样不会重复查找
//                        tokens.carPlateTokens.add(token);
//                    }
//                    break;
//                default:
//                    break;
//            }

        }
        return tokens;
    }

    /**
     * Parses a single part of a query string into a list of Tokens.
     * Supports optional negation using a leading 'U:' or 'P:' character.
     * Throws an exception for invalid token values.
     */
    private static Token prefixHandle(String value, String role) {
        Token token = null;
        if (value.isEmpty()) return token;

        String type = classify(value, role);

        // debug
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
     * Classifies a string into a token type: "plate", "name", or "invalid".
     * Rules:
     * - contains digits → "plate"
     * - only letters → "name"
     * - otherwise → "invalid"
     */
    private static String classify(String value, String role) {
        String prefix = null;
        String upper = value.toUpperCase();
        Boolean checkFlag = false; // 是否匹配上前缀

        boolean isUser = upper.startsWith("U:");
        if (isUser && role.equals(User.ADMIN)){
            prefix = upper.substring(0, 1); // only get first char.
            checkFlag = true;
        }
        // 仅仅当未匹配到前缀时才再进行比较
        if(checkFlag == false){
            boolean isCarplate = upper.startsWith("P:");
            if (isCarplate) {
                prefix = upper.substring(0, 1); // only get first char.
                checkFlag = true;
            }
        }

        // debug
        if(prefix != null){
            Log.d("Prefix",prefix);
        }

        if (prefix == null || checkFlag == false) return "invalid";
        if (prefix.equals("U")) return Token.USERNAME;
        if (prefix.equals("P")) return Token.CARPLATE;
        return "invalid";
    }
}

