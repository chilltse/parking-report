package com.example.parkingreport.search;

/**
 * Represents a token with a specific type and value.
 * Used in search parsing to represent parsed units (e.g., plate numbers, names).
 */
public class Token {
    String type;
    String value;

    // Initializes a token with the given type and value.
    public Token(String type, String value){
        this.type = type;
        this.value = value;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
