package com.example.parkingreport.service;

import android.content.Context;

import com.example.parkingreport.service.api.INotificationService;

import java.util.Map;

public class NotificationFactory {



    // Factory method
    public static INotificationService createService(String type, Context context, NotificationType notificationType, Map<String, String> replacements ) {
        switch (type) {
            case "email":
                return new EmailService(context, notificationType, replacements);  // 假设存在这样一个实现
            case "sms":
                return new SmsService(context, notificationType, replacements);    // 假设存在这样一个实现
            default:
                throw new IllegalArgumentException("Unknown service type: " + type);
        }
    }
}