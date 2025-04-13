package com.example.parkingreport.service;

import static com.example.parkingreport.utils.FileLoader.loadAssetsTemplate;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.example.parkingreport.BuildConfig;
import com.example.parkingreport.service.api.INotificationService;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SmsService implements INotificationService {
    private static final String TAG = "SmsService";
    private String templateFileName;
    private Map<String, String> replacements;
    private Context context;

    /**
     * Constructor for SmsService.
     *
     * @param context The application context.
     * @param smsType The type of SMS to be handled.
     * @param replacements Map of placeholders and their corresponding replacements in the template.
     */
    public SmsService(Context context, NotificationType smsType, Map<String, String> replacements) {
        switch (smsType) {
            case REGISTRATION:
                templateFileName = "sms_register_template.txt";
                break;
            default:
                throw new IllegalArgumentException("Unsupported SMS type: " + smsType);
        }
        this.replacements = replacements;
        this.context = context;
    }

    /**
     * Validates and formats an Australian phone number.
     *
     * @param phoneNumber The input phone number.
     * @return The formatted phone number with a +61 prefix.
     * @throws IllegalArgumentException if the input is not a valid Australian phone number.
     */
    public static String formatAustralianPhoneNumber(String phoneNumber) {
        return phoneNumber.startsWith("0") ? "+61" + phoneNumber.substring(1) : phoneNumber;
    }

    /**
     * Sends an SMS message to the specified recipient.
     *
     * @param to The recipient's phone number.
     */
    @Override
    public void sendMsg(String to) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.twilio.com/2010-04-01/Accounts/" + BuildConfig.SMS_SID + "/Messages.json";
        String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                (BuildConfig.SMS_SID + ":" + BuildConfig.SMS_TOKEN).getBytes(), Base64.NO_WRAP);

        String content = loadAssetsTemplate(TAG, context, templateFileName);
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            content = content.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        Log.d(TAG, "Prepared SMS content: " + content);

        RequestBody body = new FormBody.Builder()
                .add("From", BuildConfig.TWILIO_PHONE_NUMBER)
                .add("To", formatAustralianPhoneNumber(to))
                .add("Body", content)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", base64EncodedCredentials)
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                Log.d(TAG, "SMS sent successfully: " + response.body().string());
            } catch (IOException e) {
                Log.e(TAG, "Error sending SMS: " + e.getMessage(), e);
            }
        }).start();
    }
}
