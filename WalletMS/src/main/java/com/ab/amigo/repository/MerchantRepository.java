package com.ab.amigo.repository;

import com.ab.amigo.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Integer> {

    Optional<Merchant> findByName(String name);
}
