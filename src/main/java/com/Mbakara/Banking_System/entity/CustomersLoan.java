package com.Mbakara.Banking_System.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "loan")


public class CustomersLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String accountNumber;
    private BigDecimal amountBorrowed;
    private BigDecimal totalAmountRepayment;
    private BigDecimal balanceToPay;
    @CreationTimestamp
    private LocalDateTime dueDate;

    private String status;
}
