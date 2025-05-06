package com.example.parkingreport.LLM;
public class ChatMessage {
    public String sender; // "user" 或 "ai"
    public String message;

    public ChatMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }
}
