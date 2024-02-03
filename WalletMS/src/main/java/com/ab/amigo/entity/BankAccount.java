package com.ab.amigo.entity;

import com.ab.amigo.dto.BankDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "bank")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accgen")
    @SequenceGenerator(name = "accgen")
    @Column(name = "acc_num")
    Integer accNum;
    String ifsc;
    @Column(name = "acc_holder_name")
    String accHolderName;
    Double balance;
    @Column(name = "user_id")
    Integer userId;

    public BankAccount(Integer userId, String holderName) {
        this.ifsc = "AXIS1234567";
        this.balance = 0.0;
        this.userId = userId;
        this.accHolderName = holderName;
    }

    public boolean equalsDTO(BankDTO bankDTO) {
        return this.accNum.equals(bankDTO.accNum()) &&
                this.ifsc.equalsIgnoreCase(bankDTO.bankCode()) &&
                this.accHolderName.equalsIgnoreCase(bankDTO.accHolderName());
    }

    public BankDTO value() {
        return new BankDTO(
                this.accNum,
                this.ifsc,
                this.accHolderName
        );
    }
}
