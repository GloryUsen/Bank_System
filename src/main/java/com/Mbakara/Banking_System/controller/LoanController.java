package com.Mbakara.Banking_System.controller;

import com.Mbakara.Banking_System.dto.CustomersLoanRepaymentDTO;
import com.Mbakara.Banking_System.dto.CustomersLoanRequestDTO;
import com.Mbakara.Banking_System.dto.CustomersLoanResponseDTO;
import com.Mbakara.Banking_System.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers_loan")

public class LoanController {

    @Autowired
    LoanService loanService;

    @PostMapping("/apply")
    public CustomersLoanResponseDTO applyingForLoan(@RequestBody CustomersLoanRequestDTO request){
        return loanService.customerAppliesForLoan(request);

    }

    @PostMapping("/repayment")
    public CustomersLoanResponseDTO customerRepayLoan(@RequestBody CustomersLoanRepaymentDTO request){
        return loanService.customerRepayLoan(request);

    }
}
