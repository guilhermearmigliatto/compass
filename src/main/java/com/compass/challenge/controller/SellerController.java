package com.compass.challenge.controller;

import com.compass.challenge.domain.Seller;
import com.compass.challenge.services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    protected SellerService sellerService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Seller>> list() {
        List<Seller> sellers = sellerService.list();
        return ResponseEntity.status(HttpStatus.OK).body(sellers);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Seller> search(@PathVariable("id") Long id) {
        Seller seller = sellerService.search(id);
        return ResponseEntity.status(HttpStatus.OK).body(seller);
    }
}

