package com.yuangang.settlement.settlement;

import java.math.BigDecimal;
import java.time.Instant;

public record SettlementResponse(
        Long id,
        Long invoiceId,
        SettlementStatus status,
        BigDecimal amount,
        String currency,
        Instant createdAt,
        Instant updatedAt
) {

    public static SettlementResponse from(SettlementCase settlementCase) {
        return new SettlementResponse(settlementCase.getId(), settlementCase.getInvoice().getId(),
                settlementCase.getStatus(), settlementCase.getAmount(), settlementCase.getCurrency(),
                settlementCase.getCreatedAt(), settlementCase.getUpdatedAt());
    }
}
