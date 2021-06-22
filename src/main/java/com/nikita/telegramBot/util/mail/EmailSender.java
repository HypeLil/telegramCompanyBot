package com.nikita.telegramBot.util.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
@Slf4j
public class EmailSender {

    private final static String mainMail = "zapros@free-lines.ru";
    private final static String userLogin = "liltupics.bot@gmail.com";
    private final static String userPassword = "91823God";

    public static void send(String message, String orderType){

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userLogin, userPassword);
                    }
                });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(userLogin));
            msg.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(mainMail)
            );

            msg.setSubject(orderType);
            msg.setText(message);
            Transport.send(msg);

            log.info("Отправка mail с типом заказа {}", orderType);
        }
        catch (MessagingException mex){
            log.error("Ошибка при отправке mail");
        }
    }
}
