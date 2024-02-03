package com.ab.amigo.entity;

import com.ab.amigo.dto.BankDTO;
import com.ab.amigo.dto.CustomerDTO;
import javax.persistence.*;

import com.ab.amigo.dto.WalletDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custgen")
    @SequenceGenerator(name = "custgen")
    private Integer id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "is_admin")
    private Boolean isAdmin;
    private String region;
    private String email;
    @Column(name = "bank_account")
    private Integer bankAccountNumber;
    @Column(name = "wallet_id")
    private Integer walletId;
    private String password;

    public Customer(String firstName, String lastName, Boolean isAdmin, String region, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
        this.region = region;
        this.password = password;
        this.email = email;
    }

    public static CustomerDTO value(Customer customer, BankDTO bankAccount, WalletDTO wallet) {
        return new CustomerDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getIsAdmin(),
                customer.getRegion(),
                bankAccount,
                wallet
        );
    }
}
