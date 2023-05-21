package com.bank.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 *  @author Lakhan Singh
 *
 */
@Data
@Document(collection = "db_users")
@Builder
public class Users implements Serializable {
    @Id
    private String  id;
    @Indexed(unique = true)
    private String userName;
    private String fullName;
    @Indexed(unique = true)
    private String bankAccountNo;
    @Indexed(unique = true)
    private String customerId;
    @JsonIgnore
    private String password;
    private Date createdDate;
    private Date updtedDate;
    @DocumentReference(lookup = "{'users':?#{#self._id} }", lazy = true)
    private List<Transaction> transactions;
    private Double currentBalance=0.0;

}
