package com.Mbakara.Banking_System.service;

import com.Mbakara.Banking_System.dto.EmailDetailsDTO;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendEmailAlert(EmailDetailsDTO emailDetailsDTO);
}
