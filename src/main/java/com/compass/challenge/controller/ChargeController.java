package com.compass.challenge.controller;

import java.util.List;

import com.compass.challenge.domain.Charge;
import com.compass.challenge.services.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Charges
 */
@RestController
@RequestMapping("/charge")
public class ChargeController {

    @Autowired
    protected ChargeService chargeService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Charge>> list() {
        List<Charge> charges = chargeService.list();
        return ResponseEntity.status(HttpStatus.OK).body(charges);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Charge> search(@PathVariable("id") Long id) {
        Charge charge = chargeService.search(id);
        return ResponseEntity.status(HttpStatus.OK).body(charge);
    }


}

