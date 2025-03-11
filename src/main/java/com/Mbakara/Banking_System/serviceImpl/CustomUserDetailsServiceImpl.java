package com.Mbakara.Banking_System.serviceImpl;

import com.Mbakara.Banking_System.repository.CreateUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CreateUserRepository createUserRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return createUserRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + "Not Found"));
        //return createUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + "Not Found"));
    }
}
