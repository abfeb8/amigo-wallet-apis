package com.ab.amigo.repository;

import com.ab.amigo.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Page<Transaction> findAllByWalletId(Integer walletId, Pageable pageable);
}
