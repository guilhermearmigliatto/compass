package com.compass.challenge.repository;

import com.compass.challenge.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * This interface contains methods to retrieving and saving information about the Sellers in the database
 *
 */
public interface SellerRepository extends JpaRepository<Seller, Long> {
	// Empty
}
