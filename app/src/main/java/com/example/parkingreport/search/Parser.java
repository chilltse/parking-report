package com.example.parkingreport.search;
import java.util.*;

public class Parser {
    /**
     * Holds the final parsed result of a query expression,
     * containing lists of left and right tokens (if applicable).
     */
    public static class QueryResult {
        public List<Token> leftTokens = new ArrayList<>();
        public List<Token> rightTokens = new ArrayList<>();
    }

    /**
     * Converts a TokenPair into a QueryResult.
     * This is essentially a wrapper that assigns parsed tokens
     * into a structured result object.
     */
    public static QueryResult parse(Tokenizer.TokenPair tokenPair) {
        QueryResult result = new QueryResult();
        result.leftTokens = tokenPair.leftTokens;
        result.rightTokens = tokenPair.rightTokens;
        return result;
    }
}

