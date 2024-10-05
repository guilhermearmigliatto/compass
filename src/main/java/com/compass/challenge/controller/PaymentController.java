package com.compass.challenge.controller;

import com.compass.challenge.dto.PaymentRequestDTO;
import com.compass.challenge.dto.PaymentResponseDTO;
import com.compass.challenge.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    protected PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> processPayments(@RequestBody PaymentRequestDTO paymentRequest) {
        return ResponseEntity.ok(paymentService.processPayments(paymentRequest));
    }
}
