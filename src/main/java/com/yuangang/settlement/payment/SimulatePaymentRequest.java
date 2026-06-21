package com.yuangang.settlement.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SimulatePaymentRequest(
        @NotNull @Positive Long settlementCaseId,
        boolean successful
) {
}
