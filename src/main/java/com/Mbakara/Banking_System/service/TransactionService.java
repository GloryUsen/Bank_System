package com.Mbakara.Banking_System.service;

import com.Mbakara.Banking_System.dto.TransactionsDTO;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    void saveTransaction(TransactionsDTO transactionsDTO);
}
