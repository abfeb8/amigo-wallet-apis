package com.ab.amigo.controller;

import com.ab.amigo.dto.OfferDTO;
import com.ab.amigo.dto.ResponseDTO;
import com.ab.amigo.entity.Offer;
import com.ab.amigo.exception.ExceptionUtils;
import com.ab.amigo.service.OffersService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offers")
public class OffersController {

    @Autowired
    OffersService offersService;

    @GetMapping(value = "/all")
    public ResponseEntity<ResponseDTO<List<Offer>>> getAllOffers() {
        return ResponseEntity.ok(offersService.getAllOffers());
    }

    @GetMapping(value = "/offer-code")
    public ResponseEntity<ResponseDTO<OfferDTO>> getOffer(@RequestParam String offerCode) {
        return ResponseEntity.ok(offersService.getOfferPub(offerCode));
    }

    @PutMapping(value = "/add")
    public ResponseEntity<ResponseDTO<OfferDTO>> addNewOffer(@Valid @RequestBody OfferDTO offerDTO, @RequestParam Integer userId, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.ok(new ResponseDTO<>(ExceptionUtils.getRequestError(errors)));

        return ResponseEntity.ok(offersService.addOffer(offerDTO, userId));
    }
}
