package com.Mbakara.Banking_System.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CustomersLoanRepaymentDTO {

    // Customers Request for repaying loan.

    private String accountNumber;
    private BigDecimal amount;
}
