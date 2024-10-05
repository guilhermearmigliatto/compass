package com.compass.challenge.services;

import com.compass.challenge.domain.Charge;
import com.compass.challenge.exceptions.ChargeNotFoundException;

import java.util.List;

public interface ChargeService {

    List<Charge> list();

    Charge search(Long id) throws ChargeNotFoundException;
}
