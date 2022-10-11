package com.korbiak.service.messagesenders;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class EmailSender {
    private final JavaMailSender javaMailSender;

    public void sendEmail(String email, String messageStr) {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(msg, true);
            helper.setTo(email);
            helper.setSubject("Небезпечеий рівень загрози");
            helper.setText("<h1>" + messageStr + "</h1>", true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(msg);
    }
}
