package com.ab.amigo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;
    Timestamp timestamp;
    String info; // transaction details
    Double amount;
    String transactionType; // credit/debit
    String status;
    @Column(name = "wallet_id")
    Integer walletId;

    public boolean hasFailed() {
        return !"SCS".equalsIgnoreCase(status);
    }

}
