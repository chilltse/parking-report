package com.example.parkingreport.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * 静态工具类：根据车牌号获取手机号
 * 外部只需调用静态方法，输入车牌即可获取对应手机号
 */
public class PlatePhoneUtil {
    private static final String TAG = "PlatePhoneUtil";
    private static final String CSV_FILE = "plate_phone_2500.csv";
    private static final HashMap<String, String> plateToPhone = new HashMap<>();
    private static boolean initialized = false;

    /**
     * 初始化：从 assets 加载 CSV 数据到缓存
     */
    private static void init(Context context) {
        try (InputStream is = context.getAssets().open(CSV_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            reader.readLine(); // 跳过表头
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
            Log.e(TAG, "加载 CSV 数据失败", e);
        }
    }

    /**
     * 根据车牌号获取手机号，未找到返回 null
     * @param context 用于加载 assets
     * @param plate 车牌号，忽略大小写
     * @return 手机号字符串或 null
     */
    public static String getPhoneForPlate(Context context, String plate) {
        if (!initialized) {
            init(context);
        }
        if (plate == null) return null;
        return plateToPhone.get(plate.trim().toUpperCase());
    }
}
