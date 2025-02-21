package com.Mbakara.Banking_System.serviceImpl;

import com.Mbakara.Banking_System.dto.EmailDetailsDTO;
import com.Mbakara.Banking_System.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service

public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") // With this, one will have access to the loginUser of SMTP server(Simple mail transfer protocol)
    private String senderEmail; // Global variable so it can be called elsewhere

    @Override
    public void sendEmailAlert(EmailDetailsDTO emailDetailsDTO) {
        // indicated error
        if (emailDetailsDTO.getRecipient() == null || emailDetailsDTO.getRecipient().isBlank()) {
            System.out.println("Error: Recipient email is null or empty. Email not sent.");
            return;
        }
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail); // This setting who is this mail coming from.
            mailMessage.setTo(emailDetailsDTO.getRecipient()); // The recipient of the message.
            mailMessage.setText(emailDetailsDTO.getMessageBody()); // That's the email body.
            mailMessage.setSubject(emailDetailsDTO.getSubject()); // That's the subject inside a mail.

            javaMailSender.send(mailMessage);
            // Login out message from my console
            System.out.println("Mail Sent Successfully");
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }
}
