package com.Mbakara.Banking_System.dto;


import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionsDTO {

    private String transactionType;
    private BigDecimal amountInvolve;
    private String accountNumber;
    private String status;

    
}
