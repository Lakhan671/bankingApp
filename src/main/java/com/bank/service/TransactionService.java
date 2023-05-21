package com.bank.service;

import com.bank.BankUtils;
import com.bank.entity.Transaction;
import com.bank.entity.Users;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Date;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;

/**
 *  @author Lakhan Singh
 *
 */

@Service
@Slf4j
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
     @Autowired
    private UserRepository userRepository;
     @Autowired
     private BankUtils bankUtils;
    public Flux<Transaction> getTransactionHistory(WeakHashMap<String,String>request){
        return  transactionRepository.findByCustomerId(Long.valueOf(request.get("customerId")));
    }

    public Mono<String> saveTransaction(WeakHashMap<String,String> request) throws ExecutionException, InterruptedException {
       String cid=request.get("customerId");
       Mono<Users> userM= userRepository.findByCustomerId(cid);
      Double crb=Double.parseDouble(request.get("cb"));
        Transaction transaction = new Transaction();
        transaction.setCustomerId(cid);
        transaction.setDesc(request.get("desc"));
        String tr = request.get("transaction");
        Double updatedBalance = 0.0;
        if (tr.equals("D")) {
            updatedBalance =updatedBalance+ Double.parseDouble(request.get("deposit"));
            transaction.setDeposit(Double.parseDouble(request.get("deposit")));
        } else {
            updatedBalance = updatedBalance - Double.parseDouble(request.get("withdraw"));
            transaction.setWithdraw(Double.parseDouble(request.get("withdraw")));

        }
        transaction.setTrDate(new Date());
        transaction.setCrNo(bankUtils.generateCR());
        transaction.setCurrentBalance(crb+updatedBalance);
        Double finalUpdatedBalance = updatedBalance+crb;
         transactionRepository.save(transaction).subscribe(
                 value ->{
                     userM.subscribe(
                             usr -> {
                                 usr.setCurrentBalance(finalUpdatedBalance);
                                 userRepository.save(usr).subscribe(
                                 );
                             }
                     );
                 }

         );

         return  Mono.just("Completed");
    }

    public Flux<Transaction>getTransactionByPages(WeakHashMap<String,String>request){
             int page =Integer.parseInt(request.get("page"));
             int size=Integer.parseInt(request.get("size"));
             PageRequest pageRequest = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "trDate"));
             return transactionRepository.findByCustomerId(request.get("customerId"),pageRequest);
        }
    }


