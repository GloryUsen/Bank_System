package com.Mbakara.Banking_System.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;



import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CustomersLoanInfoDTO {

    private String accountNumber;
    private String status;
    private BigDecimal amountBorrowed;
    private BigDecimal totalAmountRepayment;
    private BigDecimal balanceToPay;
    @CreationTimestamp
    private LocalDateTime dueDate;


}
