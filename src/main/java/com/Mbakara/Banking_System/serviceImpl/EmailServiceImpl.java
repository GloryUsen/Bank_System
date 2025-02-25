package com.Mbakara.Banking_System.serviceImpl;

import com.Mbakara.Banking_System.dto.EmailDetailsDTO;
import com.Mbakara.Banking_System.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") // With this, one will have access to the loginUser of SMTP server(Simple mail transfer protocol)
    private String senderEmail; //Instance variable with Global access,     so it can be called elsewhere

    @Override
    public void sendEmailAlert(EmailDetailsDTO emailDetailsDTO) {
        // indicated error
        if (emailDetailsDTO.getRecipient() == null || emailDetailsDTO.getRecipient().trim().isEmpty()) {
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
            // Log  out message from my console
           // System.out.println("Mail Sent Successfully");
        } catch (MailException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            throw new RuntimeException("Email sending failed. Please check SMTP settings.", e);
        }
    }
}
