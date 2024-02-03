package com.ab.amigo.service;


import com.ab.amigo.controller.clients.WalletFeignClient;
import com.ab.amigo.dto.*;
import com.ab.amigo.entity.Customer;
import com.ab.amigo.exception.ExceptionUtils;
import com.ab.amigo.exception.TransactionFailedException;
import com.ab.amigo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    WalletFeignClient walletFeignClient;

    // sign-up
    public CustomerDTO createUser(CustomerCreationDTO creationDTO) {
        var customer = new Customer(
                creationDTO.fristName(), creationDTO.lastName(),
                creationDTO.isAdmin(), creationDTO.region(),
                creationDTO.password(), creationDTO.email()
        );
        // customer with no wallet or bank account
        customer = customerRepository.save(customer);

        // new bank account for the customer
        var bankAccResponse = walletFeignClient.createNewBankAccount(customer.getId(), creationDTO.fristName() + " " + creationDTO.lastName());
        if (bankAccResponse.hasError())
            throw new TransactionFailedException("Failure while creating bank account");
        customer.setBankAccountNumber(bankAccResponse.response().accNum());

        //create new wallet for the customer
        var walletResponse = walletFeignClient.createNewWallet(customer.getId(), customer.getEmail());
        if (walletResponse.hasError())
            throw new TransactionFailedException("Failure while creating wallet");
        customer.setWalletId(walletResponse.response().walletId());

        // update customer entity with new wallet and bank acc
        customer = customerRepository.save(customer);

        return Customer.value(customer, bankAccResponse.response(), walletResponse.response());
    }

    // login
    public ResponseDTO<String> validateCustomer(LoginDTO loginDTO) {
        var customer = getCustomer(loginDTO.emailId());
        if (customer.getPassword().equals(loginDTO.password()))
            return new ResponseDTO<>("Login Success");
        else
            return new ResponseDTO<>("Login Failed::Password Incorrect");
    }

    public ResponseDTO<Boolean> validateCustomerIsAdmin(Integer userId) {
        try {
            var customer = getCustomer(userId);
            return new ResponseDTO<>(customer.getIsAdmin());
        } catch (Exception ex) {
            return new ResponseDTO<>(false, ExceptionUtils.getRequestError(ex));
        }
    }

    public ResponseDTO<String> updatePass(ChangePassDTO changePassDTO) {
        var customer = getCustomer(changePassDTO.emailId());

        if (!changePassDTO.oldPass().equals(customer.getPassword()))
            throw new IllegalArgumentException("Old password is incorrect");

        customer.setPassword(changePassDTO.newPass());
        customerRepository.save(customer);

        return new ResponseDTO<>("Password updated successfully");
    }

    private Customer getCustomer(Integer userId) {
        return customerRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Customer does not exist"));
    }

    private Customer getCustomer(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Customer does not exist"));
    }
}
