package com.bank.service;

import java.util.*;
import com.bank.BankUtils;
import com.bank.entity.Users;
import com.bank.model.User;
import com.bank.model.security.Role;
import com.bank.repository.UserRepository;
import com.bank.security.JWTUtil;
import com.bank.security.PBKDF2Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
/**
 *  @author Lakhan Singh
 *
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PBKDF2Encoder passwordEncoder;

    @Autowired
    private BankUtils bankUtils;

    public Mono<User> findByUsername(String username) {
        return userRepository.findByUserName(username).map(u -> new User(u.getUserName(), u.getPassword(), true, Arrays.asList(Role.ROLE_USER)));
    }

    public Mono<Users> saveUser(WeakHashMap<String, String> requestData) {
        Date date=new Date();
        Users users= Users.builder()
                .fullName(requestData.get("name"))
                .userName(requestData.get("email"))
                .password(passwordEncoder.encode(requestData.get("password")))
                .bankAccountNo(bankUtils.generateBankAccount())
                .customerId(bankUtils.generateCustomerId())
                .currentBalance(0.00)
                .updtedDate(date)
                .createdDate(date)
                .build();
        return userRepository.save(users);
    }
    public Mono<Users> getUser(String email){
       return userRepository.findByUserName(email);
    }

}
