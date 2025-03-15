package com.Mbakara.Banking_System.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomersLoanRequestDTO {

    // Customers Request for applying a loan

    private String accountNumber;
    private BigDecimal amount;
    private int repaymentPeriod;
}
