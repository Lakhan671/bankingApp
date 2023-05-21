package com.bank.repository;


import com.bank.entity.Users;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
/**
 *  @author Lakhan Singh
 *
 */

@Repository
public interface UserRepository extends ReactiveCrudRepository<Users, String> {
    public Mono<Users>  findByUserName(String userName);
    public Mono<Users>findByCustomerId(String customerId);
}