package com.example.parkingreport.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * @author Nanxuan Xie u8016457
 * - PlatePhoneUtil is a utility class for retrieving phone numbers by license plate.
 * - Loads plate-phone mapping from a CSV file in assets and caches it in memory.
 * - Provides static lookup method to fetch phone numbers based on plate input.
 */
public class PlatePhoneUtil {
    private static final String TAG = "PlatePhoneUtil";
    private static final String CSV_FILE = "plate_phone_2500.csv";
    private static final HashMap<String, String> plateToPhone = new HashMap<>();
    private static boolean initialized = false;

    /**
     * Initialize: Load CSV data from assets into memory cache.
     * Called once on first lookup.
     */
    private static void init(Context context) {
        try (InputStream is = context.getAssets().open(CSV_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String plate = parts[0].trim().toUpperCase();
                    String phone = parts[1].trim();
                    plateToPhone.put(plate, phone);
                }
            }
            initialized = true;
        } catch (IOException e) {
            Log.e(TAG, "Load CSV fails", e);
        }
    }

    /**
     * Get phone number by license plate. Returns null if not found.
     * @param context Context used to access assets
     * @param plate License plate (case-insensitive)
     * @return Phone number string, or null if not found
     */
    public static String getPhoneForPlate(Context context, String plate) {
        if (!initialized) {
            init(context);
        }
        if (plate == null) return null;
        return plateToPhone.get(plate.trim().toUpperCase());
    }
}
