package com.ab.amigo.service;

import com.ab.amigo.dto.BankDTO;
import com.ab.amigo.dto.ResponseDTO;
import com.ab.amigo.entity.BankAccount;
import com.ab.amigo.exception.Error;
import com.ab.amigo.exception.ExceptionUtils;
import com.ab.amigo.exception.TransactionFailedException;
import com.ab.amigo.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BankService {

    @Autowired
    BankRepository bankRepository;

    public ResponseDTO<BankDTO> createNewBankAcc(String holderName, Integer userId) {
        var newAcc = new BankAccount(userId, holderName);

        return new ResponseDTO<>(bankRepository.save(newAcc).value());
    }

    public ResponseDTO<String> verifyBankDetails(BankDTO bankDTO) {
        try {
            var bankAcc = getBankAccount(bankDTO.accNum());
            if (!bankAcc.equalsDTO(bankDTO))
                throw new IllegalArgumentException("Bank details are incorrect");
        } catch (Exception ex) {
            return new ResponseDTO<>(ExceptionUtils.getRequestError(ex));
        }
        return new ResponseDTO<>("Verification success");
    }

    public ResponseDTO<String> addMoneyToBank(Integer bankId, Double amount) {
        if (amount.equals(404.0))
            throw new TransactionFailedException("Transaction Failed");

        return addMoney(getBankAccount(bankId), amount);
    }

    public ResponseDTO<String> deductMoneyFromBank(Integer bankId, Double amount) {
        return deductMoney(getBankAccount(bankId), amount);
    }

    private BankAccount getBankAccount(Integer bankId) {
        return bankRepository.findById(bankId)
                .orElseThrow(() -> new NoSuchElementException("Bank account does not exist"));
    }

    private ResponseDTO<String> addMoney(BankAccount bank, Double amount) {
        bank.setBalance(bank.getBalance() + amount);

        try {
            bankRepository.save(bank);
            return new ResponseDTO<>("Balance updated successfully");
        } catch (Exception ex) {
            return new ResponseDTO<>(new Error(501, ex.getMessage()));
        }

    }

    private ResponseDTO<String> deductMoney(BankAccount bank, Double amountToDeduct) {

        Double currentBal = bank.getBalance();
        if (currentBal < amountToDeduct)
            return new ResponseDTO<>(new Error(HttpStatus.FORBIDDEN.value(), "Insufficient Fund"));

        bank.setBalance(bank.getBalance() - amountToDeduct);

        try {
            bankRepository.save(bank);
            return new ResponseDTO<>("Balance updated successfully");
        } catch (Exception ex) {
            return new ResponseDTO<>(new Error(501, ex.getMessage()));
        }

    }
}
