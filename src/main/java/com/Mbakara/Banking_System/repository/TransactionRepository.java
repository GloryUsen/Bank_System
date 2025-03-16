package com.Mbakara.Banking_System.repository;

import com.Mbakara.Banking_System.entity.CustomerTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<CustomerTransactions, Long> {
    List<CustomerTransactions> findByAccountNumberAndCreatedAtBetween(String accountNumber,
                                                                      LocalDateTime starDate,
                                                                      LocalDateTime endDate);
   // void deleteByAccountNumber(String accountNumber);
   void deleteByAccountNumber(String accountNumber);

}
