package com.compass.challenge.services;

import com.compass.challenge.dto.PaymentRequestDTO;
import com.compass.challenge.dto.PaymentResponseDTO;

public interface PaymentService {

    PaymentResponseDTO processPayments(PaymentRequestDTO paymentRequest);
}
