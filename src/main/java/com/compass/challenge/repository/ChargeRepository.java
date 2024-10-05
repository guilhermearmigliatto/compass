package com.compass.challenge.repository;

import com.compass.challenge.domain.Charge;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * This interface contains methods to retrieving and saving information about the Charges in the database
 *
 */
public interface ChargeRepository extends JpaRepository<Charge, Long> {
	// Empty
}
