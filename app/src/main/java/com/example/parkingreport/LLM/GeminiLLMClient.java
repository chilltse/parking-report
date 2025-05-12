package com.example.parkingreport.LLM;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.parkingreport.BuildConfig;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * An implementation of the LLMClient interface using Google's Gemini API
 * to send prompts and receive generated text.
 */
public class GeminiLLMClient implements LLMClient {

    private static final String TAG = "LLM";
    private static final String API_URL = BuildConfig.GEMINI_URL_AND_TOKEN;
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Sends a text prompt to the Gemini model and handles the asynchronous response.
     *
     * @param inputText The input prompt to send to the LLM.
     * @param callback  Callback interface for handling the result.
     */
    @Override
    public void askQuestion(String inputText, LLMCallback callback) {
        try {
            // Construct the JSON request body
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

            RequestBody body = RequestBody.create(MEDIA_TYPE, requestBody.toString());

            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            // Execute the request asynchronously
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.onResponse("Network error: " + e.getMessage());
                    Log.e(TAG, "Network error", e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        try {
                            // Parse the JSON response
                            JSONObject jsonResponse = new JSONObject(responseData);
                            JSONArray candidates = jsonResponse.getJSONArray("candidates");

                            if (candidates.length() > 0) {
                                JSONObject firstCandidate = candidates.getJSONObject(0);
                                JSONObject content = firstCandidate.getJSONObject("content");
                                JSONArray parts = content.getJSONArray("parts");
                                if (parts.length() > 0) {
                                    JSONObject part = parts.getJSONObject(0);
                                    String generatedText = part.getString("text");

                                    callback.onResponse(generatedText);
                                    Log.d(TAG, "Response: " + generatedText);
                                } else {
                                    callback.onResponse("No content returned.");
                                }
                            } else {
                                callback.onResponse("No candidates returned.");
                            }
                        } catch (JSONException e) {
                            callback.onResponse("JSON parsing failed: " + e.getMessage());
                            Log.e(TAG, "Failed to parse JSON", e);
                        }
                    } else {
                        callback.onResponse("Request failed, status code: " + response.code());
                        Log.e(TAG, "Error: " + response.code());
                    }
                }
            });

        } catch (JSONException e) {
            callback.onResponse("Failed to create JSON request: " + e.getMessage());
            Log.e(TAG, "JSON construction error", e);
        }
    }
}
