package com.bank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/**
 *  @author Lakhan Singh
 *
 */
import java.util.Random;
@Component
public class BankUtils {
    @Value("${bank.id}")
     private  String banKId;
   private static Random rnd = new Random();
    public  String generateBankAccount(){
        int number = rnd.nextInt(999999);
        return banKId+number;
    }

    public  String generateCustomerId(){
        int number = rnd.nextInt(999999);
        return String.valueOf(number);
    }
    public  String generateCR(){
        int number = rnd.nextInt(99999999);
        return String.valueOf(number);
    }
}
