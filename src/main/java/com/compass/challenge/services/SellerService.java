package com.compass.challenge.services;

import com.compass.challenge.domain.Seller;
import com.compass.challenge.exceptions.SellerNotFoundException;

import java.util.List;

public interface SellerService {

    List<Seller> list();

    Seller search(Long id) throws SellerNotFoundException;

}
