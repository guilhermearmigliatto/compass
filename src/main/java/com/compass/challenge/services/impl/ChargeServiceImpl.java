package com.compass.challenge.services.impl;

import java.util.List;

import com.compass.challenge.domain.Charge;
import com.compass.challenge.exceptions.ChargeNotFoundException;
import com.compass.challenge.repository.ChargeRepository;
import com.compass.challenge.services.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * This class provides operations for charge
 *
 */
@Service
public class ChargeServiceImpl implements ChargeService {

	@Autowired
	protected ChargeRepository chargeRepository;
	
	/**
	 * List all charges
	 * @return List<Charge> - list of active charges
	 */
	public List<Charge> list() {
		List<Charge> charges = chargeRepository.findAll();
		return charges;
	}

	public Charge search(Long id) throws ChargeNotFoundException {
		Charge charge = chargeRepository.findById(id).orElse(null);
		if  (charge == null) {
			throw new ChargeNotFoundException("The charge was not found.");
		}
		return charge;
	}
}
