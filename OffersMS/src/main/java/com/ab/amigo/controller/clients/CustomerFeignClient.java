package com.ab.amigo.controller.clients;

import com.ab.amigo.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "CustomerMS")
public interface CustomerFeignClient {

    @GetMapping("customer/is-admin")
    ResponseDTO<Boolean> isCustomerAdmin(@RequestParam Integer userId);

}
