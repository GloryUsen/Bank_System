package com.Mbakara.Banking_System.repository;

import com.Mbakara.Banking_System.entity.BankCustomers;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankCustomersRepository extends JpaRepository<BankCustomers, Long> {

    // Using this method to Check if a particular User actually exist by email in the Database?
    Boolean existsByEmail(String email);

    Optional<BankCustomers> findByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);
    BankCustomers findByAccountNumber(String accountNumber);
   // Optional<BankCustomers> findByAccountNumber(String accountNumber);


}
