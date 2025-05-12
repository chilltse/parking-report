package com.example.parkingreport.LLM;

import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GeminiLLMClient implements LLMClient {

    private static final String TAG = "GeminiLLMClient";
    private static final MediaType JSON = MediaType.parse("application/json");

    private final String apiUrl;
    private final String token;
    private final OkHttpClient client = new OkHttpClient();

    public GeminiLLMClient(String apiUrl, String token) {
        this.apiUrl = apiUrl;
        this.token = token;
    }

    @Override
    public void askQuestion(String inputText, LLMCallback callback) {
        String fullUrl = apiUrl + "?key=" + token;

        JSONObject requestBody = buildRequestBody(inputText);
        Request request = new Request.Builder()
                .url(fullUrl)
                .post(RequestBody.create(JSON, requestBody.toString()))
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onResponse("Gemini Network Error: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onResponse("Gemini API Request Failed: " + response.code());
                    return;
                }

                try {
                    String responseData = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseData);
                    JSONArray candidates = jsonResponse.optJSONArray("candidates");

                    if (candidates != null && candidates.length() > 0) {
                        JSONObject firstCandidate = candidates.getJSONObject(0);
                        JSONObject content = firstCandidate.getJSONObject("content");
                        JSONArray parts = content.optJSONArray("parts");

                        if (parts != null && parts.length() > 0) {
                            String generatedText = parts.getJSONObject(0).getString("text");
                            callback.onResponse(generatedText);
                        } else {
                            callback.onResponse("Gemini Response: No content returned.");
                        }
                    } else {
                        callback.onResponse("Gemini Response: No candidates returned.");
                    }
                } catch (JSONException e) {
                    callback.onResponse("Gemini JSON Parsing Error: " + e.getMessage());
                }
            }
        });
    }

    private JSONObject buildRequestBody(String inputText) {
        try {
            JSONObject requestBody = new JSONObject();
            JSONArray contentsArray = new JSONArray();
            JSONObject contentObject = new JSONObject();
            JSONArray partsArray = new JSONArray();
            JSONObject partObject = new JSONObject();

            partObject.put("text", inputText);
            partsArray.put(partObject);
            contentObject.put("parts", partsArray);
            contentsArray.put(contentObject);

            requestBody.put("contents", contentsArray);

            return requestBody;
        } catch (JSONException e) {
            Log.e(TAG, "Failed to build request body: " + e.getMessage());
            return new JSONObject();
        }
    }
}
