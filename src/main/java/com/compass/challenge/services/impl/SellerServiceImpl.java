package com.compass.challenge.services.impl;

import com.compass.challenge.domain.Seller;
import com.compass.challenge.exceptions.SellerNotFoundException;
import com.compass.challenge.repository.SellerRepository;
import com.compass.challenge.services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 * This class provides operations for sellers
 *
 */
@Service
public class SellerServiceImpl implements SellerService {

	@Autowired
	protected SellerRepository sellerRepository;
	
	/**
	 * List all sellers
	 * @return List<Seller> - list of active sellers
	 */
	public List<Seller> list() {
		List<Seller> seller = sellerRepository.findAll();
		return seller;
	}

	public Seller search(Long id) throws SellerNotFoundException {
		Seller seller = sellerRepository.findById(id).orElse(null);
		if  (seller == null) {
			throw new SellerNotFoundException("The seller was not found.");
		}
		return seller;
	}
}
