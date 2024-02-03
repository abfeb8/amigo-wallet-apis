package com.ab.amigo.entity;

import com.ab.amigo.dto.MerchantDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "merchant")
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String utilities;
    @Column(name = "email_id")
    private String emailId;

    public static MerchantDTO value(Merchant merchant) {
        return new MerchantDTO(
                merchant.getName(),
                List.of(merchant.getUtilities().split("::"))
        );
    }
}
