package com.ab.amigo.controller;

import com.ab.amigo.dto.*;
import com.ab.amigo.exception.ExceptionUtils;
import com.ab.amigo.service.CustomerService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping("/register")
    @CircuitBreaker(name = "customerService", fallbackMethod = "createCustomerFallback")
    public ResponseEntity<ResponseDTO<CustomerDTO>> createCustomer(@Valid @RequestBody CustomerCreationDTO creationDTO, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.ok(new ResponseDTO<>(ExceptionUtils.getRequestError(errors)));

        return ResponseEntity.ok(new ResponseDTO<>(customerService.createUser(creationDTO)));
    }

    public ResponseEntity<ResponseDTO<CustomerDTO>> createCustomerFallback(@Valid @RequestBody CustomerCreationDTO creationDTO, Errors errors, Throwable throwable) {
        return ResponseEntity.ok(new ResponseDTO<>(
                        null,
                        List.of(ExceptionUtils.getRequestError(new Exception("Fallback Response")))
                )
        );
    }

    @PatchMapping("/reset/password")
    public ResponseEntity<ResponseDTO<String>> updatePass(@Valid @RequestBody ChangePassDTO changePassDTO, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.ok(new ResponseDTO<>("Invalid Data", ExceptionUtils.getRequestError(errors)));

        return ResponseEntity.ok(customerService.updatePass(changePassDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<String>> login(@Valid @RequestBody LoginDTO loginDTO, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.ok(new ResponseDTO<>("Invalid Data", ExceptionUtils.getRequestError(errors)));

        return ResponseEntity.ok(customerService.validateCustomer(loginDTO));
    }

    @GetMapping("/is-admin")
    public ResponseEntity<ResponseDTO<Boolean>> isCustomerAdmin(@RequestParam Integer userId) {
        return ResponseEntity.ok(customerService.validateCustomerIsAdmin(userId));
    }
}