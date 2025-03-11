package com.Mbakara.Banking_System.repository;

import com.Mbakara.Banking_System.entity.CreateUser;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreateUserRepository extends JpaRepository<CreateUser, Long> {

    // Using this method to Check if a particular User actually exist by email in the Database?
    Boolean existsByEmail(String email);

    Optional<CreateUser> findByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);
    CreateUser findByAccountNumber(String accountNumber);
    //Optional<CreateUser> findByUsername(String username);


}
