package com.compass.challenge.services;

import com.compass.challenge.dto.PaymentRequestDTO;

import java.util.concurrent.CompletableFuture;

public interface SqsService {

    CompletableFuture<Boolean> processPartialPaymentInQueue(PaymentRequestDTO paymentRequest);

    CompletableFuture<Boolean> processTotalPaymentInQueue(PaymentRequestDTO paymentRequest);

    CompletableFuture<Boolean> processExcessPaymentInQueue(PaymentRequestDTO paymentRequest);

}
