package com.yuangang.settlement.settlement;

import java.time.Instant;

public record SettlementStatusHistoryResponse(
        Long id,
        Long settlementCaseId,
        SettlementStatus fromStatus,
        SettlementStatus toStatus,
        Instant changedAt
) {

    static SettlementStatusHistoryResponse from(SettlementStatusHistory history) {
        return new SettlementStatusHistoryResponse(history.getId(), history.getSettlementCase().getId(),
                history.getFromStatus(), history.getToStatus(), history.getChangedAt());
    }
}
