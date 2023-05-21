package com.bank.repository;

import com.bank.entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
/**
 *  @author Lakhan Singh
 *
 */
public interface TransactionRepository  extends ReactiveMongoRepository<Transaction, String> {
    Flux<Transaction> findByCustomerId(Long customerId);
    Flux<Transaction> findByCustomerId(String customerId, Pageable pageable);
}
