package com.gamesbykevin.cryptobot.util;

import lombok.extern.log4j.Log4j;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Log4j
public class Email implements Runnable {

    /**
     * How we will notify
     */
    private static final String EMAIL_NOTIFICATION_ADDRESS = Properties.getProperty("emailNotification");

    /**
     * How often do we send an email notification (in milliseconds)
     */
    public static final long EMAIL_NOTIFICATION_DELAY = Integer.parseInt(Properties.getProperty("emailNotificationDelay")) * 60000;

    /**
     * Credentials to send email
     */
    private static final String GMAIL_SMTP_USERNAME = Properties.getProperty("gmailUsername");
    private static final String GMAIL_SMTP_PASSWORD = Properties.getProperty("gmailPassword");

    private final String subject, text;

    private Email(final String subject, final String text) {
        this.subject = subject;
        this.text = text;
    }

    @Override
    public void run() {

        java.util.Properties props;
        Session session;
        Message message;

        try {

            props = new java.util.Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(GMAIL_SMTP_USERNAME, GMAIL_SMTP_PASSWORD);
                }
            });

            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(GMAIL_SMTP_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_NOTIFICATION_ADDRESS));
            message.setSubject(subject);
            message.setText(text);

            //we are now sending
            log.info("Sending email...");
            log.info(subject);
            log.info(text);

            //send the email
            Transport.send(message);

            //display we are good
            log.info("Email sent...");

        } catch (Exception e) {

            //print and write error to log
            log.error(e.getMessage(), e);

        } finally {

            //clean up
            props = null;
            session = null;
            message = null;
        }
    }

    public static boolean hasEmailInfo() {

        //we can't send if null or too short
        if (EMAIL_NOTIFICATION_ADDRESS == null || EMAIL_NOTIFICATION_ADDRESS.trim().length() < 5)
            return false;
        if (GMAIL_SMTP_USERNAME == null || GMAIL_SMTP_USERNAME.trim().length() < 3)
            return false;
        if (GMAIL_SMTP_PASSWORD == null || GMAIL_SMTP_PASSWORD.trim().length() < 3)
            return false;

        //we are good
        return true;
    }

    public static synchronized void send(final String subject, final String text) {

        //don't send email if we don't have all the info
        if (!hasEmailInfo())
            return;

        //we will send the email on a separate thread
        Thread thread = new Thread(new Email(subject, text));

        //start sending the email on a separate thread
        thread.start();
    }
}