package com.example.parkingreport.LLM;

public interface LLMClient {
    void askQuestion(String inputText, LLMCallback callback);
}