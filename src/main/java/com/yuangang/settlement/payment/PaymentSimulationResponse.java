package com.yuangang.settlement.payment;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentSimulationResponse(
        Long id,
        Long settlementCaseId,
        PaymentStatus status,
        BigDecimal amount,
        String currency,
        String providerReference,
        Instant createdAt
) {

    static PaymentSimulationResponse from(PaymentSimulation paymentSimulation) {
        return new PaymentSimulationResponse(paymentSimulation.getId(), paymentSimulation.getSettlementCase().getId(),
                paymentSimulation.getStatus(), paymentSimulation.getAmount(), paymentSimulation.getCurrency(),
                paymentSimulation.getProviderReference(), paymentSimulation.getCreatedAt());
    }
}
