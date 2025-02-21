package com.Mbakara.Banking_System.service;

import com.Mbakara.Banking_System.dto.EmailDetailsDTO;

public interface EmailService {
    void sendEmailAlert(EmailDetailsDTO emailDetailsDTO);
}
