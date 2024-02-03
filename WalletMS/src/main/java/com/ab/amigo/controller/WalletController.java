package com.ab.amigo.controller;

import com.ab.amigo.dto.*;
import com.ab.amigo.entity.Transaction;
import com.ab.amigo.exception.ExceptionUtils;
import com.ab.amigo.service.BankService;
import com.ab.amigo.service.WalletService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    WalletService walletService;

    @Autowired
    BankService bankService;

    @PostMapping("/new-wallet")
    public ResponseEntity<ResponseDTO<WalletDTO>> createNewWallet(@RequestParam Integer customerId, @RequestParam String email){
        return ResponseEntity.ok(walletService.creatNewWallet(customerId, email));
    }

    @PostMapping("/new-bank-acc")
    public ResponseEntity<ResponseDTO<BankDTO>> createNewBankAccount(@RequestParam Integer customerId, @RequestParam String customerName){
        return ResponseEntity.ok(bankService.createNewBankAcc(customerName, customerId));
    }

    @PatchMapping("/transfer/wallet-top-up")
    public ResponseEntity<ResponseDTO<WalletDTO>> loadWallet(@Valid @RequestBody LoadWalletDTO loadWalletDTO, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.ok(new ResponseDTO<>(ExceptionUtils.getRequestError(errors)));

        return ResponseEntity.ok(walletService.rechargeWallet(loadWalletDTO));
    }

    @PatchMapping("/transfer/add-money-to-bank")
    public ResponseEntity<ResponseDTO<String>> addMoneyToBank(@RequestParam Integer bankId, @RequestParam Double amount) {
        return ResponseEntity.ok(bankService.addMoneyToBank(bankId, amount));
    }

    @PatchMapping("/transfer/wallet-to-bank")
    public ResponseEntity<ResponseDTO<String>> sendMoneyToBank(@Valid @RequestBody WalletToBankTransferDTO transferDTO, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.ok(new ResponseDTO<>(ExceptionUtils.getRequestError(errors)));
        }
        return ResponseEntity.ok(walletService.sendMoneyToBank(transferDTO));
    }

    @PatchMapping("/transfer/wallet-to-wallet")
    public ResponseEntity<ResponseDTO<String>> sendMoneyToWallet(@Valid @RequestBody WalletToWalletTransferDTO transferDTO, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.ok(new ResponseDTO<>(ExceptionUtils.getRequestError(errors)));
        }
        return ResponseEntity.ok(walletService.sendMoneyToWallet(transferDTO));
    }

    @PostMapping("/bill/pay")
    public ResponseEntity<ResponseDTO<String>> payBill(@RequestParam Integer walletId, @RequestParam String offerCode, @Valid @RequestBody BillDTO billDTO, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.ok(new ResponseDTO<>(ExceptionUtils.getRequestError(errors)));
        }
        return ResponseEntity.ok(walletService.payBill(walletId, billDTO, offerCode));
    }

    @GetMapping("/transaction/all")
    public ResponseEntity<ResponseDTO<Page<Transaction>>> getAllTransactions(@RequestParam Integer walletId, @RequestParam int page) {
        return ResponseEntity.ok(walletService.getAllTransaction(walletId, page));
    }

    @GetMapping("/bill/select-merchant")
    public ResponseEntity<ResponseDTO<List<MerchantDTO>>> getAllMerchants() {
        return ResponseEntity.ok(walletService.getALlMerchant());
    }

    @GetMapping("/bill")
    public ResponseEntity<ResponseDTO<BillDTO>> getBill(@RequestParam String merchant, @RequestParam String utility) {
        return ResponseEntity.ok(walletService.generateBill(merchant, utility));
    }
}
