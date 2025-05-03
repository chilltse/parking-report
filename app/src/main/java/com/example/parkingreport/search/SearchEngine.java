package com.example.parkingreport.search;
import java.util.*;

/**
 * Core search engine that processes input strings, tokenizes them,
 * and evaluates them against predefined plate and name mappings.
 * The engine supports negation using the '!' operator and returns a set of matching IDs.
 */
public class SearchEngine {
    private static final HashMap<String, String> plateToId = new HashMap<>();// Maps license plates to unique person IDs
    private static final HashMap<String, String> nameToId = new HashMap<>();// Maps names to unique person IDs

    // Static initializer to populate sample data for testing or demo purposes
    static {
        plateToId.put("ABC123", "ID001");
        plateToId.put("XYZ789", "ID002");
        nameToId.put("JohnDoe", "ID001");
        nameToId.put("JaneSmith", "ID003");
        nameToId.put("MikeGreen", "ID002");
    }

    /**
     * Performs the full query evaluation pipeline:
     * 1. Tokenizes the input string into left and right parts.
     * 2. Parses them into structured tokens.
     * 3. Evaluates tokens into result sets and merges them.
     * @param input Raw query string (e.g., "JohnDoe XYZ789")
     * @return Set of matching IDs after evaluating both sides of the query
     */
    public static Set<String> search(String input) {
        Tokenizer.TokenPair pair = Tokenizer.tokenize(input);
        Parser.QueryResult result = Parser.parse(pair);

        Set<String> leftIds = evaluateTokens(result.leftTokens);
        Set<String> rightIds = evaluateTokens(result.rightTokens);

        Set<String> finalIds = new HashSet<>(leftIds);
        finalIds.addAll(rightIds);
        return finalIds;
    }

    /**
     * Evaluates a list of tokens to retrieve matching IDs.
     * Supports negation tokens (starting with "not_"), which invert the match.
     * The logic:
     * - If token is "not_", include all IDs and remove the specified one.
     * - Otherwise, include only the matched ID (if any).
     * @param tokens List of Token objects representing parsed user query
     * @return Set of matching IDs for the given token list
     */
    private static Set<String> evaluateTokens(List<Token> tokens) {
        Set<String> allIds = new HashSet<>();// Combine all known IDs from both plate and name mappings for possible negation use
        allIds.addAll(plateToId.values());
        allIds.addAll(nameToId.values());

        Set<String> result = new HashSet<>();

        // Determine the correct ID by token type
        for (Token token : tokens) {
            String type = token.getType();
            String value = token.getValue();
            String id = null;

            // Determine the correct ID by token type
            if (type.endsWith("plate")) {
                id = plateToId.get(value);
            } else if (type.endsWith("name")) {
                id = nameToId.get(value);
            }
            // Handle negation logic（e.g., if token type is "not_name"）
            if (type.startsWith("not_")) {
                result.addAll(allIds);
                if (id != null) result.remove(id);
            } else {
                if (id != null) result.add(id);
            }
        }

        return result;
    }
}
