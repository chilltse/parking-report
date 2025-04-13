package com.example.parkingreport.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileLoader {
    public static String loadAssetsTemplate(String LOG_TAG, Context context, String fileName) {
        StringBuilder content = new StringBuilder();
        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
                content.append('\n');
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Read template failed", e);
        }
        Log.d(LOG_TAG, content.toString());
        return content.toString();
    }
}
