package com.example.parkingreport.LLM;

import com.example.parkingreport.BuildConfig;

public class LLMClientFactory {

    /**
     * Factory method to create LLMClient instances based on the specified type.
     *
     * @param type The type of LLM ("gemini", "openai")
     * @return An instance of LLMClient implementation.
     */
    public static LLMClient createLLMClient(String type) {
        switch (type.toLowerCase()) {
            case "gemini":
                return new GeminiLLMClient(BuildConfig.GEMINI_URL, BuildConfig.GEMINI_TOKEN);
            case "openai":
                return new OpenAILLMClient(BuildConfig.OPENAI_URL, BuildConfig.OPENAI_TOKEN);
            default:
                throw new IllegalArgumentException("Unsupported LLM type: " + type);
        }
    }
}
