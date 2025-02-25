package com.Mbakara.Banking_System.serviceImpl;

import com.Mbakara.Banking_System.dto.TransactionsDTO;
import com.Mbakara.Banking_System.entity.CustomerTransactions;
import com.Mbakara.Banking_System.repository.TransactionRepository;
import com.Mbakara.Banking_System.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionsDTO transactionsDTO) {
        CustomerTransactions customer = CustomerTransactions.builder()
                .transactionType(transactionsDTO.getTransactionType())
                .accountNumber(transactionsDTO.getAccountNumber())
                .amount(transactionsDTO.getAmountInvolve())
                .status("SUCCESS")
                .build();
        transactionRepository.save(customer);
        System.out.println("Transaction Save Successfully");

    }
}
