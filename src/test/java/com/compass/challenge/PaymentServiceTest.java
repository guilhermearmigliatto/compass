package com.compass.challenge;

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
import com.compass.challenge.services.impl.PaymentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.compass.challenge.dto.PaymentResponseDTO.Status.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private ChargeRepository chargeRepository;

    @Mock
    private SqsService sqsService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = SellerNotFoundException.class)
    public void whenSellerNotFoundThenThrowException() {
        // Setup
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setSellerId(1L);

        // Mock
        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        paymentService.processPayments(request);
    }

    @Test(expected = ChargeNotFoundException.class)
    public void whenChargeNotFoundThenThrowException() {
        // Setup
        Seller.Builder sellerBuilder = new Seller.Builder();
        sellerBuilder.id(1L);
        Seller seller = sellerBuilder.build();

        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setSellerId(1L);
        PaymentRequestDTO.PaymentDTO paymentDTO = new PaymentRequestDTO.PaymentDTO();
        paymentDTO.setChargeId(1L);
        request.setPayments(Arrays.asList(paymentDTO));

        // Mock
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(chargeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        paymentService.processPayments(request);
    }

    @Test
    public void whenPaymentIsPartialThenProcessPartialPayment() {
        // Setup
        Seller.Builder sellerBuilder = new Seller.Builder();
        sellerBuilder.id(1L);
        Seller seller = sellerBuilder.build();

        Charge.Builder builder = new Charge.Builder();
        builder.id(1L).amount(200).sellerId(1l);
        Charge charge = builder.build();

        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setSellerId(1L);
        PaymentRequestDTO.PaymentDTO paymentDTO = new PaymentRequestDTO.PaymentDTO();
        paymentDTO.setChargeId(1L);
        paymentDTO.setAmount(150);
        request.setPayments(Arrays.asList(paymentDTO));

        // Mock
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(chargeRepository.findById(1L)).thenReturn(Optional.of(charge));
        when(sqsService.processPartialPaymentInQueue(any())).thenReturn(CompletableFuture.completedFuture(true));

        // Act
        PaymentResponseDTO response = paymentService.processPayments(request);

        // Assert
        assertEquals(1L, response.getSellerId().longValue());
        assertEquals(1, response.getPayments().size());
        assertEquals(PARTIAL, response.getPayments().get(0).getStatus());
        assertEquals(true, response.getPayments().get(0).isProcessed());

        // Verify that the correct sqsService method was called
        verify(sqsService, times(1)).processPartialPaymentInQueue(any());
    }

    @Test
    public void whenPaymentIsTotalThenProcessTotalPayment() {
        // Setup
        Seller.Builder sellerBuilder = new Seller.Builder();
        sellerBuilder.id(1L);
        Seller seller = sellerBuilder.build();

        Charge.Builder builder = new Charge.Builder();
        builder.id(1L).amount(200).sellerId(1l);
        Charge charge = builder.build();

        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setSellerId(1L);
        PaymentRequestDTO.PaymentDTO paymentDTO = new PaymentRequestDTO.PaymentDTO();
        paymentDTO.setChargeId(1L);
        paymentDTO.setAmount(200);
        request.setPayments(Arrays.asList(paymentDTO));

        // Mock
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(chargeRepository.findById(1L)).thenReturn(Optional.of(charge));
        when(sqsService.processTotalPaymentInQueue(any())).thenReturn(CompletableFuture.completedFuture(true));

        // Act
        PaymentResponseDTO response = paymentService.processPayments(request);

        // Assert
        assertEquals(1L, response.getSellerId().longValue());
        assertEquals(1, response.getPayments().size());
        assertEquals(TOTAL, response.getPayments().get(0).getStatus());
        assertEquals(true, response.getPayments().get(0).isProcessed());

        // Verify that the correct sqsService method was called
        verify(sqsService, times(1)).processTotalPaymentInQueue(any());
    }

    @Test
    public void whenPaymentIsSurplusThenProcessExcessPayment() {
        // Setup
        Seller.Builder sellerBuilder = new Seller.Builder();
        sellerBuilder.id(1L);
        Seller seller = sellerBuilder.build();

        Charge.Builder builder = new Charge.Builder();
        builder.id(1L).amount(200).sellerId(1l);
        Charge charge = builder.build();

        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setSellerId(1L);
        PaymentRequestDTO.PaymentDTO paymentDTO = new PaymentRequestDTO.PaymentDTO();
        paymentDTO.setChargeId(1L);
        paymentDTO.setAmount(250);
        request.setPayments(Arrays.asList(paymentDTO));

        // Mock
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(chargeRepository.findById(1L)).thenReturn(Optional.of(charge));
        when(sqsService.processExcessPaymentInQueue(any())).thenReturn(CompletableFuture.completedFuture(false));

        // Act
        PaymentResponseDTO response = paymentService.processPayments(request);

        // Assert
        assertEquals(1L, response.getSellerId().longValue());
        assertEquals(1, response.getPayments().size());
        assertEquals(SURPLUS, response.getPayments().get(0).getStatus());
        assertEquals(false, response.getPayments().get(0).isProcessed());

        // Verify that the correct sqsService method was called
        verify(sqsService, times(1)).processExcessPaymentInQueue(any());
    }
}
