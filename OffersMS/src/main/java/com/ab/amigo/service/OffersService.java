package com.ab.amigo.service;

import com.ab.amigo.controller.clients.CustomerFeignClient;
import com.ab.amigo.dto.OfferDTO;
import com.ab.amigo.dto.ResponseDTO;
import com.ab.amigo.entity.Offer;
import com.ab.amigo.exception.Error;
import com.ab.amigo.exception.ExceptionUtils;
import com.ab.amigo.repository.OffersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OffersService {

    @Autowired
    OffersRepository offersRepository;

    @Autowired
    CustomerFeignClient customerFeignClient;


    public ResponseDTO<List<Offer>> getAllOffers() {
        return new ResponseDTO<>(offersRepository.findAll());
    }

    public ResponseDTO<OfferDTO> addOffer(OfferDTO offerDTO, Integer userId) {

        // verify user is admin
        var validationResponse = customerFeignClient.isCustomerAdmin(userId);
        if (validationResponse.hasError() || Boolean.FALSE.equals(validationResponse.response())) {
            var error = validationResponse.errors();
            error.add(new Error(HttpStatus.FORBIDDEN.value(), validationResponse.hasError() ? "Invalid User ID" : "User is not Admin"));
            return new ResponseDTO<>(null, error);
        }

        // add offer to repo
        var offer = offersRepository.save(Offer.entity(offerDTO));
        return new ResponseDTO<>(offer.value());
    }

    public ResponseDTO<OfferDTO> getOfferPub(String offerCode) {
        try {
            var offer = getOffer(offerCode);
            return new ResponseDTO<>(offer.value());
        } catch (Exception ex) {
            return new ResponseDTO<>(ExceptionUtils.getRequestError(ex));
        }
    }

    private Offer getOffer(String code) {
        return offersRepository.findByOfferCode(code)
                .orElseThrow(() -> new NoSuchElementException("Offer does not exist"));
    }
}
