package com.ab.amigo.controller.clients;

import com.ab.amigo.dto.OfferDTO;
import com.ab.amigo.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "OfferMS")
public interface OfferFeignClient {

    @GetMapping("offers/offer-code")
    ResponseDTO<OfferDTO> getOffer(@RequestParam String offerCode);

}
