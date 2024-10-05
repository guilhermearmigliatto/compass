package com.compass.challenge.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
public class Seller {

    public Seller(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
    }

    // Default constructor
    public Seller() {
        this.id = null;
        this.name = null;
    }

    @Id
    private Long id;

    @JsonInclude(Include.NON_NULL)
    private String name;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Builder {
        private Long id;
        private String name;

        public Builder() {
            // Empty
        }

        public Builder(Seller seller) {
            this.id = seller.id;
            this.name = seller.name;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Seller build() {
            return new Seller(this);
        }
    }
}
