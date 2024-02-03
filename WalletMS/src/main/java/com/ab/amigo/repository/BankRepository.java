package com.ab.amigo.repository;

import com.ab.amigo.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<BankAccount, Integer> {

    Optional<BankAccount> findByUserId(Integer userId);
}
