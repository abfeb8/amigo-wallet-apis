package com.ab.amigo.controller.clients;

import com.ab.amigo.dto.BankDTO;
import com.ab.amigo.dto.ResponseDTO;
import com.ab.amigo.dto.WalletDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "WalletMS")
public interface WalletFeignClient {

    @PostMapping("wallet/new-wallet")
    ResponseDTO<WalletDTO> createNewWallet(@RequestParam Integer customerId, @RequestParam String email);

    @PostMapping("wallet/new-bank-acc")
    ResponseDTO<BankDTO> createNewBankAccount(@RequestParam Integer customerId, @RequestParam String customerName);
}
