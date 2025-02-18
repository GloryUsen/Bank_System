package com.Mbakara.Banking_System.repository;

import com.Mbakara.Banking_System.entity.BankUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankUserRepository extends JpaRepository<BankUser, Long> {

    // Using this method to Check if a particular User actually exist by email in the Database?
    Boolean existsByEmail(String email);


}
