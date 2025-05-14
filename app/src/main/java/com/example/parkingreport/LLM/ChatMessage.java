package com.example.parkingreport.LLM;

//@author Yudong Qiu u7937030
public class ChatMessage {
    public String sender; // "user" or "ai"
    public String message;

    public ChatMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }
}
