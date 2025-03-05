package com.Mbakara.Banking_System.controller;

import com.Mbakara.Banking_System.entity.CustomerTransactions;
import com.Mbakara.Banking_System.service.BankStatementService;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/bankStatement")

public class BankTransactionController {

    private final BankStatementService bankStatementService;


    @GetMapping()
    public List<CustomerTransactions> generateAccountStatement(@RequestParam String accountNumber,
                                                               @RequestParam String startDate,
                                                               @RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return bankStatementService.generateAccountStatement(accountNumber, startDate, endDate);

    }

}
