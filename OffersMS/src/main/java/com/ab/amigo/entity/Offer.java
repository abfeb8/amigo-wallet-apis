package com.ab.amigo.entity;

import com.ab.amigo.dto.OfferDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "offer")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offgen")
    @SequenceGenerator(name = "offgen")
    Integer id;
    @Column(name = "offer_code")
    private String offerCode;
    private String info;
    @Column(name = "percentage_off")
    private Double percentOff;

    public static Offer entity(OfferDTO offerDTO) {
        var offer = new Offer();
        offer.setOfferCode(offerDTO.offerCode());
        offer.setInfo(offerDTO.info());
        offer.setPercentOff(offerDTO.percentOff().doubleValue());

        return offer;
    }

    public OfferDTO value() {
        return new OfferDTO(
                this.getOfferCode(),
                this.getInfo(),
                BigDecimal.valueOf(this.getPercentOff())
        );
    }
}
