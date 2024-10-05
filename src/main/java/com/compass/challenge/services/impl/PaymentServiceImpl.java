package com.compass.challenge.services.impl;

import com.compass.challenge.domain.Charge;
import com.compass.challenge.domain.Seller;
import com.compass.challenge.dto.PaymentRequestDTO;
import com.compass.challenge.dto.PaymentResponseDTO;
import com.compass.challenge.exceptions.ChargeNotFoundException;
import com.compass.challenge.exceptions.SellerNotFoundException;
import com.compass.challenge.repository.ChargeRepository;
import com.compass.challenge.repository.SellerRepository;
import com.compass.challenge.services.PaymentService;
import com.compass.challenge.services.SqsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.compass.challenge.dto.PaymentResponseDTO.Status.*;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    protected SellerRepository sellerRepository;

    @Autowired
    protected ChargeRepository chargeRepository;

    @Autowired
    protected SqsService sqsService;

    public PaymentResponseDTO processPayments(PaymentRequestDTO paymentRequest) {
        Optional<Seller> seller = sellerRepository.findById(paymentRequest.getSellerId());

        if (!seller.isPresent()) {
            throw new SellerNotFoundException("Seller not found.");
        }

        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
        List<PaymentResponseDTO.PaymentDTO> payments = new ArrayList<>();
        paymentResponseDTO.setSellerId(paymentRequest.getSellerId());
        paymentResponseDTO.setPayments(payments);

        for (PaymentRequestDTO.PaymentDTO payment : paymentRequest.getPayments()) {
            Optional<Charge> chargeOptional = chargeRepository.findById(payment.getChargeId());

            if (!chargeOptional.isPresent()) {
                throw new ChargeNotFoundException("Charge not found.");
            }

            Charge charge = chargeOptional.get();
            PaymentResponseDTO.PaymentDTO paymentDTO = new PaymentResponseDTO.PaymentDTO();
            paymentDTO.setChargeId(charge.getId());
            paymentDTO.setAmount(payment.getAmount());

            if (charge.getSellerId() != paymentRequest.getSellerId()) {
                throw new RuntimeException("Seller id not the same of charge seller id.");
            }

            try {
                CompletableFuture<Boolean> processedFuture;

                if (payment.getAmount() < charge.getAmount()) {
                    paymentDTO.setStatus(PARTIAL);
                    processedFuture = sqsService.processPartialPaymentInQueue(paymentRequest);
                } else if (payment.getAmount() == charge.getAmount()) {
                    paymentDTO.setStatus(TOTAL);
                    processedFuture = sqsService.processTotalPaymentInQueue(paymentRequest);
                } else {
                    paymentDTO.setStatus(SURPLUS);
                    processedFuture = sqsService.processExcessPaymentInQueue(paymentRequest);
                }

                boolean processed = processedFuture.get(5, TimeUnit.SECONDS);
                paymentDTO.setProcessed(processed);

            } catch (TimeoutException e) {
                paymentDTO.setProcessed(false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            payments.add(paymentDTO);
        }

        return paymentResponseDTO;
    }
}
