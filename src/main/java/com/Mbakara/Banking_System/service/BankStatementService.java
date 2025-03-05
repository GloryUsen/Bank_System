package com.Mbakara.Banking_System.service;

import com.Mbakara.Banking_System.entity.CustomerTransactions;
import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;


@Service



public interface BankStatementService {
    List<CustomerTransactions> generateAccountStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException;
}
