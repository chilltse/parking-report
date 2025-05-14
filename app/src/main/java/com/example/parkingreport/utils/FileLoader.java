package com.example.parkingreport.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for loading files and templates from the assets directory.
 */
public class FileLoader {
    /**
     * Reads a text file from the assets folder and returns its content as a String.
     * @param LOG_TAG Tag used for logging errors and debug info.
     * @param context Android Context used to access assets.
     * @param fileName Name of the asset file to load.
     * @return The full content of the file, or an empty string if an error occurs.
     */
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

    /**
     * Reads a CSV-like text file from assets, parsing each line into a key-value pair.
     * The file should contain lines formatted as "plate,phone". Blank lines or comments (#) are ignored.
     * @param context Android Context used to access assets.
     * @param fileName Name of the asset file containing plate-phone mappings.
     * @return A map where each key is a license plate and the value is the associated phone number.
     * @throws IOException If an I/O error occurs while reading the file.
     */
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
