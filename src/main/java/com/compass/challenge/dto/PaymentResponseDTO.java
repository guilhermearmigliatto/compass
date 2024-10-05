package com.compass.challenge.dto;

import java.util.List;

public class PaymentResponseDTO {

    private Long sellerId;
    private List<PaymentDTO> payments;

    public static class PaymentDTO {
        private Long chargeId;
        private double amount;
        private Status status;
        private boolean processed;

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

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public boolean isProcessed() {
            return processed;
        }

        public void setProcessed(boolean processed) {
            this.processed = processed;
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

    public enum Status {
        PARTIAL("partial"),
        TOTAL("total"),
        SURPLUS("surplus");

        private String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return description;
        }
    }
}


