package com.yuangang.settlement.settlement;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateSettlementRequest(@NotNull @Positive Long invoiceId) {
}
