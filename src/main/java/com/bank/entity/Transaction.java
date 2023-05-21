package com.bank.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
/**
 *  @author Lakhan Singh
 *
 */
@Document(collection = "db_Transaction")
@Data
public class Transaction {
    @Id
    private String transactionId;

    private  String crNo;

    private String desc;

    private  Double deposit;

    private  Double  withdraw ;

    private Double  currentBalance;

    private Date trDate;
    @Indexed
    private String customerId;



}
