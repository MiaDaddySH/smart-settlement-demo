package com.yuangang.settlement.settlement;

import java.time.Instant;

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
@Table(name = "settlement_status_history")
public class SettlementStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "settlement_case_id", nullable = false)
    private SettlementCase settlementCase;

    @Enumerated(EnumType.STRING)
    @Column(length = 40)
    private SettlementStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private SettlementStatus toStatus;

    @Column(nullable = false, updatable = false)
    private Instant changedAt;

    protected SettlementStatusHistory() {
    }

    public SettlementStatusHistory(SettlementCase settlementCase, SettlementStatus fromStatus,
            SettlementStatus toStatus) {
        this.settlementCase = settlementCase;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
    }

    @PrePersist
    void prePersist() {
        this.changedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public SettlementCase getSettlementCase() {
        return settlementCase;
    }

    public SettlementStatus getFromStatus() {
        return fromStatus;
    }

    public SettlementStatus getToStatus() {
        return toStatus;
    }

    public Instant getChangedAt() {
        return changedAt;
    }
}
