package com.example.parkingreport.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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


    public static Map<String, String> readPlatePhone(Context context, String fileName) throws IOException {
        Map<String, String> result = new HashMap<>();
        // 用 AssetManager 打开
        try (InputStream is = context.getAssets().open(fileName);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split(",", 2);
                if (parts.length < 2) continue;
                result.put(parts[0].trim(), parts[1].trim());
            }
        }
        return result;
    }
}
