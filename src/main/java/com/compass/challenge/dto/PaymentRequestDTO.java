package com.compass.challenge.dto;

import java.util.List;

public class PaymentRequestDTO {

    private Long sellerId;
    private List<PaymentDTO> payments;

    public static class PaymentDTO {
        private Long chargeId;
        private double amount;

        // Getters and Setters
        public Long getChargeId() {
            return chargeId;
        }

        public void setChargeId(Long chargeId) {
            this.chargeId = chargeId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }

    // Getters and Setters
    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public List<PaymentDTO> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentDTO> payments) {
        this.payments = payments;
    }
}
