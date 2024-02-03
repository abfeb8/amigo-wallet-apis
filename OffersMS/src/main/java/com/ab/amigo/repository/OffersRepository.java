package com.ab.amigo.repository;

import com.ab.amigo.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OffersRepository extends JpaRepository<Offer, Integer> {

    Optional<Offer> findByOfferCode(String offerCode);
}
