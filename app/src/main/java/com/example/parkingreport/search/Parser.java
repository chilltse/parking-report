package com.example.parkingreport.search;
import java.util.*;

public class Parser {
    public static class QueryResult {
        public List<Token> leftTokens = new ArrayList<>();
        public List<Token> rightTokens = new ArrayList<>();
    }

    public static QueryResult parse(Tokenizer.TokenPair tokenPair) {
        QueryResult result = new QueryResult();
        result.leftTokens = tokenPair.leftTokens;
        result.rightTokens = tokenPair.rightTokens;
        return result;
    }
}

