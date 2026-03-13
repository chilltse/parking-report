package com.example.parkingreport.LLM;

import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author Yudong Qiu u7937030

 * - OpenAILLMClient implements the LLMClient interface to integrate with OpenAI API.
 * - Sends user input to OpenAI (gpt-3.5-turbo) via HTTP POST requests using OkHttp.
 * - Processes JSON responses and extracts AI-generated content from the choices array.
 * - Supports asynchronous callbacks for handling responses and network errors.
 * - Includes robust error handling for request building, network failures, and JSON parsing.
 */
public class OpenAILLMClient implements LLMClient {

    private static final String TAG = "OpenAILLMClient";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final String apiUrl;
    private final String token;
    private final OkHttpClient client = new OkHttpClient();

    public OpenAILLMClient(String apiUrl, String token) {
        this.apiUrl = apiUrl;
        this.token = token;
    }

    @Override
    public void askQuestion(String inputText, LLMCallback callback) {
        try {
            JSONObject bodyJson = buildRequestBody(inputText);
            RequestBody body = RequestBody.create(bodyJson.toString(), JSON);

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.onResponse("OpenAI Network Error: " + e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        callback.onResponse("OpenAI API Request Failed: " + response.code());
                        return;
                    }

                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray choices = jsonResponse.optJSONArray("choices");

                        if (choices != null && choices.length() > 0) {
                            String content = choices.getJSONObject(0)
                                    .getJSONObject("message")
                                    .getString("content");

                            callback.onResponse(content);
                        } else {
                            callback.onResponse("OpenAI Response: No choices returned.");
                        }
                    } catch (Exception e) {
                        callback.onResponse("OpenAI JSON Parsing Error: " + e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            callback.onResponse("OpenAI Request Building Error: " + e.getMessage());
        }
    }

    private JSONObject buildRequestBody(String inputText) throws Exception {
        JSONObject messageObj = new JSONObject();
        messageObj.put("role", "user");
        messageObj.put("content", inputText);

        JSONArray messagesArray = new JSONArray();
        messagesArray.put(messageObj);

        JSONObject bodyJson = new JSONObject();
        bodyJson.put("model", "gpt-3.5-turbo");
        bodyJson.put("messages", messagesArray);

        return bodyJson;
    }
}
