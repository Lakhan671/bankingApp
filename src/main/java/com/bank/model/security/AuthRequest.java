package com.bank.model.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *  @author Lakhan Singh
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class  AuthRequest {
    private String username;
    private String password;

}
