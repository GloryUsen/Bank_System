package com.Mbakara.Banking_System.repository;

import com.Mbakara.Banking_System.entity.CreateUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateUserRepository extends JpaRepository<CreateUser, Long> {

    // Using this method to Check if a particular User actually exist by email in the Database?
    Boolean existsByEmail(String email);
    //Boolean findByUserName(String userName);


}
