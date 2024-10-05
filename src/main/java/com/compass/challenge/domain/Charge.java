package com.compass.challenge.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
public class Charge {

    public Charge(Builder builder) {
        this.id = builder.id;
        this.sellerId = builder.sellerId;
        this.amount = builder.amount;
    }

    // Default constructor
    public Charge() {
        this.id = null;
        this.sellerId = null;
        this.amount = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(Include.NON_NULL)
    private Long id;

    @JsonInclude(Include.NON_NULL)
    private Long sellerId;

    @JsonInclude(Include.NON_NULL)
    private double amount;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public static class Builder {
        private Long id;
        private Long sellerId;
        private double amount;

        public Builder() {
            // Empty
        }

        public Builder(Charge charge) {
            this.id = charge.id;
            this.sellerId = charge.sellerId;
            this.amount = charge.amount;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder sellerId(Long sellerId) {
            this.sellerId = sellerId;
            return this;
        }

        public Builder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public Charge build() {
            return new Charge(this);
        }
    }
}
