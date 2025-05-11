package com.example.parkingreport.search;

import androidx.annotation.Nullable;

/**
 * Represents a token with a specific type and value.
 * Used in search parsing to represent parsed units (e.g., plate numbers, names).
 */
public class Token {
    String type;
    String value;
    public static final String USERNAME = "userName";
    public static final String CARPLATE = "carPlate";

    // Initializes a token with the given type and value.
    public Token(String type, String value){
        this.type = type;
        this.value = value;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;  // 同一个对象
        if (o == null || getClass() != o.getClass()) return false;  // 类型不符
        Token token = (Token) o;
        return this.type.equals(((Token) o).type) && this.value.equals(((Token) o).value);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (type == null ? 0 : type.hashCode());
        result = 31 * result + (value == null ? 0 : value.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Token{" +
                "TYPE=" + type +
                ", VALUE=" + value +
                '}';
    }
}
