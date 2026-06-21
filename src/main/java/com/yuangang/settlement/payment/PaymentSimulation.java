package com.yuangang.settlement.payment;

import java.math.BigDecimal;
import java.time.Instant;

import com.yuangang.settlement.settlement.SettlementCase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_simulations")
public class PaymentSimulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "settlement_case_id", nullable = false)
    private SettlementCase settlementCase;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private PaymentStatus status;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, unique = true, length = 100)
    private String providerReference;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected PaymentSimulation() {
    }

    public PaymentSimulation(SettlementCase settlementCase, PaymentStatus status, String providerReference) {
        this.settlementCase = settlementCase;
        this.status = status;
        this.amount = settlementCase.getAmount();
        this.currency = settlementCase.getCurrency();
        this.providerReference = providerReference;
    }

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public SettlementCase getSettlementCase() {
        return settlementCase;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getProviderReference() {
        return providerReference;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
