package com.compass.challenge;

import com.compass.challenge.domain.Seller;
import com.compass.challenge.dto.PaymentRequestDTO;
import com.compass.challenge.dto.PaymentResponseDTO;
import com.compass.challenge.exceptions.ChargeNotFoundException;
import com.compass.challenge.exceptions.SellerNotFoundException;
import com.compass.challenge.repository.ChargeRepository;
import com.compass.challenge.repository.SellerRepository;
import com.compass.challenge.services.PaymentService;
import com.compass.challenge.services.SqsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static com.compass.challenge.dto.PaymentResponseDTO.Status.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PaymentServiceIntegrationTest {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private PaymentService paymentService;

    @MockBean
    private SqsService sqsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenSellerNotFoundThenThrowException() {
        // Setup
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setSellerId(300L);

        // Act & Assert
        assertThrows(SellerNotFoundException.class, () -> {
            paymentService.processPayments(request);
        });
    }

    @Test
    public void whenChargeNotFoundThenThrowException() {
        // Setup
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setSellerId(1L);
        PaymentRequestDTO.PaymentDTO paymentDTO = new PaymentRequestDTO.PaymentDTO();
        paymentDTO.setChargeId(200L);
        request.setPayments(Arrays.asList(paymentDTO));

        // Act & Assert
        assertThrows(ChargeNotFoundException.class, () -> {
            paymentService.processPayments(request);
        });
    }

    @Test
    public void whenPaymentIsPartialThenProcessPartialPayment() {
        // Setup
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setSellerId(1L); // Guilherme Moraes Armigliatto
        PaymentRequestDTO.PaymentDTO paymentDTO = new PaymentRequestDTO.PaymentDTO();
        paymentDTO.setChargeId(1L); // Charge com amount 500.00
        paymentDTO.setAmount(300);  // Pagamento parcial
        request.setPayments(Arrays.asList(paymentDTO));

        // Mock
        when(sqsService.processPartialPaymentInQueue(any())).thenReturn(CompletableFuture.completedFuture(false));

        // Act
        PaymentResponseDTO response = paymentService.processPayments(request);

        // Asserts
        assertEquals(1L, response.getSellerId().longValue());
        assertEquals(1, response.getPayments().size());
        assertEquals(PARTIAL, response.getPayments().get(0).getStatus());
        assertEquals(false, response.getPayments().get(0).isProcessed());

        // Verify that the correct sqsService method was called
        verify(sqsService, times(1)).processPartialPaymentInQueue(any());
    }

    @Test
    public void whenPaymentIsTotalThenProcessTotalPayment() {
        // Setup
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setSellerId(1L); // Guilherme Moraes Armigliatto
        PaymentRequestDTO.PaymentDTO paymentDTO = new PaymentRequestDTO.PaymentDTO();
        paymentDTO.setChargeId(2L); // Charge com amount 150.00
        paymentDTO.setAmount(150);  // Pagamento total
        request.setPayments(Arrays.asList(paymentDTO));

        // Mock
        when(sqsService.processTotalPaymentInQueue(any())).thenReturn(CompletableFuture.completedFuture(true));

        // Act
        PaymentResponseDTO response = paymentService.processPayments(request);

        // Asserts
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
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setSellerId(2L); // Pedro Silva
        PaymentRequestDTO.PaymentDTO paymentDTO = new PaymentRequestDTO.PaymentDTO();
        paymentDTO.setChargeId(4L); // Charge com amount 250.00
        paymentDTO.setAmount(300);  // Pagamento excedente
        request.setPayments(Arrays.asList(paymentDTO));

        // Mock
        when(sqsService.processExcessPaymentInQueue(any())).thenReturn(CompletableFuture.completedFuture(false));

        // Act
        PaymentResponseDTO response = paymentService.processPayments(request);

        // Asserts
        assertEquals(2L, response.getSellerId().longValue());
        assertEquals(1, response.getPayments().size());
        assertEquals(SURPLUS, response.getPayments().get(0).getStatus());
        assertEquals(false, response.getPayments().get(0).isProcessed());

        // Verify that the correct sqsService method was called
        verify(sqsService, times(1)).processExcessPaymentInQueue(any());
    }
}