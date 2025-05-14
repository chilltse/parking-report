package com.example.parkingreport.service;

import static com.example.parkingreport.utils.FileLoader.*;

import android.content.Context;
import android.util.Log;

import com.example.parkingreport.BuildConfig;
import com.example.parkingreport.service.api.INotificationService;

import java.util.Map;
import java.util.Properties;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * EmailService implements INotificationService to send email notifications.
 * It loads an HTML template, replaces placeholders, and sends emails asynchronously.
 * @author Nanxaun Xie u8016457
 */
public class EmailService implements INotificationService {
    private static final String TAG = "EmailService";
    private static final String CODE_TEMPLATE = "code_template.html";

    private static final String SMTP_HOST = BuildConfig.EMAIL_SMTP_HOST;
    private static final String SMTP_PORT = BuildConfig.EMAIL_SMTP_PORT;
    private static final String USERNAME = BuildConfig.EMAIL_SENDER_ADDRESS;
    private static final String PASSWORD = BuildConfig.EMAIL_PASSWORD;

    private final Context context;
    private final String templateFileName;
    private final String subject;
    private final Map<String, String> replacements;
    private final Session session;

    /**
     * Constructs an EmailService with context, notification type, and placeholder data.
     * @param context the Android context for loading template assets
     * @param emailType the type of email notification (e.g., REGISTRATION)
     * @param replacements key-value pairs for replacing placeholders in the template
     */
    public EmailService(Context context, NotificationType emailType, Map<String, String> replacements) {
        this.context = context;
        this.replacements = replacements;
        this.session = getSession();

        switch (emailType) {
            case REGISTRATION:
                this.templateFileName = CODE_TEMPLATE;
                this.subject = "Verification Code";
                break;
            default:
                throw new IllegalArgumentException("Unsupported email type: " + emailType);
        }
    }

    /**
     * Synchronized method to create and configure a JavaMail Session.
     * @return a configured Session object for sending emails
     */
    private static synchronized Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");

        return Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }

    /**
     * Sends an email message to the specified recipient.
     * @param to the recipient's email address
     */
    @Override
    public void sendMsg(String to) {
        try {
            String content = loadAssetsTemplate(TAG, context, templateFileName);
            content = replacePlaceholders(content);

            MimeMessage message = createMessage(to, content);
            sendEmailAsync(message, to);

        } catch (Exception e) {
            Log.e(TAG, "Failed to send email", e);
        }
    }

    /**
     * Replaces placeholders in the HTML content with actual values.
     * Placeholders use the format ${key}.
     * @param content the original HTML template content
     * @return the HTML content with placeholders replaced
     */
    private String replacePlaceholders(String content) {
        String result = content;
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            String value = entry.getValue() != null ? entry.getValue() : "";
            result = result.replace(placeholder, value);
        }
        return result;
    }

    /**
     * Creates a MimeMessage object and sets the sender, recipient, subject, and HTML content.
     * @param to the recipient's email address
     * @param content the HTML content of the email
     * @return a configured MimeMessage ready for sending
     * @throws MessagingException if message creation fails
     */
    private MimeMessage createMessage(String to, String content) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(content, "text/html; charset=utf-8");
        return message;
    }

    /**
     * Sends the email in a separate thread to avoid blocking the main thread.
     * @param message the MimeMessage to send
     * @param to the recipient's email address for logging purposes
     */
    private void sendEmailAsync(MimeMessage message, String to) {
        new Thread(() -> {
            try {
                Transport.send(message);
                Log.i(TAG, "Email sent successfully to " + to);
            } catch (MessagingException e) {
                Log.e(TAG, "Failed to send email to " + to, e);
            }
        }).start();
    }
}