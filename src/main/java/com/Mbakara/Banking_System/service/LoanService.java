package com.Mbakara.Banking_System.service;

import com.Mbakara.Banking_System.dto.CustomersLoanRepaymentDTO;
import com.Mbakara.Banking_System.dto.CustomersLoanRequestDTO;
import com.Mbakara.Banking_System.dto.CustomersLoanResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface LoanService {

    CustomersLoanResponseDTO customerAppliesForLoan(CustomersLoanRequestDTO request);
    CustomersLoanResponseDTO customerRepayLoan(CustomersLoanRepaymentDTO request);
}
