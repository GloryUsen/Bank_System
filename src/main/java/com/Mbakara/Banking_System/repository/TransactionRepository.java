package com.Mbakara.Banking_System.repository;

import com.Mbakara.Banking_System.entity.CustomerTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface TransactionRepository extends JpaRepository<CustomerTransactions, String> {



}
