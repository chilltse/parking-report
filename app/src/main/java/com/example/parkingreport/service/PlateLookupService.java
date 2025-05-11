package com.example.parkingreport.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class PlateLookupService {

    private final Map<String, String> plateToPhone = new HashMap<>();

    public PlateLookupService() throws IOException {
        loadCsvData();
    }

    private void loadCsvData() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("plate_phone_2500.csv");
        if (inputStream == null) {
            throw new IOException("找不到 CSV 文件");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        reader.readLine(); // 跳过表头
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                plateToPhone.put(parts[0].trim().toUpperCase(), parts[1].trim());
            }
        }
        reader.close();
    }

    public String getPhoneByPlate(String plate) {
        if (plate == null) return null;
        return plateToPhone.get(plate.trim().toUpperCase());
    }
}
