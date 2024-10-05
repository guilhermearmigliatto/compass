package com.compass.challenge.services.impl;

import com.compass.challenge.dto.PaymentRequestDTO;
import com.compass.challenge.services.SqsService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class SqsServiceSimulatorImpl implements SqsService {

    @Async
    public CompletableFuture<Boolean> processPartialPaymentInQueue(PaymentRequestDTO paymentRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);  // Simula um delay
                return true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        });
    }

    @Async
    public CompletableFuture<Boolean> processTotalPaymentInQueue(PaymentRequestDTO paymentRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                return true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        });
    }

    @Async
    public CompletableFuture<Boolean> processExcessPaymentInQueue(PaymentRequestDTO paymentRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                return true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        });
    }
}

