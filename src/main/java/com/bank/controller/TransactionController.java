package com.bank.controller;

import com.bank.entity.Transaction;
import com.bank.model.Message;
import com.bank.model.ResponseData;
import com.bank.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;
/**
 * This class is used to withdraw and deposit money into a bank account.
@author Lakhan Singh
 */
@RestController
@RequestMapping(value = "/api")
@Slf4j
@CrossOrigin(origins = "*")
public class TransactionController {
    @Autowired
   private TransactionService transactionService;
    @GetMapping("/resource/user")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<Message>> user() {
        return Mono.just(ResponseEntity.ok(new Message("Content for user")));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/transaction/save/v1")
    public Mono<ResponseEntity<ResponseData>> addTransaction(@RequestBody WeakHashMap<String,String> request) throws ExecutionException, InterruptedException {
        log.info(" Add transaction  request  ={}", request);
        return transactionService.saveTransaction(request)
                .doOnSuccess(response -> log.info(" request for transaction={} completed successfully", response))
                .map(res -> ResponseEntity
                        .accepted()
                        .body(ResponseData.builder().msg("Your Transaction is completed").build()))
                .onErrorResume(e -> {
                    Throwable throwable = (Throwable) e;
                    log.info("Transaction with error {}", throwable.getMessage());
                    return   Mono.just(ResponseEntity
                            .accepted()
                            .body(ResponseData.builder().msg("There is a problem with transaction processing; either the bank's server is down or the services are unavailable.").build()));
                });


    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/transaction/get/v1")
    public Flux<Transaction> getTransaction(@RequestBody WeakHashMap<String,String> request) {
        log.info(" getTransaction method request ={}", request);
        return  transactionService.getTransactionHistory(request);

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/transaction/pages/v1")
    public Flux<Transaction> getTransactionByPages(@RequestBody WeakHashMap<String,String> request) {
        log.info(" getTransactionByPages method request ={}", request);
        return  transactionService.getTransactionByPages(request);

    }

}
