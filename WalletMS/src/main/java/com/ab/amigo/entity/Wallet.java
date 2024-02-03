package com.ab.amigo.entity;

import com.ab.amigo.dto.WalletDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "walgen")
    @SequenceGenerator(name = "walgen")
    @Column(name = "wallet_id")
    private Integer walletId;
    private String email;
    private Double balance;
    @Column(name = "user_id")
    private Integer userId;

    public Wallet(Integer userId, String email, double balance) {
        this.email = email;
        this.userId = userId;
        this.balance = balance;
    }

    public WalletDTO value() {
        return new WalletDTO(
                this.getWalletId(),
                this.getEmail(),
                this.getBalance()
        );
    }
}
