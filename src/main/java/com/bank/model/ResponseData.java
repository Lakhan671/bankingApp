package com.bank.model;

import lombok.Builder;
import lombok.Data;
/**
 *  @author Lakhan Singh
 *
 */
@Builder
@Data
public class ResponseData {
    private String msg;
    private Object data;
    private String error;

}
