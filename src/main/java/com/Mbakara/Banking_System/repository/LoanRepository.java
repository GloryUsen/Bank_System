package com.Mbakara.Banking_System.repository;

import com.Mbakara.Banking_System.entity.CustomersLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<CustomersLoan, Long> {
    boolean existsByAccountNumberAndStatus(String accountNumber, String status);
    CustomersLoan findByAccountNumberAndStatus(String accountNumber, String status);

}
